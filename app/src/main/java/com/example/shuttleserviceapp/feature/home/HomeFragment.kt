package com.example.shuttleserviceapp.feature.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shuttleserviceapp.MainActivity
import com.example.shuttleserviceapp.databinding.FragmentHomeBinding
import com.example.shuttleserviceapp.feature.maps.MapsActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonTracking.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)
        }
    }

}