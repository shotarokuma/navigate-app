package com.example.shotaro_kumagai_myruns5.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shotaro_kumagai_myruns5.R
import com.example.shotaro_kumagai_myruns5.db.*

class History : Fragment() {
    private lateinit var history: View
    private lateinit var arrayList: ArrayList<Action>
    private lateinit var  historyList: ListView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModelFactory: ActionViewModelFactory
    private lateinit var database: ActionDatabase
    private lateinit var databaseDao: ActionDao
    private lateinit var repository: ActionRepository
    private lateinit var actionViewModel: ActionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        history = inflater.inflate(R.layout.fragment_history, container,false)


        database = ActionDatabase.getInstance(requireActivity())
        databaseDao = database.actionDao
        repository = ActionRepository(databaseDao)
        viewModelFactory = ActionViewModelFactory(repository)
        actionViewModel = ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]

        arrayList = ArrayList()
        historyList = history.findViewById(R.id.history_list_view)
        historyAdapter = HistoryAdapter(requireActivity(),arrayList)
        historyList.adapter = historyAdapter

        actionViewModel.allActionsLiveData.observe(requireActivity(), Observer{ it ->
            historyAdapter.replace(it)
            historyAdapter.notifyDataSetChanged()
        })

        return history
    }

    override fun onResume() {
        super.onResume()
        actionViewModel.allActionsLiveData.observe(requireActivity(), Observer{ it ->
            historyAdapter.replace(it)
            historyAdapter.notifyDataSetChanged()
        })
    }
}