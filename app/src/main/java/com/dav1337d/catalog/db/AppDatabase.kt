package com.dav1337d.catalog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RoomSeriesMovie::class, RoomBook::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun roomSeriesDao(): RoomSeriesDao
    abstract fun roomBookDao(): RoomBookDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "catalogDB")
                .build()
        }
    }
}