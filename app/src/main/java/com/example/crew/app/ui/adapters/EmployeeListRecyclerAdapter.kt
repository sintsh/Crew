package com.example.crew.app.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crew.R
import com.example.crew.domain.entities.EmployeeDE

data class EmployeeListRecyclerAdapter(
    val employeeList: List<EmployeeDE>,
    val onClick:(EmployeeClickable)-> Unit
): RecyclerView.Adapter<EmployeeListRecyclerAdapter.EmployeeListViewHolder>() {
    
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeListViewHolder {
        return EmployeeListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.employee_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: EmployeeListViewHolder, position: Int) {
        val currentEmployee = employeeList[position]
        holder.firstName.text = currentEmployee.name
        holder.lastName.text = currentEmployee.lastName
        holder.editButton.setOnClickListener {
            onClick.invoke(EmployeeClickable.EditClick(currentEmployee.employeeId))
        }

        holder.deleteButton.setOnClickListener {
            onClick.invoke(EmployeeClickable.DeleteClick(currentEmployee.employeeId))
        }
    }

    override fun getItemCount(): Int = employeeList.size
    
    
    
    inner class EmployeeListViewHolder(val item: View): RecyclerView.ViewHolder(item){
        val firstName = item.findViewById<TextView>(R.id.first_name)
        val lastName = item.findViewById<TextView>(R.id.last_name)
        val editButton = item.findViewById<ImageView>(R.id.edit_button)
        val deleteButton = item.findViewById<ImageView>(R.id.delete_button)
    }


    sealed class EmployeeClickable{
        class EditClick(val employeeId: Long): EmployeeClickable()

        class DeleteClick(val employeeId: Long): EmployeeClickable()
    }



    
}
