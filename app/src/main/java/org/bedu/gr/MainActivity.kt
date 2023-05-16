package org.bedu.gr

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import org.bedu.gr.Fragments.HistorialFragment
import org.bedu.gr.Fragments.ServiciosFragment
import org.bedu.gr.UI.ID_USER
import org.bedu.gr.UI.LoginActivity
import org.bedu.gr.UI.USER_NAME
import org.bedu.gr.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //Variables globales
    private lateinit var binding: ActivityMainBinding


    private lateinit var drawerLayout : DrawerLayout
    private lateinit var drawerToggle : ActionBarDrawerToggle

    //Fragments
    val serviciosFragment = ServiciosFragment()
    val historialFragment = HistorialFragment()

//ButtonNavigationView
private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        val idUser = bundle?.getString(ID_USER)
        val userName = bundle?.getString(USER_NAME)
        //text = findViewById(R.id.lblUserName)
        //text.text = "Bienvenido ${userName}"

       binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCurrentFragment(serviciosFragment)
        createFragments()

        //Toolbar
        val toolbarMain = findViewById<Toolbar>(R.id.toolbar_main)
        this.setSupportActionBar(toolbarMain)

        setupDrawer(toolbarMain)




    }

    private fun createFragments() {
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.btnNavigationView)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home->{
                    setCurrentFragment(serviciosFragment)
                    it.actionView?.clearFocus()
                    true
                }
                R.id.nav_historial->{

                    //                    val args = Bundle()
//                    val shared: SharedPreferences = getSharedPreferences("shared", MODE_PRIVATE)
//                    args.putInt("idConductor", shared.getInt("idConductor",0))
//                    args.putString("token", shared.getString("token","0"))
//                    tercerFragment.arguments=args
//                    setCurrentFragment(tercerFragment)
//


                    setCurrentFragment(historialFragment)
                    it.actionView?.clearFocus()
                    true
                }

                R.id.nav_notificaciones->{
                    it.actionView?.clearFocus()
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.getOrCreateBadge(R.id.nav_notificaciones).apply {
           isVisible=true
            number=3
        }
    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.containerView,fragment)
            commit()
        }
    }

    private fun setupDrawer(toolbar: Toolbar){
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close)

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true);
        navigationView.setNavigationItemSelectedListener (this)



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.drawer_settings->{
                setCurrentFragment(serviciosFragment)
                item.actionView?.clearFocus()
                true
            }
            R.id.drawer_salir->{
                item.actionView?.clearFocus()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                finish()
                true
            }
            else -> false
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)){
            return true
        }

        when(item.itemId){
            R.id.option_search->{
                item.actionView?.clearFocus()
                true
            }
            R.id.option_cabina->{
                val phone: String = getString(R.string.phone_cabina)
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$phone")
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent)
                    item.actionView?.clearFocus()
                    true
                }
                else {
                    requestPermissions(arrayOf(CALL_PHONE), 1);
                }

            }
            R.id.option_googlemaps->{
                // Crear Uri
                val IntentUri = Uri.parse("geo:0,0?q=gasolineras")
                // Crear Intent
                val intent = Intent(Intent.ACTION_VIEW, IntentUri)
                // Establecer paquete de Google Maps
                intent.setPackage("com.google.android.apps.maps")
                    startActivity(intent)
                    item.actionView?.clearFocus()

                true
            }
            else -> false
        }


        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }


}