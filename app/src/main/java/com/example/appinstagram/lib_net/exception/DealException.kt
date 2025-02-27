package com.example.appinstagram.lib_net.exception

import android.net.ParseException
import com.google.gson.JsonParseException

import org.json.JSONException
import retrofit2.HttpException

import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.net.UnknownServiceException
import javax.net.ssl.SSLHandshakeException


object DealException {

    fun handlerException(t: Throwable): ResultException {
        val ex: ResultException
        if (t is ResultException) {
            ex = t
        } else if (t is HttpException) {
            ex = when (t.code()) {
                ApiResultCode.UNAUTHORIZED,
                ApiResultCode.FORBIDDEN,
                ApiResultCode.NOT_FOUND -> ResultException(
                    t.code().toString(),
                    "Lỗi mạng"
                )
                ApiResultCode.REQUEST_TIMEOUT,
                ApiResultCode.GATEWAY_TIMEOUT -> ResultException(
                    t.code().toString(),
                    "Kết nối mạng bị timeout"
                )
                ApiResultCode.INTERNAL_SERVER_ERROR,
                ApiResultCode.BAD_GATEWAY,
                ApiResultCode.SERVICE_UNAVAILABLE -> ResultException(
                    t.code().toString(),
                    "Lỗi Server"
                )
                else -> ResultException(t.code().toString(), "Lỗi không xác định")
            }
        } else if (t is JsonParseException
            || t is JSONException
            || t is ParseException
        ) {
            ex = ResultException(
                ApiResultCode.PARSE_ERROR,
                "Lỗi phân tích dữ liệu"
            )
        } else if (t is SocketException) {
            ex = ResultException(
                ApiResultCode.REQUEST_TIMEOUT.toString(),
                "Lỗi kết nối, thử lại"
            )
        } else if (t is SocketTimeoutException) {
            ex = ResultException(
                ApiResultCode.REQUEST_TIMEOUT.toString(),
                "Mạng bị Timeout"
            )
        } else if (t is SSLHandshakeException) {
            ex = ResultException(
                ApiResultCode.SSL_ERROR,
                "Lỗi chứng chỉ SSL"
            )
            return ex
        } else if (t is UnknownHostException) {
            ex = ResultException(
                ApiResultCode.UNKNOW_HOST,
                "Lỗi mạng, thử lại với mạng khác"
            )
            return ex
        } else if (t is UnknownServiceException) {
            ex = ResultException(
                ApiResultCode.UNKNOW_HOST,
                "Lỗi mạng, thử lại với mạng khác"
            )
        } else if (t is NumberFormatException) {
            ex = ResultException(
                ApiResultCode.UNKNOW_HOST,
                "Lỗi định dạng số"
            )
        } else {
            ex = ResultException(
                ApiResultCode.UNKNOWN,
                "Lỗi không xác định"
            )
        }
        return ex
    }
}