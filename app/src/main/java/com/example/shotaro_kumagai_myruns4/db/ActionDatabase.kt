package com.example.shotaro_kumagai_myruns4.db

import android.content.Context
import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

@Database(entities = [Action::class], version = 1)
@TypeConverters(CalenderConverter::class, LangConverter::class)
abstract class ActionDatabase: RoomDatabase() {
    abstract val actionDao: ActionDao

    companion object{
        @Volatile
        private var INSTANCE: ActionDatabase? = null

        fun getInstance(context: Context) : ActionDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        ActionDatabase::class.java, "action_table").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

class CalenderConverter{
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value?.let { value ->
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = value
        }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis
}

class LangConverter{
    @TypeConverter
    fun latLngToString(locationList: ArrayList<LatLng>) : String {
        val stringList: List<String> = locationList.map {
            "(${it.latitude},${it.longitude}"
        }
        return stringList.joinToString("-")
    }

    @TypeConverter
    fun stringToLatLng(string: String) : ArrayList<LatLng>{
        val locationList: ArrayList<LatLng> = arrayListOf()
        val stringList:List<String> = string.split('-')
        stringList.forEach(){
            val s: String = it.replace("(", "").replace(")", "")
            val list: List<String> = s.split(",")
            if(list.first() != "" && list.last() != ""){
                locationList.add(LatLng(list.first().toDouble(), list.last().toDouble()))
            }
        }
        return locationList
    }
}
