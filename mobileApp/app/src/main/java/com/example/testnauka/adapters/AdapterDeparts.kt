package com.example.testnauka.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testnauka.R
import com.example.testnauka.data.DepartsData
import com.example.testnauka.utils.Positions


class AdapterDeparts(private var depart: ArrayList<DepartsData>, private val positions: Positions): IAdapterDeparts(){
    private var callbackDelete: Callback? = null
    private var callbackEdit: Callback? = null

    fun setCallbackDelete(callb: Callback){
        callbackDelete = callb
    }

    fun setCallbackEdit(calle: Callback){
        callbackEdit = calle
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(depart[position].name, depart[position].id)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private lateinit var textViewDeparts: TextView
        private lateinit var buttonDelete: ImageButton
        private lateinit var buttonEdit: ImageButton
        fun bindItems( departName: String, depId: Int) {
            if (positions == Positions.АдминистраторОтделов){
                buttonDelete = itemView.findViewById(R.id.imageButtonRemove)
                buttonEdit = itemView.findViewById(R.id.imageButtonEdit)
                buttonDelete.setOnClickListener {
                    callbackDelete?.onItemClicked(adapterPosition, depart[adapterPosition])
                }
                buttonEdit.setOnClickListener {
                    callbackEdit?.onItemClicked(adapterPosition, depart[adapterPosition])
                }
            }
            textViewDeparts= itemView.findViewById(R.id.textViewTitle)
            textViewDeparts.text = departName + " " + depId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (positions == Positions.АдминистраторОтделов){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_for_admin_depart, parent, false)
            return ViewHolder(itemView)
        }
        else{
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_default, parent, false)
            return ViewHolder(itemView)
        }
    }

    fun update(departsData: ArrayList<DepartsData>){
        depart = departsData
        notifyDataSetChanged()
    }

    fun editItem(position: Int, name: String){
        depart[position] = DepartsData(depart[position].id, name)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int){
        depart.removeAt(position)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(pos: Int, item: DepartsData)
    }

    override fun getItemCount(): Int = depart.size
}