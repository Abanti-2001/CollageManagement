package com.example.collagemanagement

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.auth.FirebaseAuth

class PageAdapter (supportFragmentManager : FragmentManager, private val des : String) : FragmentPagerAdapter(supportFragmentManager)
{
    private lateinit var user : FirebaseAuth
    var list = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        if(des == "Teacher"){
            when(position){
                0->{
                    //home

                    return homefragment_teacher()
                }
                1-> {
                    //profile
                    return  profilefragment()
                }
                else-> {
                    return homefragment_teacher()
                }
            }
        }
        else {
            when (position) {
                0 -> {
                    //home

                    return homefragment_student()
                }

                1 -> {
                    //faourite
                    return favouritefragment()
                }
                2 -> {
                    //profile
                    return profilefragment()
                }
                else -> {
                    return homefragment_teacher()
                }
            }
        }
    }
    override fun getCount(): Int {
        return list.size
    }
}