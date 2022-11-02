package com.dzq.coursedesign_android.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object GsonUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate> {
            override fun serialize(
                src: LocalDate?,
                typeOfSrc: Type?,
                context: JsonSerializationContext?
            ): JsonElement {
                return JsonPrimitive(src?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        })
        .registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDate {
                val datetime = json?.asJsonPrimitive?.asString
                return LocalDate.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
        })
        .registerTypeAdapter(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime> {
            override fun serialize(
                src: LocalDateTime?,
                typeOfSrc: Type?,
                context: JsonSerializationContext?
            ): JsonElement {
                return JsonPrimitive(src?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        })
        .registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): LocalDateTime {
                val datetime = json?.asJsonPrimitive?.asString?.replace("T", " ")
                return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
        })
        .serializeNulls()
        .setLenient()
        .create()

    inline fun <reified T> fromJson(json: String?): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun objToJson(obj: Any?): String {
        return gson.toJson(obj)
    }
}