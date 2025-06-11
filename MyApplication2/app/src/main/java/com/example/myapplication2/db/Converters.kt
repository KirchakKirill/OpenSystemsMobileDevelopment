package com.example.myapplication2.db

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromList(list: List<String>) = Gson().toJson(list)

    @TypeConverter
    fun toList(json: String) = Gson().fromJson(json, Array<String>::class.java).toList()
}