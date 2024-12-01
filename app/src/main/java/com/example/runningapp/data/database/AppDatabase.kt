package com.example.runningapp.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.runningapp.data.database.dao.CommunityPostDao
import com.example.runningapp.data.database.dao.CrewPostDao
import com.example.runningapp.data.database.dao.RunningCourseDao
import com.example.runningapp.data.database.dao.UserDao
import com.example.runningapp.data.database.entities.CommunityPost
import com.example.runningapp.data.database.entities.CrewPost
import com.example.runningapp.data.database.entities.RunningCourse
import com.example.runningapp.data.database.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, RunningCourse::class, CrewPost::class, CommunityPost::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun runningCourseDao(): RunningCourseDao
    abstract fun crewPostDao(): CrewPostDao
    abstract fun communityPostDao(): CommunityPostDao

    // DB init
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Log.d("AppDatabase", "Creating new database instance")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                Log.d("AppDatabase", "Database instance created")
                instance
            }
        }

        fun deleteDatabase(context: Context) {
            Log.d("AppDatabase", "Attempting to delete database")
            context.deleteDatabase("app_database")
            INSTANCE = null  // INSTANCE도 null로 초기화
            Log.d("AppDatabase", "Database deleted and instance nullified")
        }
    }

    // Test 데이터 추가  /* Dao 를 보면 suspend 로 함수가 구성 => Coroutine 으로 접근 해야 함 */
    private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("AppDatabase", "onCreate called")
            scope.launch {
                INSTANCE?.userDao()?.let { userDao ->
                    for (i in 1..10) {
                        val rank = "Rank $i"
                        val preferredRegion = if (i % 2 == 0) "수성구" else "중구"

                        userDao.insert(
                            User(
                                profilePhotoPath = "file:///android_asset/defaultProfile.png",
                                name = "test_user $i",
                                rank = rank,
                                preferredRegion = preferredRegion
                            )
                        )
                        Log.d("AppDatabase", "Inserted user: $i")
                    }
                }
            }
        }
    }

}
