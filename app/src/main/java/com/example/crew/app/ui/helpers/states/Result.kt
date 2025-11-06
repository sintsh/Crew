package com.example.crew.app.ui.helpers.states

sealed class Result<out T>(data: T?=null, message: String?=null) {
    data class Success<T>(val data: T): Result<T>(data)

    data class Error(val message: String): Result<Nothing>(message = message)

    class Loading<T>(): Result<T>()
}