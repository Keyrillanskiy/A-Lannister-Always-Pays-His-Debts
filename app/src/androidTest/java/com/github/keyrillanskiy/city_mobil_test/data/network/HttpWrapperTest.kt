package com.github.keyrillanskiy.city_mobil_test.data.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.keyrillanskiy.city_mobil_test.data.common.Response
import com.github.keyrillanskiy.city_mobil_test.utils.typeOf
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HttpWrapperTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val connectivityManager = initConnectivityManager(context)

    @Test
    fun initConnectivityManager_NoExceptions() {
        initConnectivityManager(context)
    }

    @Test
    fun isNetworkAvailable_ReturnTrueWhenAvailable() {
        val result = isNetworkAvailable(connectivityManager)

        assertTrue(result)
    }

    @Test
    fun requestAsync_NoParams_ResponseSuccess() {
        runBlocking {
            val flow = requestAsync<Any>(typeOf<Any>(), API_URL)

            flow.collect { response -> if (response is Response.Failure) fail() }
        }
    }

    @Test
    fun requestAsync_InvalidUrl_RequestException() {
        val url = "${API_URL}some_invalid_path"

        runBlocking {
            val flow = requestAsync<Any>(typeOf<Any>(), url)

            flow.collect { response ->
                when (response) {
                    is Response.Success,
                    is Response.Failure.NetworkUnavailable,
                    is Response.Failure.ConnectException,
                    is Response.Failure.ServerException -> fail()
                    is Response.Failure.RequestException, is Response.Loading -> { /* success */
                    }
                }
            }
        }
    }

    @Test
    fun requestAsync_NoParams_JsonParsingSuccess() {
        val responseType = typeOf<Map<String, String>>()

        runBlocking {
            val flow = requestAsync<Map<String, String>>(responseType, API_URL)

            flow.collect { response ->
                when (response) {
                    is Response.Failure -> fail()
                    is Response.Success -> {
                        assertTrue(response.value.isNotEmpty())
                        assertTrue(response.value.entries.first().value.isNotBlank())
                    }
                }
            }
        }
    }

    companion object {
        private const val API_URL = "https://anapioficeandfire.com/api/"
    }

}