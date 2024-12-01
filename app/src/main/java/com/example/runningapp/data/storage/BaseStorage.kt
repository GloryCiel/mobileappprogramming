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
        val currentData = loadFromFile<List<Any>>(context, filename)
        return if (currentData.isNullOrEmpty()) {
            1  // 첫 번째 데이터면 1부터 시작
        } else {
            try {
                // 현재 저장된 데이터 중 가장 큰 ID + 1
                val maxId = currentData.maxOf { item ->
                    (item as? Map<*, *>)?.get("id") as? Int ?: 0
                }
                maxId + 1
            } catch (e: Exception) {
                currentData.size + 1  // 예외 발생시 리스트 크기 + 1
            }
        }
    }
}