package com.example.disasterresponseplatform.ui.authentication
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentLoginBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.utils.UserRoleUtil

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var authViewModel: AuthenticationViewModel

    private val registrationFragment = RegistrationFragment()
    private val forgotPasswordFragment = ForgotPasswordFragment()

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
        // Initialize ViewModel
        authViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        // Access the views from the XML
        val signInTopLabel = view.findViewById<TextView>(R.id.sign_in_top_label)
        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val forgotPasswordButton = view.findViewById<Button>(R.id.forgot_password_button)
        val signInButton = view.findViewById<Button>(R.id.sign_in_button)
        val divider = view.findViewById<View>(R.id.divider)
        val signUpButton = view.findViewById<Button>(R.id.sign_up_button)

        // Observe LiveData for login validation
        authViewModel.loginValidation.observe(viewLifecycleOwner, Observer { isValid ->
            if (!isValid) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("", "Sent sign in request")
                val username = usernameEditText.text.toString()
                DiskStorageManager.setKeyValue("username", username)
                authViewModel.sendSignInRequest()
            }
        })

        authViewModel.signInError.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT ).show()
        })

        authViewModel.signInSuccessful.observe(viewLifecycleOwner, Observer { isSuccessful ->
            if (isSuccessful) {
                if (!UserRoleUtil.isEmailVerified(DiskStorageManager.getKeyValue("username")!!)) {
                    addFragment(EmailVerificationFragment())
                } else {
                    Toast.makeText(
                        context,
                        "Logged in as " + DiskStorageManager.getKeyValue("username"),
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().finish()
                    startActivity(requireActivity().intent)
                }
            }
        })


        // Set button click listener for sign-in
        signInButton.setOnClickListener {
            authViewModel.updateUsername(usernameEditText.text.toString())
            authViewModel.updatePassword(passwordEditText.text.toString())
            authViewModel.validateLogin()

        }
    // You can also add listeners for other buttons like signUpButton and forgotPasswordButton as needed
    }

    private fun clickButtons(){
        binding.signUpButton.setOnClickListener {
            addFragment(registrationFragment)
        }
        binding.forgotPasswordButton.setOnClickListener {
            addFragment(forgotPasswordFragment)
        }

    }
    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }
}