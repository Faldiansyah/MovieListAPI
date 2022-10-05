package com.rich.movielistapi.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rich.movielistapi.R
import com.rich.movielistapi.databinding.FragmentRegisterLoginBinding
import com.rich.movielistapi.viewmodel.UserViewModel

class RegisterLoginFragment : Fragment() {
    private lateinit var binding: FragmentRegisterLoginBinding
    private lateinit var userVM : UserViewModel
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val PREF_NAME = "dataUser"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userVM = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        showHideForm()
        setButtonListener()
    }

    private fun setButtonListener() {
        binding.registerForm.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.loginForm.btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun registerUser() {
        val email = binding.registerForm.emailInput.text.toString()
        val username = binding.registerForm.usernameInput.text.toString()
        val password = binding.registerForm.passwordInput.text.toString()
        val passwordConfirm = binding.registerForm.passwordConfirmInput.text.toString()

        if(email.isEmpty() || username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            binding.registerForm.emailInput.error = "Please fill the field"
            binding.registerForm.usernameInput.error = "Please fill the field"
            binding.registerForm.passwordInput.error = "Please fill the field"
            binding.registerForm.passwordConfirmInput.error = "Please fill the field"
        }else if(password != passwordConfirm) {
            binding.registerForm.passwordConfirmInput.error = "Confirmation Password doesn't match"
        }else{
            userVM.callRegisterUser(email, username, password)
            userVM.observerLDRegisterUser().observe(viewLifecycleOwner) {
                if (it != null) {
                    Toast.makeText(requireContext(), resources.getString(R.string.registration_success), Toast.LENGTH_SHORT).show()
                    gotoLogin()
                }
            }
        }

    }

    private fun loginUser() {
        val username = binding.loginForm.usernameInput.text.toString()
        val password = binding.loginForm.passwordInput.text.toString()
        var isFound = false

        if(username.isEmpty() || password.isEmpty()) {
            binding.loginForm.usernameInput.error = "Please fill the field"
            binding.loginForm.passwordInput.error = "Please fill the field"
        }else{
            userVM.callGetAllUser()
            userVM.observerLDGetUser().observe(viewLifecycleOwner) {
                if (it != null) {
                    for (i in it) {
                        if (i.username == username && i.password == password) {
                            isFound = true
                            sharedPref = requireActivity().getSharedPreferences(PREF_NAME, 0)
                            editor = sharedPref.edit()
                            editor.putString("id", i.id)
                            editor.putString("username", username)
                            editor.putBoolean("isLogin", true)
                            editor.apply()
                            gotoHome()
                        }
                    }
                    if(!isFound){
                        Toast.makeText(requireContext(), "Username or Password is wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun gotoHome() {
        findNavController().navigate(R.id.action_registerLoginFragment_to_movieListFragment)
    }

    private fun showHideForm(){
        binding.registerTitle.setOnClickListener {
            //Tampilkan form register ketika diklik
            gotoRegister()
        }
        binding.loginTitle.setOnClickListener {
            //Tampilkan form login ketika diklik
            gotoLogin()
        }
    }

    private fun gotoRegister(){
        if(binding.registerForm.root.visibility == View.GONE){
            binding.registerForm.root.visibility = View.VISIBLE
            binding.loginForm.root.visibility = View.GONE
            binding.registerTitle.setTextColor(resources.getColor(R.color.white))
            binding.loginTitle.setTextColor(resources.getColor(R.color.description_color))
        }else{
            binding.registerForm.root.visibility = View.GONE
            binding.loginForm.root.visibility = View.VISIBLE
            binding.registerTitle.setTextColor(resources.getColor(R.color.description_color))
            binding.loginTitle.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun gotoLogin(){
        if(binding.loginForm.root.visibility == View.GONE){
            binding.loginForm.root.visibility = View.VISIBLE
            binding.registerForm.root.visibility = View.GONE
            binding.loginTitle.setTextColor(resources.getColor(R.color.white))
            binding.registerTitle.setTextColor(resources.getColor(R.color.description_color))
        }else{
            binding.loginForm.root.visibility = View.GONE
            binding.registerForm.root.visibility = View.VISIBLE
            binding.loginTitle.setTextColor(resources.getColor(R.color.description_color))
            binding.registerTitle.setTextColor(resources.getColor(R.color.white))
        }
    }
}