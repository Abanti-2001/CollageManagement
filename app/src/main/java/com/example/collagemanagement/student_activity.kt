package com.example.collagemanagement

import android.os.Bundle
import android.os.PersistableBundle
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.fxn.ariana.ArianaBackgroundListener
import kotlinx.android.synthetic.main.activity_start.*
import java.util.ArrayList
import java.util.*

class student_activity : AppCompatActivity() {
    private var STORAGE_PERMISSION_CODE : Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.studentstartactivity)
        supportActionBar?.setTitle("Student")

        //student
        //default
        if (savedInstanceState == null) {
            menu_bottom.setItemSelected(R.id.home, true)
            val homeFragment = homefragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.viewpager, homeFragment)
                .commit()
        }

        menu_bottom.setOnItemSelectedListener { id ->
            when(id){
                R.id.home -> viewpager.currentItem = 0
                R.id.activity -> viewpager.currentItem = 1
                R.id.favorites -> viewpager.currentItem = 2
                R.id.profile -> viewpager.currentItem = 3
            }
        }
        viewpager.adapter = PageAdapter(supportFragmentManager,"Student").apply {
            list = ArrayList<String>().apply {
                add("Home")
                add("Activity")
                add("Favourite")
                add("Profile")
            }
        }
        viewpager.addOnPageChangeListener(
            ArianaBackgroundListener(
                getColors(),
                img1,
                viewpager
            )
        )
        viewpager.addOnPageChangeListener(ArianaBackgroundListener(
            getColors(), img1, viewpager))
        viewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                when(position){
                    0-> {
                        //home
                    //    Toast.makeText(applicationContext, "0", Toast.LENGTH_SHORT).show()
                        menu_bottom.setItemSelected(R.id.home, true)
                    }
                    1->{
                        //activity
                     //   Toast.makeText(applicationContext,"1", Toast.LENGTH_SHORT).show()
                        menu_bottom.setItemSelected(R.id.activity, true)
                    }
                    2->{
                        //favorites
                     //   Toast.makeText(applicationContext,"1", Toast.LENGTH_SHORT).show()
                        menu_bottom.setItemSelected(R.id.favorites, true)
                    }
                    3->{
                        //profile
                     //   Toast.makeText(applicationContext,"1", Toast.LENGTH_SHORT).show()
                        menu_bottom.setItemSelected(R.id.profile, true)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}

        })

         val data=intent.getStringExtra("data")
        if(data!=null)
            intentact(data)

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED )
        {
            requeststoragepermission()
        }
    }

    private fun getColors() : IntArray{
        return intArrayOf(
            ContextCompat.getColor(this, R.color.homebg),
            ContextCompat.getColor(this, R.color.activitybg),
            ContextCompat.getColor(this, R.color.favouritebg),
            ContextCompat.getColor(this, R.color.homebg)
        )
    }
    private fun intentact(data:String){
        //Toast.makeText(applicationContext,data,Toast.LENGTH_SHORT).show()
        viewpager.setCurrentItem(data.toInt())
    }

    private fun requeststoragepermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.INTERNET
                ), STORAGE_PERMISSION_CODE
            )

        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true);
    }
}