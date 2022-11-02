package com.example.shotaro_kumagai_myruns3.start

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shotaro_kumagai_myruns3.Dialog
import com.example.shotaro_kumagai_myruns3.R
import java.util.*


class ManualActivity : AppCompatActivity() {
    private lateinit var listMenu: ListView
    private lateinit var menuArray: Array<String>
    private lateinit var menuAdapter: ArrayAdapter<String>
    private lateinit var vm: DialogViewModel
    private val calender : Calendar = Calendar.getInstance()
    private val year: Int = calender.get(Calendar.YEAR)
    private  val month: Int = calender.get(Calendar.MONTH)
    private val day: Int = calender.get(Calendar.DAY_OF_MONTH)
    private val hour = calender.get(Calendar.HOUR)
    private val min = calender.get(Calendar.MINUTE)
    private val durBundle = Bundle()
    private val disBundle = Bundle()
    private val calBundle = Bundle()
    private val heartBundle = Bundle()
    private val noteBundle = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)

        vm = ViewModelProvider(this)[DialogViewModel::class.java]

        listMenu = findViewById(R.id.manual_menu)
        menuArray = resources.getStringArray(R.array.menu)
        menuAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,menuArray)
        listMenu.adapter = menuAdapter

        listMenu.setOnItemClickListener{ _, _, position, _  ->
            when(position){
                0 -> crateDateDialog()
                1 -> crateTimeDialog()
                2 -> crateDialog(Dialog.DURATION, "duration",durBundle)
                3 -> crateDialog(Dialog.DISTANCE, "distance",disBundle)
                4 -> crateDialog(Dialog.CALORIES,"calories",calBundle)
                5 -> crateDialog(Dialog.HEART_RATE, "heart-rate",heartBundle)
                6 -> crateDialog(Dialog.NOTE, "note", noteBundle)
            }
        }
    }

    private fun crateDateDialog(){
        val date = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{_,year,month,dayOfMonth->
            vm.dateTime.value?.set(year,month, dayOfMonth)
            println(vm.dateTime.value)
        }, year,month,day
        )
        date.show()
    }

    private fun crateTimeDialog(){
        val time = TimePickerDialog(this,TimePickerDialog.OnTimeSetListener{_,hour,min->
            vm.dateTime.value?.set(Calendar.HOUR, hour)
            vm.dateTime.value?.set(Calendar.MINUTE, min)
        },hour,min,false)
        time.show()
    }

    private fun crateDialog(type: Int, tag: String, bundle:Bundle){
        val dialog = Dialog()
        bundle.putInt(Dialog.DIALOG_KEY, type)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, tag)
    }

    fun onSave(view: View){
        Toast.makeText(this, "Entry Saved", Toast.LENGTH_SHORT).show()
        finish()
    }

    fun onCancel(view:View){
        Toast.makeText(this, "Entry Discard", Toast.LENGTH_SHORT).show()
        finish()
    }
}