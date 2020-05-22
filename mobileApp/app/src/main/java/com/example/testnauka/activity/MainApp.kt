package com.example.testnauka.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testnauka.fragments.CalendarFragment
import com.example.testnauka.R
import com.example.testnauka.fragments.DepartsFragment
import com.example.testnauka.fragments.EmployeeFragment
import com.example.testnauka.utils.Positions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class MainApp : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private val fragmentManager by lazy { supportFragmentManager }
    private val departsTag = "departsFragment"
    private val employeeTag = "employeeFragment"
    private val calendarTag = "calendarTag"
    private var positions: Positions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)

        positions = Gson().fromJson(this.intent.getStringExtra("position"), Positions::class.java)
        var departsFragment = fragmentManager.findFragmentByTag(departsTag) as? DepartsFragment
        var employeeFragment = fragmentManager.findFragmentByTag(employeeTag) as? EmployeeFragment
        var calendarFragment  = fragmentManager.findFragmentByTag(calendarTag) as? CalendarFragment

        val bundlePosition = Bundle()
        if (departsFragment == null) {
            departsFragment = DepartsFragment()
            bundlePosition.putString("position", Gson().toJson(positions))
            departsFragment.arguments = bundlePosition
            createFragment(departsFragment,departsTag)
        }

        bottomNavigation = findViewById(R.id.bottomNavigationMenu)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_departs->{
                    val bundlePosition = Bundle()
                    if (departsFragment == null) {
                        departsFragment = DepartsFragment()
                        bundlePosition.putString("position", Gson().toJson(positions))
                        departsFragment!!.arguments = bundlePosition
                        createFragment(departsFragment!!,departsTag)
                    }
                    else{
                        replaceFragment(departsFragment!!, departsTag)
                    }
                    it.isChecked = true
                }
                R.id.navigation_employee -> {
                    val bundlePosition = Bundle()
                    if (employeeFragment == null) {
                        employeeFragment = EmployeeFragment()
                        bundlePosition.putString("position", Gson().toJson(positions))
                        employeeFragment!!.arguments = bundlePosition
                        createFragment(employeeFragment!!,employeeTag)
                    }
                    else{
                        replaceFragment(employeeFragment!!, employeeTag)
                    }
                    it.isChecked = true
                }
                R.id.navigation_depart -> {
                    /*
                    val bundleId = Bundle()
                    if (songsFragment == null) {
                        if(albumsFragment!!.getIdAlbums() == 0){
                            createToast("Albums not pick")
                        }
                        else{
                            songsFragment = SongsViewFragment()
                            bundleId.putInt("id", albumsFragment!!.getIdAlbums())
                            songsFragment!!.arguments = bundleId
                            createFragment(songsFragment!!, songsTAG)
                            it.isChecked = true
                        }
                    }
                    else{
                        bundleId.putInt("id", albumsFragment!!.getIdAlbums())
                        songsFragment!!.arguments = bundleId
                        replaceFragment(songsFragment!!,songsTAG)
                        it.isChecked = true
                    }

                     */
                }
                R.id.navigation_calend -> {
                    if (calendarFragment == null) {
                        calendarFragment = CalendarFragment()
                        //bundlePosition.putString("position", Gson().toJson(positions))
                        //employeeFragment!!.arguments = bundlePosition
                        createFragment(calendarFragment!!,calendarTag)
                    }
                    else{
                        replaceFragment(calendarFragment!!, calendarTag)
                    }
                    it.isChecked = true
                }
            }
            false
        }


    }

    private fun createFragment(fragment: Fragment, tag: String){

        val fm = supportFragmentManager
        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutFragments, fragment,  tag)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()
    }

    /*
        Function for replace fragment another
     */
    private fun replaceFragment(fragment: Fragment, tag: String){

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutFragments, fragment,  tag)
        fragmentTransaction.addToBackStack(tag)
        fragmentTransaction.commit()

    }

    /*
        Function for message users that he did't pick album
     */
    private fun createToast (text: String){
        val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 50)
        toast.view.setBackgroundColor(Color.GRAY)
        toast.show()
    }

    /*
    override fun onBackPressed() {
        super.onBackPressed()
        bottomNavigation.selectedItemId = R.id.navigation_album
    }

     */

}
