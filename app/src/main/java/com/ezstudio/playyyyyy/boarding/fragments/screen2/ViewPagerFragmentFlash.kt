package com.ezstudio.playyyyyy.boarding.fragments.screen2

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.boarding.fragments.AdapterFlashViewPager
import com.ezstudio.playyyyyy.boarding.fragments.screenflash.FlashOneFragment
import com.ezstudio.playyyyyy.boarding.fragments.screenflash.FlashThreeFragment
import com.ezstudio.playyyyyy.boarding.fragments.screenflash.FlashTwoFragment
import me.relex.circleindicator.CircleIndicator3


class ViewPagerFragmentFlash : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager_flash, container, false)
        val fragmentList = arrayListOf<Fragment>(FlashOneFragment(),FlashTwoFragment(),FlashThreeFragment())

        val adapter =
            AdapterFlashViewPager(
                fragmentList,
                requireActivity().supportFragmentManager,
                lifecycle
            )
        val viewpager2 = view.findViewById<ViewPager2>(R.id.view_pager_flash)
        val circleIndicator = view.findViewById<CircleIndicator3>(R.id.circle_indicator)
        viewpager2.adapter = adapter
        Handler().postDelayed({findNavController().navigate(R.id.action_viewPagerFragmentFlash_to_homeActivity)},3000)
        circleIndicator.setViewPager(viewpager2)
        return view
    }


}