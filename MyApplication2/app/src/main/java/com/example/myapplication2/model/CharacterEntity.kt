package com.example.myapplication2.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication2.Disney.Character

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String


) {
    fun toCharacter() = Character(id, name, status, species, type, gender, image, episode, url, created)
}