package com.example.prototype.Fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.prototype.Database.DatabaseHandler
import com.example.prototype.Model.Student
import com.example.prototype.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class AddReplacementFragment : Fragment() {
    lateinit var btnSubmit: Button
    lateinit var btnCancel: Button
    lateinit var spName: Spinner
    lateinit var tvStart: TextView
    lateinit var tvEnd: TextView
    lateinit var tvDate: TextView
    var sStartTime = ""
    var sEndTime = ""
    var sDate = ""
    var aryLstName = ArrayList<String>()
    var aryStd = ArrayList<Student>()
    var dbHandler: DatabaseHandler? = null
    var aryDay =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_replacement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmit = view.findViewById(R.id.btn_submit)
        btnCancel = view.findViewById(R.id.btn_cancel)
        spName = view.findViewById(R.id.sp_stdnt_replace)
        tvStart = view.findViewById(R.id.tv_start_time)
        tvEnd = view.findViewById(R.id.tv_end_time)
        dbHandler = DatabaseHandler(view.context)

        spName.adapter = activity?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                aryLstName
            )
        }

        btnSubmit.setOnClickListener {
            submit()
        }

        btnCancel.setOnClickListener {
            findNavController().navigate(
                R.id.action_addReplacementFragment_to_timeTableFragment,
                null
            )
        }

        if (aryStd.size <= 0) {
            var iName = 0
            aryLstName.clear()
            aryStd = (dbHandler as DatabaseHandler).task()
            do {
                aryLstName.add(aryStd[iName].sName)
                iName++
            } while (iName < aryStd.size)
        }

        tvStart.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                tvStart.text = SimpleDateFormat("HH:mm").format(cal.time)
                sStartTime = tvStart.text.toString()
            }
            TimePickerDialog(
                view.context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
        tvEnd.visibility = View.GONE

        tvStart.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (tvStart.text != getString(R.string.start_time)) {
                    tvEnd.visibility = View.VISIBLE
                    Log.i("Clicked", "cliedk")
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        tvEnd.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                tvEnd.text = SimpleDateFormat("HH:mm").format(cal.time)
                sEndTime = tvEnd.text.toString()
            }
            TimePickerDialog(
                view.context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        tvDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in TextView
                        sDate = "$dayOfMonth/$monthOfYear/$year"
                        tvDate.setText(sDate)
                    },
                    year,
                    month,
                    day
                )
            }
            dpd!!.show()
        }
    }

    fun checkOverlap(): Boolean {
        if (sDate.isEmpty()) {
            return false
        }
        val format1 = SimpleDateFormat("dd/MM/yyyy")
        val dt1 = format1.parse(sDate)
        val format2: DateFormat = SimpleDateFormat("EEEE")
        val sDay: String = format2.format(dt1)


        val aryLstDay = dbHandler!!.getDay(sDay)

        if (aryLstDay.size > 0 && sStartTime.isNotBlank() && sEndTime.isNotBlank()) {
            for (item in aryLstDay) {
                if (item.sStartTime.isEmpty() || item.sEndTime.isEmpty()) {
                    continue
                }

                val sIStart = LocalTime.parse(sStartTime)
                val sFStart = LocalTime.parse(item.sStartTime)
                val sFEnd = LocalTime.parse(item.sEndTime)

                if ((sIStart >= sFStart) && (sIStart <= sFEnd)) {
                    Log.i("Time", "False")
                    return false
                }
            }
        } else {
            return false
        }
        return true
    }

    fun submit() {
        var success = false
        if (spName.selectedItem.toString()
                .isNotBlank() && sStartTime.isNotBlank() && sEndTime.isNotBlank() && sDate.isNotBlank() && checkOverlap()
        ) {
            val aName = spName.selectedItem.toString()
            val id = aryStd[spName.selectedItemPosition].id

            val _success = dbHandler!!.addReplacement(id, sStartTime, sEndTime, sDate)
            if (_success) {
                success = true
            }

        }

        if (success) {
            findNavController().navigate(
                R.id.action_addReplacementFragment_to_timeTableFragment,
                null
            )
        } else {
            sDate = ""
            sEndTime = ""
            sStartTime = ""
            tvDate.setText(sDate)
            tvStart.setText(sStartTime)
            tvEnd.setText(sEndTime)
            Log.i("Failed", "Try again")
        }
    }
}