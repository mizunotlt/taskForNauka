package com.example.testnauka.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.testnauka.R
import com.example.testnauka.adapters.AdapterDeparts
import com.example.testnauka.data.DepartsData
import com.example.testnauka.data.PostDeleteDepartRequest
import com.example.testnauka.data.PostUpdateDepart
import com.example.testnauka.di.annotation.ApplicationScope
import com.example.testnauka.di.annotation.DepartsViewModelScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.models.DepartsViewModel
import com.example.testnauka.data.PostInsertDepart
import com.example.testnauka.utils.Positions
import com.google.gson.Gson
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding
import com.example.testnauka.utils.OnItemClickListener

class DepartsFragment : Fragment() {

    companion object {
        fun newInstance() = DepartsFragment()
    }
    private val departsViewModel: DepartsViewModel by inject()
    private lateinit var viewDeparts: View
    private lateinit var buttonAddNew: ImageButton
    private lateinit var listDeparts: RecyclerView
    private lateinit var adapterDeparts: AdapterDeparts
    private lateinit var position: Positions

    private fun injectDependencies() {

        KTP.openScopes(ApplicationScope::class.java)
            .openSubScope(DepartsViewModelScope::class.java) { scope: Scope ->
                scope.installViewModelBinding<DepartsViewModel>(this)
                    .closeOnViewModelCleared(this)
                    .installModules(
                        RepositoryModule()
                    )
            }
            .closeOnDestroy(this)
            .inject(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        val bundle = arguments
        position = Gson().fromJson(bundle!!.getString("position"),Positions::class.java)
        Log.i("help", position.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapterDeparts = AdapterDeparts(arrayListOf(), position)
        if (position == Positions.АдминистраторОтделов){
            viewDeparts = inflater.inflate(R.layout.departs_fragment_for_admin_depart, container, false)
            buttonAddNew = viewDeparts.findViewById(R.id.buttonAddNew)
            buttonAddNew.setOnClickListener {
                val layoutInflater = LayoutInflater.from(context)
                val dialogView = layoutInflater.inflate(R.layout.dialog_update_depart, null)
                val mDialogBuilder = AlertDialog.Builder(context)
                mDialogBuilder.setView(dialogView)
                val userInput = dialogView.findViewById<EditText>(R.id.input_text)
                mDialogBuilder.setPositiveButton("YES"){dialog, which ->
                    departsViewModel.insertDepart(
                        PostInsertDepart(
                            userInput.text.toString()
                        )
                    )
                    departsViewModel.getDeparts()
                    dialog.cancel()
                }
                mDialogBuilder.setNegativeButton("No"){dialog,which ->
                    dialog.cancel()
                }
                val alertDialog = mDialogBuilder.create()
                alertDialog.show()
            }

            //Слушать кнопки удаление
            adapterDeparts.setCallbackDelete(object : AdapterDeparts.Callback {
                override fun onItemClicked(position: Int, depart: DepartsData) {
                    Log.i("help", depart.toString())
                    Log.i("help", position.toString())
                    departsViewModel.deleteDepart(PostDeleteDepartRequest(depart.id))
                    adapterDeparts.deleteItem(position)
                }
            })

            //Слушатель кнопки изменения
            adapterDeparts.setCallbackEdit(object : AdapterDeparts.Callback {
                override fun onItemClicked(position: Int, depart: DepartsData) {
                    val layoutInflater = LayoutInflater.from(context)
                    val dialogView = layoutInflater.inflate(R.layout.dialog_update_depart, null)
                    val mDialogBuilder = AlertDialog.Builder(context)
                    mDialogBuilder.setView(dialogView)
                    val userInput = dialogView.findViewById<EditText>(R.id.input_text)
                    mDialogBuilder.setPositiveButton("YES"){dialog, which ->
                        departsViewModel.updateDepart(PostUpdateDepart(depart.name, userInput.text.toString()))
                        adapterDeparts.editItem(position, userInput.text.toString())
                        dialog.cancel()
                    }
                    mDialogBuilder.setNegativeButton("No"){dialog,which ->
                        dialog.cancel()
                    }
                    val alertDialog = mDialogBuilder.create()
                    alertDialog.show()
                }
            })

        }
        else{
            viewDeparts = inflater.inflate(R.layout.departs_fragment, container, false)
        }
        listDeparts = viewDeparts.findViewById(R.id.departsList)
        listDeparts.adapter = adapterDeparts
        departsViewModel.getDeparts()

        /*
        //Мб добавить кнопку в элемент хмм... еще кнопка update
        listDeparts.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                //Delete depart
            }
        })
         */

        return viewDeparts
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        departsViewModel.departsLiveData.observe(viewLifecycleOwner, Observer<List<DepartsData>> {
            adapterDeparts.update((departsViewModel.departsLiveData.value!!))
        })
    }

    //ItemClickListener
    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {

            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                }
            }
        })
    }

    private fun createToast (text: String){
        val toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.view.setBackgroundColor(Color.GRAY)
        toast.show()
    }

}
