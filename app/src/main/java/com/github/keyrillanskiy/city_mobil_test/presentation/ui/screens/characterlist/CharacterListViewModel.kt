package com.github.keyrillanskiy.city_mobil_test.presentation.ui.screens.characterlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.keyrillanskiy.city_mobil_test.data.common.Response
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

}