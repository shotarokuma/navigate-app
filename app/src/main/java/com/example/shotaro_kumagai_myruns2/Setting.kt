package com.example.shotaro_kumagai_myruns2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout

class Setting : Fragment() {
    private lateinit var  setting: View
    private lateinit var intent:Intent
    private lateinit var profile: LinearLayout
    private lateinit var privacy: LinearLayout
    private lateinit var privacyCheck: CheckBox
    private lateinit var misc:LinearLayout
    private lateinit var uri: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setting = inflater.inflate(R.layout.fragment_setting, container, false)

        profile = setting.findViewById(R.id.profile)
        profile.setOnClickListener{
            intent = Intent(context,ProfileActivity::class.java)
            startActivity(intent)
        }

        privacy = setting.findViewById(R.id.privacy)
        privacyCheck = setting.findViewById(R.id.privacy_check)

        if(savedInstanceState?.getBoolean(PRIVACY) !== null){
            privacyCheck.isChecked = savedInstanceState.getBoolean(PRIVACY)
        }

        privacy.setOnClickListener{
            val check =  !(privacyCheck.isChecked)
            privacyCheck.isChecked = check
            savedInstanceState?.putBoolean(PRIVACY,check)
        }

        misc = setting.findViewById(R.id.misc)
        misc.setOnClickListener{
            uri = Uri.parse("https://www.sfu.ca/computing.html")
            intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return  setting
    }

    companion object{
        const val PRIVACY = "privacy"
    }
}