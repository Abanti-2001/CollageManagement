package com.example.collagemanagement


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log


class MainActivity : AppCompatActivity() , DiaryGestureListerner.Gesture  {

    private lateinit var NightView: View
    private lateinit var MorningView: View
    private lateinit var detector: GestureDetectorCompat
    private lateinit var animationl: Animation
    private lateinit var animationr: Animation
    private lateinit var animationup: Animation
    private lateinit var animationdown: Animation
    private lateinit var gesturelistner : DiaryGestureListerner

    private lateinit var auth: FirebaseAuth
    private lateinit var Userid: String

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        Userid = auth.currentUser?.uid.toString()
        if (auth.currentUser != null && isOnline(this)) {

            var intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
        gesturelistner = DiaryGestureListerner(this,this)
        detector = GestureDetectorCompat(this,gesturelistner )
        NightView = findViewById(R.id.night)
        MorningView = findViewById(R.id.morning)
        animationl = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_left)
        animationr = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_right)
        animationup = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
        animationdown = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)

        signinbtn.setOnClickListener() {
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            if (password.isNotEmpty() && email.isNotEmpty()) {
                if (password.length >= 6)
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            var intent = Intent(this, StartActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Incorrect details", Toast.LENGTH_SHORT).show()
                        }
                else
                    Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Email/Password fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
        }
        signupbtn.setOnClickListener() {
            //Toast.makeText(this,"Email"+email+"Pass"+password,Toast.LENGTH_SHORT).show()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            if (password.isNotEmpty() && email.isNotEmpty()) {
                if (password.length >= 6) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                var intent = Intent(this, StartActivity::class.java)
                                startActivity(intent)
                            } else
                                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT)
                                    .show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Couldn't create user", Toast.LENGTH_SHORT).show()
                        }
                } else
                    Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "Email/Password fields cannot be empty", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        } else {
            return super.onTouchEvent(event)
        }
    }



   override fun swipeLeft() {
        if(!MorningView.isVisible)
        {  morning.startAnimation(animationr)
            NightView.visibility = View.INVISIBLE
            MorningView.visibility = View.VISIBLE
        }
        //Toast.makeText(this,"Swiped left",Toast.LENGTH_SHORT).show()
    }

     override fun swipeRight() {
        //becomes night
        if(!NightView.isVisible)
        {
            night.startAnimation(animationl)
            MorningView.visibility = View.INVISIBLE
            NightView.visibility = View.VISIBLE
        }
        // Toast.makeText(this,"Swiped right",Toast.LENGTH_SHORT).show()
    }
    override fun swipeDown() {
        //becomes day
        if(!MorningView.isVisible){
            morning.startAnimation(animationdown)
            NightView.visibility = View.INVISIBLE
            MorningView.visibility = View.VISIBLE
        }
    }
    override fun swipeUp() {
        //become night
        if(!NightView.isVisible){
            night.startAnimation(animationup)
            MorningView.visibility = View.INVISIBLE
            NightView.visibility = View.VISIBLE
        }
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

}


