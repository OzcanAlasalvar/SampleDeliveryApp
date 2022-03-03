package com.ozcanalasalvar.delivery_app.data.utils.network

sealed class NetworkResult<out T > {
    data class Success<out T >(val data: T) : NetworkResult<T>()
    data class Error(val error: NetworkErrors) : NetworkResult<Nothing>()

    object Loading : NetworkResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=${error.toString()}]"
            Loading -> "Loading"
        }
    }
}