package com.example.crew.app.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crew.R
import com.example.crew.domain.entities.EmployeeDE

class EmployeeListRecyclerAdapter(
    private val onClick: (EmployeeClickable) -> Unit
) : ListAdapter<EmployeeDE, EmployeeListRecyclerAdapter.EmployeeListViewHolder>(EmployeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_list_item, parent, false)
        return EmployeeListViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeListViewHolder, position: Int) {
        val currentEmployee = getItem(position)
        holder.bind(currentEmployee, onClick)
    }

    class EmployeeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val firstName: TextView = itemView.findViewById(R.id.first_name)
        private val lastName: TextView = itemView.findViewById(R.id.last_name)
        private val editButton: ImageView = itemView.findViewById(R.id.edit_button)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bind(employee: EmployeeDE, onClick: (EmployeeClickable) -> Unit) {
            firstName.text = employee.name
            lastName.text = employee.lastName

            editButton.setOnClickListener {
                employee.employeeId?.let {
                    onClick(EmployeeClickable.EditClick(it))
                }
            }

            deleteButton.setOnClickListener {
                employee.employeeId?.let {
                    onClick(EmployeeClickable.DeleteClick(it))
                }
            }
        }
    }

    sealed class EmployeeClickable {
        data class EditClick(val employeeId: Long) : EmployeeClickable()
        data class DeleteClick(val employeeId: Long) : EmployeeClickable()
    }

    private class EmployeeDiffCallback : DiffUtil.ItemCallback<EmployeeDE>() {
        override fun areItemsTheSame(oldItem: EmployeeDE, newItem: EmployeeDE): Boolean {
            return oldItem.employeeId == newItem.employeeId
        }

        override fun areContentsTheSame(oldItem: EmployeeDE, newItem: EmployeeDE): Boolean {
            return oldItem == newItem
        }
    }
}
