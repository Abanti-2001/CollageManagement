package com.example.collagemanagement

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.collagemanagement.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.setting.*
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class Setting : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : StorageReference
    private lateinit var Userid : String
    private  var username : String = "null"
    private  var email : String ="null"
    private  var collageid : String = "null"
    private  var organisation : String = "null"
    private  var profilepic : String ="null"
    private lateinit var ImageURI : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        supportActionBar?.setTitle("Setting")
        val submit = findViewById<Button>(R.id.submit)

        database = Firebase.database.reference
        auth= FirebaseAuth.getInstance()
        Userid=auth.currentUser?.uid.toString()
        storage = Firebase.storage.reference
        //define empty fields
        val datausername=findViewById<EditText>(R.id.editusername)
        val datacollageid=findViewById<EditText>(R.id.editcollageid)
        val dataemail =findViewById<EditText>(R.id.editemail)
        val dataorganisation =findViewById<EditText>(R.id.editorganisation)
        val dataprofilepic = findViewById<ImageView>(R.id.editpic)

        storage.child("ProfilePics/$Userid").downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext)
                .load(it)
                .fitCenter()
                .placeholder(R.drawable.profile)
                .into(dataprofilepic)
            profilepic=it.toString()
        }.addOnFailureListener {  }

        val data =FirebaseDatabase.getInstance().getReference("users")
        data.child(Userid).get().addOnSuccessListener {
            if (it != null) {

                username = it.child("username").value.toString()
                collageid = it.child("collageID").value.toString()
                email = it.child("email").value.toString()
                organisation = it.child("organisation").value.toString()

                //Toast.makeText(applicationContext,username,Toast.LENGTH_SHORT).show()

                if (username == "null") datausername?.setText("Username")
                else datausername?.setText(username)

                if (collageid == "null") datacollageid?.setText("CollageID")
                else datacollageid?.setText(collageid ?: "CollageID")

                if (email == "null") dataemail?.setText("Email")
                else dataemail?.setText(email)

                if (organisation == "null") dataorganisation?.setText("Organisation")
                else dataorganisation?.setText(organisation)
            }
        }


        editpic.setOnClickListener {
            launchGallery()
        }

        submit.setOnClickListener(){
             username=findViewById<EditText>(R.id.editusername).text.toString()
             collageid=findViewById<EditText>(R.id.editcollageid).text.toString()
             email = findViewById<EditText>(R.id.editemail).text.toString()
             organisation = findViewById<EditText>(R.id.editorganisation).text.toString()
           // Toast.makeText(applicationContext," $profilepic ",Toast.LENGTH_SHORT).show()
            if(username.isNotEmpty() && collageid.isNotEmpty() && email.isNotEmpty() && organisation.isNotEmpty()) {
                writeNewUser(username, collageid, email, organisation,profilepic)

                val intent = Intent(this,StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra("data","3")
                startActivity(intent)
            }
            else
                Toast.makeText(applicationContext,"Cannot add empty values",Toast.LENGTH_SHORT).show()
        }
    }
    private fun writeNewUser(Username: String, CollageID: String, Email: String,Organisation : String , Profilepic : String){

        val user = User(Username,CollageID,Email,Organisation,Profilepic)
        //Toast.makeText(applicationContext,Userid,Toast.LENGTH_SHORT).show()
        database.child("users").child(Userid).setValue(user)

        database.child("users").child(Userid).get().addOnSuccessListener {
          // Toast.makeText(applicationContext," Done ",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(applicationContext," Failed ",Toast.LENGTH_SHORT).show()
        }
    }
    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            ImageURI = data?.data!!
            editpic.setImageURI(ImageURI)
            //Toast.makeText(applicationContext," image ",Toast.LENGTH_SHORT).show()
            UploadImage()
        }
    }
    private fun UploadImage() {
        //progress dialog
        val progressbar = ProgressDialog(this)
        progressbar.setMessage("Is Uploading...")
        progressbar.setCancelable(false)
        progressbar.show()

        storage.child("ProfilePics/$Userid").putFile(ImageURI).addOnSuccessListener {
            if (progressbar.isShowing) progressbar.dismiss()
        }.addOnFailureListener {
            if (progressbar.isShowing) progressbar.dismiss()
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            editpic.setImageResource(R.drawable.profile)
        }
    }
}