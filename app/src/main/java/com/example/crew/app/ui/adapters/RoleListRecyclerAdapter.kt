package com.example.crew.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crew.R
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.entities.RolesWithEmployeeDE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoleListRecyclerAdapter(private val onClick: (RoleClickable) -> Unit
) : ListAdapter<RolesWithEmployeeDE, RoleListRecyclerAdapter.RoleListViewHolder>(RoleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.role_list_item, parent, false)
        return RoleListViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoleListViewHolder, position: Int) {
        val currentEmployee = getItem(position)
        holder.bind(currentEmployee, onClick)
    }

    class RoleListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roleName: TextView = itemView.findViewById(R.id.role_name)

        private val employeesButton: Button = itemView.findViewById<Button>(R.id.employees_list)

        private val editButton: ImageView = itemView.findViewById(R.id.edit_button)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bind(roleWithEmployee: RolesWithEmployeeDE, onClick: (RoleClickable) -> Unit) {

            val role = roleWithEmployee.role
            roleName.text = role.roleName

            editButton.setOnClickListener {
                role.roleId.let {
                    onClick(RoleClickable.EditClick(RolesDE(
                        roleId = role.roleId,
                        roleName = role.roleName
                    )))
                }
            }

            deleteButton.setOnClickListener {
                    onClick(RoleClickable.DeleteClick(role))
            }


            employeesButton.setOnClickListener {
                val lifecycleOwner = itemView.findViewTreeLifecycleOwner()
                lifecycleOwner?.lifecycleScope?.launch {
                    val allEmployees = roleWithEmployee.employees
                    displayListOfEmployees(itemView.context, role.roleName, allEmployees)
                }
               // onClick.invoke(RoleClickable.EmployeeButton(roleWithEmployee.role.roleName))
            }
        }
        fun displayListOfEmployees(context: Context,role:String, employees: List<EmployeeDE>?=null){

                val hyphenate = employees?.map { "\t- ${it.name} ${it.lastName}" }?.toTypedArray()
                AlertDialog.Builder(context)
                    .setTitle("Employees with <$role> role")
                    .setItems(hyphenate){ dialog, which ->

                    }
                    .setNegativeButton("Cancel"){dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
        }
    }




    sealed class RoleClickable {
        data class EditClick(val role: RolesDE) : RoleClickable()
        data class DeleteClick(val role: RolesDE) : RoleClickable()

        data class EmployeeButton(val roleName: String): RoleClickable()
    }

    private class RoleDiffCallback : DiffUtil.ItemCallback<RolesWithEmployeeDE>() {
        override fun areItemsTheSame(oldItem: RolesWithEmployeeDE, newItem: RolesWithEmployeeDE): Boolean {
            return oldItem.role == newItem.role
        }

        override fun areContentsTheSame(oldItem: RolesWithEmployeeDE, newItem: RolesWithEmployeeDE): Boolean {
            return oldItem == newItem
        }
    }
}