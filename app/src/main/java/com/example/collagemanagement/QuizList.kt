package com.example.collagemanagement

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.collagemanagement.models.QuizQuestion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.quizlist.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class QuizList : AppCompatActivity() , QuizListAdapter.QuizonItemClickedListner , DiaryGestureListerner.Gesture{
    private  var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private  var UserID : String =auth.currentUser?.uid.toString()
    private val documentReference : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var quizlist : MutableList<QuizQuestion>
    private lateinit var Recycleradpater :  QuizListAdapter
    private lateinit var QuizView : RecyclerView
    private lateinit var newitem : QuizQuestion
    private lateinit var Title : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizlist)
        supportActionBar?.hide()
        quizlist= mutableListOf<QuizQuestion>()
        QuizView=findViewById(R.id.quiz_item)
        QuizView.setHasFixedSize(false)
        Title=intent.getStringExtra("Title")
        displaylist()
        val itemTouchHelper = ItemTouchHelper(SimpleCallObject)
        itemTouchHelper.attachToRecyclerView(QuizView)
            submit.setOnClickListener {
                if(quizlist.size==0)
                {
                    Toast.makeText(applicationContext,"Cannot upload empty list",Toast.LENGTH_SHORT).show()
                }else
                finish()
            }
        additem.setOnClickListener { InsertItem(quizlist.size) }
    }
    private fun displaylist(){
        documentReference.collection("Users/$UserID/List/$Title/$Title").get().addOnSuccessListener { snapshot ->
                quizlist.clear()
                var i : Int = 0
                if(snapshot!=null){
                    for (data in snapshot){
                        val data = data.toObject(QuizQuestion::class.java)
                        quizlist.add(i,data)
                        i++
                    }
                    Recycleradpater=QuizListAdapter(quizlist,this@QuizList)
                    QuizView.adapter = Recycleradpater
                }else
                {
                    Recycleradpater=QuizListAdapter(quizlist,this@QuizList)
                    QuizView.adapter = Recycleradpater
                }
        }
    }

    private var SimpleCallObject = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),ItemTouchHelper.LEFT .or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder,
            target: ViewHolder
        ): Boolean {
            var sp = viewHolder.absoluteAdapterPosition
            var dp=target.absoluteAdapterPosition

            Collections.swap(quizlist,sp,dp)
            recyclerView.adapter?.notifyItemMoved(sp,dp)
            return true
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            var pos = viewHolder.absoluteAdapterPosition
            RemoveItem(pos)
        }
    }


    override fun onitemclick(position: Int) {
        super.onitemclick(position)
        Toast.makeText(this,"Position : $position", Toast.LENGTH_SHORT).show()
        //reference to the cliked item:
        val reffernce_item  = quizlist[position]  //reference to the item
        val builder =AlertDialog.Builder(this)
        val inflater  = layoutInflater
        val dialogboxlayout=inflater.inflate(R.layout.questionbuilder,null)
        with(builder) {
            val q = dialogboxlayout.findViewById<EditText>(R.id.Question)
            val op1=dialogboxlayout.findViewById<EditText>(R.id.op1)
            val op2=dialogboxlayout.findViewById<EditText>(R.id.op2)
            val op3=dialogboxlayout.findViewById<EditText>(R.id.op3)
            val op4=dialogboxlayout.findViewById<EditText>(R.id.op4)
            val ans=dialogboxlayout.findViewById<EditText>(R.id.ans)
            q?.setText(reffernce_item?.Question)
            op1.setText(reffernce_item?.Option1)
            op2.setText(reffernce_item?.Option2)
            op3.setText(reffernce_item?.Option3)
            op4.setText(reffernce_item?.Option4)
            ans.setText(reffernce_item?.Correct)
            setPositiveButton("Done"){dialog,which->
                ///changing the content
                val ques = q?.text.toString()
                var identical : Boolean =false
                if(ques.isNullOrEmpty()) {
                    for(i in 0 until quizlist.size){
                        if(quizlist[i].Question.equals("$ques",true)) {
                            identical = true
                            break
                        }
                    }
                if(!identical) {
                    newitem = QuizQuestion(
                        q?.text.toString(), op1?.text.toString(), op2?.text.toString(),
                        op3?.text.toString(), op4?.text.toString(), ans?.text.toString()
                    )
                    documentReference.collection("Users/$UserID/List/$Title/$Title")
                        .document("$ques").set(newitem).addOnSuccessListener {
                            Toast.makeText(applicationContext, "Made Text", Toast.LENGTH_SHORT)
                                .show()
                        }
                    quizlist[position] = newitem
                    Recycleradpater.notifyItemChanged(position)
                }else Toast.makeText(applicationContext,"Cannot have identical questions!",Toast.LENGTH_SHORT).show()
                }else Toast.makeText(applicationContext,"Question cannot be empty",Toast.LENGTH_SHORT).show()
            }
            setNegativeButton("Cancel"){dialog,which->
                dialog.dismiss()
            }
            setView(dialogboxlayout)
            show()
        }

    }
    fun InsertItem(index : Int) {
        val builder =AlertDialog.Builder(this)
        val inflater  = layoutInflater
        val dialogboxlayout=inflater.inflate(R.layout.questionbuilder,null)
    with(builder){
        val q = dialogboxlayout.findViewById<EditText>(R.id.Question)
        val op1=dialogboxlayout.findViewById<EditText>(R.id.op1)
        val op2=dialogboxlayout.findViewById<EditText>(R.id.op2)
        val op3=dialogboxlayout.findViewById<EditText>(R.id.op3)
        val op4=dialogboxlayout.findViewById<EditText>(R.id.op4)
        val ans=dialogboxlayout.findViewById<EditText>(R.id.ans)
        setTitle("Enter the question")
        setIcon(R.drawable.profile)
    setPositiveButton("Done"){dialog,which->
        val ques = q?.text.toString()
        var identical : Boolean =false
            if(!ques.isNullOrEmpty()) {
                for(i in 0 until quizlist.size){
                    if(quizlist[i].Question.equals("$ques",true)) {
                        identical = true
                        break
                    }
                }
                if(!identical) {
                    newitem = QuizQuestion(
                        q?.text.toString(), op1?.text.toString(), op2?.text.toString(),
                        op3?.text.toString(), op4?.text.toString(), ans?.text.toString()
                    )
                    documentReference.collection("Users/$UserID/List/$Title/$Title")
                        .document("$ques").set(newitem).addOnSuccessListener {
                            Toast.makeText(applicationContext, "Made Text", Toast.LENGTH_SHORT)
                                .show()
                        }
                    quizlist.add(index, newitem)
                    Recycleradpater.notifyItemInserted(index)
                }else Toast.makeText(applicationContext,"Cannot have identical questions!",Toast.LENGTH_SHORT).show()

            }else Toast.makeText(applicationContext,"Question cannot be empty",Toast.LENGTH_SHORT).show()
    }
    setNegativeButton("Cancel"){dialog,which->
        dialog.dismiss()
    }
    setView(dialogboxlayout)
    show()
    }
        }

    fun RemoveItem(pos: Int) {

        documentReference.collection("Users/$UserID/List/$Title/$Title")
            .document("${quizlist[pos].Question.toString()}").delete().addOnSuccessListener {
                Toast.makeText(this,"Deleted!",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Couldn't delete!",Toast.LENGTH_SHORT).show()
            }
        quizlist.removeAt(pos)
        Recycleradpater.notifyItemRemoved(pos)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}




