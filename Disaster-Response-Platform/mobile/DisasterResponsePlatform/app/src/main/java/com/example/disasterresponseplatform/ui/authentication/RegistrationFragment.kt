package com.example.disasterresponseplatform.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val emailVerificationFragment = EmailVerificationFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var authViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        val fullname = view.findViewById<TextView>(R.id.fullname)
        val username = view.findViewById<TextView>(R.id.username)
        val phoneNumber = view.findViewById<TextView>(R.id.phoneNumber)
        val email = view.findViewById<TextView>(R.id.email)
        val password = view.findViewById<TextView>(R.id.password)
        val password_confirm = view.findViewById<TextView>(R.id.password_confirm)

        authViewModel.signUpValidation.observe(viewLifecycleOwner, Observer { isValid ->
            if (isValid == 0) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (isValid == 1) {
                Toast.makeText(context, "Passwords doesn't match", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.sendSignUpRequest()
                //Toast.makeText(context, "Api request has been sent check Logcat for more details", Toast.LENGTH_SHORT).show()
            }
        })

        authViewModel.signUpError.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT ).show()
        })

        authViewModel.signUpSuccessful.observe(viewLifecycleOwner, Observer { isSuccessful ->
            if (isSuccessful) {
                Toast.makeText(context, "proceeding", Toast.LENGTH_SHORT ).show()
                parentFragmentManager.popBackStack()
                addFragment(emailVerificationFragment)
            }

        })


        binding.signUpButton.setOnClickListener {
            authViewModel.updateSignUpFullName(fullname.text.toString())
            authViewModel.updateSignUpUsername(username.text.toString())
            authViewModel.updatePhoneNumber(phoneNumber.text.toString())
            authViewModel.updateSignUpEmail(email.text.toString())
            authViewModel.updateSignUpPassword(password.text.toString())
            authViewModel.updateSignUpConfirmPassword(password_confirm.text.toString())
            authViewModel.validateSignUp()
        }
        binding.signInButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}