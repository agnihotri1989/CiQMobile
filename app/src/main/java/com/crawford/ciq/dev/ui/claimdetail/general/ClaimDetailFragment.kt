package com.crawford.ciq.dev.ui.claimdetail.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.adapters.CustomPagerAdapter
import com.crawford.ciq.dev.model.*
import com.crawford.ciq.dev.utils.Utilize
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_claimdetail.*
import kotlinx.android.synthetic.main.fragment_claimdetail.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClaimDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClaimDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var myClaimResult: MyClaimResult? = null

    lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myClaimResult = it.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult

        }

    }

    fun forpolicyinfo(claimSubscription: ClaimSubscription) {

        val frag =
            mView.viewPager.adapter!!.instantiateItem(mView.viewPager, mView.viewPager.currentItem)

        if (frag is GeneralFragment) {
            val generalfrag = frag as GeneralFragment
            generalfrag.forpolicyinfo(claimSubscription)
        }
    }

    fun getcliamlist(claimResult: ClaimResult) {
        val frag =
            mView.viewPager.adapter!!.instantiateItem(mView.viewPager, mView.viewPager.currentItem)

        if (frag is GeneralFragment) {
            val generalfrag = frag as GeneralFragment
            generalfrag.getcliamlist(claimResult)
        }
    }

    fun getclientsubscriber(claimSubscription: ClaimSubscription) {
        val frag =
            mView.viewPager.adapter!!.instantiateItem(mView.viewPager, mView.viewPager.currentItem)

        if (frag is GeneralFragment) {
            val generalfrag = frag as GeneralFragment
            generalfrag.getclientsubscriber(claimSubscription)
        }
    }

    fun getinsured(insuredaddressapiresult: InsuredAddressApiResult) {
        val frag =
            mView.viewPager.adapter!!.instantiateItem(mView.viewPager, mView.viewPager.currentItem)

        if (frag is GeneralFragment) {
            val generalfrag = frag as GeneralFragment
            generalfrag.getinsured(insuredaddressapiresult)
        }
    }

    fun getclaimant(claimantResult:List< ClaimantApiResult>) {
        val frag =
            mView.viewPager.adapter!!.instantiateItem(mView.viewPager, mView.viewPager.currentItem)

        if (frag is GeneralFragment) {
            val generalfrag = frag as GeneralFragment
            generalfrag.getclaimant(claimantResult)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_claimdetail, container, false)
        mView = view
        mView.tv_claim.text = "Claim ID- ${myClaimResult!!.filenumber.toString()}"
        val tb = mView.tabLayout
        // setTablayoutwidth(tb)
        tb.addTab(tb.newTab().setText("General"))
        tb.addTab(tb.newTab().setText("Dockets"))
        tb.addTab(tb.newTab().setText("Activities"))
        tb.addTab(tb.newTab().setText("CI"))
        tb.addTab(tb.newTab().setIcon(R.drawable.paperclip))
        // tb.tabGravity = TabLayout.GRAVITY_FILL
        val bundle = Bundle()
        bundle.putSerializable(Utilize.CLAIMRESULT.name, myClaimResult)

        val adapter = CustomPagerAdapter(
            activity!!.applicationContext, fragmentManager!!,
            tb.tabCount, bundle
        )

        mView.viewPager.adapter = adapter
        mView.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        mView.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        mView.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                val tab = tb.getTabAt(position)
                tab?.select()
            }

        })


        return mView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClaimDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ClaimDetailFragment().apply {
                arguments = bundle
            }
    }

}