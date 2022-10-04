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
        const val UNIT_PREFERENCE = 6
        const val COMMENTS = 1
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
            else -> {
                createUnitPreference()
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

    override fun onClick(dialog: DialogInterface, item: Int) {}
}