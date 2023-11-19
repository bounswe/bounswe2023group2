package com.example.disasterresponseplatform.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.authModels.Language
import com.example.disasterresponseplatform.data.models.authModels.LanguageArray
import com.example.disasterresponseplatform.data.models.authModels.Profession
import com.example.disasterresponseplatform.data.models.authModels.ProfessionArray
import com.example.disasterresponseplatform.data.models.authModels.ProfileBody
import com.example.disasterresponseplatform.data.models.authModels.ProfileOptionalBody
import com.example.disasterresponseplatform.data.models.authModels.Skill
import com.example.disasterresponseplatform.data.models.authModels.SkillArray
import com.example.disasterresponseplatform.data.models.authModels.SocialMediaArray
import com.example.disasterresponseplatform.data.models.authModels.SocialMediaLink
import com.example.disasterresponseplatform.data.models.authModels.UsersMeResponse
import com.example.disasterresponseplatform.data.models.usertypes.AuthenticatedUser
import com.example.disasterresponseplatform.data.models.usertypes.CredibleUser
import com.example.disasterresponseplatform.data.models.usertypes.RoleBasedUser
import com.example.disasterresponseplatform.databinding.FragmentProfileEditBinding
import com.example.disasterresponseplatform.databinding.ProfileEditItemBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.managers.NetworkManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
    private lateinit var user: AuthenticatedUser
    private lateinit var globalProfileImage: ImageView
    private var socialMediaCount = 0
    private var skillCount = 0
    private var languageCount = 0
    private var professionCount = 0
    private val networkManager = NetworkManager()
    private val headers = mapOf(
        "Authorization" to "bearer " + DiskStorageManager.getKeyValue("token"),
        "Content-Type" to "application/json"
    )
    private val gson = Gson()

    private val changeImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val data: Intent? = result.data
                val selectedImage: Uri? = data?.data
                globalProfileImage.setImageURI(selectedImage)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Too big file", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileEditBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get user from intent
        user = (arguments?.getSerializable("user") as AuthenticatedUser?)!!

        socialMediaCount = 0;
        skillCount = 0;
        languageCount = 0;
        professionCount = 0;

        fillInformations(user)
        setOnClicks(user)
    }

    private fun fillInformations(user: AuthenticatedUser) {
        binding.apply {
            // read image from url and set it to profilePhoto
            if (user.profilePhoto != null) {
                Picasso.get().load(user.profilePhoto!!.toUri()).into(profileImage)
            }
            globalProfileImage = profileImage
            profileName.setText(user.name)
            profileSurname.setText(user.surname)
            profileInfoVisible.isChecked = user.profileInfoShared
            profileUsername.text = user.username
            profileEmail.setText(user.email)
            profilePhoneNumber.setText(user.phone)
            profileEmailVerifiedIcon.visibility = if (user.isEmailVerified) View.VISIBLE else View.GONE
            profilePhoneVerifiedIcon.visibility = if (user.isPhoneVerified) View.VISIBLE else View.GONE
            when (user) {
                is CredibleUser -> {
                    profileRegionLayout.visibility = View.VISIBLE
                    profileRegion.setText(user.region)
                }
                is RoleBasedUser -> {
                    profileProficiencyLayout.visibility = View.VISIBLE
                    profileProficiency.setText(user.proficiency)
                }
            }

            if (user.birth != null) {
                profileBirthLayout.visibility = View.VISIBLE
                profileBirth.setText(user.birth.toString())
            }

            if (user.nationality != null) {
                profileNationalityLayout.visibility = View.VISIBLE
                profileNationality.setText(user.nationality)
            }

            if (user.idNumber != null) {
                profileIdNumberLayout.visibility = View.VISIBLE
                profileIdNumber.setText(user.idNumber)
            }

            if (user.address != null) {
                profileAddressLayout.visibility = View.VISIBLE
                profileAddress.setText(user.address)
            }

            if (user.education != null) {
                profileEducationLayout.visibility = View.VISIBLE
                profileEducation.setText(user.education)
            }

            if (user.healthCondition != null) {
                profileHealthConditionLayout.visibility = View.VISIBLE
                profileHealthCondition.setText(user.healthCondition)
            }

            if (user.bloodType != null) {
                profileBloodTypeLayout.visibility = View.VISIBLE
                profileBloodType.setText(user.bloodType)
            }

            profileAddSocialMediaButton.setOnClickListener {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.hint = "Platform Name"
                profileItemBinding.profileItemText2.hint = "Profile URL"
                profileItemBinding.profileItemText3.visibility = View.GONE
                profileTopLayout.addView(profileItemBinding.root, 16 + socialMediaCount)
                socialMediaCount++
            }
            for (socialMedia in user.socialMedia) {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(socialMedia.platformName)
                profileItemBinding.profileItemText1.hint = "Platform Name"
                profileItemBinding.profileItemText2.setText(socialMedia.profileURL)
                profileItemBinding.profileItemText2.hint = "Profile URL"
                profileItemBinding.profileItemText3.visibility = View.GONE
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    networkManager.makeRequest(
                        endpoint=Endpoint.SOCIAL_MEDIA_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(SocialMediaLink(user.username, profileItemBinding.profileItemText1.text.toString(), profileItemBinding.profileItemText2.text.toString())).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        callback=object : retrofit2.Callback<ResponseBody> {
                            override fun onFailure(
                                call: retrofit2.Call<ResponseBody>,
                                t: Throwable
                            ) {
                                Toast.makeText(
                                    requireContext(),
                                    "Network error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: retrofit2.Call<ResponseBody>,
                                response: retrofit2.Response<ResponseBody>
                            ) {}
                        }
                    )
                    socialMediaCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 16 + socialMediaCount)
                socialMediaCount++
            }

            profileAddSkillButton.setOnClickListener {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.hint = "Definition"
                profileItemBinding.profileItemText2.hint = "Level"
                profileItemBinding.profileItemText3.hint = "Document Link"
                profileTopLayout.addView(profileItemBinding.root, 18 + socialMediaCount + skillCount)
                skillCount++
            }
            for (skill in user.skills) {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(skill.definition)
                profileItemBinding.profileItemText2.setText(skill.level)
                profileItemBinding.profileItemText3.setText(skill.document)
                profileItemBinding.profileItemText1.hint = "Definition"
                profileItemBinding.profileItemText2.hint = "Level"
                profileItemBinding.profileItemText3.hint = "Document Link"
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    networkManager.makeRequest(
                        endpoint=Endpoint.SKILL_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(Skill(user.username, profileItemBinding.profileItemText1.text.toString(), profileItemBinding.profileItemText2.text.toString())).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        callback=object : retrofit2.Callback<ResponseBody> {
                            override fun onFailure(
                                call: retrofit2.Call<ResponseBody>,
                                t: Throwable
                            ) {
                                Toast.makeText(
                                    requireContext(),
                                    "Network error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: retrofit2.Call<ResponseBody>,
                                response: retrofit2.Response<ResponseBody>
                            ) {}
                        }
                    )
                    skillCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 18 + socialMediaCount + skillCount)
                skillCount++
            }

            profileAddLanguageButton.setOnClickListener {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.hint = "Language"
                profileItemBinding.profileItemText2.hint = "Level"
                profileItemBinding.profileItemText3.visibility = View.GONE
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    languageCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 20 + socialMediaCount + skillCount + languageCount)
                languageCount++
            }
            for (language in user.languages) {
                println("language: " + language.language)
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(language.language)
                println(profileItemBinding.profileItemText1.text)
                profileItemBinding.profileItemText2.setText(language.level)
                profileItemBinding.profileItemText1.hint = "Language"
                profileItemBinding.profileItemText2.hint = "Level"
                profileItemBinding.profileItemText3.visibility = View.GONE
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    println("Delete request made")
                    println(gson.toJson(Language(user.username, profileItemBinding.profileItemText1.text.toString(), profileItemBinding.profileItemText2.text.toString())))
                    networkManager.makeRequest(
                        endpoint=Endpoint.LANGUAGE_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(Language(user.username, profileItemBinding.profileItemText1.text.toString(), profileItemBinding.profileItemText2.text.toString())).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        callback=object : retrofit2.Callback<ResponseBody> {
                            override fun onFailure(
                                call: retrofit2.Call<ResponseBody>,
                                t: Throwable
                            ) {
                                Toast.makeText(
                                    requireContext(),
                                    "Network error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: retrofit2.Call<ResponseBody>,
                                response: retrofit2.Response<ResponseBody>
                            ) {}
                        }
                    )
                    languageCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 20 + socialMediaCount + skillCount + languageCount)
                languageCount++
            }

            profileAddProfessionButton.setOnClickListener {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.hint = "Profession"
                profileItemBinding.profileItemText2.hint = "Level"
                profileItemBinding.profileItemText3.visibility = View.GONE
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    professionCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 22 + socialMediaCount + skillCount + languageCount + professionCount)
                professionCount++
            }
            for (profession in user.professions) {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(profession.profession)
                profileItemBinding.profileItemText2.setText(profession.level)
                profileItemBinding.profileItemText1.hint = "Profession"
                profileItemBinding.profileItemText2.hint = "Level"
                profileItemBinding.profileItemText3.visibility = View.GONE
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    networkManager.makeRequest(
                        endpoint=Endpoint.PROFESSION_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(Profession(user.username, profileItemBinding.profileItemText1.text.toString(), profileItemBinding.profileItemText2.text.toString())).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        callback=object : retrofit2.Callback<ResponseBody> {
                            override fun onFailure(
                                call: retrofit2.Call<ResponseBody>,
                                t: Throwable
                            ) {
                                Toast.makeText(
                                    requireContext(),
                                    "Network error: ${t.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: retrofit2.Call<ResponseBody>,
                                response: retrofit2.Response<ResponseBody>
                            ) {}
                        }
                    )
                    professionCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 22 + socialMediaCount + skillCount + languageCount + professionCount)
                professionCount++
            }
        }
    }

    private fun saveChanges(user: AuthenticatedUser) {
        binding.apply {
            //                when (user) {
//                    is CredibleUser -> user.region = profileRegion.text.toString()
//                    is RoleBasedUser -> user.proficiency = profileProficiency.text.toString()
//                }

            val body = ProfileBody(
                email = profileEmail.text.toString(),
                firstName = profileName.text.toString(),
                lastName = profileSurname.text.toString(),
                phoneNumber = profilePhoneNumber.text.toString(),
                privateAccount = !profileInfoVisible.isChecked
            )
            val json = gson.toJson(body)
            val requestBody =
                json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            networkManager.makeRequest(
                endpoint = Endpoint.ME_SET,
                requestType = RequestType.PUT,
                headers = headers,
                requestBody = requestBody,
                callback = object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Network error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody>,
                        response: retrofit2.Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
//                                Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
                        } else {
//                                Toast.makeText(requireContext(), "Me set Some error happened: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
            val obody = ProfileOptionalBody(
                username = profileUsername.text.toString(),
                dateOfBirth = if (profileBirth.text.isBlank()) null else profileBirth.text.toString(),
                nationality = if (profileNationality.text.isBlank()) null else profileNationality.text.toString(),
                identityNumber = if (profileIdNumber.text.isBlank()) null else profileIdNumber.text.toString(),
                education = if (profileEducation.text.isBlank()) null else profileEducation.text.toString(),
                healthCondition = if (profileHealthCondition.text.isBlank()) null else profileHealthCondition.text.toString(),
                bloodType = if (profileBloodType.text.isBlank()) null else profileBloodType.text.toString(),
                address = if (profileAddress.text.isBlank()) null else profileAddress.text.toString()
            )
            val json2 = gson.toJson(obody)
            println(json2)
            val requestBody2 =
                json2.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            networkManager.makeRequest(
                endpoint = Endpoint.ME_OPTIONAL_SET,
                requestType = RequestType.POST,
                headers = headers,
                requestBody = requestBody2,
                callback = object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Network error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody>,
                        response: retrofit2.Response<ResponseBody>
                    ) {
                    }
                }
            )


            // social media
            for (i in 0 until socialMediaCount) {
                val socialMedia = binding.profileTopLayout.getChildAt(16 + i)
                val socialMediaName =
                    socialMedia.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text1).text.toString()
                val socialMediaURL =
                    socialMedia.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text2).text.toString()
                val social_media_body =
                    SocialMediaLink(user.username, socialMediaName, socialMediaURL)
                val jsonSocialMedia = gson.toJson(social_media_body)
                val requestBodySocialMedia =
                    jsonSocialMedia.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                networkManager.makeRequest(
                    endpoint = Endpoint.SOCIAL_MEDIA_SET,
                    requestType = RequestType.POST,
                    headers = headers,
                    requestBody = requestBodySocialMedia,
                    callback = object : retrofit2.Callback<ResponseBody> {
                        override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "Network error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
//                                Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
                            } else {
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Social media set Some error happened: ${response.message()}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                            }
                        }
                    }
                )
            }

            // skills
            for (i in 0 until skillCount) {
                val skill = binding.profileTopLayout.getChildAt(18 + socialMediaCount + i)
                val skillDefinition =
                    skill.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text1).text.toString()
                val skillLevel =
                    skill.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text2).text.toString()
                val skillDocument =
                    skill.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text3).text.toString()
                val skill_body = Skill(user.username, skillDefinition, skillLevel)
                val jsonSkill = gson.toJson(skill_body)
                val requestBodySkill =
                    jsonSkill.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                networkManager.makeRequest(
                    endpoint = Endpoint.SKILL_SET,
                    requestType = RequestType.POST,
                    headers = headers,
                    requestBody = requestBodySkill,
                    callback = object : retrofit2.Callback<ResponseBody> {
                        override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "Network error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
//                                Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
                            } else {
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Skill set Some error happened: ${response.message()}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                            }
                        }
                    }
                )
            }

            // languages
            for (i in 0 until languageCount) {
                val language = binding.profileTopLayout.getChildAt(20 + socialMediaCount + skillCount + i)
                val languageName =
                    language.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text1).text.toString()
                val languageLevel =
                    language.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text2).text.toString()
                val language_body = Language(user.username, languageName, languageLevel)
                val jsonLanguage = gson.toJson(language_body)
                println(jsonLanguage)
                val requestBodyLanguage =
                    jsonLanguage.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                networkManager.makeRequest(
                    endpoint = Endpoint.LANGUAGE_SET,
                    requestType = RequestType.POST,
                    headers = headers,
                    requestBody = requestBodyLanguage,
                    callback = object : retrofit2.Callback<ResponseBody> {
                        override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "Network error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
//                                Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
                            } else {
                                println(response.body())
                                println(response.errorBody())
                            }
                        }
                    }
                )
            }
            // profession
            for (i in 0 until professionCount) {
                val profession = binding.profileTopLayout.getChildAt(22 + socialMediaCount + skillCount + languageCount + i)
                val professionName =
                    profession.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text1).text.toString()
                val professionLevel =
                    profession.findViewById<AppCompatEditText>(com.example.disasterresponseplatform.R.id.profile_item_text2).text.toString()
                val profession_body = Profession(user.username, professionName, professionLevel)
                val jsonProfession = gson.toJson(profession_body)
                val requestBodyProfession =
                    jsonProfession.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                networkManager.makeRequest(
                    endpoint = Endpoint.PROFESSION_SET,
                    requestType = RequestType.POST,
                    headers = headers,
                    requestBody = requestBodyProfession,
                    callback = object : retrofit2.Callback<ResponseBody> {
                        override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "Network error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
//                                Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
                            } else {
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Profession set Some error happened: ${response.message()}",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                            }
                        }
                    }
                )
            }

            parentFragmentManager.popBackStack()
        }

        Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
    }

    private fun removeViews() {
        binding.apply {

            // social media
            for (i in 0 until socialMediaCount) {
                val socialMedia = binding.profileTopLayout.getChildAt(16)
                binding.profileTopLayout.removeView(socialMedia)
            }

            // skills
            for (i in 0 until skillCount) {
                val skill = binding.profileTopLayout.getChildAt(18)
                binding.profileTopLayout.removeView(skill)
            }

            // languages
            for (i in 0 until languageCount) {
                val language = binding.profileTopLayout.getChildAt(20)
                binding.profileTopLayout.removeView(language)
            }
            // profession
            for (i in 0 until professionCount) {
                val profession = binding.profileTopLayout.getChildAt(22)
                binding.profileTopLayout.removeView(profession)
            }
        }
    }

    private fun setOnClicks(user: AuthenticatedUser) {
        binding.apply {
            profileImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                try {
                    changeImage.launch(intent)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Some unexpected error occurred", Toast.LENGTH_LONG).show()
                }
            }

            profileEditConfirmButton.setOnClickListener {
                saveChanges(user)
            }
        }
    }

    override fun onPause() {
        removeViews()
        super.onPause()
    }

}