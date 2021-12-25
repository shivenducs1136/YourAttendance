package com.kenvent.yourattendance.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kenvent.yourattendance.R
import com.kenvent.yourattendance.databinding.FragmentDevContactBinding


class DevContactFragment : Fragment() {

    lateinit var binding: FragmentDevContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDevContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plane"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hello")
            intent.putExtra(Intent.EXTRA_TEXT, "Hello Shivendu \n shivenduaps987@gmail.com")

            startActivity(Intent.createChooser(intent, "Send Email"))
        }
        binding.linkedin.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://www.linkedin.com/in/shivendu-mishra-94ba36200/")
            startActivity(openURL)
        }
        binding.github.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://github.com/shivenducs1136/YourAttendance.git")
            startActivity(openURL)
        }

    }
}