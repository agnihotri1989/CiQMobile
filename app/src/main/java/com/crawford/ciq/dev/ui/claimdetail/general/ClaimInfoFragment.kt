package com.crawford.ciq.dev.ui.claimdetail.general

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.databinding.FragmentClaimInfoBinding
import com.crawford.ciq.dev.model.*
import com.crawford.ciq.dev.utils.Utilize

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClaimInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClaimInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var myClaimResult: MyClaimResult? = null

    lateinit var mview: View
    private var onclaimInfoListener: onClaimInfoListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myClaimResult = it.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult
        }
    }

    fun retriveaddressfrominsured(addressapiresult: AddressApiResult) {
        if (claimResult!!.filenumber == addressapiresult.claimId) {
         claimResult?.apply {
             address1 = addressapiresult.address1
             address2 = addressapiresult.address2
             city = addressapiresult.city
             province = addressapiresult.province
             country = addressapiresult.country
             postalCode = addressapiresult.postalCode
         }
        }
        fragmentClaimInfoBinding!!.invalidateAll()
       // fragmentClaimInfoBinding!!.claim = claimResult
    }

    fun getcliamlist(claimresult: ClaimResult) {
        claimResult = claimresult
        fragmentClaimInfoBinding!!.claim = claimResult
    }

    fun forpolicyinfo(claimSubscription: ClaimSubscription) {
        fragmentClaimInfoBinding!!.claimsubscription = claimSubscription
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentClaimInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_claim_info, container, false)
        val view = fragmentClaimInfoBinding!!.root
        fragmentClaimInfoBinding!!.myclaim = myClaimResult
        onclaimInfoListener!!.requestforclaimlist()
        onclaimInfoListener!!.requestclaimsubsforpolicy()
        onclaimInfoListener!!.requestaddressforclaiminfo(ClaimInfoFragment())
        return view
    }

    companion object {
        var fragmentClaimInfoBinding: FragmentClaimInfoBinding? = null
        private var claimResult: ClaimResult? = null

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ClaimInfoFragment().apply {
                arguments = bundle
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is onClaimInfoListener) {
            onclaimInfoListener = context as onClaimInfoListener
        } else {
            throw ClassCastException(
                "$context must implement OnGetItemListener"
            )
        }
    }

    public interface onClaimInfoListener {
        fun requestforclaimlist()
        fun requestclaimsubsforpolicy()
        fun requestaddressforclaiminfo(fragment: ClaimInfoFragment)
    }
}