package com.github.keyrillanskiy.city_mobil_test.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import com.github.keyrillanskiy.city_mobil_test.data.common.Response
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

val CONNECT_TIMEOUT = TimeUnit.SECONDS.toMillis(30).toInt()
val READ_TIMEOUT = TimeUnit.SECONDS.toMillis(30).toInt()

val gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Gson() }
lateinit var connectivityManager: ConnectivityManager //сомнительное решение, зато функцией request удобно пользоваться

enum class HttpRequestMethod { GET }

/**
 * Инициализация [ConnectivityManager] для работы с сетью.
 * Нужно произвести инициализацию при старте приложения!
 */
fun initConnectivityManager(context: Context) {
    connectivityManager = getSystemService(context, ConnectivityManager::class.java)
        ?: throw IllegalArgumentException("connectivityManager is null")
}

@Suppress("DEPRECATION")
fun isNetworkAvailable(): Boolean {
    if (Build.VERSION.SDK_INT < 23) {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected &&
                (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
    } else {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}

/**
 * Обертка над [HttpURLConnection], которая сначала возвращает [Response.Loading],
 * а затем либо [Response.Success], либо какую-то ошибку [Response.Failure].
 * Если устройство не подключено к интернету, то сразу возвращается [Response.Failure.NetworkUnavailable].
 *
 * @param url Url строка запроса.
 * @param requestMethod Метод запроса.
 * @param headers Заголовки в виде набора "ключ-значение".
 * @param queryParameters Параметры запроса в виде набора "ключ-значение".
 * @param body Тело запроса в виде строки, может быть null.
 *
 * @return [Flow] с типизированным ответом.
 */
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun <T> requestAsync(
    responseType: Type,
    url: String,
    requestMethod: HttpRequestMethod = HttpRequestMethod.GET,
    headers: Map<String, String> = emptyMap(),
    queryParameters: Map<String, String> = emptyMap(),
    body: String? = null
): Flow<Response<T>> {
    return flow {
        if (isNetworkAvailable().not()) {
            emit(Response.Failure.NetworkUnavailable())
            return@flow
        }

        emit(Response.Loading())

        val queryString = queryParameters.toList()
            .mapIndexed { i, (key, value) -> if (i == 0) return@mapIndexed "?$key=$value" else "&$key=$value" }
            .joinToString(separator = "")
        val urlWithQuery = url + queryString

        val urlConnection = try {
            (URL(urlWithQuery).openConnection() as? HttpURLConnection
                ?: throw IllegalArgumentException("Not http connection"))
        } catch (e: IOException) {
            emit(Response.Failure.ConnectException<T>(e))
            return@flow
        }

        with(urlConnection) {
            connectTimeout = CONNECT_TIMEOUT
            readTimeout = READ_TIMEOUT

            setRequestMethod(requestMethod.name)
            headers.forEach { (key, value) -> addRequestProperty(key, value) }

            body?.let { bodyString ->
                doOutput = true
                OutputStreamWriter(outputStream).use { it.write(bodyString) }
            }
        }

        when (val errorCode = urlConnection.responseCode) {
            in 100..199 -> throw IllegalArgumentException("Unexpected response code")
            in 200..299 -> {
                val responseString = InputStreamReader(urlConnection.inputStream).readText()
                emit(Response.Success(gson.fromJson(responseString, responseType)))
            }
            in 300..399 -> throw IllegalArgumentException("Unexpected response code")
            in 400..499 -> Response.Failure.RequestException<T>(errorCode, urlConnection.responseMessage)
            in 500..599 -> Response.Failure.ServerException<T>(errorCode, urlConnection.responseMessage)
            else -> throw IllegalArgumentException("Unexpected response code")
        }
    }
}