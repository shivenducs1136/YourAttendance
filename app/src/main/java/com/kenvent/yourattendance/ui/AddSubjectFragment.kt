package com.kenvent.yourattendance.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kenvent.yourattendance.R
import com.kenvent.yourattendance.databinding.FragmentAddSubjectBinding
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import android.net.NetworkInfo
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService





class AddSubjectFragment : Fragment() {

    lateinit var binding: FragmentAddSubjectBinding
    var value: Int = 0
    var subject_name: String =""
    var total_present: String = ""
    var total_classes: String = ""
    var flag : Int = 1
    var status = ""
    lateinit var s:String
     var criteriaper = ""
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddSubjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        binding.slider.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
            this.value = binding.slider.value.toInt()
            binding.attendanceCriteriaPercentage.text = "${value} %"
        }

        binding.donebtn.setOnClickListener {
            subject_name = binding.subjectNameEdittext.text.toString()
            total_classes = binding.totalClassesEdittext.text.toString()
            total_present = binding.subjectPresentEdittext.text.toString()
//            Log.e("subject_name",subject_name+" ")
//            Log.e("total_classes",total_classes+" ")
//            Log.e("total_present",total_present+" ")
//            Log.e("criteria_percentage",binding.slider.value.toString()+" ")
            if(total_present.isNotEmpty() && total_classes.isNotEmpty() && checkstring(total_present) && checkstring(total_classes)) {
                status = calculatestatus(
                    total_present.toInt(),
                    total_classes.toInt(),
                    binding.slider.value.toInt()
                )
            }else{
                Snackbar.make(requireView(),"Please enter the required details", Snackbar.LENGTH_SHORT).show()

            }
//            Log.e("status",status)
            var currdatetime:String = getDateTime().toString()
            var bundle = Bundle()
            bundle.putString("subject_name",subject_name)
            bundle.putString("total_classes",total_classes)
            bundle.putString("total_present",total_present)
            bundle.putString("criteria_percentage",binding.slider.value.toString())
            bundle.putString("status",status)
            bundle.putString("lastupdate",currdatetime)
            bundle.putInt("flag",flag)
            criteriaper = binding.slider.value.toString()
            if(!subject_name.isNullOrEmpty() && !total_classes.isNullOrEmpty() &&
                !total_present.isNullOrEmpty() && !status.isNullOrEmpty() &&
                !currdatetime.isNullOrEmpty() ){

                if( total_classes.toInt()>= total_present.toInt()){
                    if(total_classes.toInt() != 0 || total_present.toInt() != 0){
                        findNavController().navigate(
                            R.id.action_addSubjectFragment_to_homeFragment,
                            bundle
                        )
                    }
                    else{
                        Snackbar.make(binding.root,"Field Cant be Zero",Snackbar.LENGTH_SHORT).show()
                    }
                }
                else{
                    Snackbar.make(binding.root,"Present Classes Should be <= Total Classes",Snackbar.LENGTH_SHORT).show()
                }
                subject_name = ""
                total_classes =""
                total_present =""
                status=""
                currdatetime=""
                flag++
            }
            else{
                Snackbar.make(requireView(),"Please enter the required details", Snackbar.LENGTH_SHORT).show()
            }
            this.binding.root?.let { view ->
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }



    }

    private fun checkstring(mystring: String): Boolean {
        for(i in mystring){
            if(i<'0' || i>'9'){
                return false
            }
        }
        return true
    }


    private fun calculatestatus(present: Int, totalclass: Int, criteriaper: Int): String {


        var per = (present.toFloat()/totalclass.toFloat()) * 100

        var status: String = ""
        var totcl : Int = totalclass
        var pres : Int = present
        if (per > criteriaper) {
            var count: Int = 0
            while (per >= criteriaper) {
                per = ((present.toFloat()) / (totcl.toFloat())) * 100
                count++
                totcl++
            }
            if(count!=0)
                status = "You may leave ${count} classes and you will be on track"
            else{
                status = "You are on the track"
            }
        }
        else if(per<criteriaper){
            var count: Int = 0
            while (per <= criteriaper) {

                per = ((pres.toFloat()) / (totcl.toFloat())) * 100
                count++
                pres++
                totcl++
            }
            if(count!=0)
                status = "You have to attend ${count} classes to maintain your track"
            else{
                status = "You are on the track"
            }
        }
        else {
            status = "You are on the track"
        }
        return status
    }
    fun getDateTime(): String? {
        val currentDateTimeString: String = DateFormat.getDateTimeInstance()
            .format(Date())
        return currentDateTimeString
    }
    private fun isNetworkConnected(): Boolean {
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}