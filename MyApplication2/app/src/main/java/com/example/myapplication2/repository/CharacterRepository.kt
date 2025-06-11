package com.example.myapplication2.repository

import android.util.Log
import com.example.myapplication2.db.CharacterDao
import com.example.myapplication2.RickAndMortyApiService
import com.example.myapplication2.Disney.Character

class CharacterRepository(
    private val api: RickAndMortyApiService,
    private val dao: CharacterDao
) {
    suspend fun getCharacters(page: Int, pageSize: Int): List<Character> {
        val offset = (page - 1) * pageSize
        val local = dao.getCharactersPaged(pageSize, offset).map { it.toCharacter() }

        return if (local.size < pageSize) {
            try {
                val response = api.getCharacters(page)
                val remote = response.results
                dao.insertAll(remote.map { it.toEntity() })
                remote
            } catch (e: Exception) {
                if (local.isNotEmpty()) local else throw e
            }
        } else {
            local
        }
    }

    suspend fun getCharacterById(id: Int): Character {
        val dbChar = dao.getCharacterById(id)?.toCharacter()
        return if (dbChar != null) {
            Log.d("REPO", "From DB: ${dbChar.image}") // Логируем URL
            dbChar
        } else {
            val apiChar = api.getCharacterById(id)
            Log.d("REPO", "From API: ${apiChar.image}") // Логируем URL
            dao.insertAll(listOf(apiChar.toEntity()))
            apiChar
        }
    }

}
