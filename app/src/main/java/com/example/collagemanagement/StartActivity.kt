package com.example.collagemanagement

import android.accessibilityservice.GestureDescription
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fxn.ariana.ArianaBackgroundListener
import kotlinx.android.synthetic.main.activity_start.*
import java.util.*
import java.util.jar.Manifest
import androidx.core.app.ActivityCompat

import android.content.DialogInterface
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat


class StartActivity :AppCompatActivity()  {
    private var STORAGE_PERMISSION_CODE : Int = 1
    private lateinit var gesturelistner : DiaryGestureListerner
    private lateinit var detector: GestureDetectorCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        supportActionBar?.setTitle("Welcome")

        //viewpager is the page adapter that contains all the fragments as well
        menu_bottom.setOnItemSelectedListener { id ->
            when(id){
                R.id.home -> viewpager.currentItem = 0
                R.id.activity -> viewpager.currentItem = 1
                R.id.favorites -> viewpager.currentItem = 2
                R.id.profile -> viewpager.currentItem = 3
            }
        }

        viewpager.adapter = PageAdapter(supportFragmentManager).apply {
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
                            this@StartActivity, arrayOf(
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