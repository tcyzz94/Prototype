package com.example.prototype.Fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.prototype.Adapter.MondayRvAdapter
import com.example.prototype.Adapter.TimeTableAdapter
import com.example.prototype.Database.DatabaseHandler
import com.example.prototype.Model.Student
import com.example.prototype.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.dialog_replacement.view.*

class TimeTableFragment : Fragment() {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var fbReplacement: FloatingActionButton
    var aryLstName = ArrayList<Student>()
    var aryDay =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var dbHandler: DatabaseHandler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)
        fbReplacement = view.findViewById(R.id.fb_repalcement)
        dbHandler = DatabaseHandler(view.context)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Mon"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Tue"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Wed"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Thur"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Fri"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Sat"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Sun"))

        val adapter = TimeTableAdapter(childFragmentManager)

        fbReplacement.setOnClickListener {
            findNavController().navigate(R.id.action_timeTableFragment_to_addReplacementFragment,null)
        }
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position

                Log.i("time table", viewPager.currentItem.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                Log.i("time table", "unselect")
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                Log.i("time table", "reselect")
            }
        })


    }
}