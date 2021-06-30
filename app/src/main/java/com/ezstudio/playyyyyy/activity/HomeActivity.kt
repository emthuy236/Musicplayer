package com.ezstudio.playyyyyy.activity

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ezstudio.playyyyyy.AlbumArt
import com.ezstudio.playyyyyy.R
import com.ezstudio.playyyyyy.boarding.fragments.screen.AlbumFragment
import com.ezstudio.playyyyyy.boarding.fragments.screen.ArtistFragment
import com.ezstudio.playyyyyy.boarding.fragments.screen.TracksFragment
import com.ezstudio.playyyyyy.boarding.fragments.ViewPagerAdapter
import com.ezstudio.playyyyyy.bottomsheet.HandleBottomSheet
import com.ezstudio.playyyyyy.bottomsheet.HandleListBottomSheet
import com.ezstudio.playyyyyy.databinding.ActivityHomeBinding
import com.ezstudio.playyyyyy.models.TrackModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var trackFragment: TracksFragment? = null
    private var listBottomSheet:HandleListBottomSheet? = null


    companion object {
        val REQUEST_CODE = 1
        val instance = HomeActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        permission()

    }



    override fun getViewBottomSheet(): HandleBottomSheet? {

        return binding.bottomSheet
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun updateAdapter(trackModel: TrackModel) {
        trackFragment?.let {
            it.updateAdapter(trackModel)
        }
    }




    private fun getfragment() {
        lifecycleScope.launch {
            trackFragment = TracksFragment()

            val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
            trackFragment?.let {
                adapter.addFragmentInViewPager(it)
            }
            adapter.addFragmentInViewPager(AlbumFragment())
            adapter.addFragmentInViewPager(ArtistFragment())
            var tablayout: TabLayout = findViewById(R.id.tablayoutHome)
            val viewPager2 = findViewById<ViewPager2>(R.id.viewpagerHome)
            viewPager2.adapter = adapter
            TabLayoutMediator(tablayout, viewPager2) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.track)
                    1 -> tab.text = getString(R.string.album)
                    2 -> tab.text = getString(R.string.artist)
                }
            }.attach()
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

    }

    private fun permission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        } else {
            getfragment()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getfragment()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }


}