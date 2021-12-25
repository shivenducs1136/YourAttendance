package com.kenvent.yourattendance.repository

import androidx.lifecycle.LiveData
import com.kenvent.yourattendance.dao.SubjectDao
import com.kenvent.yourattendance.entity.SubjectEnitity

class SubjectRepo(private  val subjectdao: SubjectDao) {

    val allNotes: LiveData<List<SubjectEnitity>> = subjectdao.getSubjectDao()


     fun insert(subjectEnitity: SubjectEnitity){
        subjectdao.insert(subjectEnitity)
    }
     fun delete(subjectEnitity: SubjectEnitity){
        subjectdao.delete(subjectEnitity)
    }
    fun  update(Subjectname: String, status: String, TotalClasses: String, AttendedClasses: String,currdatetime:String){
        subjectdao.updateSubject(Subjectname,status,TotalClasses,AttendedClasses,currdatetime)
    }
    fun getSubject(){
        subjectdao.getSubjectDao()
    }
    fun deleteall(){
        subjectdao.deletall()
    }
}