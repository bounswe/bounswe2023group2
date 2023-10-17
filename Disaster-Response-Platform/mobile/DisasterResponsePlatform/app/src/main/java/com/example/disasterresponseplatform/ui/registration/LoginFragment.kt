package com.example.disasterresponseplatform.ui.registration

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    val registrationFragment = RegistrationFragment()
    val forgotPasswordFragment = ForgotPasswordFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickButtons()
    }

    private fun clickButtons(){
        binding.signUpButton.setOnClickListener {
            addFragment(registrationFragment)
        }
        binding.forgotPasswordButton.setOnClickListener {
            addFragment(forgotPasswordFragment)
        }
        binding.signInButton.setOnClickListener {
            Toast.makeText(context,"Signing in...",Toast.LENGTH_SHORT).show()
        }

    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }


}