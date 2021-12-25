package com.kenvent.yourattendance.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.kenvent.yourattendance.MainActivity
import com.kenvent.yourattendance.R
import com.kenvent.yourattendance.SplashActivity
import com.kenvent.yourattendance.databinding.FragmentMenuBinding
import com.kenvent.yourattendance.entity.SubjectEnitity
import com.kenvent.yourattendance.entity.Subjects
import com.kenvent.yourattendance.viewmodel.SubjectViewModel
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var database: DatabaseReference
     var list: ArrayList<Subjects> = arrayListOf()
    private lateinit var viewModel: SubjectViewModel
    var flag = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadinglottie.visibility = View.GONE
        if(            GoogleSignIn.getLastSignedInAccount(this.requireContext())
            !=null)
        {
            viewModel =  ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(
                SubjectViewModel::class.java)
            var signInAccount: GoogleSignInAccount =
                GoogleSignIn.getLastSignedInAccount(this.requireContext())
            database = FirebaseDatabase.getInstance().getReference(signInAccount.id)
            binding.settingsLogout.setOnClickListener {
                binding.settingsLogout.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.onclick_anim
                    )
                )
                findNavController().navigate(R.id.action_menuFragment_to_feedBackFragment)
            }
            binding.settingsHelp.setOnClickListener {
                binding.settingsHelp.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.onclick_anim
                    )
                )

                findNavController().navigate(R.id.action_menuFragment_to_devContactFragment)
            }
            binding.settingsLogout1.setOnClickListener {
                binding.settingsLogout1.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.onclick_anim
                    )
                )
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                googleSignInClient.signOut()
                Snackbar.make(requireView(), "Signout Successfully", Snackbar.LENGTH_SHORT).show()
                val i = Intent(requireActivity(), SplashActivity::class.java)
                startActivity(i)
                viewModel.deleteall()
                requireActivity().finish()
            }
            binding.settingsRestore.setOnClickListener {
                binding.settingsRestore.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.onclick_anim
                    )
                )
                binding.loadinglottie.visibility = View.VISIBLE
                if (isNetworkConnected()) {

                    viewModel.deleteall()
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (datasnapshot: DataSnapshot in snapshot.child(signInAccount.displayName)
                                .child("Subjects").children) {
                                var subject: Subjects? = datasnapshot.getValue(Subjects::class.java)
                                if (subject != null) {
                                    list.add(subject)
                                } else {
                                    Snackbar.make(
                                        binding.root,
                                        "No backup found",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                                subject!!.SubjectName?.let { it1 ->
                                    subject!!.AttendedClasses?.let { it2 ->
                                        subject!!.CriteriaPercentage?.let { it3 ->
                                            subject!!.status?.let { it4 ->
                                                subject!!.TotalClasses?.let { it5 ->
                                                    SubjectEnitity(
                                                        it1, it5,
                                                        it2, "", it3, it4, ""
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                    ?.let { it2 -> viewModel.insertFriend(it2) }

                                flag = 1
                            }
                            binding.loadinglottie.visibility = View.GONE

                        }

                        override fun onCancelled(error: DatabaseError) {
                            binding.loadinglottie.visibility = View.GONE
                            Snackbar.make(binding.root, "Please try again.", Snackbar.LENGTH_SHORT)
                                .show()
                        }

                    })
                    if (flag == 1) {
                        binding.loadinglottie.visibility = View.GONE
                        Snackbar.make(binding.root, "Restored Successfully", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                } else if (flag == 0) {
                    Snackbar.make(
                        requireView(),
                        "Please check your Internet Connection",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    binding.loadinglottie.visibility = View.GONE
                }
            }
        }
        else{
            binding.settingsLogout1.text= "Login"
            binding.settingsLogout1.setOnClickListener {
                findNavController().navigate(R.id.cwg_nav_graph_xml)
            }
            binding.settingsRestore.setOnClickListener {
                Snackbar.make(requireView(), "Please Signin to Save or Restore Backup", Snackbar.LENGTH_SHORT).show()
            }
            binding.settingsLogout.setOnClickListener {
                Snackbar.make(requireView(), "Please Signin to Give Feedback", Snackbar.LENGTH_SHORT).show()
            }
            binding.settingsHelp.setOnClickListener {
                binding.settingsHelp.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.onclick_anim
                    )
                )

                findNavController().navigate(R.id.action_menuFragment_to_devContactFragment)
            }
            Snackbar.make(requireView(), "Please Signin to Save or Restore Backup", Snackbar.LENGTH_SHORT).show()
        }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


}