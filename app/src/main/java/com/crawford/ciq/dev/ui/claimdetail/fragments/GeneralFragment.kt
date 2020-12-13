package com.crawford.ciq.dev.ui.claimdetail.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.adapters.MygeneralRecyclerViewAdapter
import com.crawford.ciq.dev.model.*
import com.crawford.ciq.dev.ui.home.fragments.*
import com.crawford.ciq.dev.utils.Utilize
import kotlinx.android.synthetic.main.fragment_claimdetail.view.*
import kotlinx.android.synthetic.main.fragment_general_list.view.*

/**
 * A fragment representing a list of Items.
 */
class GeneralFragment : Fragment(), MygeneralRecyclerViewAdapter.onItemClickListner {

    private var columnCount = 1
    lateinit var fragment: Fragment

    private var myClaimResult: MyClaimResult? = null
    lateinit var bundle:Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
//            columnCount = it.getInt(ARG_COLUMN_COUNT)
            myClaimResult = it.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_general_list, container, false)
        val mygeneralRecyclerViewAdapter =
            MygeneralRecyclerViewAdapter(DummyContent.ITEMS, activity!!)
        mygeneralRecyclerViewAdapter.setonClickListner(this)
        view.rcv_general.adapter = mygeneralRecyclerViewAdapter
        bundle = Bundle()
        bundle.putSerializable(Utilize.CLAIMRESULT.name, myClaimResult)
        fragment = ClaimInfoFragment.newInstance(bundle)
        setUpFragment(fragment)
        return view
    }

    fun getcliamlist(claimResult: ClaimResult) {

        if (fragment is ClaimInfoFragment) {
            val claiminfofrag  = fragment as ClaimInfoFragment
            claiminfofrag.getcliamlist(claimResult)
        }
    }
    fun forpolicyinfo(claimSubscription: ClaimSubscription) {

        if (fragment is ClaimInfoFragment) {
            val claiminfofrag  = fragment as ClaimInfoFragment
            claiminfofrag.forpolicyinfo(claimSubscription)
        }
    }
    fun getclientsubscriber(claimSubscription: ClaimSubscription) {

        if (fragment is ClientSubscriberFragment) {
            val claiminfofrag  = fragment as ClientSubscriberFragment
            claiminfofrag.getclientSbscriber(claimSubscription)
        }
    }
    fun getinsured(insuredaddressapiresult: InsuredAddressApiResult) {

        if (fragment is InsuredFragment) {
            val claiminfofrag  = fragment as InsuredFragment
            claiminfofrag.getinsured(insuredaddressapiresult)
        }
    }
    fun getclaimant(claimantResult: List<ClaimantApiResult>) {

        if (fragment is ClaimantFragment) {
            val claiminfofrag  = fragment as ClaimantFragment
            claiminfofrag.getclaimant(claimantResult)
        }
    }
    fun setUpFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction()
            .replace(R.id.framelayout_container, fragment)
            .commit()
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(bundle: Bundle) =
//            GeneralFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(ARG_COLUMN_COUNT, columnCount)
//                }
//            }
            GeneralFragment().apply {
                arguments = bundle
            }
    }


    override fun onItemClick(content: String) {

        when (content) {
            "Info" -> {
                fragment = ClaimInfoFragment.newInstance(bundle)
                setUpFragment(fragment)
            }
            "Location" -> {
                fragment = LossLocationFragment.newInstance(bundle)
                setUpFragment(fragment)
            }
            "Subscription" -> {
                fragment = ClientSubscriberFragment.newInstance(bundle)
                setUpFragment(fragment)
            }

            "Insured" -> {
                fragment = InsuredFragment.newInstance(bundle)
                setUpFragment(fragment)
            }
            "Claimant" -> {
                fragment = ClaimantFragment.newInstance(bundle)
                setUpFragment(fragment)
            }
            else -> {
                setUpFragment(RecentFragment())
            }
        }
    }
}