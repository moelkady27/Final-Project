package com.example.finalproject.ui.profile.db

import androidx.room.TypeConverter
import com.example.finalproject.ui.profile.models.Image
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UserImageConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromImage(image: Image?): String {
        return gson.toJson(image)
    }

    @TypeConverter
    fun toImage(imageString: String?): Image? {
        if (imageString == null) {
            return null
        }
        return gson.fromJson(
            imageString,
            object : TypeToken<Image?>() {}.type
        )
    }
}