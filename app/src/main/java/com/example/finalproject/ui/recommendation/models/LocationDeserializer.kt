package com.example.finalproject.ui.recommendation.models

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class LocationDeserializer : JsonDeserializer<Location> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Location {
        return if (json.isJsonObject) {
            val jsonObject = json.asJsonObject
            Location(
                city = jsonObject.get("city")?.asString ?: "",
                coordinates = jsonObject.get("coordinates")?.asJsonArray?.map { it.asDouble } ?: listOf(),
                country = jsonObject.get("country")?.asString ?: "",
                fullAddress = jsonObject.get("fullAddress")?.asString ?: "",
                state = jsonObject.get("state")?.asString ?: "",
                type = jsonObject.get("type")?.asString ?: ""
            )
        } else {
            Location("", listOf(), "", json.asString, "", "")
        }
    }
}