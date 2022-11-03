package com.example.shotaro_kumagai_myruns3.history

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.shotaro_kumagai_myruns3.R
import com.example.shotaro_kumagai_myruns3.Unit
import com.example.shotaro_kumagai_myruns3.db.Action
import java.text.SimpleDateFormat

class HistoryAdapter (
    private val context: Context,
    private var actionList: List<Action>
    ) : BaseAdapter(){
    private var layoutInflater: LayoutInflater? = null
    private lateinit var intent: Intent

    override fun getItem(position: Int): Any {
        return actionList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return actionList.size
    }

    override fun getView(
        position: Int,
        target: View?,
        parent: ViewGroup?
    ): View {
        var target: View? = target
        if (layoutInflater == null) {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if(target == null){
            target = layoutInflater!!.inflate(R.layout.history_list, null)
        }
        val actionData:TextView = target!!.findViewById(R.id.action_data)
        val timeData: TextView = target!!.findViewById(R.id.time_data)

        val sdf = SimpleDateFormat("HH:mm:ss MMM dd yyyy")
        val unit: Unit = Unit.getInstance()
        val fixUnit: Double = if (unit.isMile) 1.0 else 1.60934
        val strUnit: String = if (unit.isMile) "Miles" else "Kilometer"

        actionData.text = "${context.resources.getStringArray(R.array.inputs)[actionList[position].inputType]}" +
                ":${context.resources.getStringArray(R.array.activities)[actionList[position].activityType]}" +
                "${sdf.format(actionList[position].dateTime.time)}"
        timeData.text = "${actionList[position].distance * fixUnit} ${strUnit}, 0sec"

        target.setOnClickListener{
            intent = Intent(context, EachActionActivity::class.java)
            intent.putExtra(EACH_ACTION, position)
            context.startActivity(intent)
        }
        return target
    }

    fun replace(newActionList: List<Action>){
        actionList = newActionList
    }

    companion object{
        const val EACH_ACTION = "each_action"
    }
}