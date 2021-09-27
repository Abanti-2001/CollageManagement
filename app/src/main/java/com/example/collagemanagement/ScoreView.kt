package com.example.collagemanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collagemanagement.models.scorelist
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.scores.view.*

class ScoreView(private val listitem : List<scorelist>, val Listner : onItemClickedListner) : RecyclerView.Adapter<ScoreView.ScoreHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.scores
        ,parent,false)
        return ScoreHolder(itemview)
    }

    override fun onBindViewHolder(holder: ScoreHolder, position: Int) {
        val current_item = listitem[position]
        holder.Quiz_Logo.setImageResource(current_item.Logo)
        holder.Quiz_Name.text = current_item.Quizname
        holder.Quiz_Remarks.text = current_item.QuizReview
        holder.Quiz_Name.setOnClickListener{

        }

    }
    override fun getItemCount() = listitem.size

    inner class ScoreHolder(itemview : View) : RecyclerView.ViewHolder(itemview),
            View.OnClickListener

    {
            val Quiz_Logo : CircleImageView = itemview.Quiz_Logo
            val Quiz_Name : TextView = itemview.Quiz_name
            val Quiz_Remarks : TextView = itemview.Quiz_Remarks
            init {
                itemview.setOnClickListener(this)
            }
        override fun onClick(v: View?) {
            val pos =absoluteAdapterPosition
            Listner.onitemclick(pos)
        }
    }
    interface onItemClickedListner{
        fun onitemclick(position: Int){
        }
    }
}