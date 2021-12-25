package com.kenvent.yourattendance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kenvent.yourattendance.dao.SubjectDao
import com.kenvent.yourattendance.entity.SubjectEnitity

@Database(entities = [SubjectEnitity::class],version = 1)
abstract class SubjectDatabase: RoomDatabase() {

    abstract fun subjectdao(): SubjectDao
    companion object{

        @Volatile
        private var INSTANCE: SubjectDatabase? = null
        fun getDatabase(context: Context): SubjectDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SubjectDatabase::class.java,
                    "subject_database"
                ).build()
                INSTANCE = instance

                instance
            }
        }

    }
}