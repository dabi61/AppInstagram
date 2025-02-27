package com.example.appinstagram.lib_net.net

import com.example.appinstagram.lib_net.exception.DealException
import com.example.appinstagram.lib_net.exception.ResultException
import com.example.appinstagram.lib_net.model.BaseModel
import com.example.appinstagram.lib_net.model.NetResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


open class BaseRepository {

    // Thực hiện call api
    suspend fun <T : Any> callRequest(
        call: suspend () -> NetResult<T>
    ): NetResult<T> {
        return try {
            call() // Gọi api
        } catch (e: Exception) {
            e.printStackTrace()
            NetResult.Error(DealException.handlerException(e)) // Bắt lỗi
        }
    }

    // Xử lý kết quả trả về
    suspend fun <T : Any> handleResponse(
        response: BaseModel<T>,
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): NetResult<T> {
        return coroutineScope {
            if (response.errorCode == -1) {
                errorBlock?.let { it() } // Xử lý khi có lỗi
                NetResult.Error(
                    ResultException(
                        response.errorCode.toString(),
                        response.errorMsg
                    )
                )
            } else {
                successBlock?.let { it() } // Xử lý khi thành công
                NetResult.Success(response.data)
            }
        }
    }


}