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
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.prototype.Database.DatabaseHandler
import com.example.prototype.Model.Student
import com.example.prototype.R
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class AddStudentFragment : Fragment() {

    private lateinit var etName: TextInputLayout
    private lateinit var etFees: TextInputLayout
    private lateinit var spDay: Spinner
    private lateinit var spGrade: Spinner
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private lateinit var tvStart: TextView
    private lateinit var tvEnd: TextView
    private lateinit var tvStartDate: TextView
    var aryDay =
        arrayOf("Monday", "Tuesday", "Wednesday", "Thusrsday", "Friday", "Saturday", "Sunday")
    var aryGrade = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "FUN")
    var sDay: String = ""
    var sStartTime: String = ""
    var sEndTime: String = ""
    var sGrade: String = ""
    var sStartDate: String = ""

    var dbHandler: DatabaseHandler? = null
    private var aryLstDay = ArrayList<Student>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(
            R.layout.fragment_add_student, container,
            false
        )
        etName = view.findViewById(R.id.til_name)
        etFees = view.findViewById(R.id.til_fees)
        spDay = view.findViewById(R.id.sp_day)
        spGrade = view.findViewById(R.id.sp_grade)
        btnSubmit = view.findViewById(R.id.btn_submit)
        btnCancel = view.findViewById(R.id.btn_cancel)
        tvStart = view.findViewById(R.id.tv_start_time)
        tvEnd = view.findViewById(R.id.tv_end_time)
        tvStartDate = view.findViewById(R.id.tv_start_date)

        spDay.adapter = activity?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                aryDay
            )
        }
        spGrade.adapter = activity?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                aryGrade
            )
        }

        dbHandler = DatabaseHandler(view.context)
        btnSubmit.setOnClickListener { submit() }
        btnCancel.setOnClickListener { findNavController().navigate(R.id.action_addStudentFragment_to_homedest) }// use action id to pop up back to home fragment, else it will crate new instance of the fragment

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

        tvStartDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in TextView
                        sStartDate = "$dayOfMonth/$monthOfYear/$year"
                        tvStartDate.setText(sStartDate)
                    },
                    year,
                    month,
                    day
                )
            }
            dpd!!.show()
        }
        return view
    }

    fun submit() {
        // check if the data exist in the databae or not,cannot duplicate id, and overlapp schedule
        val sName = etName.editText!!.text.toString()
        val sFees = etFees.editText!!.text.toString()
        val student = Student()
        var success = false
        var validData = false

        if (sName.isNotBlank() && sFees.isNotBlank() && sStartTime.isNotBlank() && sEndTime.isNotBlank() && sStartDate.isNotBlank() && checkOverlap()) {
            student.sName = sName
            student.sFees = sFees
            student.sStartTime = sStartTime
            student.sEndTime = sEndTime
            student.sStartDate = sStartDate
            validData = true
        } else {
            validData = false
            sStartTime = ""
            sEndTime = ""
            tvStart.text = getString(R.string.start_time)
            tvEnd.text = getString(R.string.end_time)
            Log.i("sName is error", sName)
        }

        sDay = spDay.selectedItem.toString()
        sGrade = spGrade.selectedItem.toString()

        student.sDay = sDay
        student.sGrade = sGrade

        if (validData) {
            success = dbHandler!!.addTask(student)
        } else {
            Toast.makeText(
                context,
                "Input again",
                Toast.LENGTH_LONG
            ).show()
        }
        if (success) {
            Log.i("Db data added", "scuessssss")
            findNavController().navigate(R.id.action_addStudentFragment_to_homedest)
        } else {
            Log.e("Failed", "RIP DB")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("hahahahaha", "asdgaskhdgjasd")
    }

    fun checkOverlap(): Boolean {
        aryLstDay.clear()
        sDay = spDay.selectedItem.toString()
        if (dbHandler!!.checkRecordExist() > 0) {
            aryLstDay = dbHandler!!.getDay(sDay)
        }

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
        }
        return true
    }
}