package com.example.collagemanagement
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_splash_screen.*

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var Userid : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Glide.with(applicationContext)
            .load(R.drawable.loading)
            .fitCenter()
            .placeholder(R.drawable.placeholder_splashscreen)
            .into(LoadingScreen)
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
       // val delay = checkUser()
            Handler().postDelayed({
                if(!checkUser()) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }, 1000)

    }

    private fun checkUser() : Boolean{
        auth = FirebaseAuth.getInstance()
        Userid = auth.currentUser?.uid.toString()
        val userref = FirebaseDatabase.getInstance().getReference("teacher")
        if (auth.currentUser != null && isOnline(this)) {
            userref.child("$Userid").get().addOnSuccessListener {
                var des: String = ""
                if (it != null) {
                    des = it.value.toString()
                }
                if (des == "yes") {
                    //teacher
                 //   Toast.makeText(this,"Teacher $des", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, teacher_activity::class.java)
                   // intent.putExtra("des","Teacher")
                    startActivity(intent)
                    finish()
                } else {
                 //   Toast.makeText(this,"Student $des",Toast.LENGTH_SHORT).show()
                    var intent = Intent(this, student_activity::class.java)
                   // intent.putExtra("des","Student")
                    startActivity(intent)
                    finish()
                }
            }
            return true
        }else
            return false
        return false
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

