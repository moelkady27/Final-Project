package com.example.finalproject.ui.profile.db

import androidx.room.TypeConverter
import com.example.finalproject.ui.profile.models.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserLocationConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return location?.let {
            gson.toJson(it)
        }
    }

    @TypeConverter
    fun toLocation(locationString: String?): Location? {
        return locationString?.let {
            gson.fromJson(it, object : TypeToken<Location>() {}.type)  // Convert JSON string to Location object
        }
    }
}