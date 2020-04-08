package com.example.prototype.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.prototype.Adapter.TimeTableAdapter
import com.example.prototype.R
import com.google.android.material.tabs.TabLayout

class TimeTableFragment: Fragment() {

    lateinit var tabLayout:TabLayout
    lateinit var viewPager:ViewPager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout=view.findViewById(R.id.tabLayout)
        viewPager=view.findViewById(R.id.viewPager)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Mon"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Tue"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Wed"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Thur"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Fri"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Sat"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Sun"))

        val adapter =TimeTableAdapter(activity!!.supportFragmentManager)
        viewPager.adapter=adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })


    }
}