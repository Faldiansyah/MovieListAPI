package com.rich.movielistapi.fragment

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rich.movielistapi.R
import com.rich.movielistapi.databinding.FragmentProfileBinding
import com.rich.movielistapi.viewmodel.UserViewModel

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private lateinit var userVM : UserViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val PREF_NAME = "dataUser"
    private lateinit var id : String
    private lateinit var oldPassword : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userVM = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        sharedPref = requireActivity().getSharedPreferences(PREF_NAME, 0)
        editor = sharedPref.edit()
        id = sharedPref.getString("id", "").toString()
        getDataUser()
        setListener()
    }


    private fun getDataUser(){
        showLoading(true)
        userVM.callGetUserById(id)
        userVM.observerLDGetUserById().observe(viewLifecycleOwner) {
            if (it != null) {
                showLoading(false)
                binding.emailInput.setText(it.email)
                binding.usernameInput.setText(it.username)
                binding.passwordInput.setText(it.password)
                oldPassword = it.password.toString()
            }
        }
    }

    private fun setListener() {
        binding.topAppBar.menu.findItem(R.id.action_logout).setOnMenuItemClickListener {
            logout()
            true
        }
        binding.topAppBar.setNavigationOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })

        binding.btnSaveUpdate.setOnClickListener {
            updateUser()
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure want to logout?")
            .setPositiveButton("Yes") { dialog, which ->
                editor.clear()
                editor.apply()
                findNavController().navigate(R.id.action_profileFragment_to_registerLoginFragment)
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateUser(){
        val email = binding.emailInput.text.toString()
        val username = binding.usernameInput.text.toString()
        val password = binding.passwordInput.text.toString()
        userVM.callUpdateUser(id, email, username, password)
        userVM.observerLDUpdateUser().observe(viewLifecycleOwner) {
            if (it != null) {
                if(!oldPassword.equals(password)){
                    logout()
                }
                Toast.makeText(requireContext(), resources.getString(R.string.update_success), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading : Boolean) {
        if(isLoading){
            binding.lottieLoading.visibility = View.VISIBLE
            binding.progressBarContainer.visibility = View.VISIBLE
        }else{
            binding.lottieLoading.visibility = View.GONE
            binding.progressBarContainer.visibility = View.GONE
        }
    }
}