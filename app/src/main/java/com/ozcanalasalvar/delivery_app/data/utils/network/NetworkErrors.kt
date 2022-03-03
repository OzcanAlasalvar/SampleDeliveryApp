package com.ozcanalasalvar.delivery_app.data.utils.network

import com.ozcanalasalvar.delivery_app.R
import java.lang.Exception

sealed class NetworkErrors {
    data class NoInternet(val exception: Exception) : NetworkErrors()
    data class Timeout(val exception: Exception) : NetworkErrors()
    data class Unknown(val exception: Exception) : NetworkErrors()

     fun toErrorMessage(): Int {
        return when (this) {
            is NoInternet -> R.string.err_http_internet
            is Timeout -> R.string.err_http_time_out
            is Unknown -> R.string.err_http_unkown
        }
    }
}