package com.kenvent.yourattendance.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kenvent.yourattendance.entity.SubjectEnitity

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(subjectEnitity: SubjectEnitity)

    @Delete
     fun delete(subjectEnitity: SubjectEnitity)

    @Query("DELETE FROM subject_table")
    fun deletall()

    @Query("SELECT * FROM subject_table")
    fun getSubjectDao(): LiveData<List<SubjectEnitity>>

    @Query("UPDATE subject_table SET TotalClasses = :TotalClasses ,AttendedClasses= :AttendedClasses,Status= :status , lastupdate=:currdatetime WHERE SubjectName LIKE :Subjectname ")
    fun updateSubject(Subjectname: String, status: String, TotalClasses: String, AttendedClasses: String,currdatetime:String)
}