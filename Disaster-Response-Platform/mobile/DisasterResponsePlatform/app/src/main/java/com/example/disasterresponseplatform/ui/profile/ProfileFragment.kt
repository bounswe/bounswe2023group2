package com.example.disasterresponseplatform.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.authModels.LanguageArray
import com.example.disasterresponseplatform.data.models.authModels.ProfessionArray
import com.example.disasterresponseplatform.data.models.authModels.SkillArray
import com.example.disasterresponseplatform.data.models.authModels.SocialMediaArray
import com.example.disasterresponseplatform.data.models.authModels.UsersMeOptionalResponse
import com.example.disasterresponseplatform.data.models.authModels.UsersMeResponse
import com.example.disasterresponseplatform.data.models.authModels.UsersOptionalArray
import com.example.disasterresponseplatform.databinding.FragmentProfileBinding
import com.example.disasterresponseplatform.databinding.ProfileItemBinding
import com.example.disasterresponseplatform.data.models.usertypes.AuthenticatedUser
import com.example.disasterresponseplatform.data.models.usertypes.CredibleUser
import com.example.disasterresponseplatform.data.models.usertypes.RoleBasedUser
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.example.disasterresponseplatform.ui.authentication.LoginFragment
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment(var username: String?) : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val editProfileFragment = EditProfileFragment()
    private var profileLevel: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.primary)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileLevel = 0
        lateinit var user: AuthenticatedUser
        if (!DiskStorageManager.hasKey("token")) {
            binding.profileLoginFirstText.visibility = View.VISIBLE
            binding.profileProgressBar.visibility = View.GONE
            replaceFragment(LoginFragment())
        } else {
            val networkManager = NetworkManager()
            val headers = mapOf(
                "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
                "Content-Type" to "application/json"
            )
            if (username == null) { // get self profile info
                binding.profileCallButton.text = getString(R.string.pr_logout)
                binding.profileCallButton.setOnClickListener {
                    DiskStorageManager.removeKey("token")
                    replaceFragment(LoginFragment())
                }
                user = AuthenticatedUser("", "", "", "", "")
                networkManager.makeRequest(
                    endpoint = Endpoint.ME,
                    requestType = RequestType.GET,
                    headers = headers,
                    callback = object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            binding.profileLoginFirstText.visibility = View.VISIBLE
                            binding.profileProgressBar.visibility = View.GONE
                            binding.profileLoginFirstText.text = getString(R.string.pr_no_connection)
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val res = gson.fromJson(body, UsersMeResponse::class.java)

                                user.username = res.username
                                user.email = res.email
                                user.phone = res.phoneNumber
                                user.name = res.firstName
                                user.surname = res.lastName
                                user.profileInfoShared = res.privateAccount
                                user.isEmailVerified = res.isEmailVerified

                                clickButtons(user)
                                fillInformations(user)
                            } else {
                                binding.profileLoginFirstText.visibility = View.VISIBLE
                                binding.profileProgressBar.visibility = View.GONE
                                replaceFragment(LoginFragment())
                            }
                        }
                    }
                )
                networkManager.makeRequest(
                    endpoint = Endpoint.ME_OPTIONAL,
                    requestType = RequestType.GET,
                    headers = headers,
                    callback = object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful()) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val resr = gson.fromJson(body, UsersOptionalArray::class.java)
                                var res: UsersMeOptionalResponse? = null
                                val myusername = DiskStorageManager.getKeyValue("username")
                                for (resmi in resr.list) {
                                    if (resmi.username == myusername) {
                                        res = resmi
                                    }
                                }
                                if (res != null) {
                                    if (res.dateOfBirth != null && res.dateOfBirth.isNotBlank()) {
                                        user.birth = res.dateOfBirth.split(" ")[0]
                                    }
                                    user.nationality = res.nationality
                                    user.idNumber = res.identityNumber
                                    user.education = res.education
                                    user.healthCondition = res.healthCondition
                                    user.bloodType = res.bloodType
                                    user.address = res.address
                                } else {
                                    println("res null")
                                }
                                fillInformations(user)
                            } else {
                                println("response not successful")
                                println(response.message())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            println("Unexpected Error occurred")
                            println(t.message)
                        }

                    }
                )
                networkManager.makeRequest(
                    endpoint = Endpoint.LANGUAGE_GET,
                    requestType = RequestType.GET,
                    headers = headers,
                    callback = object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val res = gson.fromJson(body, LanguageArray::class.java)
                                if (res.list != null) {
                                    for (language in res.list) {
                                        user.languages.add(
                                            AuthenticatedUser.Language(
                                                language.second,
                                                language.third
                                            )
                                        )
                                    }
                                } else {
                                    println("Res nasıl null çıkıyo ya")
                                }
                                fillInformations(user)
                            } else {
                                println("response not successful: " + response.code())
                                println(response.message())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}

                    }
                )
                networkManager.makeRequest(
                    endpoint = Endpoint.SOCIAL_MEDIA_GET,
                    requestType = RequestType.GET,
                    headers = headers,
                    callback = object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful()) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val res = gson.fromJson(body, SocialMediaArray::class.java)
                                if (res.list != null)
                                    for (socialMedia in res.list) {
                                        user.socialMedia.add(
                                            AuthenticatedUser.SocialMedia(
                                                socialMedia.second,
                                                socialMedia.third
                                            )
                                        )
                                    }
                                fillInformations(user)
                            } else {
                                println("response not successful")
                                println(response.message())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}

                    }
                )
                networkManager.makeRequest(
                    endpoint = Endpoint.SKILL_GET,
                    requestType = RequestType.GET,
                    headers = headers,
                    callback = object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful()) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val res = gson.fromJson(body, SkillArray::class.java)
                                if (res.list != null)
                                    for (skill in res.list) {
                                        user.skills.add(
                                            AuthenticatedUser.Skill(
                                                skill.second,
                                                skill.third,
                                                ""
                                            )
                                        )
                                    }
                                fillInformations(user)
                            } else {
                                println("response not successful")
                                println(response.message())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}

                    }
                )
                networkManager.makeRequest(
                    endpoint = Endpoint.PROFESSION_GET,
                    requestType = RequestType.GET,
                    headers = headers,
                    callback = object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful()) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val res = gson.fromJson(body, ProfessionArray::class.java)
                                if (res.list != null)
                                    for (profession in res.list) {
                                        user.professions.add(
                                            AuthenticatedUser.Profession(
                                                profession.second,
                                                profession.third
                                            )
                                        )
                                        println("profession added: " + profession.second)
                                    }

                                fillInformations(user)
                            } else {
                                println("response not successful")
                                println(response.message())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}

                    }
                )
            } else {
                println("username: " + username)
                profileLevel += 4
                binding.profileCallButton.setOnClickListener {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Permission is granted, make the phone call
                        var callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse(
                            "tel:" + binding.profilePhoneNumber.text.toString().replace(" ", "")
                        )
                        startActivity(callIntent)
                    } else {
                        // Permission is not granted, request the permission
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.CALL_PHONE),
                            1
                        )
                    }
                }
                user = AuthenticatedUser("", "", "", "", "")
                networkManager.makeRequest(
                    endpoint = Endpoint.USERS,
                    requestType = RequestType.GET,
                    headers = headers,
                    id = username,
                    callback = object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            binding.profileLoginFirstText.visibility = View.VISIBLE
                            binding.profileProgressBar.visibility = View.GONE
                            binding.profileLoginFirstText.text = getString(R.string.pr_no_connection)
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val res = gson.fromJson(body, UsersMeResponse::class.java)

                                user.username = res.username
                                user.email = res.email
                                user.phone = res.phoneNumber
                                user.name = res.firstName
                                user.surname = res.lastName
                                user.profileInfoShared = res.privateAccount
                                user.isEmailVerified = res.isEmailVerified

                                clickButtons(user)
                                fillInformations(user)
                            } else {
                                print(response.message())
                                println("Hello")
                                binding.profileLoginFirstText.visibility = View.VISIBLE
                                binding.profileProgressBar.visibility = View.GONE
                                binding.profileLoginFirstText.text =
                                    getString(R.string.pr_hidden_profile)
//                                replaceFragment(LoginFragment())
                            }
                        }
                    }
                )
                networkManager.makeRequest(
                    endpoint = Endpoint.ME_OPTIONAL,
                    requestType = RequestType.GET,
                    headers = headers,
                    callback = object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful()) {
                                val body = response.body()?.string()
                                val gson = Gson()
                                println(body)
                                val resr = gson.fromJson(body, UsersOptionalArray::class.java)
                                var res: UsersMeOptionalResponse? = null
                                for (resmi in resr.list) {
                                    if (resmi.username == username) {
                                        res = resmi
                                    }
                                }
                                if (res != null) {
                                    if (res!!.dateOfBirth != null && res!!.dateOfBirth.isNotBlank()) {
                                        user.birth = res!!.dateOfBirth.split(" ")[0]
                                    }
                                    user.nationality = res!!.nationality
                                    user.idNumber = res!!.identityNumber
                                    user.education = res!!.education
                                    user.healthCondition = res!!.healthCondition
                                    user.bloodType = res!!.bloodType
                                    user.address = res!!.address
                                } else {
                                    println("res null")
                                }
                                fillInformations(user)
                            } else {
                                println("response not successful")
                                println(response.message())
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            println("Unexpected Error occurred")
                            println(t.message)
                        }

                    }
                )
            }
        }
    }

    private fun fillInformations(user: AuthenticatedUser) {
        profileLevel += 1
        println("profileLevel: " + profileLevel)
        if (profileLevel < 6) return
        binding.apply {
            // read image from url and set it to profilePhoto
            profileProgressBar.visibility = View.GONE
            profileScrollView.visibility = View.VISIBLE
            if (user.profilePhoto != null) {
                Picasso.get().load(user.profilePhoto!!.toUri()).into(profileImage)
            }
            profileFullname.text = user.name + " " + user.surname
            profileUsername.text = user.username
            profileEmail.text = user.email
            profilePhoneNumber.text = user.phone
            profileEmailVerifiedIcon.visibility =
                if (user.isEmailVerified) View.VISIBLE else View.GONE
            profilePhoneVerifiedIcon.visibility =
                if (user.isPhoneVerified) View.VISIBLE else View.GONE
            when (user) {
                is CredibleUser -> {
                    profileRegionLayout.visibility = View.VISIBLE
                    profileRegion.text = "Credible User in " + user.region
                }

                is RoleBasedUser -> {
                    profileProficiencyLayout.visibility = View.VISIBLE
                    profileProficiency.text = "Profficient User: " + user.proficiency
                }
            }
            when (user.verificationLevel) {
                1 -> {
                    profileVerifiedIcon.visibility = View.VISIBLE
                    profileVerifiedByLayout.visibility = View.VISIBLE
                    profileVerifiedBy.text = "Verified By " + user.verifiedBy
                }

                2 -> profileAdminIcon.visibility = View.VISIBLE
            }

            if (user.birth != null) {
                profileBirthLayout.visibility = View.VISIBLE
                profileBirth.text = user.birth.toString()
            }

            if (user.nationality != null) {
                profileNationalityLayout.visibility = View.VISIBLE
                profileNationality.text = user.nationality
            }

            if (user.idNumber != null) {
                profileIdNumberLayout.visibility = View.VISIBLE
                profileIdNumber.text = user.idNumber
            }

            if (user.address != null) {
                profileAddressLayout.visibility = View.VISIBLE
                profileAddress.text = user.address
            }

            if (user.education != null) {
                val backendLevelArray: Array<String> =
                    arrayOf("ilk", "orta", "lise", "yuksekokul", "universite")
                val showArray = resources.getStringArray(R.array.education)
                profileEducationLayout.visibility = View.VISIBLE
                profileEducation.text = showArray[backendLevelArray.indexOf(user.education)]
            }

            if (user.healthCondition != null) {
                profileHealthConditionLayout.visibility = View.VISIBLE
                profileHealthCondition.text = user.healthCondition
            }

            if (user.bloodType != null) {
                profileBloodTypeLayout.visibility = View.VISIBLE
                profileBloodType.text = user.bloodType
            }

            var counter = 0
            for (socialMedia in user.socialMedia) {
                val profileItemBinding: ProfileItemBinding =
                    ProfileItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText.text = socialMedia.platformName
                profileItemBinding.profileItemLink.visibility = View.VISIBLE
                profileItemBinding.profileItemLink.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(socialMedia.profileURL))
                    startActivity(browserIntent)
                }
                when (socialMedia.platformName.lowercase()) {
                    "linkedin" -> profileItemBinding.profileItemIcon.setImageResource(R.drawable.linkedin_icon)
                    "x" -> profileItemBinding.profileItemIcon.setImageResource(R.drawable.x_icon)
                    "github" -> profileItemBinding.profileItemIcon.setImageResource(R.drawable.github_icon)
                    "youtube" -> profileItemBinding.profileItemIcon.setImageResource(R.drawable.youtube_icon)
                }
                profileTopLayout.addView(profileItemBinding.root, 16 + counter)
                counter++
            }

            for (skill in user.skills) {
                val profileItemBinding: ProfileItemBinding =
                    ProfileItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText.text = skill.definition + ": " + skill.level
                profileItemBinding.profileItemLink.visibility = View.VISIBLE
                profileItemBinding.profileItemLink.setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(skill.document))
                    startActivity(browserIntent)
                }
                profileTopLayout.addView(profileItemBinding.root, 18 + counter)
                counter++
            }

            for (language in user.languages) {
                val profileItemBinding: ProfileItemBinding =
                    ProfileItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText.text = language.language + ": " + language.level
                profileTopLayout.addView(profileItemBinding.root, 20 + counter)
                counter++
            }

            for (profession in user.professions) {
                val profileItemBinding: ProfileItemBinding =
                    ProfileItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText.text =
                    profession.profession + ": " + profession.level
                profileTopLayout.addView(profileItemBinding.root, 22 + counter)
                counter++
            }
        }
    }

    private fun clickButtons(user: AuthenticatedUser) {

        binding.profileEditButton.setOnClickListener {
            addFragment(editProfileFragment, user)
        }
    }

    private fun addFragment(fragment: Fragment, user: AuthenticatedUser) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        fragment.arguments = bundle
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment) //replacing fragment
            commit() //call signals to the FragmentManager that all operations have been added to the transaction
        }
    }
}