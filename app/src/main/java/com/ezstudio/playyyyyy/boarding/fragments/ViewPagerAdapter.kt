package com.ezstudio.playyyyyy.boarding.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private var listFragment = mutableListOf<Fragment>()

    fun addFragmentInViewPager(fragment: Fragment) {
        listFragment.add(fragment)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }


}