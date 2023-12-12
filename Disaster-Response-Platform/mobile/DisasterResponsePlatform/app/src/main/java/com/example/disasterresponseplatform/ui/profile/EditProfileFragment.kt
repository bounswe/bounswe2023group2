package com.example.disasterresponseplatform.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.data.enums.Endpoint
import com.example.disasterresponseplatform.data.enums.RequestType
import com.example.disasterresponseplatform.data.models.authModels.Language
import com.example.disasterresponseplatform.data.models.authModels.Profession
import com.example.disasterresponseplatform.data.models.authModels.ProfficiencyRequest
import com.example.disasterresponseplatform.data.models.authModels.ProfileBody
import com.example.disasterresponseplatform.data.models.authModels.ProfileOptionalBody
import com.example.disasterresponseplatform.data.models.authModels.Skill
import com.example.disasterresponseplatform.data.models.authModels.SocialMediaLink
import com.example.disasterresponseplatform.data.models.usertypes.AuthenticatedUser
import com.example.disasterresponseplatform.databinding.FragmentProfileEditBinding
import com.example.disasterresponseplatform.databinding.ProfileEditItemBinding
import com.example.disasterresponseplatform.databinding.ProfileEditSkillBinding
import com.example.disasterresponseplatform.databinding.ProfileEditSocialMediaBinding
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
    private var saveEndCount = 0
    private var bloodType = -1
    private var education = -1
    private var date = ""
    private val networkManager = NetworkManager()
    private var map: HashMap<String, String> = hashMapOf()
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

        socialMediaCount = 0
        skillCount = 0
        languageCount = 0
        professionCount = 0
        saveEndCount = 0
        bloodType = -1
        education = -1
        date = ""
        map = hashMapOf()

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

//            if (user.credibleRegion != null) {
//                profileRegionLayout.visibility = View.VISIBLE
//                profileRegion.setText(user.credibleRegion)
//            } else if (user.roleBasedProficiency != null) {
//                profileProficiencyLayout.visibility = View.VISIBLE
//                profileProficiency.setText(user.roleBasedProficiency)
//            }
            val profArray: Array<String> = arrayOf("bilingual", "doctor", "pharmacist", "rescue_member", "infrastructure_engineer", "it_specialist", "other")
            val profAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                profArray
            )
            profileProficiency.setAdapter(profAdapter)
            profileProficiency.setOnItemClickListener{_, _, position, _ ->
                user.roleBasedProficiency = profArray[position]
            }
            if (user.education != null) {
                profileProficiency.setText(user.roleBasedProficiency)
            }


            if (user.birth != null) {
                profileBirthLayout.visibility = View.VISIBLE
                date = user.birth!!.split(" ")[0]
                profileBirthButton.text = date
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

            profileEducationLayout.visibility = View.VISIBLE
            val backendLevelArray: Array<String> = arrayOf("ilk", "orta", "lise", "yuksekokul", "universite")
            val levelArray2 = resources.getStringArray(R.array.education)

            val levelAdapter2 = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                levelArray2
            )
            spEducation.setAdapter(levelAdapter2)
            spEducation.setOnItemClickListener{_, _, position, _ ->
                education = position
            }
            if (user.education != null) {
                education = backendLevelArray.indexOf(user.education)
                spEducation.setText(levelArray2[education])
            }

            if (user.healthCondition != null) {
                profileHealthConditionLayout.visibility = View.VISIBLE
                profileHealthCondition.setText(user.healthCondition)
            }

            profileBloodTypeLayout.visibility = View.VISIBLE
            val levelArray = resources.getStringArray(R.array.blood_types)
            val levelAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                levelArray
            )
            spBloodType.setAdapter(levelAdapter)
            spBloodType.setOnItemClickListener{_, _, position, _ ->
                bloodType = position
            }
            if (user.bloodType != null) {
                bloodType = levelArray.indexOf(user.bloodType)
                spBloodType.setText(levelArray[bloodType])
            }

            profileAddSocialMediaButton.setOnClickListener {
                val profileItemBinding: ProfileEditSocialMediaBinding = ProfileEditSocialMediaBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemHint1.hint = "Platform Name"
                profileItemBinding.profileItemHint2.hint = "Profile URL"
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    socialMediaCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 16 + socialMediaCount)
                socialMediaCount++
            }
            for (socialMedia in user.socialMedia) {
                val profileItemBinding: ProfileEditSocialMediaBinding = ProfileEditSocialMediaBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(socialMedia.platformName)
                profileItemBinding.profileItemHint1.hint = "Platform Name"
                profileItemBinding.profileItemText2.setText(socialMedia.profileURL)
                profileItemBinding.profileItemHint2.hint = "Profile URL"
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
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
                    profileTopLayout.removeView(profileItemBinding.root)
                    socialMediaCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 16 + socialMediaCount)
                socialMediaCount++
            }

            profileAddSkillButton.setOnClickListener {
                val profileItemBinding: ProfileEditSkillBinding = ProfileEditSkillBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemHint1.hint = "Definition"
                val backendLevelArray: Array<String> = arrayOf("beginner", "basic", "intermediate", "skilled", "expert")
                val levelArray = resources.getStringArray(R.array.skill_level_types)
                val levelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    levelArray
                )
                profileItemBinding.spinner.setAdapter(levelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.hint = backendLevelArray[position]
                    map[profileItemBinding.profileItemText1.text.toString()] = backendLevelArray[position]
                }
                profileItemBinding.profileItemHint3.hint = "Document Link"
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    skillCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 18 + socialMediaCount + skillCount)
                skillCount++
            }
            for (skill in user.skills) {
                map[skill.definition] = skill.level
                val profileItemBinding: ProfileEditSkillBinding = ProfileEditSkillBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(skill.definition)
                profileItemBinding.profileItemText3.setText(skill.document)
                profileItemBinding.profileItemHint1.hint = "Definition"
                val backendLevelArray: Array<String> = arrayOf("beginner", "basic", "intermediate", "skilled", "expert")
                val levelArray = resources.getStringArray(R.array.skill_level_types)
                val levelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    levelArray
                )
                profileItemBinding.spinner.setAdapter(levelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.hint = backendLevelArray[position]
                    map[skill.definition] = backendLevelArray[position]
                }
                profileItemBinding.spinner.hint = skill.level
                map[skill.definition] = skill.level
                profileItemBinding.profileItemHint3.hint = "Document Link"
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    networkManager.makeRequest(
                        endpoint=Endpoint.SKILL_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(Skill(user.username, profileItemBinding.profileItemText1.text.toString(), map[profileItemBinding.profileItemText1.text.toString()]!!)).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
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
                    profileTopLayout.removeView(profileItemBinding.root)
                    skillCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 18 + socialMediaCount + skillCount)
                skillCount++
            }

            profileAddLanguageButton.setOnClickListener {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemHint1.hint = "Language"
                val backendLevelArray: Array<String> = arrayOf("beginner", "intermediate", "advanced", "native")
                val levelArray = resources.getStringArray(R.array.language_level_types)
                val levelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    levelArray
                )
                profileItemBinding.spinner.setAdapter(levelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.hint = backendLevelArray[position]
                    map[profileItemBinding.profileItemText1.text.toString()] = backendLevelArray[position]
                }
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    languageCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 20 + socialMediaCount + skillCount + languageCount)
                languageCount++
            }
            for (language in user.languages) {
                map[language.language] = language.level
                println("language: " + language.language)
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(language.language)
                println(profileItemBinding.profileItemText1.text)
                profileItemBinding.profileItemHint1.hint = "Language"
                val backendLevelArray: Array<String> = arrayOf("beginner", "intermediate", "advanced", "native")
                val levelArray = resources.getStringArray(R.array.language_level_types)
                val levelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    levelArray
                )
                profileItemBinding.spinner.setAdapter(levelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.hint = backendLevelArray[position]
                    map[profileItemBinding.profileItemText1.text.toString()] = backendLevelArray[position]
                }
                println("language spinner")
                println(language.level)
                println(backendLevelArray.indexOf(language.level))
                profileItemBinding.spinner.hint = language.level
                map[language.language] = language.level
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    networkManager.makeRequest(
                        endpoint=Endpoint.LANGUAGE_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(Language(user.username, profileItemBinding.profileItemText1.text.toString(), map[profileItemBinding.profileItemText1.text.toString()]!!)).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
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
                    profileTopLayout.removeView(profileItemBinding.root)
                    languageCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 20 + socialMediaCount + skillCount + languageCount)
                languageCount++
            }

            profileAddProfessionButton.setOnClickListener {
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemHint1.hint = "Profession"
                val backendLevelArray: Array<String> = arrayOf("amateur", "pro", "certified pro")
                val levelArray = resources.getStringArray(R.array.profession_level_types)
                val levelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    levelArray
                )
                profileItemBinding.spinner.setAdapter(levelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.hint = backendLevelArray[position]
                    map[profileItemBinding.profileItemText1.text.toString()] = backendLevelArray[position]
                }
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    profileTopLayout.removeView(profileItemBinding.root)
                    professionCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 22 + socialMediaCount + skillCount + languageCount + professionCount)
                professionCount++
            }
            for (profession in user.professions) {
                map[profession.profession] = profession.level
                val profileItemBinding: ProfileEditItemBinding = ProfileEditItemBinding.inflate(LayoutInflater.from(requireContext()))
                profileItemBinding.profileItemText1.setText(profession.profession)
                profileItemBinding.profileItemHint1.hint = "Profession"
                val backendLevelArray: Array<String> = arrayOf("amateur", "pro", "certified pro")
                val levelArray = resources.getStringArray(R.array.profession_level_types)
                val levelAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        levelArray
                    )
                profileItemBinding.spinner.setAdapter(levelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.hint = backendLevelArray[position]
                    map[profileItemBinding.profileItemText1.text.toString()] = backendLevelArray[position]
                }
                profileItemBinding.spinner.hint = profession.level
                map[profession.profession] = profession.level
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    networkManager.makeRequest(
                        endpoint=Endpoint.PROFESSION_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(Profession(user.username, profileItemBinding.profileItemText1.text.toString(), map[profileItemBinding.profileItemText1.text.toString()]!!)).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
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
                    profileTopLayout.removeView(profileItemBinding.root)
                    professionCount--
                }
                profileTopLayout.addView(profileItemBinding.root, 22 + socialMediaCount + skillCount + languageCount + professionCount)
                professionCount++
            }
            profileTopLayout.requestLayout()
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
                        println("zorunlu fail")
                        saveEnded()
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody>,
                        response: retrofit2.Response<ResponseBody>
                    ) {
                        println("zorunlu response")
                        saveEnded()
                    }
                }
            )
            if (user.roleBasedProficiency != "") {
                val profBody = ProfficiencyRequest(user.roleBasedProficiency, "details")
                val profJson = gson.toJson(profBody)
                val profRequestBody =
                    profJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                networkManager.makeRequest(
                    endpoint = Endpoint.PROFICIENCY_REQUEST,
                    requestType = RequestType.POST,
                    headers = headers,
                    requestBody = profRequestBody,
                    callback = object : retrofit2.Callback<ResponseBody> {
                        override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "Network error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            println("prof fail")
                            saveEnded()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            println("prof response")
                            saveEnded()
                        }
                    }
                )
            } else saveEndCount++

            val backendLevelArray: Array<String> = arrayOf("ilk", "orta", "lise", "yuksekokul", "universite")
            val obody = ProfileOptionalBody(
                username = profileUsername.text.toString(),
                dateOfBirth = date.ifBlank { null },
                nationality = if (profileNationality.text.isBlank()) null else profileNationality.text.toString(),
                identityNumber = if (profileIdNumber.text.isBlank()) null else profileIdNumber.text.toString(),
                education = if (spEducation.text.isBlank()) null else backendLevelArray[education],
                healthCondition = if (profileHealthCondition.text.isBlank()) null else profileHealthCondition.text.toString(),
                bloodType = if (spBloodType.text.isBlank()) null else resources.getStringArray(R.array.blood_types)[bloodType],
                address = if (profileAddress.text.isBlank()) null else profileAddress.text.toString()
            )
            val json2 = gson.toJson(obody)
            println("gonderiyorum")
            println(json2)
            val requestBody2 =
                json2.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            networkManager.makeRequest(
                endpoint = Endpoint.ME_OPTIONAL_DELETE,
                requestType = RequestType.DELETE,
                headers = headers,
                callback = object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Network error: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        println("optional fail")
                        saveEnded()
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody>,
                        response: retrofit2.Response<ResponseBody>
                    ) {
                        println("Delete response:")
                        println(response.code())
                        println(response.body())
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

                                    println("delete fail")
                                    saveEnded()
                                }

                                override fun onResponse(
                                    call: retrofit2.Call<ResponseBody>,
                                    response: retrofit2.Response<ResponseBody>
                                ) {
                                    println("delete response")
                                    saveEnded()
                                }
                            }
                        )
                    }
                }
            )


            // social media
            for (i in 0 until socialMediaCount) {
                val socialMedia = binding.profileTopLayout.getChildAt(16 + i)
                val socialMediaName =
                    socialMedia.findViewById<AppCompatEditText>(R.id.profile_item_text1).text.toString()
                val socialMediaURL =
                    socialMedia.findViewById<AppCompatEditText>(R.id.profile_item_text2).text.toString()
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
                            println("social media fail")
                            saveEnded()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            println("social media response")
                            saveEnded()
                        }
                    }
                )
            }

            // skills
            for (i in 0 until skillCount) {
                val skill = binding.profileTopLayout.getChildAt(18 + socialMediaCount + i)
                val skillDefinition =
                    skill.findViewById<AppCompatEditText>(R.id.profile_item_text1).text.toString()
                val skillLevel = map[skillDefinition]!!
                val skillDocument =
                    skill.findViewById<AppCompatEditText>(R.id.profile_item_text3).text.toString()
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
                            println("skill fail")
                            saveEnded()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            println("skill response")
                            saveEnded()
                        }
                    }
                )
            }

            // languages
            for (i in 0 until languageCount) {
                val language = binding.profileTopLayout.getChildAt(20 + socialMediaCount + skillCount + i)
                val languageName =
                    language.findViewById<AppCompatEditText>(R.id.profile_item_text1).text.toString()
                val languageLevel = map[languageName]!!
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
                            println("language fail")
                            saveEnded()
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
                            println("language response")
                            saveEnded()
                        }
                    }
                )
            }
            // profession
            for (i in 0 until professionCount) {
                val profession = binding.profileTopLayout.getChildAt(22 + socialMediaCount + skillCount + languageCount + i)
                val professionName =
                    profession.findViewById<AppCompatEditText>(R.id.profile_item_text1).text.toString()
                val professionLevel = map[professionName]!!
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
                            println("profession fail")
                            saveEnded()
                        }

                        override fun onResponse(
                            call: retrofit2.Call<ResponseBody>,
                            response: retrofit2.Response<ResponseBody>
                        ) {
                            println("profession response")
                            saveEnded()
                        }
                    }
                )
            }
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

            profileBirthButton.setOnClickListener {
                var year = 2000
                var month = 1
                var day = 1
                if (date.isNotBlank()) {
                    year = date.split("-")[0].toInt()
                    month = date.split("-")[1].toInt()
                    day = date.split("-")[2].toInt()
                }
                val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener {
                        _, year, month, dayOfMonth ->
                    date = "$year-${month+1}-$dayOfMonth"
                    profileBirthButton.text = date
                }, year, month, day)
                datePickerDialog.show()
            }
        }
    }

    private fun saveEnded() {
        saveEndCount++
        if (saveEndCount == 3 + socialMediaCount + skillCount + languageCount + professionCount) {
            Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    override fun onPause() {
        removeViews()
        super.onPause()
    }

}