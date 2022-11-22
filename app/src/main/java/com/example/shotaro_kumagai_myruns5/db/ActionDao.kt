package com.example.shotaro_kumagai_myruns5.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {
    @Insert
    fun insertAction(action: Action)

    @Query("SELECT * FROM action_table")
    fun getAllActions(): Flow<List<Action>>

    @Query("DELETE FROM action_table WHERE id = :key")
    fun deleteAction(key:Long)
}