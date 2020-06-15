package com.example.prototype.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.prototype.Model.Student
import com.google.gson.Gson

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    val CREATE_STD =
        "CREATE TABLE $TABLE_STUDENT ($STD_ID INTEGER PRIMARY KEY, $NAME TEXT,$FEES TEXT,$GRADE TEXT,$START_DATE TEXT,$END_DATE TEXT,$EXIST_BOOL TEXT);"
    val CREATE_CLASS =
        "CREATE TABLE $TABLE_CLASS ($CLASS_ID INTEGER PRIMARY KEY,$DAY TEXT,$START_TIME TEXT,$END_TIME TEXT,FOREIGN KEY($CLASS_ID)REFERENCES $TABLE_STUDENT($STD_ID));"
    val CREATE_REPLACEMENT =
        "CREATE TABLE $TABLE_REPLACEMENT ($REPLACE_ID INTEGER,$DATE TEXT,$START_TIME TEXT,$END_TIME TEXT,FOREIGN KEY($REPLACE_ID)REFERENCES $TABLE_STUDENT($STD_ID));"
    val CREATE_ATTD =
        "CREATE TABLE $TABLE_ATTD ($ATTENDANCE_ID INTEGER PRIMARY KEY,$ATTENDANCE_BOOL TEXT,$DATE TEXT,$START_TIME TEXT,$END_TIME TEXT,$CLASS_TYPE TEXT,$REMARK TEXT);"

    //    val CLASS_V2 =
//        "ALTER TABLE $TABLE_CLASS ADD FOREIGN KEY($STD_ID) REFERENCES $TABLE_STUDENT($STD_ID) ON DELETE CASCADE; "
    var iCoount = 0
    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_STD)
        db.execSQL(CREATE_CLASS)
        db.execSQL(CREATE_REPLACEMENT)
        db.execSQL(CREATE_ATTD)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_STD = "DROP TABLE IF EXISTS $TABLE_STUDENT"
        val DROP_CLASS = "DROP TABLE IF EXISTS $TABLE_CLASS"
        val DROP_REPLACE = "DROP TABLE IF EXISTS $TABLE_REPLACEMENT"
        val DROP_ATTD = "DROP TABLE IF EXISTS $TABLE_ATTD"
        db.execSQL(DROP_STD)
        db.execSQL(DROP_CLASS)
        db.execSQL(DROP_REPLACE)
        db.execSQL(DROP_ATTD)
//
//        if(oldVersion<4){
//            db.execSQL(CLASS_V2)
//        }
        onCreate(db)
    }

    fun addTask(stdnt: Student): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
//        val gson = Gson()
//        val aryIngredient = gson.toJson(stdnt.aryLstIngredients).toString()
//        val arySteps = gson.toJson(stdnt.aryLstSteps).toString()

        values.put(NAME, stdnt.sName)
        values.put(FEES, stdnt.sFees)
        values.put(GRADE, stdnt.sGrade)
        values.put(EXIST_BOOL, stdnt.sExist)
        values.put(START_DATE, stdnt.sStartDate)
        values.put(EXIST_BOOL,"Y")
        val _success1 = db.insert(TABLE_STUDENT, null, values)
        if (_success1 > -1) {
            values.clear()
            val selectQuery ="SELECT * FROM $TABLE_STUDENT ORDER BY $STD_ID DESC LIMIT 1"
            val cursor =db.rawQuery(selectQuery,null)
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    val id=cursor.getInt(0)
                    values.put(CLASS_ID,id)
                }else{
                    Log.e("FAIL", "FAILL")
                }
            }
            values.put(DAY, stdnt.sDay)//class
            values.put(START_TIME, stdnt.sStartTime)//class
            values.put(END_TIME, stdnt.sEndTime)//class
        }
        val _success: Long
        _success = db.insert(TABLE_CLASS, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getTask(_id: Int): Student {
        val stdnt = Student()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_STUDENT WHERE $STD_ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        stdnt.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(STD_ID)))
//        stdnt.sStudentName = cursor.getString(cursor.getColumnIndex(NAME))
//        stdnt.sImage = cursor.getString(cursor.getColumnIndex(DAY))
        val aryIngredient = cursor.getString(cursor.getColumnIndex(FEES))
        val arSteps = cursor.getString(cursor.getColumnIndex(GRADE))
//        Log.i("Ingredients without :!:", stdnt.aryLstIngredients.toString())
//        Log.i("Steps without :!:", stdnt.aryLstSteps.toString())
        cursor.close()
        return stdnt
    }

    fun task(): ArrayList<Student> {
        val stdntList = ArrayList<Student>()
        val db = writableDatabase
        val selectQuery =
            "SELECT $TABLE_CLASS.$START_TIME,$TABLE_CLASS.$END_TIME,$TABLE_STUDENT.$STD_ID,$TABLE_CLASS.$DAY,$TABLE_STUDENT.$NAME FROM $TABLE_CLASS JOIN $TABLE_STUDENT ON $TABLE_CLASS.$CLASS_ID =$TABLE_STUDENT.$STD_ID WHERE $TABLE_STUDENT.$EXIST_BOOL = 'Y'"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val stdnt = Student()
                    stdnt.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(STD_ID)))
                    stdnt.sName = cursor.getString(cursor.getColumnIndex(NAME))
                    stdnt.sDay = cursor.getString(cursor.getColumnIndex(DAY))
//                    stdnt.sFees = cursor.getString(cursor.getColumnIndex(FEES))
//                    stdnt.sGrade = cursor.getString(cursor.getColumnIndex(GRADE))
                    stdnt.sStartTime = cursor.getString(cursor.getColumnIndex(START_TIME))
                    stdnt.sEndTime = cursor.getString(cursor.getColumnIndex(END_TIME))
//                    val gson = Gson()
//                    val ttIngredient = object : TypeToken<ArrayList<String>>() {}.type
//                    val aryLstIngredient: ArrayList<String> =
//                        gson.fromJson(sJsIngredient, ttIngredient)
//                    val ttSteps = object : TypeToken<ArrayList<String>>() {}.type
//                    val aryLstStep: ArrayList<String> = gson.fromJson(sJsSteps, ttSteps)
//                    Log.i("Ing", sJsIngredient)
//                    Log.i("Steps", sJsSteps)
//                    stdnt.aryLstIngredients = aryLstIngredient
//                    stdnt.aryLstSteps = aryLstStep
                    stdntList.add(stdnt)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return stdntList
    }

    fun getDay(sDay: String): ArrayList<Student> {
        val stdntList = ArrayList<Student>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CLASS JOIN $TABLE_STUDENT ON $TABLE_CLASS.$CLASS_ID =$TABLE_STUDENT.$STD_ID WHERE $TABLE_CLASS.$DAY='$sDay' AND $TABLE_STUDENT.$EXIST_BOOL = 'Y'"
//        val selectQuery = "SELECT * FROM $TABLE_CLASS WHERE $DAY = '$sDay'"
//        if (checkRecordExist() > 0) {
//            selectQuery =
//                "SELECT * FROM $TABLE_CLASS JOIN $TABLE_STUDENT ON $TABLE_CLASS.$CLASS_ID =$TABLE_STUDENT.$STD_ID WHERE $TABLE_STUDENT.$EXIST_BOOL = 'Y'"
//        }

        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val stdnt = Student()
                    stdnt.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(STD_ID)))//std

                    stdnt.sName = cursor.getString(cursor.getColumnIndex(NAME))//std
                    stdnt.sDay = cursor.getString(cursor.getColumnIndex(DAY))
                    stdnt.sFees = cursor.getString(cursor.getColumnIndex(FEES))//std
                    stdnt.sGrade = cursor.getString(cursor.getColumnIndex(GRADE))//std
                    stdnt.sStartTime = cursor.getString(cursor.getColumnIndex(START_TIME))
                    stdnt.sEndTime = cursor.getString(cursor.getColumnIndex(END_TIME))
                    Log.i("id",stdnt.sDay)
                    stdntList.add(stdnt)
                } while (cursor.moveToNext())
            }else{
                Log.i("bad","bad")
            }
        }
        cursor.close()
        return stdntList
    }

    fun updateTask(stdnt: Student): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
//        values.put(NAME, stdnt.sStudentName)
//        values.put(DAY, stdnt.sImage)
//        val aryIngredient = stdnt.aryLstIngredients.joinToString(":!:")
//        val arySteps = stdnt.aryLstSteps.joinToString(":!:")
//        values.put(FEES, aryIngredient)
//        values.put(GRADE, arySteps)
        val _success =
            db.update(TABLE_STUDENT, values, STD_ID + "=?", arrayOf(stdnt.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_STUDENT, STD_ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllTasks(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_STUDENT, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun checkRecordExist(): Int {
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_STUDENT LIMIT 1;"
        val cursor = db.rawQuery(selectQuery, null)
        iCoount = cursor.count
        return iCoount
    }

    fun addReplacement(id:Int,sTime:String,eTime:String,sDate:String):Boolean{
        val db= this.writableDatabase
        val values = ContentValues()
        values.put(REPLACE_ID,id)
        values.put(DATE,sDate)
        values.put(START_TIME,sTime)
        values.put(END_TIME,eTime)
        val _success=db.insert(TABLE_REPLACEMENT,null,values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }


    companion object {
        private val DB_VERSION = 3
        private val DB_NAME = "MyStudent"
        private val TABLE_STUDENT = "Student"
        private val TABLE_REPLACEMENT = "Replacement"
        private val TABLE_ATTD = "Attendance"
        private val TABLE_CLASS = "Class"
        private val STD_ID = "id"
        private val ATTENDANCE_ID = "attd_id"
        private val CLASS_ID = "class_id"
        private val REPLACE_ID = "replace_id"
        private val ATTENDANCE_BOOL = "att_bool"
        private val NAME = "name"
        private val FEES = "fees"
        private val GRADE = "grade"
        private val DAY = "day"
        private val DATE = "date"
        private val CLASS_DATE = "class_date"
        private val START_TIME = "starttime"
        private val END_TIME = "endtime"
        private val CLASS_TYPE = "class_type"
        private val REMARK = "remark"
        private val START_DATE = "start_date"
        private val END_DATE = "end_date"
        private val EXIST_BOOL = "exist_bool"

    }
}