package com.cube.cubeacademy.lib.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cube.cubeacademy.lib.common.DATABASE_NAME
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.lib.room.dao.NominationDao
import com.cube.cubeacademy.lib.room.dao.NomineeDao

/**
 * The Room database for this app
 */
@Database(entities = [Nomination::class, Nominee::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun nominationDao(): NominationDao
    abstract fun nomineeDao(): NomineeDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}
