package com.ozcanalasalvar.delivery_app.data.utils.network

import com.ozcanalasalvar.delivery_app.data.utils.toNetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    // responsible for getting data from database
    crossinline query: suspend () -> ResultType,
    // responsible for fetching new data from rest api
    crossinline fetch: suspend () -> RequestType,
    // responsible for taking data from fetch and saving it to database
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    // responsible for deciding new data from api is needed or cache is enough
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    // get one list of station from database
    val data = query()

    // if its time to update cache if data is decent or not
    val flow = if (shouldFetch(data)) {

        // loading and cache data
        emit(NetworkResult.Loading)

        try {

            // save data to catch
            saveFetchResult(fetch())

            // new data from api
            NetworkResult.Success(data)
        } catch (exception: Exception) {

            // error and cache data
            NetworkResult.Error(exception.toNetworkError())
        }
    } else {

        // cache data
       NetworkResult.Success(data)
    }

    emit(flow)
}.flowOn(Dispatchers.IO)