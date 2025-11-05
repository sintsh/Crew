package com.example.crew.app.ui.adapters

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.launch
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.crew.R
import com.example.crew.domain.entities.EmployeeDE
import com.example.crew.domain.entities.EmployeeWithRolesDE
import com.example.crew.domain.entities.RolesDE
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EmployeeListRecyclerAdapter(
    private val rolesFLow : Flow<List<RolesDE>>,
    private val roleActions: (DataReceiver) -> Unit,
    private val onClick: (EmployeeClickable) -> Unit
) : ListAdapter<EmployeeWithRolesDE, EmployeeListRecyclerAdapter.EmployeeListViewHolder>(EmployeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_list_item, parent, false)
        return EmployeeListViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeListViewHolder, position: Int) {
        val currentEmployee = getItem(position)
        holder.bind(currentEmployee, onClick, rolesFLow, action = roleActions)
    }

    class EmployeeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val firstName: TextView = itemView.findViewById(R.id.first_name)
        private val lastName: TextView = itemView.findViewById(R.id.last_name)

        private val age: TextView = itemView.findViewById(R.id.age)

        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val editButton: ImageView = itemView.findViewById(R.id.edit_button)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        private val addRolesButton: MaterialButton = itemView.findViewById(R.id.add_roles)

        fun bind(employeeWR: EmployeeWithRolesDE, onClick: (EmployeeClickable) -> Unit, rolesFlow: Flow<List<RolesDE>>, action: (DataReceiver) -> Unit) {
            firstName.text = employeeWR.employee.name
            lastName.text = employeeWR.employee.lastName
            age.text = employeeWR.employee.age.toString()
            userName.text = employeeWR.employee.username


            addRolesButton.setOnClickListener {
                val lifecycleOwner = itemView.findViewTreeLifecycleOwner()
                lifecycleOwner?.lifecycleScope?.launch {
                    val allRoles = rolesFlow.first()
                    updateRolesUi(employeeWR.employee.employeeId,employeeWR.roles, allRoles, action = action)
                }
            }



            editButton.setOnClickListener {
                employeeWR.employee.employeeId?.let {
                    onClick(EmployeeClickable.EditClick(it))
                }
            }

            deleteButton.setOnClickListener {
                employeeWR.employee.employeeId?.let {
                    onClick(EmployeeClickable.DeleteClick(it))
                }
            }
        }


        private fun updateRolesUi(currentEmployeeId: Long?, employeeRoles: List<RolesDE>, allAvailableRoles: List<RolesDE>, action:(DataReceiver) -> Unit) {
//            binding.rolesChipGroup.removeAllViews()
//
//            allAvailableRoles.forEach { role ->
//                val chip = Chip(itemView.context).apply {
//                    text = role.name
//                    isChecked = employeeRoles.any { it.id == role.id }
//                    isClickable = false
//                }
//                binding.rolesChipGroup.addView(chip)
//            }
            Log.i("RolesInRecycler", "updateRolesUi: $allAvailableRoles")
            val roles: Array<String> = allAvailableRoles.map {
                it.roleName
            }.toTypedArray()

                val selectedItems = ArrayList<Int>()
                val unselectedItem = ArrayList<Int>()
                val empRoles = employeeRoles.map { it.roleName }
                val empRoleIds = employeeRoles.map { it.roleId }
                val checkedItems = allAvailableRoles.map {
                        empRoles.contains(it.roleName)
                }.toBooleanArray()
                AlertDialog.Builder(itemView.context)
                    .setTitle("Select Role")
                    .setMultiChoiceItems(roles, checkedItems) { dialog, which, isChecked ->
                        if (isChecked){
                            selectedItems.add(allAvailableRoles[which].roleId.toInt())
                            unselectedItem.remove(allAvailableRoles[which].roleId.toInt())
                        } else{
                            selectedItems.remove(allAvailableRoles[which].roleId.toInt())
                            unselectedItem.add(allAvailableRoles[which].roleId.toInt())

                        }
                    }
                    .setPositiveButton("OK") { dialog, _ ->
                        val selected = selectedItems.filter {
                            !empRoleIds.contains(it.toLong())
                        }
                        val unselected = empRoleIds.filter {
                            unselectedItem.contains(it.toInt())
                        }
                        currentEmployeeId?.let { empId->
                            action(DataReceiver.RoleData(empId,selected, unselected))
                        }
                        Log.i("checkboxStatus", "selected : $selectedItems \n unselected: $unselected")
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

        }
    }

    sealed class EmployeeClickable {
        data class EditClick(val employeeId: Long) : EmployeeClickable()
        data class DeleteClick(val employeeId: Long) : EmployeeClickable()

    }

    sealed class DataReceiver {
        class RoleData(val employeeId:Long,val selected : List<Int>, val unselected : List<Long>): DataReceiver()
    }

    private class EmployeeDiffCallback : DiffUtil.ItemCallback<EmployeeWithRolesDE>() {
        override fun areItemsTheSame(oldItem: EmployeeWithRolesDE, newItem: EmployeeWithRolesDE): Boolean {
            return oldItem.employee.employeeId == newItem.employee.employeeId
        }

        override fun areContentsTheSame(oldItem: EmployeeWithRolesDE, newItem: EmployeeWithRolesDE): Boolean {
            return oldItem == newItem
        }
    }
}
