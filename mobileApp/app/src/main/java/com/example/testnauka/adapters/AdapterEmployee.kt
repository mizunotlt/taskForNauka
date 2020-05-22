package com.example.testnauka.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testnauka.R
import com.example.testnauka.data.EmployeeData
import com.example.testnauka.utils.Positions
import java.util.*
import kotlin.collections.ArrayList

class AdapterEmployee(private var employee: ArrayList<EmployeeData>, private val positions: Positions): IAdapterEmployee(){

    private var callbackDelete: Callback? = null
    private var callbackEdit: Callback? = null


    fun setCallbackDelete(callb: Callback){
        callbackDelete = callb
    }

    fun setCallbackEdit(calle: Callback){
        callbackEdit = calle
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(employee[position].name, employee[position].id, employee[position].birth,
            employee[position].positions)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private lateinit var textViewEmployeeName: TextView
        private lateinit var textViewEmployeeId: TextView
        private lateinit var textViewEmployeeBirth: TextView
        private lateinit var textViewEmployeePositions: TextView
        private lateinit var buttonDelete: ImageButton
        private lateinit var buttonEdit: ImageButton
        fun bindItems( empName: String, empId: Int, empBirth: Date, empPos: Positions) {

            if (positions == Positions.АдминистраторСотрудников){
                buttonDelete = itemView.findViewById(R.id.imageButtonRemove)
                buttonEdit = itemView.findViewById(R.id.imageButtonEdit)
                buttonDelete.setOnClickListener {
                    callbackDelete?.onItemClicked(adapterPosition, employee[adapterPosition])
                }
                buttonEdit.setOnClickListener {
                    callbackEdit?.onItemClicked(adapterPosition, employee[adapterPosition])
                }
            }

            textViewEmployeeId= itemView.findViewById(R.id.textViewID)
            textViewEmployeeName= itemView.findViewById(R.id.textViewName)
            textViewEmployeeBirth= itemView.findViewById(R.id.textViewDate)
            textViewEmployeePositions= itemView.findViewById(R.id.textViewPosition)
            textViewEmployeeId.text = empId.toString()
            textViewEmployeeName.text = empName
            textViewEmployeeBirth.text = empBirth.toString()
            textViewEmployeePositions.text = empPos.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (positions == Positions.АдминистраторСотрудников){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_for_admin_employee, parent, false)
            return ViewHolder(itemView)
        }
        else{
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_employee_default, parent, false)
            return ViewHolder(itemView)
        }
    }

    fun update(employeeData: ArrayList<EmployeeData>){
        employee = employeeData
        notifyDataSetChanged()
    }


    fun editItem(position: Int, name: String, posInt: Int){
        var pos: Positions? = null
        when(posInt){
            0 -> pos = Positions.ОбычныйРабочий
            1 -> pos = Positions.Табельщик
            2 -> pos = Positions.АдминистраторОтделов
            3 -> pos = Positions.АдминистраторСотрудников
        }
        employee[position] = EmployeeData(employee[position].id, name, employee[position].birth, pos!!)
        notifyDataSetChanged()
    }


    fun deleteItem(position: Int) {
        employee.removeAt(position)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(pos: Int, item: EmployeeData)
    }

    override fun getItemCount(): Int = employee.size
}