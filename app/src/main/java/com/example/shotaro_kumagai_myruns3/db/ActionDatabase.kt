package com.example.shotaro_kumagai_myruns3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Action::class], version = 1)
abstract class ActionDatabase: RoomDatabase() {
    abstract val actionDao: ActionDao

    companion object{
        @Volatile
        private var INSTANCE: ActionDatabase? = null

        fun getInstance(context: Context): ActionDatabase{
            synchronized(this){}
            var instance = INSTANCE
            if (instance == null){
                instance = Room.databaseBuilder(context.applicationContext,
                    ActionDatabase::class.java,
                    "action__table").build()
                INSTANCE = instance
            }
            return instance
        }
    }
}