package com.example.prototype.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.prototype.Adapter.MondayRvAdapter
import com.example.prototype.Database.DatabaseHandler
import com.example.prototype.Interface.ISchedule
import com.example.prototype.Model.Student
import com.example.prototype.R

class MondayFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, ISchedule {
    private lateinit var rvMonday: RecyclerView
    private lateinit var adpMonday: MondayRvAdapter
    var dbHandler: DatabaseHandler? = null
    private lateinit var mSwipeRecyclerView: SwipeRefreshLayout
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private var aryStudent = ArrayList<Student>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_monday, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHandler = DatabaseHandler(view.context)
        rvMonday = view.findViewById(R.id.rv_monday)
        mSwipeRecyclerView = view.findViewById(R.id.swipe)
        mLinearLayoutManager = LinearLayoutManager(context)
        rvMonday.layoutManager = mLinearLayoutManager

        adpMonday = MondayRvAdapter(aryStudent)
        adpMonday.setCallBackListener(this)
        rvMonday.adapter = adpMonday
        mSwipeRecyclerView.setOnRefreshListener(this)
    }

    override fun scheduleCallBack(stdnt: Student) {
        Log.i("Student", stdnt.sName)
    }

    override fun onRefresh() {
        updateList()
        mSwipeRecyclerView.isRefreshing = false
    }

    fun updateList() {
        aryStudent = (dbHandler as DatabaseHandler).getDay("Monday")

        for(std in aryStudent){
            Log.i("Student name",std.sName);
        }
        val student = Student()
        student.sName="aa"
        aryStudent.add(student)
        adpMonday.updateList(aryStudent)
    }

}