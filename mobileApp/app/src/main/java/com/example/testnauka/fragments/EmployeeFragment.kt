package com.example.testnauka.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.testnauka.R
import com.example.testnauka.adapters.AdapterEmployee
import com.example.testnauka.data.*
import com.example.testnauka.di.annotation.ApplicationScope
import com.example.testnauka.di.annotation.EmployeeViewModelScope
import com.example.testnauka.di.module.RepositoryModule
import com.example.testnauka.models.EmployeeViewModel
import com.example.testnauka.utils.Positions
import com.google.gson.Gson
import toothpick.Scope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.closeOnViewModelCleared
import toothpick.smoothie.viewmodel.installViewModelBinding
import java.util.*


class EmployeeFragment : Fragment() {

    private val employeeViewModel: EmployeeViewModel by inject()
    private lateinit var viewEmployee: View
    private lateinit var buttonAddNew: ImageButton
    private lateinit var buttonAddNewDepEmp: ImageButton
    private lateinit var listEmployee: RecyclerView
    private lateinit var adapterEmployee: AdapterEmployee
    private lateinit var departListAdapter: ArrayAdapter<DepartsData>
    private lateinit var employeeListAdapter: ArrayAdapter<EmployeeData>
    private lateinit var position: Positions
    private var pickDepart: DepartsData? = null
    private var pickEmployee: EmployeeData? = null

    companion object {
        fun newInstance() = EmployeeFragment()
    }

    private fun injectDependencies() {
        KTP.openScopes(ApplicationScope::class.java)
            .openSubScope(EmployeeViewModelScope::class.java) { scope: Scope ->
                scope.installViewModelBinding<EmployeeViewModel>(this)
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
        position = Gson().fromJson(bundle!!.getString("position"), Positions::class.java)
        Log.i("help", position.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapterEmployee = AdapterEmployee(arrayListOf(), position)
        if (position == Positions.АдминистраторСотрудников){
            viewEmployee = inflater.inflate(R.layout.employee_fragment_for_admin_employee, container, false)
            buttonAddNew = viewEmployee.findViewById(R.id.buttonAddNew)
            buttonAddNewDepEmp = viewEmployee.findViewById(R.id.buttonAddNewDepEmp)
            //Слушать кнопки удаление
            adapterEmployee.setCallbackDelete(object : AdapterEmployee.Callback {
                override fun onItemClicked(position: Int, item: EmployeeData) {
                    Log.i("help", item.toString())
                    Log.i("help", position.toString())
                    employeeViewModel.deleteEmployee(PostDeleteEmployeeRequest(item.id))
                    adapterEmployee.deleteItem(position)
                }
            })
            //Добавление нового работника
            buttonAddNew.setOnClickListener {
                val layoutInflater = LayoutInflater.from(context)
                val dialogView = layoutInflater.inflate(R.layout.insert_employee_diolog, null)
                val mDialogBuilder = AlertDialog.Builder(context)
                mDialogBuilder.setView(dialogView)
                val spinPositions = dialogView.findViewById<Spinner>(R.id.spinnerPos)
                val adapterPositions: ArrayAdapter<Positions> =
                    activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, Positions.values())}!!
                adapterPositions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinPositions.adapter = adapterPositions
                val userInput = dialogView.findViewById<EditText>(R.id.input_text)
                var pickPos: Positions? = null
                spinPositions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        pickPos = null
                    }
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        pickPos = adapterPositions.getItem(p2)
                    }
                }
                val datePicker = dialogView.findViewById<DatePicker>(R.id.datePickerEdit)
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                var newYear: Int? = null
                var newMonth: Int? = null
                var newDay: Int? = null
                var posInt = 0
                when(pickPos){
                    Positions.ОбычныйРабочий -> posInt = 0
                    Positions.Табельщик -> posInt = 1
                    Positions.АдминистраторОтделов -> posInt = 2
                    Positions.АдминистраторСотрудников -> posInt = 3
                }

                // Устанавливаем текущую дату для DatePicker
                datePicker.init(year, month, day, null)
                datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                    newYear = year
                    newMonth = monthOfYear
                    newDay = dayOfMonth
                }
                mDialogBuilder.setPositiveButton("YES"){dialog, which ->
                    employeeViewModel.insertEmployee(PostEmployeeRequest(userInput.text.toString(),newYear!!,
                        newMonth!!, newDay!!, posInt))
                    dialog.cancel()
                }
                mDialogBuilder.setNegativeButton("No"){dialog,which ->
                    dialog.cancel()
                }
                val alertDialog = mDialogBuilder.create()
                alertDialog.show()
            }

            //Добавления сотрудника в специальный отдел
            buttonAddNewDepEmp.setOnClickListener {
                val layoutInflater = LayoutInflater.from(context)
                val dialogView = layoutInflater.inflate(R.layout.dialog_inser_depart_employee, null)
                val mDialogBuilder = AlertDialog.Builder(context)
                mDialogBuilder.setView(dialogView)
                val spinDepart = dialogView.findViewById<Spinner>(R.id.spinnerDepart)
                val spinEmployee = dialogView.findViewById<Spinner>(R.id.spinnerEmployee)
                spinDepart.adapter = departListAdapter
                spinEmployee.adapter = employeeListAdapter

                spinDepart.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        pickDepart = null
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        pickDepart = departListAdapter.getItem(p2)
                        Log.i("help", pickDepart.toString())
                    }
                }

                spinEmployee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        pickEmployee = null
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        pickEmployee = employeeListAdapter.getItem(p2)
                        Log.i("help", pickEmployee.toString())
                    }
                }

                mDialogBuilder.setPositiveButton("YES"){dialog, which ->
                    employeeViewModel.insertDepartEmployee(pickEmployee!!.id, pickDepart!!.id)
                    dialog.cancel()
                }
                mDialogBuilder.setNegativeButton("No"){dialog,which ->
                    dialog.cancel()
                }
                val alertDialog = mDialogBuilder.create()
                alertDialog.show()
            }

            //Слушатель кнопки изменения
            adapterEmployee.setCallbackEdit(object : AdapterEmployee.Callback {
                override fun onItemClicked(position: Int, employee: EmployeeData) {
                    val layoutInflater = LayoutInflater.from(context)
                    val dialogView = layoutInflater.inflate(R.layout.dialog_update_employee, null)
                    val mDialogBuilder = AlertDialog.Builder(context)
                    mDialogBuilder.setView(dialogView)
                    val userInput = dialogView.findViewById<EditText>(R.id.input_text)
                    val spinnerPositions = dialogView.findViewById<Spinner>(R.id.spinnerPos)
                    val adapterPositions: ArrayAdapter<Positions> =
                        activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, Positions.values())}!!
                    adapterPositions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerPositions.adapter = adapterPositions
                    var pickPos: Positions? = null
                    spinnerPositions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            pickPos = null
                        }
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            pickPos = adapterPositions.getItem(p2)
                        }
                    }
                    var posInt = 0
                    when(pickPos){
                        Positions.ОбычныйРабочий -> posInt = 0
                        Positions.Табельщик -> posInt = 1
                        Positions.АдминистраторОтделов -> posInt = 2
                        Positions.АдминистраторСотрудников -> posInt = 3
                    }
                    var prevPos = 0
                    when(employee.positions){
                        Positions.ОбычныйРабочий -> prevPos = 0
                        Positions.Табельщик -> prevPos = 1
                        Positions.АдминистраторОтделов -> prevPos = 2
                        Positions.АдминистраторСотрудников -> prevPos = 3
                    }
                    mDialogBuilder.setPositiveButton("YES"){dialog, which ->
                        employeeViewModel.updateEmployee(PostUpdateEmployeeRequest(employee.id, employee.name,
                            userInput.text.toString(),prevPos,posInt)
                        )
                        adapterEmployee.editItem(position, userInput.text.toString(), posInt)
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
            viewEmployee = inflater.inflate(R.layout.employee_fragment, container, false)
        }
        listEmployee = viewEmployee.findViewById(R.id.employeeList)
        listEmployee.adapter = adapterEmployee
        employeeViewModel.getEmployee()
        employeeViewModel.getDeparts()
        return viewEmployee
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        employeeViewModel.employeeLiveData.observe(viewLifecycleOwner, Observer<List<EmployeeData>> {
            adapterEmployee.update((employeeViewModel.employeeLiveData.value!!))
            employeeListAdapter=
                activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, employeeViewModel.employeeLiveData.value!!)}!!
            employeeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        })
        employeeViewModel.departsLiveData.observe(viewLifecycleOwner, Observer <List<DepartsData>>{
            departListAdapter=
                activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, employeeViewModel.departsLiveData.value!!)}!!
            departListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        })
    }

}
