package com.example.shotaro_kumagai_myruns3.db

import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ActionRepository (private val actionDao: ActionDao){
    val allActions: Flow<List<Action>> = actionDao.getAllActions()

    fun insert(action: Action){
        CoroutineScope(IO).launch {
            actionDao.insertAction(action)
        }
    }

    fun delete(id: Long) {
        CoroutineScope(IO).launch {
            actionDao.deleteAction(id)
        }
    }
}