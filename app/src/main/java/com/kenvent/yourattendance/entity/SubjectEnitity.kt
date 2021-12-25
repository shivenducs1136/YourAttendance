package com.kenvent.yourattendance.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_table")
class SubjectEnitity(
    @PrimaryKey
    @ColumnInfo(name = "SubjectName") val SubjectName: String,
    @ColumnInfo(name = "TotalClasses") val TotalClasses: String,
    @ColumnInfo(name = "AttendedClasses") val AttendedClasses: String,
    @ColumnInfo(name ="AbsentClasses") val AbsentClasses:String,
    @ColumnInfo(name = "criteria_percentage") val criteria_percentage:String,
    @ColumnInfo(name = "Status") val status:String,
    @ColumnInfo(name = "lastupdate") val lastupdate:String
    )