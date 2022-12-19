package com.openclassrooms.firebaseREM.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.firebaseREM.model.ElementRoom
import com.openclassrooms.firebaseREM.model.PropertyRoom

@Database(entities = [PropertyRoom::class, ElementRoom::class], version = 1)

abstract class REMRoomDatabase : RoomDatabase() {

    abstract fun remDao(): REMDao

    companion object {
        @Volatile
        private var INSTANCE: REMRoomDatabase? = null
        private val LOCK = Any()

        fun getDatabase(
            context: Context,
        ): REMRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(LOCK) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    REMRoomDatabase::class.java,
                    "rem_database"
                )
                    //.allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
                INSTANCE = instance
                instance
            }
        }
    }

}
