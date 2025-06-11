package com.example.myapplication2.Disney

import com.example.myapplication2.model.CharacterEntity

data class Character(
    val id: Int,
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
    fun toEntity() = CharacterEntity(id, name, status, species, type, gender, image, episode, url, created)



    fun getEpisodesGroupedBySeason(): Map<Int, List<String>> {
        return episode.map { url ->
            val episodeId = url.substringAfterLast("/").toIntOrNull() ?: 0
            val season = (episodeId / 10) + 1
            val episodeNum = episodeId % 10
            season to "E${episodeNum.toString().padStart(2, '0')}"
        }.groupBy({ it.first }, { it.second })
    }

}
