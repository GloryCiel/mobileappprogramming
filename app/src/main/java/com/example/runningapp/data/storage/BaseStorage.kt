package com.example.runningapp.data.storage

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

abstract class BaseStorage {
    protected val gson = Gson()

    protected fun <T> saveToFile(context: Context, filename: String, data: T) {
        // T 타입의 데이터를 JSON으로 저장
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
        // JSON을 T 타입으로 변환하여 로드
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

    protected fun getNextId(context: Context, filename: String): Int {
        val currentData = loadFromFile<List<*>>(context, filename)
        return if (currentData.isNullOrEmpty()) {
            1  // 첫 번째 데이터면 1부터 시작
        } else {
            try {
                // LinkedTreeMap으로 변환된 객체에서 id를 안전하게 추출
                val maxId = currentData.maxOf { item ->
                    when (item) {
                        is Map<*, *> -> (item["id"] as? Number)?.toInt() ?: 0
                        else -> 0
                    }
                }
                maxId + 1
            } catch (e: Exception) {
                Log.e("BaseStorage", "Error getting next id: ${e.message}")
                currentData.size + 1
            }
        }
    }
}