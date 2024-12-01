package com.example.runningapp.data.storage

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

abstract class BaseStorage {
    protected val gson = Gson()

    protected fun <T> saveToFile(context: Context, filename: String, data: T) {
        try {
            val jsonString = gson.toJson(data)
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
            Log.d("Storage", "Successfully saved data to $filename")
        } catch (e: Exception) {
            Log.e("Storage", "Error saving to $filename: ${e.message}")
        }
    }

    protected inline fun <reified T> loadFromFile(context: Context, filename: String): T? {
        return try {
            val file = File(context.filesDir, filename)
            if (!file.exists()) return null

            val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
            gson.fromJson(jsonString, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            Log.e("Storage", "Error loading from $filename: ${e.message}")
            null
        }
    }
}