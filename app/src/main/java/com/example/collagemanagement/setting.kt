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
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import kotlin.collections.HashMap


class Setting : AppCompatActivity() {
    private var auth : FirebaseAuth =FirebaseAuth.getInstance()
    private  val storage : StorageReference =Firebase.storage.reference
    private  val Userid : String =auth.currentUser?.uid.toString()
    private lateinit var ImageURI : Uri
    private val mDoc : DocumentReference = FirebaseFirestore.getInstance().collection("Users").document("$Userid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        supportActionBar?.title = "Setting"
        //ImageView
        storage.child("ProfilePics/$Userid").downloadUrl.addOnSuccessListener {
            Glide.with(applicationContext)
                .load(it)
                .fitCenter()
                .placeholder(R.drawable.profile)
                .into(editpic)
        }.addOnFailureListener {
            //Fails to retrieve pic
            editpic.setImageResource(R.drawable.profile)
        }
        UserDisplay()
        editpic.setOnClickListener {
            launchGallery()
        }
        submit.setOnClickListener(){
            if( !editusername?.text.toString().isNullOrEmpty() &&
                !editcollageid?.text.toString().isNullOrEmpty() &&
                !editemail?.text.toString().isNullOrEmpty() &&
                !editorganisation?.text.toString().isNullOrEmpty()) {
                    //check username conditions
                    if(!editusername?.text.toString().contains(" ")) {
                        writeNewUser(
                            editusername?.text.toString(), editcollageid?.text.toString(),
                            editemail?.text.toString(), editorganisation?.text.toString()
                        )
                        finish()
                    }
                else
                    Toast.makeText(applicationContext,"Username cannot contain space",Toast.LENGTH_SHORT).show()

            }
            else
                Toast.makeText(applicationContext,"Cannot add empty values",Toast.LENGTH_SHORT).show()
        }
    }

    private fun UserDisplay() {
        //Create the profile display
        mDoc.get().addOnSuccessListener { snapshot ->
            if(snapshot!=null) {
                val Data = snapshot.toObject(User::class.java)
                Log.v("Details: ",snapshot.data.toString())
                editusername?.setText(Data?.username)
                editcollageid?.setText(Data?.collageID)
                editemail?.setText(Data?.email)
                editorganisation?.setText(Data?.organisation)
            }
        }
    }

    private fun writeNewUser(Username: String, CollageID: String, Email: String,Organisation : String){
         mDoc.get().addOnSuccessListener {
             val role =  it?.getString("role")
             val User_Details = User(Username,CollageID,Email,Organisation,role)
             auth.currentUser?.updateEmail(Email)
             mDoc.set(User_Details).addOnSuccessListener {
                 Toast.makeText(applicationContext,"Success",Toast.LENGTH_SHORT).show()
             }.addOnFailureListener {
                 Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
                 Log.v("Failed: ",it.toString())
             }
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