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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.setting.*
import kotlin.math.sign

private const val ARG_PARAM = "param1"
class profilefragment() : Fragment() {
    private var param1: String? = null
    private val auth : FirebaseAuth =FirebaseAuth.getInstance()
    private val storage : StorageReference =Firebase.storage.reference
    private val Userid : String =auth.currentUser?.uid.toString()
    private val mDoc : DocumentReference = FirebaseFirestore.getInstance().collection("Users").document("$Userid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview =inflater.inflate(R.layout.fragment_profile, container, false)
        val editsetting = rootview.findViewById<ImageButton>(R.id.settings)

         val username=rootview.findViewById<TextView>(R.id.username)
         val collageId=rootview.findViewById<TextView>(R.id.collageId)
         val email=rootview.findViewById<TextView>(R.id.emailid)
         val organisation=rootview.findViewById<TextView>(R.id.organisation)
         val profilepic = rootview.findViewById<ImageView>(R.id.profile_pic)

        editsetting.setOnClickListener {
            val intent = Intent(activity, Setting::class.java)
            startActivity(intent)
        }
        displayprofile(username,collageId,email,organisation,profilepic)

        val signout = rootview.findViewById<Button>(R.id.SignOut)
        signout.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity,MainActivity::class.java)
            startActivity(intent)
        }

        return rootview
    }

    private fun displayprofile(username: TextView?, collageId: TextView?, email: TextView?,
                               organisation: TextView?, profilepic: ImageView) {
       //Firestore
        mDoc.addSnapshotListener { value, error ->
            mDoc.get().addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    val Data = snapshot.toObject(User::class.java)
                    Log.v("Details: ",snapshot.data.toString())
                    username?.text = Data?.username
                    collageId?.text = Data?.collageID
                    email?.text = Data?.email
                    organisation?.text = Data?.organisation
                }
        }
        }
        storage.child("ProfilePics/$Userid").downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .fitCenter()
                .placeholder(R.drawable.profile)
                .into(profilepic)

        }.addOnFailureListener {
            Glide.with(this)
                .load(R.drawable.profile)
                .fitCenter()
                .into(profilepic)
        }
    }
}
