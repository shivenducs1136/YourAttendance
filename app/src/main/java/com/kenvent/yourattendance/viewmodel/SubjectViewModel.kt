package com.kenvent.yourattendance.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kenvent.yourattendance.database.SubjectDatabase
import com.kenvent.yourattendance.entity.SubjectEnitity
import com.kenvent.yourattendance.repository.SubjectRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SubjectViewModel(application: Application): AndroidViewModel(application) {

    val allSubjects: LiveData<List<SubjectEnitity>>
    private val repository: SubjectRepo
    init{
        val dao = SubjectDatabase.getDatabase(application).subjectdao()
        repository = SubjectRepo(dao)
        allSubjects = repository.allNotes
    }

    fun deleteFriend(subjectEnitity: SubjectEnitity) = viewModelScope.launch(    Dispatchers.IO){

        repository.delete(subjectEnitity)
    }

    fun insertFriend(subjectEnitity: SubjectEnitity)= viewModelScope.launch (Dispatchers.IO){
        repository.insert(subjectEnitity)
    }
    fun updateSubject(Subjectname: String, status: String, TotalClasses: String, AttendedClasses: String,currdatetime:String)= viewModelScope.launch (Dispatchers.IO){
        repository.update(Subjectname,status,TotalClasses,AttendedClasses,currdatetime)
    }
    fun getSub()=viewModelScope.launch (Dispatchers.IO){
        repository.getSubject()
    }
    fun deleteall() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteall()
    }
}