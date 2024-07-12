package com.example.appgithubusers.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.appgithubusers.R
import com.example.appgithubusers.fragment.FollowersFragment
import com.example.appgithubusers.fragment.FollowingFragment

class ViewPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    bundle: Bundle,
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentBundle = bundle

    @StringRes
    private val tabTitle = intArrayOf(R.string.Followers, R.string.Following)

    init {
        fragmentBundle = bundle
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabTitle[position])
    }

}