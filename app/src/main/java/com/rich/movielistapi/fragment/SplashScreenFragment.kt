package com.rich.movielistapi.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.rich.movielistapi.R
import com.rich.movielistapi.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var sharedPref: SharedPreferences
    private val PREF_NAME = "dataUser"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireActivity().getSharedPreferences(PREF_NAME, 0)
        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPref.getBoolean("isLogin", false)){
                findNavController().navigate(R.id.action_splashScreenFragment_to_movieListFragment)
            }else{
                findNavController().navigate(R.id.action_splashScreenFragment_to_registerLoginFragment)
            }
        }, 3000)
    }

}