package com.example.collagemanagement


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

import com.google.firebase.database.*


class MainActivity : AppCompatActivity() , DiaryGestureListerner.Gesture  {


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

            gesturelistner = DiaryGestureListerner(this, this)
            detector = GestureDetectorCompat(this, gesturelistner)
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
                                checkuser()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Incorrect details", Toast.LENGTH_SHORT).show()
                            }
                    else
                        Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(
                        this,
                        "Email/Password fields cannot be empty",
                        Toast.LENGTH_SHORT
                    )
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
                                    checkuser()
                                } else
                                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT)
                                        .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Couldn't create user", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    } else
                        Toast.makeText(this, "Password is too short", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(
                        this,
                        "Email/Password fields cannot be empty",
                        Toast.LENGTH_SHORT
                    )
                        .show()
            }
        }

    private fun checkuser() {
        Userid = auth.currentUser?.uid.toString()
        val userref = FirebaseDatabase.getInstance().getReference("teacher")
        userref.child("$Userid").get().addOnSuccessListener {
            var des: String = "default"
            if (it != null) {
                des = it.value.toString()
            }
            if (des == "yes") {
                //teacher
               // Toast.makeText(this,"Teacher $Userid",Toast.LENGTH_SHORT).show()
                var intent = Intent(this, teacher_activity::class.java)
                startActivity(intent)
                finish()
            } else {
              //  Toast.makeText(this,"Student $Userid",Toast.LENGTH_SHORT).show()
                var intent = Intent(this, student_activity::class.java)
                startActivity(intent)
                finish()
            }
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
        if(!morning.isVisible)
        {  morning.startAnimation(animationr)
            night.visibility = View.INVISIBLE
            morning.visibility = View.VISIBLE
        }
        //Toast.makeText(this,"Swiped left",Toast.LENGTH_SHORT).show()
    }

     override fun swipeRight() {
        //becomes night
        if(!night.isVisible)
        {
            night.startAnimation(animationl)
            morning.visibility = View.INVISIBLE
            night.visibility = View.VISIBLE
        }
        // Toast.makeText(this,"Swiped right",Toast.LENGTH_SHORT).show()
    }
    /*
    override fun swipeDown() {
        //becomes day
        if(!morning.isVisible){
            morning.startAnimation(animationdown)
            night.visibility = View.INVISIBLE
            morning.visibility = View.VISIBLE
        }
    }
    override fun swipeUp() {
        //become night
        if(!night.isVisible){
            night.startAnimation(animationup)
            morning.visibility = View.INVISIBLE
            night.visibility = View.VISIBLE
        }
    }*/


}


