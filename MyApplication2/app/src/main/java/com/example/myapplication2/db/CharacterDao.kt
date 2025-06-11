package com.example.myapplication2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication2.model.CharacterEntity
import kotlin.coroutines.Continuation

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters LIMIT :limit OFFSET :offset")
    suspend fun getCharactersPaged(limit: Int, offset: Int): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)
}