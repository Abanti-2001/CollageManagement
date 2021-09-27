package com.example.collagemanagement

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
import java.util.*


class QuizList : AppCompatActivity() , QuizListAdapter.onItemClickedListner , DiaryGestureListerner.Gesture{
    private var database: DatabaseReference = Firebase.database.reference
    private  var auth : FirebaseAuth = FirebaseAuth.getInstance()
    private  var UserID : String =auth.currentUser?.uid.toString()
    private lateinit var listref : DatabaseReference
    private lateinit var quizlist : MutableList<QuizQuestion>
    private lateinit var Recycleradpater :  QuizListAdapter
    private lateinit var QuizView : RecyclerView
    private lateinit var ImageURI : Uri
    private lateinit var newitem : QuizQuestion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quizlist)
        supportActionBar?.setTitle("Quizes")

        quizlist= mutableListOf<QuizQuestion>()
        QuizView=findViewById(R.id.quiz_item)
        QuizView.setHasFixedSize(false)
        displaylist()
        val itemTouchHelper = ItemTouchHelper(SimpleCallObject)
        itemTouchHelper.attachToRecyclerView(QuizView)
            ImageURI= Uri.EMPTY
            submit.setOnClickListener {
                if(quizlist.size!=0)
                { database.child("Lists").child("$UserID").setValue(quizlist).addOnSuccessListener {
                    Toast.makeText(applicationContext,"Success",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show() }
                }else
                    Toast.makeText(applicationContext,"Cannot Upload Empty List",Toast.LENGTH_SHORT).show()
            }

            //Toast.makeText(applicationContext,"${datalist[0].Question}",Toast.LENGTH_SHORT).show()

        additem.setOnClickListener { InsertItem(quizlist.size) }
    }
    private fun displaylist(){
        listref = FirebaseDatabase.getInstance().getReference("Lists/$UserID")
        listref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                quizlist.clear()
                if(snapshot.exists()){
                    for (QuizSnapshot in snapshot.children){
                            val quiz = QuizSnapshot.getValue(QuizQuestion::class.java)
                            quizlist.add(quiz!!)
                    }
                     Recycleradpater=QuizListAdapter(quizlist,this@QuizList)
                    QuizView.adapter = Recycleradpater
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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
        //Toast.makeText(this,"Position : $position", Toast.LENGTH_SHORT).show()
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

            q?.setText(reffernce_item?.Question)
            op1.setText(reffernce_item?.Option1)
            op2.setText(reffernce_item?.Option2)
            op3.setText(reffernce_item?.Option3)
            op4.setText(reffernce_item?.Option4)

            setPositiveButton("Done"){dialog,which->
                ///changing the content
                newitem = QuizQuestion(q?.text.toString(),op1?.text.toString(),op2?.text.toString(),
                    op3?.text.toString(),op4?.text.toString())
                quizlist[position] = newitem
                Recycleradpater.notifyItemChanged(position)
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
    setTitle("Enter the question")
    setPositiveButton("Done"){dialog,which->

      //  Toast.makeText(applicationContext,"${q?.text.toString()}",Toast.LENGTH_SHORT).show()
            newitem = QuizQuestion(q?.text.toString(),op1?.text.toString(),op2?.text.toString(),
                op3?.text.toString(),op4?.text.toString())
            quizlist.add(index, newitem)
            Recycleradpater.notifyItemInserted(index)
          //  Toast.makeText(applicationContext,"Made Text",Toast.LENGTH_SHORT).show()
    }
    setNegativeButton("Cancel"){dialog,which->
        dialog.dismiss()
    }
    setView(dialogboxlayout)
    show()
    }
        }

    fun RemoveItem(pos: Int) {
        quizlist.removeAt(pos)
        Recycleradpater.notifyItemRemoved(pos)
        //Toast.makeText(this,"$index",Toast.LENGTH_SHORT).show()
    }


}




