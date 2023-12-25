package com.example.disasterresponseplatform.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
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
import com.example.disasterresponseplatform.ui.authentication.LoginFragment
import com.example.disasterresponseplatform.utils.FileUploadTask
import com.example.disasterresponseplatform.utils.ImageUploadTask
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody

@Suppress("DEPRECATION")
class EditProfileFragment(var user: AuthenticatedUser) : Fragment() {

    private lateinit var binding: FragmentProfileEditBinding
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
    private var imageChanged = false
    private val skillMap = HashMap<Int, Any?>()

    private var changeImage: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> onResult(result) }

    private fun onResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val data: Intent? = result.data
                val selectedImage: Uri? = data?.data
                selectedImage.let { uri -> Glide.with(requireContext()).load(uri).into(globalProfileImage) }
                imageChanged = true
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Profile picture could not be updated", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Change ActionBar and StatusBar color
        (activity as AppCompatActivity).supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.primary)))
        (activity as AppCompatActivity).window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)

        binding = FragmentProfileEditBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                profileItemBinding.profileItemHint1.hint = getString(R.string.platform_name)
                profileItemBinding.profileItemHint2.hint = getString(R.string.profile_url)
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
                profileItemBinding.profileItemHint1.hint = getString(R.string.platform_name)
                profileItemBinding.profileItemText2.setText(socialMedia.profileURL)
                profileItemBinding.profileItemHint2.hint = getString(R.string.profile_url)
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
                profileItemBinding.profileItemHint1.hint = getString(R.string.definition)
                val profileBackendLevelArray: Array<String> = arrayOf("beginner", "basic", "intermediate", "skilled", "expert")
                val profileLevelArray = resources.getStringArray(R.array.skill_level_types)
                val profileLevelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    profileLevelArray
                )
                profileItemBinding.spinner.setAdapter(profileLevelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.setText(profileLevelArray[position])
                    map[profileItemBinding.profileItemText1.text.toString()] = profileBackendLevelArray[position]
                }
                profileItemBinding.uploadFile.setOnClickListener {
                    val intent = Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT)
                    startActivityForResult(Intent.createChooser(intent,
                        getString(R.string.select_a_file)),
                        profileItemBinding.uploadFile.hashCode())
                }
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
                skillMap[profileItemBinding.uploadFile.hashCode()] = skill.document
                profileItemBinding.profileItemText1.setText(skill.definition)
                profileItemBinding.profileItemHint1.hint = getString(R.string.definition)
                val skillBackendLevelArray: Array<String> = arrayOf("beginner", "basic", "intermediate", "skilled", "expert")
                val skillLevelArray = resources.getStringArray(R.array.skill_level_types)
                val skillLevelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    skillLevelArray
                )
                profileItemBinding.spinner.setAdapter(skillLevelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.setText(skillLevelArray[position])
                    map[skill.definition] = skillBackendLevelArray[position]
                }
                profileItemBinding.spinner.setText(skillLevelArray[skillBackendLevelArray.indexOf(skill.level)])
                map[skill.definition] = skill.level
                profileItemBinding.uploadFile.setOnClickListener {
                    val intent = Intent().setType("*/*").setAction(Intent.ACTION_GET_CONTENT)
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_a_file)),
                        profileItemBinding.uploadFile.hashCode())
                }
                profileItemBinding.profileDeleteItemIcon.setOnClickListener {
                    networkManager.makeRequest(
                        endpoint=Endpoint.SKILL_DELETE,
                        requestType=RequestType.POST,
                        headers=headers,
                        requestBody=gson.toJson(Skill(user.username, profileItemBinding.profileItemText1.text.toString(), map[profileItemBinding.profileItemText1.text.toString()]!!, null)).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()),
                        callback=object : retrofit2.Callback<ResponseBody> {
                            override fun onFailure(
                                call: retrofit2.Call<ResponseBody>,
                                t: Throwable
                            ) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.network_error, t.message),
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
                profileItemBinding.profileItemHint1.hint = getString(R.string.language)
                val languageBackendLevelArray: Array<String> = arrayOf("beginner", "intermediate", "advanced", "native")
                val languageLevelArray = resources.getStringArray(R.array.language_level_types)
                val languageLevelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    languageLevelArray
                )
                profileItemBinding.spinner.setAdapter(languageLevelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.setText(languageLevelArray[position])
                    map[profileItemBinding.profileItemText1.text.toString()] = languageBackendLevelArray[position]
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
                profileItemBinding.profileItemHint1.hint = getString(R.string.language)
                val languageBackendLevelArray: Array<String> = arrayOf("beginner", "intermediate", "advanced", "native")
                val languageLevelArray = resources.getStringArray(R.array.language_level_types)
                val languageLevelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    languageLevelArray
                )
                profileItemBinding.spinner.setAdapter(languageLevelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.setText(languageLevelArray[position])
                    map[profileItemBinding.profileItemText1.text.toString()] = languageBackendLevelArray[position]
                }
                println("language spinner")
                println(language.level)
                println(languageBackendLevelArray.indexOf(language.level))
                profileItemBinding.spinner.setText(languageLevelArray[languageBackendLevelArray.indexOf(language.level)])
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
                                    getString(R.string.network_error),
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
                profileItemBinding.profileItemHint1.hint = getString(R.string.profession)
                val professionBackendLevelArray: Array<String> = arrayOf("amateur", "pro", "certified pro")
                val professionLevelArray = resources.getStringArray(R.array.profession_level_types)
                val professionLevelAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    professionLevelArray
                )
                profileItemBinding.spinner.setAdapter(professionLevelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.setText(professionLevelArray[position])
                    map[profileItemBinding.profileItemText1.text.toString()] = professionBackendLevelArray[position]
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
                profileItemBinding.profileItemHint1.hint = getString(R.string.profession)
                val professionBackendLevelArray: Array<String> = arrayOf("amateur", "pro", "certified pro")
                val professionLevelArray = resources.getStringArray(R.array.profession_level_types)
                val professionLevelAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        professionLevelArray
                    )
                profileItemBinding.spinner.setAdapter(professionLevelAdapter)
                profileItemBinding.spinner.setOnItemClickListener{_, _, position, _ ->
                    profileItemBinding.spinner.setText(professionLevelArray[position])
                    map[profileItemBinding.profileItemText1.text.toString()] = professionBackendLevelArray[position]
                }
                profileItemBinding.spinner.setText(professionLevelArray[professionBackendLevelArray.indexOf(profession.level)])
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
                                    getString(R.string.network_error),
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

    private fun updateOptional(user: AuthenticatedUser) {
        binding.apply {
            val backendLevelArray: Array<String> =
                arrayOf("ilk", "orta", "lise", "yuksekokul", "universite")
            val obody = ProfileOptionalBody(
                username = profileUsername.text.toString(),
                dateOfBirth = date.ifBlank { null },
                nationality = if (profileNationality.text.isBlank()) null else profileNationality.text.toString(),
                identityNumber = if (profileIdNumber.text.isBlank()) null else profileIdNumber.text.toString(),
                education = if (spEducation.text.isBlank()) null else backendLevelArray[education],
                healthCondition = if (profileHealthCondition.text.isBlank()) null else profileHealthCondition.text.toString(),
                bloodType = if (spBloodType.text.isBlank()) null else resources.getStringArray(R.array.blood_types)[bloodType],
                address = if (profileAddress.text.isBlank()) null else profileAddress.text.toString(),
                profilePicture = user.profilePhoto
            )
            val json2 = gson.toJson(obody)
            println("optional gonderiyorum")
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
                            getString(R.string.network_error),
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
                                override fun onFailure(
                                    call: retrofit2.Call<ResponseBody>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.network_error),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    println("optional set fail")
                                    saveEnded()
                                }

                                override fun onResponse(
                                    call: retrofit2.Call<ResponseBody>,
                                    response: retrofit2.Response<ResponseBody>
                                ) {
                                    println("optional set successful")
                                    println(response.code())
                                    println(response.body())
                                    saveEnded()
                                }
                            }
                        )
                    }
                }
            )
        }
    }

    private fun saveChanges(user: AuthenticatedUser) {
        binding.apply {

            // send image data as multipart
            println("uploading image")
            if (imageChanged)
                ImageUploadTask(globalProfileImage.drawable.toBitmap(), user.username, object : ImageUploadTask.OnImageUploadListener {
                    override fun onImageUploadSuccess(response: String) {
                        println("Image upload successful")
                        var url = response.substring(response.indexOf("\"url\":\"") + 7)
                        url = url.substring(0, url.indexOf("\""))
                        url = url.replace("\\/", "/")
                        user.profilePhoto = url
                        updateOptional(user)
                    }

                    override fun onImageUploadFailure(errorMessage: String) {
                        println("Image upload error: $errorMessage")
                        Toast.makeText(requireContext(),
                            getString(R.string.profile_picture_could_not_be_updated), Toast.LENGTH_LONG).show()
                        updateOptional(user)
                    }
                }).execute()
            else updateOptional(user)


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
                            getString(R.string.network_error),
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
                                getString(R.string.network_error),
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
            } else saveEnded()


            // social media
            for (i in 0 until socialMediaCount) {
                val socialMedia = binding.profileTopLayout.getChildAt(16 + i)
                val socialMediaName =
                    socialMedia.findViewById<AppCompatEditText>(R.id.profile_item_text1).text.toString()
                val socialMediaURL =
                    socialMedia.findViewById<AppCompatEditText>(R.id.profile_item_text2).text.toString()
                val socialMediaBody =
                    SocialMediaLink(user.username, socialMediaName, socialMediaURL)
                val jsonSocialMedia = gson.toJson(socialMediaBody)
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
                                getString(R.string.network_error),
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
                when (val document = skillMap[skill.findViewById<ImageButton>(R.id.upload_file).hashCode()]) {
                    is String -> {
                        val skillBody = Skill(user.username, skillDefinition, skillLevel, document)
                        val jsonSkill = gson.toJson(skillBody)
                        println("Skill post request body: $jsonSkill")
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
                                        getString(R.string.network_error),
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
                                    println(response.code())
                                    println(response.body())
                                    println(response.errorBody())
                                    saveEnded()
                                }
                            }
                        )

                    }

                    is Uri -> FileUploadTask(document, user.username,  requireContext().contentResolver, object : FileUploadTask.OnFileUploadListener {
                        override fun onFileUploadSuccess(response: String) {
                            println("File upload successful: $response")
                            val fileUrl = response.substring(8, response.length - 2)
                            println("File url: $fileUrl")
                            val skillBody = Skill(user.username, skillDefinition, skillLevel, fileUrl)
                            val jsonSkill = gson.toJson(skillBody)
                            println("Skill post request body: $jsonSkill")
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
                                            getString(R.string.network_error),
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
                                        println(response.code())
                                        println(response.body())
                                        println(response.errorBody())
                                        saveEnded()
                                    }
                                }
                            )
                        }

                        override fun onFileUploadFailure(errorMessage: String) {
                            println("File upload error: $errorMessage")
                            Toast.makeText(requireContext(),
                                getString(R.string.file_could_not_be_uploaded, errorMessage), Toast.LENGTH_LONG).show()

                            val skillBody = Skill(user.username, skillDefinition, skillLevel, null)
                            val jsonSkill = gson.toJson(skillBody)
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
                                            getString(R.string.network_error),
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
                    }).execute()

                    else -> {
                        val skillBody = Skill(user.username, skillDefinition, skillLevel, null)
                        val jsonSkill = gson.toJson(skillBody)
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
                                        getString(R.string.network_error),
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
                }
            }

            // languages
            for (i in 0 until languageCount) {
                val language = binding.profileTopLayout.getChildAt(20 + socialMediaCount + skillCount + i)
                val languageName =
                    language.findViewById<AppCompatEditText>(R.id.profile_item_text1).text.toString()
                val languageLevel = map[languageName]!!
                val languageBody = Language(user.username, languageName, languageLevel)
                val jsonLanguage = gson.toJson(languageBody)
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
                                getString(R.string.network_error),
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
                val professionBody = Profession(user.username, professionName, professionLevel)
                val jsonProfession = gson.toJson(professionBody)
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
                                getString(R.string.network_error),
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

        Toast.makeText(requireContext(),
            getString(R.string.profile_successfully_updated), Toast.LENGTH_SHORT).show()
    }

    private fun setOnClicks(user: AuthenticatedUser) {
        binding.apply {
            profileImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                changeImage.launch(intent)
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
                val datePickerDialog = DatePickerDialog(requireContext(), {
                        _, selectedYear, selectedMonth, dayOfMonth ->
                    date = "$selectedYear-${selectedMonth+1}-$dayOfMonth"
                    profileBirthButton.text = date
                }, year, month, day)
                datePickerDialog.show()
            }

            deleteAccount.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setTitle(getString(R.string.warning))
                alertDialogBuilder.setMessage(getString(R.string.delete_warning_message))
                alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { _, _ ->
                    networkManager.makeRequest(
                        endpoint = Endpoint.USERS,
                        requestType = RequestType.DELETE,
                        headers = headers,
                        callback = object : retrofit2.Callback<ResponseBody> {
                            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.network_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onResponse(
                                call: retrofit2.Call<ResponseBody>,
                                response: retrofit2.Response<ResponseBody>
                            ) {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.account_successfully_deleted),
                                    Toast.LENGTH_SHORT
                                ).show()
                                DiskStorageManager.removeKey("token")
                                replaceFragment(LoginFragment())

                            }
                        }
                    )
                }
                alertDialogBuilder.setIcon(R.drawable.ic_warning)
                alertDialogBuilder.setNegativeButton(getString(R.string.no)) { _, _ -> }
                alertDialogBuilder.show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            println("$requestCode File selected")
            val selectedFile: Uri? = data?.data
            if (selectedFile == null) {
                println("Selected file is null")
                Toast.makeText(requireContext(),
                    getString(R.string.file_could_not_be_selected), Toast.LENGTH_SHORT).show()
                return
            }
            println("Selected file successful")
            skillMap[requestCode] = selectedFile
        } else {
            println("$requestCode File could not be selected")
        }
    }

    private fun saveEnded() {
        saveEndCount++
        if (saveEndCount == 3 + socialMediaCount + skillCount + languageCount + professionCount) {
            println("WHUTT")
//            Toast.makeText(requireContext(), "Profile successfully updated", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack("EditProfileFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment) //replacing fragment
            commit() //call signals to the FragmentManager that all operations have been added to the transaction
        }
    }

}