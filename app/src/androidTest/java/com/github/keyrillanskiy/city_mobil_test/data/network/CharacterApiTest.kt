package com.github.keyrillanskiy.city_mobil_test.data.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.keyrillanskiy.city_mobil_test.data.common.Response
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharacterApiTest {

    @Test
    fun getCharacterList_NoParams_ResultSuccess() {
        runBlocking {
            val flow = getCharacterList()

            flow.collect { response -> if (response is Response.Failure) fail() }
        }
    }

    @Test
    fun getCharacterList_2And3Page_ResultsNotEqual() {
        runBlocking {
            val flowPage2 = getCharacterList(page = 2)
            val flowPage3 = getCharacterList(page = 3)

            val page2 = flowPage2.toList().last() as Response.Success
            val page3 = flowPage3.toList().last() as Response.Success

            assertNotEquals(page2.value.first(), page3.value.first())
        }
    }

    @Test
    fun getCharacterList_PageSize50_PageSizeActually50() {
        val pageSize = 50
        runBlocking {
            val flow = getCharacterList(pageSize = pageSize)

            flow.collect { response ->
                if (response is Response.Success) {
                    assertEquals(response.value.count(), pageSize)
                }
            }
        }
    }

    @Test
    fun getCharacterList_PageSize51_PageSizeStill50() {
        val pageSize = 51
        runBlocking {
            val flow = getCharacterList(pageSize)

            flow.collect { response ->
                if (response is Response.Success) {
                    assertEquals(response.value.count(), 50)
                }
            }
        }
    }

    @Test
    fun getCharacterList_PageSize0_ServerException() {
        val pageSize = 0
        runBlocking {
            val flow = getCharacterList(pageSize)

            flow.collect { response -> if (response is Response.Success) fail() }
        }
    }

    @Test
    fun getCharacterInfo_CharacterId1_ResultSuccess() {
        runBlocking {
            val characterId = 1
            val flow = getCharacterInfo(characterId)

            flow.collect { response -> if (response is Response.Failure) fail() }
        }
    }

    @Test
    fun getCharacterInfo_CharacterId583_ResultJonSnow() {
        runBlocking {
            val jonSnowId = 583
            val flow = getCharacterInfo(jonSnowId)

            flow.collect { response -> if (response is Response.Success) assertEquals(response.value.name, "Jon Snow") }
        }
    }

}