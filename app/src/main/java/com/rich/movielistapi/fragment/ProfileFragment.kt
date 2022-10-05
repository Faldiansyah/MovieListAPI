package com.rich.movielistapi.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.adapters.ViewGroupBindingAdapter.setListener
import androidx.lifecycle.ViewModelProvider
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
        id = requireArguments().getString("id")!!
        getDataUser()
        setListener()
    }


    private fun getDataUser(){
        userVM.callGetUserById(id)
        userVM.observerLDGetUserById().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.emailInput.setText(it.email)
                binding.usernameInput.setText(it.username)
                binding.passwordInput.setText(it.password)
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
        editor.clear()
        editor.apply()
        requireActivity().finish()
    }

    private fun updateUser(){
        val email = binding.emailInput.text.toString()
        val username = binding.usernameInput.text.toString()
        val password = binding.passwordInput.text.toString()
        userVM.callUpdateUser(id, email, username, password)
        userVM.observerLDUpdateUser().observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), resources.getString(R.string.update_success), Toast.LENGTH_SHORT).show()
            }
        }
    }
}