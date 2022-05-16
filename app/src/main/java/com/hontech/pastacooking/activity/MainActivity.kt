package com.hontech.pastacooking.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hontech.pastacooking.R
import com.hontech.pastacooking.activity.fragment.HeaterFragment
import com.hontech.pastacooking.activity.fragment.MainFragment
import com.hontech.pastacooking.activity.fragment.SettingFragment
import com.hontech.pastacooking.activity.window.valveWindow
import com.hontech.pastacooking.app.hideSoftKey

class MainActivity : AppCompatActivity() {

    private val mViewPager: ViewPager2 by lazy { findViewById(R.id.id_view_pager) }
    private val mNav: BottomNavigationView by lazy { findViewById(R.id.id_nav) }
    private val mFragments = arrayOf(SettingFragment(), HeaterFragment(), MainFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onStop() {
        super.onStop()
        valveWindow.dismiss()
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            hideSoftKey(this)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun init() {
        mViewPager.adapter = Adapter(this, mFragments)
        mViewPager.registerOnPageChangeCallback(mPageChange)
        mNav.setOnItemSelectedListener(mItemSelected)
    }

    private val mPageChange = object: ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            mNav.selectedItemId = mNav.menu.getItem(position).itemId
        }
    }

    private val mItemSelected = object: NavigationBarView.OnItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.menu_setting -> mViewPager.currentItem = 0
                R.id.menu_boiler -> mViewPager.currentItem = 1
                R.id.menu_main -> mViewPager.currentItem = 2
//                R.id.menu_weight -> mViewPager.currentItem = 3
            }
            return true
        }
    }

    private class Adapter(act: MainActivity, val fragments: Array<Fragment>) : FragmentStateAdapter(act) {

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}

