package com.example.nntodo

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@RequiresApi(Build.VERSION_CODES.O)
@Database(entities = [TaskItem::class], version = 1, exportSchema = false)
abstract class TaskItemDatabase : RoomDatabase(){
    abstract fun taskItemDao(): TaskItemDao

    companion object{
        @Volatile
        private var INSTANCE: TaskItemDatabase? = null

        fun getDatabase(context: Context): TaskItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskItemDatabase::class.java,
                    "task_item_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}