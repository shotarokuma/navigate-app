package com.example.shotaro_kumagai_myruns3.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.shotaro_kumagai_myruns3.R
import com.example.shotaro_kumagai_myruns3.Unit
import com.example.shotaro_kumagai_myruns3.db.*
import java.text.SimpleDateFormat

class EachActionActivity : AppCompatActivity() {
    private var ind = -1
    private var target: Action? = null
    private lateinit var viewModelFactory: ActionViewModelFactory
    private lateinit var database: ActionDatabase
    private lateinit var databaseDao: ActionDao
    private lateinit var repository: ActionRepository
    private lateinit var actionViewModel: ActionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_each_action)


        database = ActionDatabase.getInstance(this)
        databaseDao = database.actionDao
        repository = ActionRepository(databaseDao)
        viewModelFactory = ActionViewModelFactory(repository)
        actionViewModel = ViewModelProvider(this, viewModelFactory)[ActionViewModel::class.java]
        ind = intent.getIntExtra(HistoryAdapter.EACH_ACTION, -1)
        actionViewModel.allActionsLiveData.observe(this, Observer{ _ ->
            target = actionViewModel.eachAction(ind)
            findViewById<EditText>(R.id.input_type).setText(resources.getStringArray(R.array.inputs)[target?.inputType?.toInt()!!])
            findViewById<EditText>(R.id.activity_type).setText(resources.getStringArray(R.array.activities)[target?.activityType?.toInt()!!])
            val sdf = SimpleDateFormat("HH:mm:ss MMM dd yyyy")
            findViewById<EditText>(R.id.date_and_time).setText(sdf.format(target?.dateTime?.time))
            findViewById<EditText>(R.id.duration).setText(target?.duration!!.toInt().toString() + "secs")

            val unit: Unit = Unit.getInstance()
            val fixUnit: Double = if (unit.isMile) 1.0 else 1.60934
            val strUnit: String = if (unit.isMile) "Miles" else "Kilometer"

            findViewById<EditText>(R.id.distance).setText((target!!.distance * fixUnit).toString() + strUnit)
            findViewById<EditText>(R.id.calories).setText(target?.calorie!!.toInt().toString() + "cals")
            findViewById<EditText>(R.id.heart_rate).setText(target?.heartRate!!.toInt().toString() + "bpm")
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId != R.id.delete_button) {
            return  false
        }
        val id: Long = target?.id ?: return false
        ind -= 1
        actionViewModel.deleteSelect(id)
        finish()
        return true
    }
}