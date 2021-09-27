package com.example.collagemanagement

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.collagemanagement.models.QuizQuestion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.questionmodel.view.*
import kotlinx.android.synthetic.main.scores.view.*
import kotlinx.android.synthetic.main.scores.view.Quiz_Logo

class QuizListAdapter(private val listitem: MutableList<QuizQuestion>,
                      private val Listner: onItemClickedListner)
    : RecyclerView.Adapter<QuizListAdapter.QuizHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.questionmodel
            ,parent,false)
        return QuizHolder(itemview)
    }
    override fun getItemCount() = listitem.size

    override fun onBindViewHolder(holder: QuizHolder, position: Int) {

        val current_item = listitem[position]
        holder.Question.text = current_item.Question
        holder.Question1.text = current_item.Option1
        holder.Question2.text = current_item.Option2
        holder.Question3.text = current_item.Option3
        holder.Question4.text = current_item.Option4
    }



    inner class QuizHolder(itemview : View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener
    {
        val Question : TextView = itemview.Question
        val Question1 : TextView = itemview.question1
        val Question2 : TextView = itemview.question2
        val Question3 : TextView = itemview.question3
        val Question4 : TextView = itemview.question4
        init {
            itemview.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val pos =absoluteAdapterPosition
            if(pos!=RecyclerView.NO_POSITION)
                Listner.onitemclick(pos)
        }
    }
    interface onItemClickedListner{
        fun onitemclick(position: Int){
        }
    }


}
