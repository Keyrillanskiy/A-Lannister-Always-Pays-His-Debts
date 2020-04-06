package com.github.keyrillanskiy.city_mobil_test.data.common

/**
 * Класс для оборачивания данных, которые приходят из data source.
 * Таким образом данные "приобретают 3 состояния": успешно (success),
 * ошибка (failure) и загрузка (loading)
 */
@Suppress("unused")
sealed class Response<out T> {
    data class Success<out T>(val value: T) : Response<T>()

    class Loading<out T> : Response<T>()

    sealed class Failure<out T> : Response<T>() {
        /**
         * Выбрасывается, если устройство не подключено к интернету.
         */
        class NetworkUnavailable<out T>: Failure<T>()

        /**
         * Выбрасывается, если не удалось установить соединение.
         *
         * @param error Ошибка, из-за которой не удалось установить соединение.
         */
        class ConnectException<out T>(val error: Throwable): Failure<T>()

        /**
         * Выбрасывается, если запрос сделан неверно.
         *
         * @param errorCode Код статуса ответа (400..499).
         * @param responseMessage Сообщение статуса ответа.
         */
        class RequestException<out T>(val errorCode: Int, val responseMessage: String): Failure<T>()

        /**
         * Выбрасывается, если произошла ошибка на стороне сервера.
         *
         * @param errorCode Код статуса ответа (500..599).
         * @param responseMessage Сообщение статуса ответа.
         */
        class ServerException<out T>(val errorCode: Int, val responseMessage: String): Failure<T>()
    }
}
