package com.example.collagemanagement

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.collagemanagement.models.QuizQuestion
import com.example.collagemanagement.models.scorelist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
class activityfragment : Fragment() {
    private var param1: String? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var arraylist : ArrayList<scorelist>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        database = Firebase.database.reference
        auth= FirebaseAuth.getInstance()
        arraylist=addlist()
       /* database.child("dummyData").setValue(arraylist).addOnSuccessListener {
            Toast.makeText(activity,"Success",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(activity,"Failure",Toast.LENGTH_SHORT).show()
        }*/
    }

    private fun addlist() : ArrayList<scorelist>{
        val list_item = ArrayList<scorelist>()
       for(i in 0 until 10) {
           val str = scorelist(1, "1", "1")
           list_item+=str
       }
        return  list_item
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview =inflater.inflate(R.layout.fragment_activity, container, false)
        val btn=rootview.findViewById<Button>(R.id.dummy)
        btn.setOnClickListener {

        }

        return rootview
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment activity.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            activityfragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}