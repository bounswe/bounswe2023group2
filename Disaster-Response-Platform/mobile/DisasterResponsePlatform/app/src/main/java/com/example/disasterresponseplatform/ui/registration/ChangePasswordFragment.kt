package com.example.disasterresponseplatform.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentChangePasswordBinding
import com.example.disasterresponseplatform.databinding.FragmentForgotPasswordBinding


class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater,container,false)
        binding.verifyCode.setOnClickListener {
            Toast.makeText(context,"Code incorrect!", Toast.LENGTH_SHORT).show()
        }

        binding.resend.setOnClickListener {
            Toast.makeText(context,"Code sent!", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

}