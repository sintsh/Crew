package com.example.crew.app.ui.helpers.states

sealed class UIState<T>(val data:T? = null, val message:String?=null) {

    class Redirect<T>(url:T, message: String?=null): UIState<T>(data = url, message = message)

    class Loading<T>(): UIState<T>()

    class Error<T>(errorMessage: String): UIState<T>(message = errorMessage)

    class Success<T>(data:T, responseMessage: String?=null): UIState<T>(data = data, message = responseMessage)

}