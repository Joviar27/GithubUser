package com.example.githubuser

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    var user :String = ""
    var type : Int = 0

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment : Fragment?
        fragment = FollowFragment()

        fragment.arguments = Bundle().apply {
            putString(FollowFragment.EXTRA_NAME, user)
            putInt(FollowFragment.EXTRA_TYPE, type)
            Log.d(ContentValues.TAG, " Di section pager $type $user")
        }
        return fragment
    }
}