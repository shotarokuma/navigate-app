package com.example.shotaro_kumagai_myruns3.db

import androidx.lifecycle.*

class ActionViewModel(private val repository: ActionRepository):ViewModel() {
    val allActionsLiveData: LiveData<List<Action>> = repository.allActions.asLiveData()

    fun insert(action: Action){
        repository.insert(action)
    }

    fun deleteSelect(index:Int){
        val actionList = allActionsLiveData.value
        if (actionList != null && actionList.isNotEmpty()){
            val id = actionList[index].id
            repository.delete(id)
        }
    }
}

class CommentViewModelFactory (private val repository: ActionRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(ActionViewModel::class.java))
            return ActionViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}