package com.example.disasterresponseplatform

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.disasterresponseplatform.databinding.ActivityMainBinding
import com.example.disasterresponseplatform.managers.DiskStorageManager
import com.example.disasterresponseplatform.ui.activity.HomeFragment
import com.example.disasterresponseplatform.ui.activity.need.NeedViewModel
import com.example.disasterresponseplatform.ui.activity.userdata.UserDataViewModel
import com.example.disasterresponseplatform.ui.activity.action.ActionViewModel
import com.example.disasterresponseplatform.ui.activity.emergency.EmergencyViewModel
import com.example.disasterresponseplatform.ui.activity.event.EventViewModel
import com.example.disasterresponseplatform.ui.activity.resource.ResourceViewModel
import com.example.disasterresponseplatform.ui.authentication.LoginFragment
import com.example.disasterresponseplatform.ui.map.MapFragment
import com.example.disasterresponseplatform.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var needViewModel: NeedViewModel
    private lateinit var userDataViewModel: UserDataViewModel
    private lateinit var actionViewModel: ActionViewModel
    private lateinit var eventViewModel: EventViewModel
    private lateinit var emergencyViewModel: EmergencyViewModel
    private lateinit var resourceViewModel: ResourceViewModel

    private lateinit var mapFragment: MapFragment
    private val profileFragment = ProfileFragment()
    private val loginFragment = LoginFragment()
    private lateinit var homeFragment: HomeFragment

    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //instead setContentView(R.layout.activity_main) doing that with binding
        setContentView(binding.root)

        createViewModels()
        mapFragment = MapFragment(needViewModel, resourceViewModel, actionViewModel, eventViewModel, emergencyViewModel)
        navBarListener()
//        toggleListener()
        // This clears the shadow of action bar and set background color
        supportActionBar?.elevation = 0F
        supportActionBar?.setBackgroundDrawable(getDrawable(R.color.primary))
        arrangeVisibility()
        initializeFragments()

    }

    private fun createViewModels(){
        // it is created by dependency Injection without creating any of db, dao or repo
        val getNeedViewModel: NeedViewModel by viewModels()
        needViewModel = getNeedViewModel

        val getUserDataViewModel: UserDataViewModel by viewModels()
        userDataViewModel = getUserDataViewModel

        val getActionViewModel: ActionViewModel by viewModels()
        actionViewModel = getActionViewModel

        val getEventViewModel: EventViewModel by viewModels()
        eventViewModel = getEventViewModel

        val getEmergencyViewModel: EmergencyViewModel by viewModels()
        emergencyViewModel = getEmergencyViewModel

        val getResourceViewModel: ResourceViewModel by viewModels()
        resourceViewModel = getResourceViewModel
    }

    private fun initializeFragments(){
        homeFragment = HomeFragment(needViewModel,resourceViewModel,actionViewModel,this)
        replaceFragment(homeFragment)
    }

    private fun arrangeVisibility(){
        // Set logged in state
        if (DiskStorageManager.hasKey("token")) {
            binding.navView.menu.findItem(R.id.miLogin).isVisible = false
            binding.navView.menu.findItem(R.id.miLoggedInAs).isVisible = true
            binding.navView.menu.findItem(R.id.miLogout).isVisible = true
            // This value will be fetched from API when it is ready
            binding.navView.menu.findItem(R.id.miLoggedInAs).title = DiskStorageManager.getKeyValue("username")
        } else {
            binding.navView.menu.findItem(R.id.miLogin).isVisible = true
            binding.navView.menu.findItem(R.id.miLoggedInAs).isVisible = false
            binding.navView.menu.findItem(R.id.miLogout).isVisible = false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun toggleListener(){
        toggle = ActionBarDrawerToggle(this, binding.root,R.string.open,R.string.close) //like adapter
        binding.root.addDrawerListener(toggle) // add toggle into layout
        toggle.syncState() // to be ready to use
        // able to close navigation when clicked to back arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.miLoggedInAs -> replaceNavFragment(profileFragment)
                R.id.miLogin -> replaceNavFragment(loginFragment)
                R.id.miLogout -> logOutActions()
            }
            binding.root.closeDrawer(GravityCompat.START) //whenever clicked item on drawer, closing it automatically
            true
        }
    }

    private fun logOutActions() {
        DiskStorageManager.removeKey("token")
        finish()
        startActivity(intent)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true // notifies that user clicked that toggle button
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * This function is for replacing corresponding fragments when user clicks corresponding navbar button.
     */
    private fun navBarListener(){
        binding.bottomNavigationView.setOnItemSelectedListener {menuItem ->
            when (menuItem.itemId){
                R.id.miHome -> replaceNavFragment(homeFragment)
                R.id.miMap -> replaceNavFragment(mapFragment)
                R.id.miProfile -> replaceNavFragment(profileFragment)
            }
            true
        }
    }

    /**
     * It closes fragments in backstack then replace the corresponding fragment.
     * i.e user selects home page when s/he is on add action fragment
     * this function firstly kills add action fragment, actionFragment, activityFragment correspondingly
     * then opens the home page fragment.
     */
    private fun replaceNavFragment(currentFragment: Fragment){
        // to prevent bugs when backstack is not empty
        val backStackEntryCount: Int = supportFragmentManager.backStackEntryCount
        for (i in 0 until backStackEntryCount) {
            Log.i("PopBackStack: ",i.toString())
            popFragment()
        }
        replaceFragment(currentFragment)
    }


    /** This adds fragments to the back stack, in this way user can return this fragment after the view has been changed.
     * and replace fragment.
     */
    fun addFragment(fragment: Fragment) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    /**
     * This pops the fragment from fragment stack
     */
    private fun popFragment() {
        supportFragmentManager.popBackStack()
    }

    /**
     * This is for replacing the fragment without adding it to the stack
     * .apply means ft. in addFragment
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment) //replacing fragment
            commit() //call signals to the FragmentManager that all operations have been added to the transaction
        }
    }
}