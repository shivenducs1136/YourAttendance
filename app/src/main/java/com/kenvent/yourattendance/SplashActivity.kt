package com.kenvent.yourattendance

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.kenvent.yourattendance.ui.ContinueWithGoogleFragment

class SplashActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signInAccount: GoogleSignInAccount
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        firebaseAuth = FirebaseAuth.getInstance()
//         Enabled When Sigin Work is done
        val cont = findViewById<FragmentContainerView>(R.id.sfragmentContainerView)
        cont.visibility = View.GONE
        val logo = findViewById<ImageView>(R.id.splash_logo)
        val tt1 = findViewById<TextView>(R.id.tt1)
        val tt2 = findViewById<TextView>(R.id.tt2)
        logo.visibility = View.VISIBLE
        tt1.visibility = View.VISIBLE
        tt2.visibility = View.VISIBLE
        val sharedPref = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val highScore = sharedPref.getInt("Flag", 0)
        Log.e("FLAGGG",highScore.toString())
        if(GoogleSignIn.getLastSignedInAccount(this) == null && highScore != 1){
            cont.visibility = View.VISIBLE
            logo.visibility = View.GONE
            tt1.visibility = View.GONE
            tt2.visibility = View.GONE
            supportFragmentManager.beginTransaction().replace(R.id.sfragmentContainerView,
                ContinueWithGoogleFragment()
            )
        }
        else
            splashfun()

    }

    fun splashfun() {
        Handler().postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 3000)

    }
}