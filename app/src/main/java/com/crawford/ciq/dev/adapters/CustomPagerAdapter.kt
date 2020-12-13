package com.crawford.ciq.dev.adapters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.crawford.ciq.dev.ui.claimdetail.activity.ActivityFragment
import com.crawford.ciq.dev.ui.claimdetail.docket.DocketFragment
import com.crawford.ciq.dev.ui.claimdetail.general.GeneralFragment
import com.crawford.ciq.dev.ui.home.fragments.RecentFragment

class CustomPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int, var bundle:Bundle
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {

                val generalFragment = GeneralFragment.newInstance(bundle)
                return generalFragment

            }
            1 -> {
                val docketFragment =
                    DocketFragment()
                return docketFragment

            }
            2 -> {

                return ActivityFragment()
            }
            3 -> {
                // val movieFragment = MovieFragment()
                return RecentFragment()
            }
            4 -> {
                // val movieFragment = MovieFragment()
                return RecentFragment()
            }
            else -> return null!!
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
}