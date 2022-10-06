package com.example.shotaro_kumagai_myruns2

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment

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
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        builder.setTitle("Duration")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        return  builder.create()
    }


    private fun createDistance():Dialog{
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        builder.setTitle("Distance")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        return  builder.create()
    }


    private fun createCalories():Dialog{
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        builder.setTitle("Calories")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        return  builder.create()
    }


    private fun crateHeartRate():Dialog{
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.input_dialog, null)
        builder.setView(view)
        builder.setTitle("Heart Rate")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        return  builder.create()
    }


    private fun crateNote():Dialog{
        val builder = AlertDialog.Builder(requireActivity())
        val view: View = requireActivity().layoutInflater.inflate(R.layout.note_dialog, null)
        builder.setView(view)
        builder.setTitle("Comments")
        builder.setNegativeButton("cancel", this)
        builder.setPositiveButton("OK", this)
        return  builder.create()
    }





    override fun onClick(dialog: DialogInterface, item: Int) {}
}