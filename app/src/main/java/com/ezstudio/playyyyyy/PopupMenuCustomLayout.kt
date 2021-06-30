package com.ezstudio.playyyyyy

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout


class PopupMenuCustomLayout(context: Context, rLayoutId: Int, onClickListener: PopupMenuCustomOnClickListener) {
    private val popupWindow: PopupWindow
    private val popupView: View
    fun setAnimationStyle(animationStyle: Int) {
        popupWindow.animationStyle = animationStyle
    }

    fun show() {
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
    }

    fun show(anchorView: View, gravity: Int, offsetX: Int, offsetY: Int) {
        popupWindow.showAsDropDown(anchorView, 0, -2 * anchorView.height)
    }

    interface PopupMenuCustomOnClickListener {
        fun onClick(menuItemId: Int)
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = inflater.inflate(rLayoutId,null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        popupWindow = PopupWindow(popupView, width, height, focusable)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            popupWindow.elevation = 30f
//        }
        val LinearLayout = popupView as LinearLayout
        for (i in 0 until LinearLayout.childCount) {
            val v = LinearLayout.getChildAt(i)
            v.setOnClickListener { v1: View ->
                onClickListener.onClick(v1.id)
                 popupWindow.dismiss()
            }
        }
    }
}