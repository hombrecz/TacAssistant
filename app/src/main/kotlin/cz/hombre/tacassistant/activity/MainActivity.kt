package cz.hombre.tacassistant.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import cz.hombre.tacassistant.R
import cz.hombre.tacassistant.R.string.navigation_drawer_close
import cz.hombre.tacassistant.R.string.navigation_drawer_open
import cz.hombre.tacassistant.activity.reports.*
import cz.hombre.tacassistant.layout.MainUI
import cz.hombre.tacassistant.services.DateTimeService
import cz.hombre.tacassistant.services.LocationService
import cz.hombre.tacassistant.services.PreferencesService
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.setContentView
import org.koin.android.ext.android.inject

const val PERMISSION_REQUEST_LOCATION = 0

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private var mainUI = MainUI()

    private val dateTimeService: DateTimeService by inject()
    private val locationService: LocationService by inject()
    private val preferencesService: PreferencesService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainUI.setContentView(this)
        setSupportActionBar(mainUI.toolbar)

        val toggle = object : ActionBarDrawerToggle(this, mainUI.drawerLayout, mainUI.toolbar, navigation_drawer_open, navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                setHeaderValues()
            }
        }

        mainUI.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        mainUI.navigationView.setNavigationItemSelectedListener(this)

        PreferenceManager.setDefaultValues(this, R.xml.pref_settings, false)

        requestLocationDataPermission()
    }

    private fun requestLocationDataPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                longSnackbar(this.currentFocus, R.string.report_preview_ramrod_message)
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSION_REQUEST_LOCATION)
            }
        }

    }

    private fun setHeaderValues() {
        status_callname.text = preferencesService.getCallSign()
        status_frequency.text = preferencesService.getFrequency()

        status_gps.text = locationService.getCurrentGPSLocation()
        status_mgrs.text = locationService.getCurrentMGRSLocation()
        status_precision.text = locationService.getLocationPrecision()
        status_time_position_retrieved.text = locationService.getLocationTime()
        status_duration_position_retrieved.text = locationService.getLocationTimeAgo()

        status_local_date.text = dateTimeService.getLocalDate()
        status_local_time.text = dateTimeService.getLocalTime()
        status_dtg_zulu_time.text = dateTimeService.getZuluDateTimeGroup()
    }

    override fun onBackPressed() {
        if (mainUI.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainUI.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_medevac -> {
                startActivity(Intent(this, MedevacReport::class.java))
                return true
            }
            R.id.nav_sitrep -> {
                startActivity(Intent(this, SituationReport::class.java))
                return true
            }
            R.id.nav_salute -> {
                startActivity(Intent(this, SaluteReport::class.java))
                return true
            }
            R.id.nav_saltr -> {
                startActivity(Intent(this, SaltrReport::class.java))
                return true
            }
            R.id.nav_ied -> {
                startActivity(Intent(this, ExplosiveReport::class.java))
                return true
            }
            R.id.nav_glossary -> {

            }
        }

        mainUI.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    longSnackbar(this.currentFocus, R.string.navigation_permission_granted)
                } else {
                    longSnackbar(this.currentFocus, R.string.navigation_permission_denied)
                }
                return
            }
        }

    }
}
