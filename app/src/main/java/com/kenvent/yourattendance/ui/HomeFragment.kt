package com.kenvent.yourattendance.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kenvent.yourattendance.R
import com.kenvent.yourattendance.adapter.SubjectAdapter
import com.kenvent.yourattendance.entity.SubjectEnitity
import com.kenvent.yourattendance.viewmodel.SubjectViewModel
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode
import java.text.DecimalFormat
import android.widget.EditText
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kenvent.yourattendance.MainActivity
import com.kenvent.yourattendance.databinding.FragmentHomeBinding
import java.lang.Exception

import java.util.*


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel : SubjectViewModel
    var subject_name: String =""
    var total_present: String = ""
    var total_classes: String = ""
    var criteria_percentage : String =""
    var status : String =""
    var currdatetime: String = ""
    var flag :Int = 0
    lateinit var database : DatabaseReference
    lateinit var allmySubject :List<SubjectEnitity>
    lateinit var currsub:SubjectEnitity
    lateinit var signInAccount: GoogleSignInAccount
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var contex =requireContext()
        binding.lottie1.visibility = View.GONE
        binding.lottie2.visibility = View.GONE
        binding.overallProgressBarGreen.visibility = View.INVISIBLE
        binding.overallProgressBarYellow.visibility = View.INVISIBLE
        binding.overallProgressBarRed.visibility = View.INVISIBLE

        binding.homeMenuAddBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addSubjectFragment)
        }
        binding.homeMenuBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }

        addtoroom(subject_name,total_present,total_classes,criteria_percentage,status,currdatetime)
        viewModel.allSubjects.observe(viewLifecycleOwner, Observer{
        if(it.isNotEmpty()) {
            binding.nocontent.visibility = View.GONE
            binding.overallProgressBarGreen.visibility = View.VISIBLE
            binding.overallPercent.visibility = View.VISIBLE
            binding.overallRatio.visibility = View.VISIBLE
            binding.overallattText.visibility = View.VISIBLE
            var sum = 0
            var totsum = 0

            for (i in 0..(it.size - 1)) {
                currsub = it[i]
                sum = sum + currsub.AttendedClasses.toInt()
                totsum = totsum + currsub.TotalClasses.toInt()
            }
            if (sum == 0) {
                binding.nocontent.visibility = View.VISIBLE
                binding.overallProgressBarGreen.visibility = View.GONE
                binding.overallPercent.visibility = View.GONE
                binding.overallRatio.visibility = View.GONE
                binding.overallattText.visibility = View.GONE
                binding.overallProgressBarGreen.visibility = View.INVISIBLE
                binding.overallProgressBarYellow.visibility = View.INVISIBLE
                binding.overallProgressBarRed.visibility = View.INVISIBLE
            } else {
                binding.nocontent.visibility = View.GONE
                binding.overallProgressBarGreen.visibility = View.VISIBLE
                binding.overallPercent.visibility = View.VISIBLE
                binding.overallRatio.visibility = View.VISIBLE
                binding.overallattText.visibility = View.VISIBLE
            }
            var per = (sum.toDouble() / totsum.toDouble()) * 100
            Log.e("PERCENT", per.toString())
            per = roundOffDecimal(per)
            binding.overallRatio.text = "${sum}/${totsum}"
            binding.overallPercent.text = "${per}%"
            if (sum != 0 && it[0].criteria_percentage.isNotEmpty() && per.toString().isNotEmpty()) {
                if (per.toFloat() > it[0].criteria_percentage.toFloat() + 3) {
                    binding.overallProgressBarGreen.visibility = View.VISIBLE
                    binding.overallProgressBarGreen.progress = per.toInt()
                    binding.overallProgressBarYellow.visibility = View.INVISIBLE
                    binding.overallProgressBarRed.visibility = View.INVISIBLE

                } else if (per.toFloat() >= it[0].criteria_percentage.toFloat() - 1 && per.toFloat() <= it[0].criteria_percentage.toFloat() + 3) {
                    binding.overallProgressBarYellow.visibility = View.VISIBLE
                    binding.overallProgressBarYellow.progress = per.toInt()
                    binding.overallProgressBarGreen.visibility = View.INVISIBLE
                    binding.overallProgressBarRed.visibility = View.INVISIBLE
                } else {
                    binding.overallProgressBarRed.visibility = View.VISIBLE
                    binding.overallProgressBarRed.progress = per.toInt()
                    binding.overallProgressBarYellow.visibility = View.INVISIBLE
                    binding.overallProgressBarGreen.visibility = View.INVISIBLE
                }
            }
            val bundle = this.arguments
            if(bundle!=null){ flag = bundle!!.getInt("flag") }

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    for(i in it){
                        Log.e("i" , i.toString())
                        Log.e("FirebaseAttendedClasses", i.AttendedClasses)
                        Log.e("FirebaseSubjectN", i.SubjectName)
                        Log.e("FirebaseTotalClasses", i.TotalClasses)
                        Log.e("FirebaseCriterai", i.criteria_percentage)
                        Log.e("Firebasestatus", i.status)
                        if(  !i.AttendedClasses.isNullOrEmpty() && !i.SubjectName.isNullOrEmpty() &&
                            !i.TotalClasses.isNullOrEmpty() && !i.criteria_percentage.isNullOrEmpty()
                            && !i.status.isNullOrEmpty()
                            &&  flag != 1
                        ){
                            Log.e("Conditon" , "Passed")
                                    if(isNetworkConnected()){
                                        addDatatoFirebase(contex,i.SubjectName,i.TotalClasses,i.AttendedClasses,i.criteria_percentage,i.status)
                                    }
                                    else{
                                        Snackbar.make(binding.root,"No Internet for backup.",Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                    }
                }
            }, 2000)


        } else{
            binding.nocontent.visibility = View.VISIBLE
            binding.overallProgressBarGreen.visibility = View.GONE
            binding.overallPercent.visibility = View.GONE
            binding.overallRatio.visibility = View.GONE
            binding.overallattText.visibility = View.GONE
            binding.overallProgressBarGreen.visibility = View.INVISIBLE
            binding.overallProgressBarYellow.visibility = View.INVISIBLE
            binding.overallProgressBarRed.visibility = View.INVISIBLE
        }
        })
    }

    private fun addDatatoFirebase(contex:Context,subname:String,totclas:String,presclas:String,criper:String,stat:String) {

        if(getLastSignedInAccount(contex)!=null) {
            signInAccount = getLastSignedInAccount(contex)
            database = FirebaseDatabase.getInstance().getReference(signInAccount.id)
        }
        else{
//            Snackbar.make(binding.root,"Please Signin to Save Data ",Snackbar.LENGTH_SHORT).show()
        }
        if(getLastSignedInAccount(contex)!=null) {
            signInAccount = getLastSignedInAccount(contex)
            database.child(signInAccount.displayName).child("Subjects").child(subname).child("SubjectName").setValue(subname)
            database.child(signInAccount.displayName).child("Subjects").child(subname).child("TotalClasses").setValue(totclas)
            database.child(signInAccount.displayName).child("Subjects").child(subname).child("AttendedClasses").setValue(presclas)
            database.child(signInAccount.displayName).child("Subjects").child(subname).child("CriteriaPercentage").setValue(criper)
            database.child(signInAccount.displayName).child("Subjects").child(subname).child("status").setValue(stat)
        }else{
//            Snackbar.make(binding.root,"Please Signin to Save Data", Snackbar.LENGTH_SHORT).show()
        }
    }
    fun roundOffDecimal(number: Double): Double {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(number).toDouble()
    }
     fun addtoroom( subject_name:String,total_present:String,total_classes:String,criteria_percentage:String,status:String,currdatetime:String) {
        val adapter = SubjectAdapter(requireContext(),this)
        val recyclerView = binding.homeRecview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
            SubjectViewModel::class.java)
        viewModel.allSubjects.observe(viewLifecycleOwner, Observer {list ->

            list?.let{
                adapter.updateSubjectList(it)
            }
        })

        var bundleid = this.arguments

        if(bundleid != null){

            this.subject_name = bundleid.getString("subject_name").toString()
            this.total_present = bundleid.getString("total_present").toString()
            this.total_classes = bundleid.getString("total_classes").toString()
            this.criteria_percentage = bundleid.getString("criteria_percentage").toString()
            this.status = bundleid.getString("status").toString()
            this.currdatetime = bundleid.getString("lastupdate").toString()
            flag = bundleid.getInt("flag")
        }else{
            this.subject_name = subject_name
            this.status = status
            this.total_classes = total_classes
            this.total_present = total_present
            this.criteria_percentage = criteria_percentage
            this.currdatetime = currdatetime

        }
         Log.e("subject_name",this.subject_name+" ")
         Log.e("total_classes",this.total_classes+" ")
         Log.e("total_present",this.total_present+" ")
         Log.e("criteria_percentage",this.criteria_percentage+" ")
         Log.e("status",this.status+" ")
               if(this.total_classes.isNotEmpty() && this.subject_name.isNotEmpty() && this.total_present.isNotEmpty() && this.criteria_percentage.isNotEmpty() && this.status.isNotEmpty() && this.currdatetime.isNotEmpty() ) {
                   viewModel.insertFriend(SubjectEnitity(this.subject_name,this.total_classes,this.total_present,"${this.total_classes.toInt()-this.total_present.toInt()}",this.criteria_percentage,this.status,this.currdatetime)
                   )
           }
    }

    fun updateSubject(Subjectname: String, status: String, TotalClasses: String, AttendedClasses: String,currdatetime: String){

        viewModel.updateSubject(Subjectname,status,TotalClasses,AttendedClasses,currdatetime)

    }

    fun onItemClicked(subjectEnitity: SubjectEnitity) {

        viewModel.deleteFriend(subjectEnitity)
        Toast.makeText(requireContext(),"Deleted",Toast.LENGTH_LONG).show()

    }

    fun dialogBox(itemview:SubjectEnitity) {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage("Are you sure want to delete ")
        alertDialogBuilder.setPositiveButton("Ok",
            DialogInterface.OnClickListener { arg0, arg1 ->
                onItemClicked(itemview)
            })
        alertDialogBuilder.setNegativeButton("cancel",
            DialogInterface.OnClickListener { arg0, arg1 -> })
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
    fun anackbar(str:String){
        Snackbar.make(binding.root,str,Snackbar.LENGTH_SHORT) .show()
    }
    fun gottyanim(i :Int){
        if(i==1){
            binding.lottie1.visibility = View.VISIBLE
            binding.lottie2.visibility = View.GONE
            binding.lottie1.playAnimation()
            Handler().postDelayed({
                binding.lottie1.visibility = View.GONE
            }, 1000)
        }
        if(i==2){
            binding.lottie1.visibility = View.GONE
            binding.lottie2.visibility = View.VISIBLE
            binding.lottie2.playAnimation()
            Handler().postDelayed({
                binding.lottie2.visibility = View.GONE
            },2000)
        }
    }
    fun makedialogue( subject_name:String,criteria_percentage:String,status:String,currdatetime:String)
        {
        val alert = AlertDialog.Builder(requireContext())
        val factory = LayoutInflater.from(requireContext())

//text_entry is an Layout XML file containing two text field to display in alert dialog

//text_entry is an Layout XML file containing two text field to display in alert dialog
        val textEntryView: View = factory.inflate(com.kenvent.yourattendance.R.layout.text_entry, null)

        val input1 = textEntryView.findViewById<View>(com.kenvent.yourattendance.R.id.EditText1) as EditText
        val input2 = textEntryView.findViewById<View>(com.kenvent.yourattendance.R.id.EditText2) as EditText


        input1.setText(null, TextView.BufferType.EDITABLE)
        input2.setText(null, TextView.BufferType.EDITABLE)

        alert.setIcon(com.kenvent.yourattendance.R.drawable.ic_baseline_edit_note_24).setTitle("Enter the Following details:").setView(textEntryView)
            .setPositiveButton("Save",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    if(checkstring(input1.text.toString()) && checkstring(input2.text.toString())){
                        this.total_classes = input2.text.toString()
                        this.total_present = input1.text.toString()
                        Log.i("AlertDialog", "TextEntry 1 Entered " + input1.text.toString())
                        Log.i("AlertDialog", "TextEntry 2 Entered " + input2.text.toString())
                        viewModel.updateSubject(
                            subject_name,
                            status,
                            total_classes,
                            total_present,
                            currdatetime
                        )
                    }
                    else{
                        Toast.makeText(requireContext(),"Please Enter a Valid Data",Toast.LENGTH_SHORT).show()
                    }
                    /* User clicked OK so do some stuff */
                }).setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, whichButton -> /*
     * User clicked cancel so do some stuff
     */
                })
        alert.show()
    }
    private fun isNetworkConnected(): Boolean {
        if(activity != null){
            val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null
        }
        return true
    }
    private fun checkstring(mystring: String): Boolean {
        for(i in mystring){
            if(i<'0' || i>'9'){
                return false
            }
        }
        return true
    }

}