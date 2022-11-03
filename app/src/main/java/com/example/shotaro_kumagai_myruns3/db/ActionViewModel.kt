package com.example.shotaro_kumagai_myruns3.db

import androidx.lifecycle.*

class ActionViewModel(private val repository: ActionRepository):ViewModel() {
    val allActionsLiveData: LiveData<List<Action>> = repository.allActions.asLiveData()

    fun eachAction(index: Int): Action? {
        val actionList = allActionsLiveData.value
        return actionList?.get(index)
    }

    fun insert(action: Action){
        repository.insert(action)
    }

    fun deleteSelect(id:Long){
            repository.delete(id)
        }
}

class ActionViewModelFactory (private val repository: ActionRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(ActionViewModel::class.java))
            return ActionViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}