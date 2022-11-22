package com.example.shotaro_kumagai_myruns5.db

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng

class ActionViewModel(private val repository: ActionRepository):ViewModel() {
    val allActionsLiveData: LiveData<List<Action>> = repository.allActions.asLiveData()

    fun eachAction(index: Int): Action? {
        val actionList = allActionsLiveData.value
        return actionList?.get(index)
    }

    fun latestLocationList():ArrayList<LatLng>{
        var latest: Int = 0
        val data: List<Action>? = allActionsLiveData.value
        if(data == null || data.isEmpty()){
            return ArrayList()
        }
        for(i in data.indices){
                if(data[latest].dateTime < data[i].dateTime){
                    latest = i
                }
            }
       return allActionsLiveData.value!![latest].locationList
    }

    fun insert(action: Action){
        sum += 1
        repository.insert(action)
    }

    fun deleteSelect(id:Long){
            repository.delete(id)
        }

    companion object{
        var sum: Int = 0
    }
}

class ActionViewModelFactory (private val repository: ActionRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(ActionViewModel::class.java))
            return ActionViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}