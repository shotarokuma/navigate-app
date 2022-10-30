package com.example.shotaro_kumagai_myruns3.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.shotaro_kumagai_myruns3.history.History
import com.example.shotaro_kumagai_myruns3.R
import com.example.shotaro_kumagai_myruns3.setting.Setting
import com.example.shotaro_kumagai_myruns3.start.Start
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy


class MainActivity : AppCompatActivity() {
    private lateinit var history: History
    private lateinit var  setting: Setting
    private lateinit var  start: Start
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var myFragmentStateAdapter: MyFragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private val tabTitles = arrayOf("Start", "History", "Setting")
    private lateinit var tabConfigurationStrategy: TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager2 = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab)


        history = History()
        setting = Setting()
        start = Start()

        fragments = arrayListOf(start, history, setting)

        myFragmentStateAdapter =  MyFragmentStateAdapter(this, fragments)
        viewPager2.adapter = myFragmentStateAdapter
        tabConfigurationStrategy = TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
            tab.text = tabTitles[position] }

        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()
    }


    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}