package com.example.shotaro_kumagai_myruns3.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {
    @Insert
    suspend fun insertAction(action: Action)

    @Query("SELECT * FROM action_table")
    fun getAllActions(): Flow<List<Action>>

    @Query("DELETE FROM action_table WHERE id = :key")
    suspend fun deleteAction()
}