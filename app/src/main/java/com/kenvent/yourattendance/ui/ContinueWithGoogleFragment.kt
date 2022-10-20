package com.kenvent.yourattendance.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kenvent.yourattendance.MainActivity
import com.kenvent.yourattendance.R
import com.kenvent.yourattendance.SignInActivity
import com.kenvent.yourattendance.SplashActivity
import com.kenvent.yourattendance.databinding.FragmentContinueWithGoogleBinding
import java.lang.Exception


const val RC_SIGN_IN = 100
class ContinueWithGoogleFragment : Fragment() {

    private lateinit var binding: FragmentContinueWithGoogleBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContinueWithGoogleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRequest()
        binding.load.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        binding.btnSignInButton.setOnClickListener {
            binding.btnSignInButton.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.onclick_anim));
                binding.load.visibility = View.VISIBLE
                signIn()
        }
        binding.skipbtn.setOnClickListener {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with (sharedPref.edit()) {
                putInt("Flag", 1)
                apply()
            }
            val i = Intent(requireContext(),SplashActivity::class.java)
            findNavController().popBackStack()
            startActivity(i)
        }
        binding.emailbtn.setOnClickListener {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
            with (sharedPref.edit()) {
                putInt("Flag", 1)
                apply()
            }
            val i = Intent(requireContext(),SignInActivity::class.java)
            findNavController().popBackStack()
            startActivity(i)
        }



    } private fun createRequest() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                binding.load.visibility = View.GONE
                Toast.makeText(requireContext(),"Sign In Failed Please Try Again ", Toast.LENGTH_SHORT).show()
                // Google Sign In failed, update UI appropriately
                Log.w(ContentValues.TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val i = Intent(requireActivity(),MainActivity::class.java)
                    startActivity(i)
                    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@addOnCompleteListener
                    with (sharedPref.edit()) {
                        putInt("Flag", 0)
                        apply()
                    }
                    binding.load.visibility = View.GONE
                    requireActivity().finish()
                } else {
                    binding.load.visibility = View.GONE
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }
    private fun isNetworkConnected(): Boolean {
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

}