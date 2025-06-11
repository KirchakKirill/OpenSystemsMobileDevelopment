package com.example.myapplication2

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.Disney.Character
import com.example.myapplication2.repository.CharacterRepository
import kotlinx.coroutines.launch

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {
    var currentPage = 1
    val pageSize = 20

    var characters by mutableStateOf<List<Character>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isEnd by mutableStateOf(false)


    fun loadPrevPage() {
        if (currentPage <= 1 || isLoading) return
        changePage(currentPage - 1)
    }

    fun loadNextPage() {
        if (isLoading || isEnd) return
        changePage(currentPage + 1)
    }

    private fun changePage(newPage: Int) {
        currentPage = newPage
        characters = emptyList()
        isEnd = false
        viewModelScope.launch {
            isLoading = true
            try {
                val newChars = repository.getCharacters(currentPage, pageSize)
                characters = newChars
                isEnd = newChars.isEmpty()
            } catch (e: Exception) {
                Log.e("ViewModel", "Error loading page $currentPage", e)
            } finally {
                isLoading = false
            }
        }
    }




    suspend fun getCharacterById(id: Int) {
        val character = repository.getCharacterById(id)
        characters = characters + character
    }

}