package com.example.shotaro_kumagai_myruns4

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.shotaro_kumagai_myruns4.start.manual.DialogViewModel

class Dialog : DialogFragment(), DialogInterface.OnClickListener{
    companion object{
        const val DIALOG_KEY = "key"
        const val UNIT_PREFERENCE = 0
        const val COMMENTS = 1
        const val DURATION = 2
        const val DISTANCE = 3
        const val CALORIES = 4
        const val HEART_RATE = 5
        const val NOTE = 7
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val bundle = arguments
        return when (bundle?.getInt(DIALOG_KEY)) {
            UNIT_PREFERENCE -> {
                createUnitPreference()
            }
            COMMENTS -> {
                createComments()
            }
            DURATION -> {
                createDuration()
            }
            DISTANCE -> {
                createDistance()
            }
            CALORIES -> {
                createCalories()
            }
            HEART_RATE -> {
                crateHeartRate()
            }
            else -> {
                crateNote()
            }
        }
    }

    private fun createUnitPreference():Dialog{
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.radio_dialog, null)
        builder.setView(view)
        builder.setTitle("Unit Preference")
        builder.setNegativeButton("cancel", this)
        val unit: Unit = Unit.getInstance()
        view.findViewById<RadioButton>(R.id.miles).isChecked = unit.isMile
        view.findViewById<RadioButton>(R.id.meter).isChecked = !unit.isMile
        view.findViewById<RadioGroup>(R.id.unit_radio).setOnCheckedChangeListener{_,_ ->
            unit.isMile = !unit.isMile
        }
        return builder.create()
    }


    private fun createComments(): Dialog{
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        builder.setTitle("Comments")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        return  builder.create()
    }

    private fun createDuration():Dialog{
        val vm: DialogViewModel = ViewModelProvider(requireActivity())[DialogViewModel::class.java]
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        val input: EditText = view.findViewById(R.id.input)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setTitle("Duration")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK") { _, _ ->
            if(input.text.toString() != ""){
                vm.duration.value =  input.text.toString().toDouble()
            }
        }
        return  builder.create()
    }


    private fun createDistance():Dialog{
        val vm: DialogViewModel = ViewModelProvider(requireActivity())[DialogViewModel::class.java]
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        val input: EditText = view.findViewById(R.id.input)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setTitle("Distance")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK") { _, _ ->
            if(input.text.toString() != ""){
                vm.distance.value = input.text.toString().toDouble()
            }
        }
        return  builder.create()
    }


    private fun createCalories():Dialog{
        val vm: DialogViewModel = ViewModelProvider(requireActivity())[DialogViewModel::class.java]
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        val input: EditText = view.findViewById(R.id.input)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setTitle("Calories")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        builder.setPositiveButton("OK") { _, _ ->
            if(input.text.toString() != ""){
                vm.calorie.value = input.text.toString().toDouble()
            }
        }
        return  builder.create()
    }


    private fun crateHeartRate():Dialog{
        val vm: DialogViewModel = ViewModelProvider(requireActivity())[DialogViewModel::class.java]
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        val input: EditText = view.findViewById(R.id.input)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setTitle("Heart Rate")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK") { _, _ ->
            if(input.text.toString() != ""){
                vm.heartRate.value = input.text.toString().toDouble()
            }
        }
        return  builder.create()
    }


    private fun crateNote():Dialog{
        val vm: DialogViewModel = ViewModelProvider(requireActivity())[DialogViewModel::class.java]
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.note_dialog, null)
        builder.setView(view)
        val input: TextView = view.findViewById(R.id.input)
        builder.setTitle("Comments")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        builder.setPositiveButton("OK") { _, _ ->
            vm.comment.value = input.text.toString()
        }
        return  builder.create()
    }

    override fun onClick(dialog: DialogInterface, item: Int) {}
}