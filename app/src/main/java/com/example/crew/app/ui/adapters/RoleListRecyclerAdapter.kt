package com.example.crew.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crew.R
import com.example.crew.domain.entities.RolesDE

class RoleListRecyclerAdapter( private val onClick: (RoleClickable) -> Unit
) : ListAdapter<RolesDE, RoleListRecyclerAdapter.RoleListViewHolder>(RoleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_list_item, parent, false)
        return RoleListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoleListViewHolder, position: Int) {
        val currentEmployee = getItem(position)
        holder.bind(currentEmployee, onClick)
    }

    class RoleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val firstName: TextView = itemView.findViewById(R.id.first_name)
        private val lastName: TextView = itemView.findViewById(R.id.last_name)

        private val age: TextView = itemView.findViewById(R.id.age)

        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val editButton: ImageView = itemView.findViewById(R.id.edit_button)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bind(role: RolesDE, onClick: (RoleClickable) -> Unit) {
            firstName.text = role.roleName
//            lastName.text = employee.lastName
//            age.text = employee.age.toString()
//            userName.text = employee.username

//            editButton.setOnClickListener {
//                employee.employeeId?.let {
//                    onClick(EmployeeClickable.EditClick(it))
//                }
//            }

//            deleteButton.setOnClickListener {
//                employee.employeeId?.let {
//                    onClick(EmployeeClickable.DeleteClick(it))
//                }
//            }
        }
    }

    sealed class RoleClickable {
        data class EditClick(val roleId: Long) : RoleClickable()
        data class DeleteClick(val roleId: Long) : RoleClickable()
    }

    private class RoleDiffCallback : DiffUtil.ItemCallback<RolesDE>() {
        override fun areItemsTheSame(oldItem: RolesDE, newItem: RolesDE): Boolean {
            return oldItem.roleId == newItem.roleId
        }

        override fun areContentsTheSame(oldItem: RolesDE, newItem: RolesDE): Boolean {
            return oldItem == newItem
        }
    }
}