package com.example.collagemanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collagemanagement.models.scorelist
import kotlinx.android.synthetic.main.fragment_favourite.*

private const val ARG_PARAM1 = "param1"
class favouritefragment : Fragment() , ScoreView.onItemClickedListner  {
    private var param1: String? = null
    private val quizlist = list(10)
    private val adpater =  ScoreView(quizlist,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootview =inflater.inflate(R.layout.fragment_favourite, container, false)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scoreboard.adapter = adpater
        scoreboard.layoutManager = LinearLayoutManager(activity)
        scoreboard.setHasFixedSize(true)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment favourite.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            favouritefragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

    override fun onitemclick(position: Int) {
        super.onitemclick(position)
        Toast.makeText(activity,"Position : $position",Toast.LENGTH_SHORT).show()
        //reference to the cliked item:
        val reffernce_item  = quizlist[position]  //refference to the item

        adpater.notifyItemChanged(position)
    }

    private fun list(size : Int) : List<scorelist>
    {
        //Objective : Sort the scores and send then accordinly
        val list_item = ArrayList<scorelist>()
        val name = "QuizName"
        val review = "Review"
        for(i in 0 until size){
            val resource = when (i%2){
                0 ->R.drawable.heart
                1->R.drawable.home
                else ->
                    R.drawable.home
            }
            val item=scorelist(resource,name,review)
            list_item+=item
        }
        return list_item
    }
}