package com.dzq.coursedesign_android.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtil {
    val gson: Gson = GsonBuilder()
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