package com.example.collagemanagement

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fxn.ariana.ArianaBackgroundListener
import kotlinx.android.synthetic.main.activity_start.*

import androidx.core.app.ActivityCompat

import androidx.core.view.GestureDetectorCompat
import androidx.viewpager.widget.ViewPager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_start.img1
import kotlinx.android.synthetic.main.activity_start.menu_bottom
import kotlinx.android.synthetic.main.activity_start.viewpager
import kotlinx.android.synthetic.main.studentstartactivity.*



class teacher_activity :AppCompatActivity()  {
    private var STORAGE_PERMISSION_CODE : Int = 1
    private lateinit var gesturelistner : DiaryGestureListerner
    private lateinit var detector: GestureDetectorCompat

    private lateinit var auth: FirebaseAuth
    private lateinit var Userid: String
    private var database: DatabaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        Userid = auth.currentUser?.uid.toString()
        val email = auth.currentUser?.email.toString()
        database.child("users").child("$Userid").child("email").setValue(email)
        setContentView(R.layout.activity_start)
        supportActionBar?.setTitle("Teacher")

        //viewpager is the page adapter that contains all the fragments as well
        //teacher:
        //default
        if (savedInstanceState == null) {
            menu_bottom.setItemSelected(R.id.home, true)
            val homeFragment = homefragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.viewpager, homeFragment)
                .commit()
        }

        viewpager.adapter = PageAdapter(supportFragmentManager, "Teacher").apply {
            list = ArrayList<String>().apply {
                add("Home")
                add("Profile")
            }
        }


        menu_bottom.setOnItemSelectedListener { id ->
            when(id){
                R.id.home -> {
                    viewpager.currentItem = 0
                }
                R.id.profile -> {
                    viewpager.currentItem = 1
                }
            }
        }



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
                      //  Toast.makeText(applicationContext, "0", Toast.LENGTH_SHORT).show()
                        menu_bottom.setItemSelected(R.id.home, true)
                    }
                    1->{
                        //profile
                      //  Toast.makeText(applicationContext,"1",Toast.LENGTH_SHORT).show()
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
                            this@teacher_activity, arrayOf(
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