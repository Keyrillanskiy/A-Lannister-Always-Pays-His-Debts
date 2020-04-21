package com.github.keyrillanskiy.city_mobil_test.data.network

import com.github.keyrillanskiy.city_mobil_test.data.common.Response
import com.github.keyrillanskiy.city_mobil_test.domain.models.CharacterInfoResponse
import com.github.keyrillanskiy.city_mobil_test.utils.typeOf
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow

const val CHARACTER_PAGE_SIZE = 25
private const val API_URL = "https://anapioficeandfire.com/api/characters/"

/**
 * Получает список персонажей. Поддерживает пагинацию.
 *
 * @param page Номер страницы.
 * @param pageSize Размер страницы.
 *
 * @return [Flow] со списком персонажей.
 */
suspend fun getCharacterList(page: Int = 1, pageSize: Int = CHARACTER_PAGE_SIZE): Flow<Response<List<CharacterInfoResponse>>> {
    return requestAsync(
        responseType = typeOf<List<CharacterInfoResponse>>(),
        url = API_URL,
        queryParameters = mapOf("page" to page.toString(), "pageSize" to pageSize.toString())
    )
}

/**
 * Получает информацию о конкретном персонаже.
 *
 * @param characterId Идентификатор персонажа.
 *
 * @return [Flow] с информацией о персонаже.
 */
suspend fun getCharacterInfo(characterId: Int): Flow<Response<CharacterInfoResponse>> {
    return requestAsync(
        responseType = typeOf<CharacterInfoResponse>(),
        url = "$API_URL$characterId"
    )
}