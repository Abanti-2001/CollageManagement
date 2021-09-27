package com.example.collagemanagement


import android.content.Intent
import android.content.Intent.getIntent
import android.media.Image
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.collagemanagement.models.User
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlin.math.sign

private const val ARG_PARAM = "param1"
class profilefragment(private val des : String) : Fragment() {
    private var param1: String? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var storage : StorageReference
    private lateinit var imgref : StorageReference
    private lateinit var Userid : String
    private lateinit var username : String
    private lateinit var email : String
    private lateinit var pro : String
    private lateinit var collageid : String
    private lateinit var organisation : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM)
        }
        auth= FirebaseAuth.getInstance()
        Userid=auth.currentUser?.uid.toString()
        database =FirebaseDatabase.getInstance().reference.child("users")
        storage=Firebase.storage.reference


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview =inflater.inflate(R.layout.fragment_profile, container, false)
        val editsetting = rootview.findViewById<ImageButton>(R.id.settings)
        val usernameA=rootview.findViewById<TextView>(R.id.username)
        val collageIdA=rootview.findViewById<TextView>(R.id.collageId)
        val emailA=rootview.findViewById<TextView>(R.id.emailid)
        val organisationA=rootview.findViewById<TextView>(R.id.organisation)
        val profilepicA = rootview.findViewById<ImageView>(R.id.profile_pic)
        editsetting.setOnClickListener {
            val intent = Intent(activity, Setting::class.java)
            intent.putExtra("des",des)
            startActivity(intent)
        }
        displayprofile(usernameA,collageIdA,emailA,organisationA,profilepicA)

        val signout = rootview.findViewById<Button>(R.id.SignOut)
        signout.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity,MainActivity::class.java)
            startActivity(intent)
        }

        return rootview
    }

    private fun displayprofile(usernameA: TextView?, collageIdA: TextView?, emailA: TextView?,
                               organisationA: TextView?, profilepicA: ImageView) {
        database.child(Userid).get().addOnSuccessListener {
            if(it!=null) {
                username = it.child("username").value.toString()
                collageid = it.child("collageID").value.toString()
                email = it.child("email").value.toString()
                organisation = it.child("organisation").value.toString()
                pro=it.child("profilepic").value.toString()
            }
            usernameA?.setText(username)

            collageIdA?.setText(collageid)

            emailA?.setText(email)

            organisationA?.setText(organisation)


                }
        storage.child("ProfilePics/$Userid").downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .fitCenter()
                .placeholder(R.drawable.profile)
                .into(profilepicA)

        }.addOnFailureListener {  }
    }


}
