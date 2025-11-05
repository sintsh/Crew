package com.example.crew.app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crew.domain.entities.RolesDE
import com.example.crew.domain.usecases.role.DeleteRoleUseCase
import com.example.crew.domain.usecases.role.GetAllRolesUseCase
import com.example.crew.domain.usecases.role.GetRoleByIdUseCase
import com.example.crew.domain.usecases.role.InsertRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRolesViewModel @Inject constructor(
    private val getAllRolesUseCase: GetAllRolesUseCase,
    private val insertRoleUseCase: InsertRoleUseCase,
    private val deleteRoleUseCase: DeleteRoleUseCase,
    private val getRoleByIdUseCase: GetRoleByIdUseCase
): ViewModel() {
    private val _limit = MutableStateFlow(6)
    val limit = _limit.asStateFlow()

    private val _offset = MutableStateFlow(0)
    val offset = _offset.asStateFlow()
    private val _maxRoleCount = MutableStateFlow(0)
    val maxRoleCount = _maxRoleCount.asStateFlow()

    private val _searchQueries = MutableStateFlow("")
    val searchQueries = _searchQueries.asStateFlow()

    val hasNextPage: Flow<Boolean> =
        combine(offset, limit, maxRoleCount) { currentOffset, currentLimit, maxCount ->
            (currentOffset + 1) * currentLimit < maxCount
        }

    val hasPreviousPage: Flow<Boolean> = offset.combine(limit) { currentOffset, _ ->
        currentOffset > 0
    }

    private val dataUpdateTrigger = MutableStateFlow(0)
    var roles: Flow<List<RolesDE>> = combine(
        offset,
        limit,
        _searchQueries.debounce(600),
        dataUpdateTrigger,
    ) { currentOffset, currentLimit, searchQueries,_ ->
        Pair(currentOffset, currentLimit)
    }.flatMapLatest { (currentOffset, currentLimit) ->
        val dbOffset = currentOffset * currentLimit

            getAllRolesUseCase()//currentLimit, dbOffset)
    }

    init {
        fetchRoleCount()
        viewModelScope.launch {
            roles.collectLatest { rolesDES ->
                Log.i("rolescheck", ": $rolesDES")
            }
        }
    }

    private fun fetchRoleCount() {
        viewModelScope.launch {
            _maxRoleCount.value = 1//getAllRolesUseCase.getRoleCount()
        }
    }


    fun addRole(role: RolesDE= RolesDE(roleName = "test")){
        viewModelScope.launch {
            insertRoleUseCase(role)
            fetchRoleCount()
        }
    }


    fun deleteRole(roleId: Long){
        viewModelScope.launch {
            val role: RolesDE = getRoleByIdUseCase(roleId)
            deleteRoleUseCase(role)
        }
    }

}