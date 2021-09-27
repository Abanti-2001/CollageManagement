package com.example.collagemanagement

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast

public class DiaryGestureListerner(var context: Context , var listerner : DiaryGestureListerner.Gesture)
    : GestureDetector.SimpleOnGestureListener() {
    private val swipedist = 100
    private val swipespeed = 100
    override fun onFling(
        downevent: MotionEvent?,
        moveevent: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        var diffX= moveevent?.x?.minus(downevent!!.x) ?:0.0f //checks how far the swipe was horizontally
        var diffY = moveevent?.y?.minus(downevent!!.y) ?:0.0f //checks how far the swipe was vertically
        //determining the direction
        return if(Math.abs(diffX)>Math.abs(diffY)){
            //horizontalswipe
            if(Math.abs(diffX)>swipedist && Math.abs(diffX)>swipespeed){
                if(diffX>0.0f)
                {
                    //right swipe
                    listerner.swipeRight()
                }
                else {
                    //left swipe
                    listerner.swipeLeft()
                }
                true
            }else
                super.onFling(downevent, moveevent, velocityX, velocityY)
        }else {
            //vertical
            if(Math.abs(diffY)>swipedist && Math.abs(diffY)>swipespeed)
            {
                if(diffY<0.0f)
                {
                    //Swipe Up
                    listerner.swipeUp()
                }
                else {
                    //Swipe Down
                    listerner.swipeDown()
                }
                true
            }else
                super.onFling(downevent, moveevent, velocityX, velocityY)
        }
    }
    interface Gesture {
        fun swipeDown() {}

        fun swipeUp() {}

        fun swipeLeft() {}

        fun swipeRight() {}
    }
}
