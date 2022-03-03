package com.ozcanalasalvar.delivery_app.data.utils

import com.ozcanalasalvar.delivery_app.data.utils.network.NetworkErrors
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


fun Exception.toNetworkError(): NetworkErrors {
    return when (this) {
        is UnknownHostException, is SocketTimeoutException -> {
            NetworkErrors.NoInternet(this)
        }
        is TimeoutException -> {
            NetworkErrors.Timeout(this)
        }
        else -> {
            NetworkErrors.Unknown(this)
        }
    }
}