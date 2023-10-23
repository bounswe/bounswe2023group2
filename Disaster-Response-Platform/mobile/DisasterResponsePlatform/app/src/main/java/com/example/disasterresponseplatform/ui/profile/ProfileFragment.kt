package com.example.disasterresponseplatform.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.R
import com.example.disasterresponseplatform.databinding.FragmentProfileBinding
import com.example.disasterresponseplatform.models.AuthenticatedUser
import com.example.disasterresponseplatform.models.CredibleUser
import com.example.disasterresponseplatform.models.RoleBasedUser
import com.example.disasterresponseplatform.ui.profile.notification.SubscribeNotificationFragment
import com.example.disasterresponseplatform.ui.profile.pastUserActions.PastUserActionsFragment
import com.squareup.picasso.Picasso
import java.time.LocalDate
import kotlin.random.Random


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val pastUserActionsFragment = PastUserActionsFragment()
    private val subscribeNotificationFragment = SubscribeNotificationFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickButtons()
        lateinit var tempUser: AuthenticatedUser
        when (Random.nextInt(3)) {
            0 -> {
                tempUser = RoleBasedUser("suzanuskudarli", "suzan.uskudarli@boun.edu.tr",
                    "+90 534 062 38 47", "Suzan", "Üsküdarlı", "Professor")
                tempUser.profileInfoShared = true
                tempUser.isEmailVerified = true
                tempUser.verificationLevel = 2
                tempUser.profilePhoto = "https://media.licdn.com/dms/image/C4E03AQGOt48wKCVf0Q/profile-displayphoto-shrink_800_800/0/1516273377753?e=1703721600&v=beta&t=dXz_0jzvzbeZcy2XM_0OyT6AJVSdmZtK5i3OkHXAHsc"
            }
            1 -> {
                tempUser = CredibleUser("alperahmetoglu", "alper.ahmetoglu@boun.edu.tr",
                    "+90 534 062 38 47", "Alper", "Ahmetoğlu", "Marmara Region")
                tempUser.verifiedBy = "suzanuskudarli"
                tempUser.profileInfoShared = true
                tempUser.isPhoneVerified = true
                tempUser.verificationLevel = 1
                tempUser.region = "Marmara Region"
                tempUser.profilePhoto = "https://media.licdn.com/dms/image/C4E03AQH6i9kLm7Ogpg/profile-displayphoto-shrink_800_800/0/1610042947274?e=1703721600&v=beta&t=lfbNSMArUXZH26rZ4kmjHabh1v7TlPnQ5CEWw74HP5Q"
                tempUser.birth = LocalDate.of(1996, 4, 26)
                tempUser.nationality = "Turkey"
                tempUser.idNumber = "26419826416"
                tempUser.address = "İstanbul, Turkey"
                tempUser.education = "PhD"
                tempUser.healthCondition = "Healthy"
                tempUser.bloodType = "A+"
                tempUser.socialMedia.add(AuthenticatedUser.SocialMedia("LinkedIn", "https://www.linkedin.com/in/alperahmetoglu/"))
                tempUser.socialMedia.add(AuthenticatedUser.SocialMedia("X", "https://twitter.com/alperahmetoglu"))
                tempUser.socialMedia.add(AuthenticatedUser.SocialMedia("GitHub", "https://github.com/alper111"))
                tempUser.socialMedia.add(AuthenticatedUser.SocialMedia("YouTube", "https://www.youtube.com/@higgsbozonu"))
                tempUser.skills.add(AuthenticatedUser.Skill("Java", "Expert", "https://notepad.pw/cahidingecicisayfasi"))
                tempUser.skills.add(AuthenticatedUser.Skill("Kotlin", "Expert", "https://notepad.pw/cahidingecicisayfasi"))
                tempUser.skills.add(AuthenticatedUser.Skill("Python", "Expert", "https://notepad.pw/cahidingecicisayfasi"))
                tempUser.skills.add(AuthenticatedUser.Skill("C++", "Expert", "https://notepad.pw/cahidingecicisayfasi"))
                tempUser.languages.add(AuthenticatedUser.Language("Turkish", "Native"))
                tempUser.languages.add(AuthenticatedUser.Language("English", "Fluent"))
                tempUser.languages.add(AuthenticatedUser.Language("German", "Intermediate"))
                tempUser.professions.add(AuthenticatedUser.Profession("Software Developer", "Expert"))
                tempUser.professions.add(AuthenticatedUser.Profession("Software Architect", "Expert"))
                tempUser.professions.add(AuthenticatedUser.Profession("Software Engineer", "Expert"))

            }
            2 -> {
                tempUser = AuthenticatedUser("cahideneskeles", "cahid.keles@boun.edu.tr",
                    "+90 534 062 38 47", "Cahid Enes", "Keleş")
            }
        }

        fillInformations(tempUser)
    }

    private fun fillInformations(user: AuthenticatedUser) {
        binding.apply {
            // read image from url and set it to profilePhoto
            if (user.profilePhoto != null) {
                Picasso.get().load(user.profilePhoto!!.toUri()).into(profileImage)
            }
            profileFullname.text = user.name + " " + user.surname
            profileUsername.text = user.username
            profileEmail.text = user.email
            profilePhoneNumber.text = user.phone
            profileEmailVerifiedIcon.visibility = if (user.isEmailVerified) View.VISIBLE else View.GONE
            profilePhoneVerifiedIcon.visibility = if (user.isPhoneVerified) View.VISIBLE else View.GONE
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
                profileEducationLayout.visibility = View.VISIBLE
                profileEducation.text = user.education
            }

            if (user.healthCondition != null) {
                profileHealthConditionLayout.visibility = View.VISIBLE
                profileHealthCondition.text = user.healthCondition
            }

            if (user.bloodType != null) {
                profileBloodTypeLayout.visibility = View.VISIBLE
                profileBloodType.text = user.bloodType
            }

            var counter = 0;
            for (socialMedia in user.socialMedia) {
                val newView: View = LayoutInflater.from(requireContext()).inflate(R.layout.profile_item, null)
                newView.findViewById<TextView>(R.id.profile_item_text).text = socialMedia.platformName
                newView.findViewById<ImageView>(R.id.profile_item_link).visibility = View.VISIBLE
                newView.findViewById<ImageView>(R.id.profile_item_link).setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(socialMedia.profileURL))
                    startActivity(browserIntent)
                }
                when (socialMedia.platformName.lowercase()) {
                    "linkedin" -> newView.findViewById<ImageView>(R.id.profile_item_icon).setImageResource(R.drawable.linkedin_icon)
                    "x" -> newView.findViewById<ImageView>(R.id.profile_item_icon).setImageResource(R.drawable.x_icon)
                    "github" -> newView.findViewById<ImageView>(R.id.profile_item_icon).setImageResource(R.drawable.github_icon)
                    "youtube" -> newView.findViewById<ImageView>(R.id.profile_item_icon).setImageResource(R.drawable.youtube_icon)
                }
                profileTopLayout.addView(newView, 16 + counter)
                counter++
            }

            for (skill in user.skills) {
                val newView: View = LayoutInflater.from(requireContext()).inflate(R.layout.profile_item, null)
                newView.findViewById<TextView>(R.id.profile_item_text).text = skill.definition + ": " + skill.level
                newView.findViewById<ImageView>(R.id.profile_item_link).visibility = View.VISIBLE
                newView.findViewById<ImageView>(R.id.profile_item_link).setOnClickListener {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(skill.document))
                    startActivity(browserIntent)
                }
                profileTopLayout.addView(newView, 18 + counter)
                counter++
            }

            for (language in user.languages) {
                val newView: View = LayoutInflater.from(requireContext()).inflate(R.layout.profile_item, null)
                newView.findViewById<TextView>(R.id.profile_item_text).text = language.language + ": " + language.level
                profileTopLayout.addView(newView, 20 + counter)
                counter++
            }

            for (profession in user.professions) {
                val newView: View = LayoutInflater.from(requireContext()).inflate(R.layout.profile_item, null)
                newView.findViewById<TextView>(R.id.profile_item_text).text = profession.profession + ": " + profession.level
                profileTopLayout.addView(newView, 22 + counter)
                counter++
            }
        }
    }

    private fun clickButtons(){
        binding.profileCallButton.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED ) {
                // Permission is granted, make the phone call
                var callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:"+binding.profilePhoneNumber.text.toString().replace(" ",""))
                startActivity(callIntent)
            } else {
                // Permission is not granted, request the permission
                ActivityCompat.requestPermissions( requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1 )
            }
        }
    }

    private fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

}