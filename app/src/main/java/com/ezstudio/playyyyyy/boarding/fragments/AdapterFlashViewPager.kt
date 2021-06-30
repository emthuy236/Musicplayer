package com.ezstudio.playyyyyy.boarding.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class AdapterFlashViewPager(list: ArrayList<Fragment>,
                            fm: FragmentManager,
                            lifecycle: Lifecycle
) : FragmentStateAdapter(fm,lifecycle) {
    val fragmentList = list
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}