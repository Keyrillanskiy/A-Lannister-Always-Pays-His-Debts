package com.github.keyrillanskiy.city_mobil_test.utils

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Функция для более красивого кода получения [Type].
 */
fun <T> typeOf(): Type {
    return object : TypeToken<T>() {}.type
}