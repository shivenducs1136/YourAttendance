package com.kenvent.yourattendance.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kenvent.yourattendance.R
import com.kenvent.yourattendance.entity.SubjectEnitity
import com.kenvent.yourattendance.ui.HomeFragment
import com.mikhaellopez.circularprogressbar.CircularProgressBar


import android.view.View.OnLongClickListener
import android.widget.ImageView
import android.widget.ProgressBar
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.math.RoundingMode
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList


class SubjectAdapter(val context: Context, val listener: HomeFragment): RecyclerView.Adapter<SubjectAdapter.NoteViewHolder>() {

    val allSubject = ArrayList<SubjectEnitity>()
    var stack = ArrayDeque<Boolean>()
    var i=0
    var currdatetime =""
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val SubjectName = itemView.findViewById<TextView>(R.id.subject_name)
        val subjectattended = itemView.findViewById<TextView>(R.id.subject_attended)
        val subjectpresent = itemView.findViewById<MaterialCardView>(R.id.subject_present)
        var subjectabsent = itemView.findViewById<MaterialCardView>(R.id.subject_absent)
        val subjectprogressgreen = itemView.findViewById<CircularProgressIndicator>(R.id.subject_ProgressBar_green)
        val subjectprogressred = itemView.findViewById<CircularProgressIndicator>(R.id.subject_ProgressBar_red)
        val subjectprogressyellow = itemView.findViewById<CircularProgressIndicator>(R.id.subject_ProgressBar_yellow)
        val subjectupdate = itemView.findViewById<ConstraintLayout>(R.id.subject_update)
        val status = itemView.findViewById<TextView>(R.id.status)
        val subjectper = itemView.findViewById<TextView>(R.id.subject_percentage)
        val undobtn = itemView.findViewById<ImageView>(R.id.undo)
        val editsub = itemView.findViewById<ImageView>(R.id.edit_update)
        val lastupdate = itemView.findViewById<TextView>(R.id.lasty)
        val redbar = itemView.findViewById<ImageView>(R.id.red_attandance_bar)
        val greenbar = itemView.findViewById<ImageView>(R.id.green_attendance_bar)
        val yellowbar = itemView.findViewById<ImageView>(R.id.yellow_attendance_bar)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false))

        viewHolder.itemView.setOnLongClickListener(OnLongClickListener {
                listener.dialogBox(allSubject[viewHolder.adapterPosition])
            true // <- set to true
        })
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentsubject = allSubject[position]
        holder.SubjectName.text = currentsubject.SubjectName
        holder.subjectattended.text = "${currentsubject.AttendedClasses} / ${currentsubject.TotalClasses}"
        holder.subjectupdate.visibility = View.GONE
        var per = (currentsubject.AttendedClasses.toDouble()/currentsubject.TotalClasses.toDouble()) * 100
        per = roundOffDecimal(per)
        holder.subjectper.text =
            "${per}%"
        if(currentsubject.lastupdate.isNullOrEmpty()){
            holder.lastupdate.text = ""
        }
            holder.lastupdate.text = currentsubject.lastupdate
        if(per.toFloat() > currentsubject.criteria_percentage.toFloat()+3){
            holder.greenbar.visibility = View.VISIBLE
            holder.redbar.visibility = View.INVISIBLE
            holder.yellowbar.visibility = View.INVISIBLE
            holder.subjectprogressgreen.visibility = View.VISIBLE
            holder.subjectprogressgreen.progress = per.toInt()
            holder.subjectprogressyellow.visibility = View.INVISIBLE
            holder.subjectprogressred.visibility = View.INVISIBLE
        }
        else if(per.toFloat() >= currentsubject.criteria_percentage.toFloat()-1 && per.toFloat() <= currentsubject.criteria_percentage.toFloat()+3){
            holder.greenbar.visibility = View.INVISIBLE
            holder.redbar.visibility = View.INVISIBLE
            holder.yellowbar.visibility = View.VISIBLE
            holder.subjectprogressyellow.visibility = View.VISIBLE
            holder.subjectprogressyellow.progress = per.toInt()
            holder.subjectprogressgreen.visibility = View.INVISIBLE
            holder.subjectprogressred.visibility = View.INVISIBLE
        }
        else{
                holder.greenbar.visibility = View.INVISIBLE
                holder.redbar.visibility = View.VISIBLE
             holder.yellowbar.visibility = View.INVISIBLE
            holder.subjectprogressred.visibility = View.VISIBLE
            holder.subjectprogressred.progress = per.toInt()
            holder.subjectprogressyellow.visibility = View.INVISIBLE
            holder.subjectprogressgreen.visibility = View.INVISIBLE
        }
        var status :String =""
        status = calculatestatus(currentsubject.AttendedClasses.toInt(),currentsubject.TotalClasses.toInt(),currentsubject.criteria_percentage.toFloat())
        holder.status.text = status
        holder.editsub.setOnClickListener {
            var currdatetime:String = getDateTime().toString()
            listener.makedialogue(currentsubject.SubjectName, currentsubject.criteria_percentage,status,currdatetime)
            holder.subjectattended.text = "${currentsubject.AttendedClasses} / ${currentsubject.TotalClasses}"
        }
        holder.itemView.setOnClickListener {
            if(holder.subjectupdate.isVisible){
                holder.subjectupdate.visibility = View.GONE
            }
            else{
                holder.subjectupdate.visibility = View.VISIBLE
                holder.subjectpresent.setOnClickListener {
                    currdatetime = getDateTime().toString()
                    listener.updateSubject(currentsubject.SubjectName,status,"${currentsubject.TotalClasses.toInt()+1}","${currentsubject.AttendedClasses.toInt()+1}",currdatetime)
                    listener.gottyanim(1)
                    stack.add(true)
                }
                holder.subjectabsent.setOnClickListener {
                    currdatetime = getDateTime().toString()
                    holder.lastupdate.text = currdatetime
                    currdatetime= getDateTime().toString()
                    listener.updateSubject(currentsubject.SubjectName,status,"${currentsubject.TotalClasses.toInt()+1}","${currentsubject.AttendedClasses.toInt()}",currdatetime)
                    stack.add(false)
                    listener.gottyanim(2)
//                    listener.anackbar("Marked Absent")
                    i++
                }
                holder.undobtn.setOnClickListener {
                    if(stack.isNotEmpty()){
                        var flag = stack.last()
                        if (flag) {
                            currdatetime = getDateTime().toString()
                            listener.updateSubject(
                                currentsubject.SubjectName,
                                status,
                                "${currentsubject.TotalClasses.toInt() - 1}",
                                "${currentsubject.AttendedClasses.toInt() - 1}"
                            ,currdatetime)
                            listener.anackbar("Reverted Changes")
                        } else {
                            currdatetime = getDateTime().toString()
                            listener.updateSubject(
                                currentsubject.SubjectName,
                                status,
                                "${currentsubject.TotalClasses.toInt() - 1}",
                                "${currentsubject.AttendedClasses.toInt()}"
                            ,currdatetime)
                            listener.anackbar("Reverted Changes")
                        }
                        stack.removeLast()
                    }
                    else{
                        listener.anackbar("Nothing to revert.")
                    }
                }
            }

        }

    }

    private fun calculatestatus(present: Int, totalclass: Int, criteriaper: Float): String {


        var per = (present.toDouble()/totalclass.toDouble()) * 100
        per = roundOffDecimal(per)
        var status: String = ""
        var totcl : Int = totalclass
        var pres : Int = present
        if (per > criteriaper) {
            var count: Int = 0
            while (per >= criteriaper) {
                per = ((present.toDouble()) / (totcl.toDouble())) * 100
                count++
                totcl++
            }
            if((count-2)!=0)
                status = "You may leave ${count-2} classes and you will be on track"
            else{
                status = "You are on the track"
            }
        }
        else if(per<criteriaper){
            var count: Int = 0
            while (per <= criteriaper) {

                per = ((pres.toDouble()) / (totcl.toDouble())) * 100
                count++
                pres++
                totcl++
            }
            if((count-2)!=0)
                status = "You have to attend ${count-2} classes to maintain your track"
            else{
                status = "You are on the track"
            }
        }
        else {
            status = "You are on the track"
        }
        return status
    }
    override fun getItemCount(): Int {
        return allSubject.size
    }
    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }
    fun updateSubjectList(newlist:List<SubjectEnitity>){
        allSubject.clear()
        allSubject.addAll(newlist)
        notifyDataSetChanged()
    }
    fun getDateTime(): String? {
        val currentDateTimeString: String = DateFormat.getDateTimeInstance()
            .format(Date())
        return currentDateTimeString
    }
}
interface INotesRvAdapter{
    fun onItemClicked(subjectEnitity: SubjectEnitity)
}