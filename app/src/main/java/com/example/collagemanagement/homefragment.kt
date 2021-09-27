package com.example.collagemanagement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.collagemanagement.models.QuizQuestion


private const val ARG_PARAM= "param1"
class homefragment : Fragment() {
    private var param1 : String?=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM)
        }
    }
    companion object{
        @JvmStatic
        fun newInstance(
            param1 :String
        )=homefragment().apply{arguments= Bundle().apply {
            putString(ARG_PARAM,param1) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = LayoutInflater.from(context).inflate(R.layout.fragment_home,container,false)
        val Quiz = viewRoot.findViewById<Button>(R.id.Quiz)
        Quiz.setOnClickListener{
           // Toast.makeText(activity,"Clicked",Toast.LENGTH_SHORT).show()
            val intent = Intent(activity,QuizList::class.java)
            startActivity(intent)
        }
        return viewRoot
    }

}