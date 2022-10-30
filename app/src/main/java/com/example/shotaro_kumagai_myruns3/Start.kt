package com.example.shotaro_kumagai_myruns3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner


class Start : Fragment() {
    private lateinit var start: View
    private lateinit var intent: Intent
    private lateinit var inputs: Array<String>
    private lateinit var activities: Array<String>
    private lateinit var inputSpinner: Spinner
    private lateinit var activitiesSpinner: Spinner
    private lateinit var inputAdapter: ArrayAdapter<String>
    private lateinit var activitiesAdapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        start =  inflater.inflate(R.layout.fragment_start, container, false)

        start.findViewById<Button>(R.id.start_button)?.setOnClickListener{
            intent = if (inputSpinner.selectedItem.toString() == "Manual Entry"){
                Intent(context, ManualActivity::class.java)
            }else{
                Intent(context, MapActivity::class.java)
            }
            startActivity(intent)
        }


        inputs = resources.getStringArray(R.array.inputs)
        inputSpinner = start.findViewById(R.id.inputs)
        inputAdapter = ArrayAdapter(start.context,android.R.layout.simple_list_item_1,inputs)
        inputSpinner.adapter = inputAdapter

        val selectedInput: String? = savedInstanceState?.getString(INPUT_TYPE,null)
        if(selectedInput !== null){
            inputSpinner.setSelection(inputAdapter.getPosition(selectedInput))
        }


        activities = resources.getStringArray(R.array.activities)
        activitiesSpinner = start.findViewById(R.id.activities)
        activitiesAdapter = ArrayAdapter(start.context, android.R.layout.simple_list_item_1,activities)
        activitiesSpinner.adapter = activitiesAdapter

        val selectedActivity : String? = savedInstanceState?.getString(ACTIVITIES_TYPE, null)
        if(selectedInput !== null){
            activitiesSpinner.setSelection(activitiesAdapter.getPosition(selectedActivity))
        }

        return start
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TYPE, inputSpinner.selectedItem.toString())
        outState.putString(ACTIVITIES_TYPE, activitiesSpinner.selectedItem.toString())
    }

    companion object {
        const val INPUT_TYPE = "input_type"
        const val ACTIVITIES_TYPE = "activities_type"
    }
}