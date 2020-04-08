package com.example.prototype.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.prototype.Fragment.*

class TimeTableAdapter(fm:FragmentManager):FragmentPagerAdapter(fm) {
    val weekDay= arrayOf("1","2","3","4","5","6","7")
    override fun getItem(position: Int): Fragment {
        when(position){
            1->return TuesdayFragment()
            2->return WednesdayFragment()
            3->return ThursdayFragment()
            4->return FridayFragment()
            5->return SaturdayFragment()
            6->return SundayFragment()
            else ->return MondayFragment()
        }
    }

    override fun getCount(): Int {
        return weekDay.size
    }
}