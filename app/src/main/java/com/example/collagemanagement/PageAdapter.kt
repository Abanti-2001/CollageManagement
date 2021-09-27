package com.example.collagemanagement

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.firebase.auth.FirebaseAuth

class PageAdapter (supportFragmentManageer : FragmentManager , val des : String) : FragmentStatePagerAdapter(supportFragmentManageer)
{
    private lateinit var user : FirebaseAuth
    var list = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        if(des == "Teacher"){
            when(position){
                0->{
                    //home

                    return homefragment()
                }
                1-> {
                    //profile
                    return  profilefragment()
                }
                else-> {
                    return homefragment()
                }
            }
        }
        else {
            when (position) {
                0 -> {
                    //home

                    return homefragment()
                }
                1 -> {
                    //activity
                    return activityfragment()
                }
                2 -> {
                    //faourite
                    return favouritefragment()
                }
                3 -> {
                    //profile
                    return profilefragment()
                }
                else -> {
                    return homefragment()
                }
            }
        }
    }

    override fun getCount(): Int {
        return list.size
    }
}