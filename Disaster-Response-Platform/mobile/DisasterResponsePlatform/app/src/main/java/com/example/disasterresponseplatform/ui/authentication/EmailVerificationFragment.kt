package com.example.disasterresponseplatform.ui.authentication

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentEmailVerificationBinding

class EmailVerificationFragment : Fragment() {

    private lateinit var binding: FragmentEmailVerificationBinding
    private lateinit var authViewModel: AuthenticationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailVerificationBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel = ViewModelProvider(requireActivity()).get(AuthenticationViewModel::class.java)

        // Triggering the sending of an email verification when the fragment is created
        sendVerificationEmail()

        // Setting up a click listener for the "Resend Code" button to re-trigger email verification
        binding.btResendCode.setOnClickListener {
            sendVerificationEmail()
        }

        // Observing the success message after sending email verification
        authViewModel.emailVerificationSendSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Email verification sent successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        // Observing errors encountered while sending email verification
        authViewModel.emailVerificationSendError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Setting up a click listener for the "Verify Code" button to verify the entered code
        binding.btVerifyCode.setOnClickListener {
            val verificationCode = binding.etVerificationCode.text.toString()
            authViewModel.verifyEmail(verificationCode)
        }

        // Observing changes in email verification success status
        authViewModel.emailVerificationSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(context, "Your email address has been successfully verified.", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
                startActivity(requireActivity().intent)
            }
        }

        // Observing errors that might occur during email verification
        authViewModel.emailVerificationError.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendVerificationEmail() {
        // Setting up a click listener for the "Resend Code" button to re-trigger email verification
        authViewModel.sendEmailVerification()
        val resendCodeText = getString(R.string.resend_code)
        val btResendCode = binding.btResendCode
        if (btResendCode.isClickable) {
            // Disable button temporarily
            btResendCode.isClickable = false
            btResendCode.isEnabled = false

            // Countdown directly inside the onClickListener
            object : CountDownTimer(10000, 1000) { // 10 seconds, tick every 1 second
                override fun onTick(millisUntilFinished: Long) {
                    // Update button text with countdown value
                    val countdownText = "$resendCodeText (${(millisUntilFinished / 1000) + 1})"
                    btResendCode.text = countdownText
                }

                override fun onFinish() {
                    // Enable button and reset text when countdown finishes
                    context?.let {
                        btResendCode.isClickable = true
                        btResendCode.isEnabled = true
                        btResendCode.text = getString(R.string.resend_code)
                    }
                }
            }.start()
        }
    }

}