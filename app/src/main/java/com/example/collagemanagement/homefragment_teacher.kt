package com.example.collagemanagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_quiz_model.view.*
import java.util.*
import kotlin.collections.HashMap


private const val ARG_PARAM= "param1"
class homefragment_teacher : Fragment(), QuizAdapter.onItemClickedListner{
    private var param1 : String?=" "
    private lateinit var quizlist : MutableList<String>
    private lateinit var Recycleradpater :  QuizAdapter
    private lateinit var Title : String
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val Userid : String =auth.currentUser?.uid.toString()
    private val documentReference : FirebaseFirestore = FirebaseFirestore.getInstance()
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            homefragment_teacher().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, param1)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM)
        }
        quizlist= mutableListOf<String>()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = LayoutInflater.from(context).inflate(R.layout.fragment_home_teacher,container,false)
        val adpter = viewRoot.findViewById<RecyclerView>(R.id.quiz_item)
        createAdapter(adpter)
        viewRoot.findViewById<Button>(R.id.addquizlist).setOnClickListener {
              addlist(quizlist.size)
        }
        return viewRoot
    }

    private fun createAdapter(QuizView: RecyclerView?) {
            QuizView?.setHasFixedSize(false)
        val itemTouchHelper = ItemTouchHelper(SimpleCallObject)
        itemTouchHelper.attachToRecyclerView(QuizView)
        documentReference.collection("Users/$Userid/List").get().addOnSuccessListener {
            quizlist.clear()
            if(it!=null) {
                for (document in it) {
                    quizlist.add(document.id)
                }
                Recycleradpater = QuizAdapter(quizlist,this@homefragment_teacher)
                QuizView?.adapter = Recycleradpater
            }else{
                Recycleradpater = QuizAdapter(quizlist,this@homefragment_teacher)
                QuizView?.adapter = Recycleradpater
            }
        }
    }
    override fun onitemclick(position: Int) {
        super.onitemclick(position)
        //Toast.makeText(activity,"$position",Toast.LENGTH_SHORT).show()
        val intent = Intent(activity,QuizList::class.java)
        intent.putExtra("Title",quizlist[position])
        startActivity(intent)
    }

    private fun addlist(index : Int){
      // Toast.makeText(activity,"Clicked!",Toast.LENGTH_SHORT).show()
        val builder = context?.let { AlertDialog.Builder(it) }
        val inflater  = layoutInflater
        val dialogboxlayout=inflater.inflate(R.layout.quizcreate,null)
        with(builder) {
            builder?.setTitle("Enter a name for the quiz!!")
            builder?.setPositiveButton("Add"){ dialog, which->
                ///changing the content
              val  quizname = dialogboxlayout.findViewById<EditText>(R.id.quizname)?.text.toString()
                if(!quizname.isNullOrEmpty()) {
                    quizlist.add(index,"$quizname")
                    Title=quizname
                    Recycleradpater.notifyItemInserted(index)
                    val map =HashMap<String,String>()
                    map.put(Title,quizlist[index])
                    documentReference.collection("Users/$Userid/List")
                        .document("$Title").set(map)
                    //Toast.makeText(activity, quizname, Toast.LENGTH_SHORT).show()
                }
                else
                Toast.makeText(activity,"Cannot create list with empty name", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            builder?.setNegativeButton("Cancel"){dialog,which->
                dialog.dismiss()
            }
            builder?.setView(dialogboxlayout)
            builder?.show()
        }
    }
    private var SimpleCallObject = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP.or(
            ItemTouchHelper.DOWN), ItemTouchHelper.LEFT .or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            var sp = viewHolder.absoluteAdapterPosition
            var dp=target.absoluteAdapterPosition
            Collections.swap(quizlist,sp,dp)
            recyclerView.adapter?.notifyItemMoved(sp,dp)
            return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            var pos = viewHolder.absoluteAdapterPosition
            documentReference.collection("Users/$Userid/List")
                .document(quizlist[pos]).delete().addOnSuccessListener {
                    Toast.makeText(activity,"Deleted!!",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(activity,"Unable to delete",Toast.LENGTH_SHORT).show()
                }
            quizlist.removeAt(pos)
            Recycleradpater.notifyItemRemoved(pos)
        }
    }
}
class QuizAdapter(private val listitem: MutableList<String>, private val Listner: onItemClickedListner) :  RecyclerView.Adapter<QuizAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.home_quiz_model
            ,parent,false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current_item = listitem[position]
        holder.Title.text = current_item
    }

    override fun getItemCount() = listitem.size
    inner class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview),View.OnClickListener{
        val Title : TextView = itemview.Title
        init{
            itemview.setOnClickListener {
                Listner.onitemclick(absoluteAdapterPosition)
            }
        }
        override fun onClick(v: View?) {
            val pos = absoluteAdapterPosition
            if(pos!=RecyclerView.NO_POSITION)
                Listner.onitemclick(pos)
        }
    }
    interface onItemClickedListner{
        fun onitemclick(position: Int){
        }
    }
}