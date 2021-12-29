package com.kenvent.yourattendance.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kenvent.yourattendance.databinding.FragmentFeedBackBinding
import java.text.SimpleDateFormat
import java.util.*
import android.net.NetworkInfo

import androidx.core.content.ContextCompat.getSystemService

import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception
import java.text.DateFormat


class FeedBackFragment : Fragment() {

    lateinit var binding: FragmentFeedBackBinding
    lateinit var s:String
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var signInAccount: GoogleSignInAccount =
            GoogleSignIn.getLastSignedInAccount(this.requireContext())
        binding.loadinglottie1.visibility = View.GONE
        if(signInAccount!=null)
        database = FirebaseDatabase.getInstance().getReference(signInAccount.id)
        binding.feedbackDonebtn.setOnClickListener {
            binding.loadinglottie1.visibility = View.VISIBLE
            if( isNetworkConnected()){
                s = binding.feedbackEdittext.text.toString()
                if (!s.isNullOrEmpty()) {
                    var ss = getDateTime()

                    if (ss != null) {
                        var signInAccount: GoogleSignInAccount =
                            GoogleSignIn.getLastSignedInAccount(this.requireContext())
                        if (signInAccount != null) {
                            Log.e("NAME",getonlycharString(signInAccount.displayName))
                            database.child("Feedback").child(getonlycharString(signInAccount.displayName)).child(ss).setValue(s)
                            binding.loadinglottie1.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Thanks for your feedback",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    findNavController().popBackStack()
                }
            }
            else{
                binding.loadinglottie1.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Please Check your Internet Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

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
    fun getonlycharString(str:String): String{
        var mystring:String =""
        for(i in str){
            if(i in 'A'..'Z' || i in 'a'..'z' || i==' '){
                mystring+=i
            }
        }
        return mystring
    }
}