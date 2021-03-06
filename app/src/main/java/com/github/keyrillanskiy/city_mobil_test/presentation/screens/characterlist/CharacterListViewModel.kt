package com.github.keyrillanskiy.city_mobil_test.presentation.screens.characterlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.keyrillanskiy.city_mobil_test.data.common.Response
import com.github.keyrillanskiy.city_mobil_test.data.network.getCharacterInfo
import com.github.keyrillanskiy.city_mobil_test.data.network.getCharacterList
import com.github.keyrillanskiy.city_mobil_test.domain.models.CharacterInfoResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CharacterListViewModel : ViewModel() {

    private val _characterListLiveData = MutableLiveData<Response<List<CharacterInfoResponse>>>()
    val characterListLiveData: LiveData<Response<List<CharacterInfoResponse>>>
        get() = _characterListLiveData

    private val _characterLiveData = MutableLiveData<Response<CharacterInfoResponse>>()
    val characterLiveData: LiveData<Response<CharacterInfoResponse>>
        get() = _characterLiveData

    private var currentPage = 1

    /**
     * Делает запрос страницы с информацией о персонажах.
     * При успешном выполнении счетчик страниц инкрементируется.
     */
    fun fetchNextCharacterInfoPage() {
        GlobalScope.launch {
            getCharacterList(page = currentPage)
                .onEach { response -> if (response is Response.Success) currentPage++ }
                .collect { response -> _characterListLiveData.postValue(response) }
        }
    }

    /**
     * Делает запрос информации о персонаже.
     */
    fun fetchCharacterInfo(characterId: Int) {
        GlobalScope.launch {
            getCharacterInfo(characterId).collect { response -> _characterLiveData.postValue(response) }
        }
    }

}