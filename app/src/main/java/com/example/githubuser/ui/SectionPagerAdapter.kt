package com.example.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter internal constructor(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    var user :String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment : Fragment?
        fragment = FollowFragment()

        fragment.arguments = Bundle().apply {
            putString(FollowFragment.EXTRA_NAME, user)
            putInt(FollowFragment.EXTRA_TYPE, position)
        }
        return fragment
    }
}