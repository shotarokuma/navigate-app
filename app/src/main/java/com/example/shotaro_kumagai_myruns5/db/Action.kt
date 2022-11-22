package com.example.shotaro_kumagai_myruns5.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.util.Calendar

@Entity(tableName = "action_table")
data class Action (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "action_column")
    var inputType: Int = 0,
    var activityType: Int  = 0,
    var dateTime: Calendar = Calendar.getInstance(),
    var duration: Double = 0.0,
    var distance: Double = 0.0,
    var avgPace: Double = 0.0,
    var avgSpeed: Double = 0.0,
    var calorie: Double = 0.0,
    var climb: Double = 0.0,
    var heartRate: Double = 0.0,
    var comment: String  = "",
    var locationList: ArrayList<LatLng> = arrayListOf()
)