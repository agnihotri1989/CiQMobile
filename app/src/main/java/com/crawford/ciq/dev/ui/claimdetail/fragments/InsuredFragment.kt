package com.crawford.ciq.dev.ui.claimdetail.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.databinding.FragmentInsuredBinding
import com.crawford.ciq.dev.model.ClaimResult
import com.crawford.ciq.dev.model.ClaimSubscription
import com.crawford.ciq.dev.model.InsuredAddressApiResult
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.utils.Utilize
import kotlinx.android.synthetic.main.fragment_client_subscriber.view.*
import kotlinx.android.synthetic.main.fragment_client_subscriber.view.tv_emailaddress
import kotlinx.android.synthetic.main.fragment_insured.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InsuredFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InsuredFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var myClaimResult: MyClaimResult? = null
    private var onInsuredListener: OnInsuredListener? = null
    lateinit var mview: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myClaimResult = it.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult

        }
    }

    fun getinsured(insuredaddressapiresult: InsuredAddressApiResult) {
        fragmentInsuredBinding!!.insuredaddress = insuredaddressapiresult

    }

    fun getclaim(claimresult:ClaimResult){
        fragmentInsuredBinding!!.claim = claimresult
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentInsuredBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_insured, container, false)
        val view = fragmentInsuredBinding!!.root
        fragmentInsuredBinding!!.myclaim = myClaimResult
        onInsuredListener!!.requestforclaim(InsuredFragment())
        onInsuredListener!!.requestforInsured()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClientSubscriberFragment.onClientSubscriberListener) {
            onInsuredListener = context as OnInsuredListener
        } else {
            throw ClassCastException(
                "$context must implement OnGetItemListener"
            )
        }

    }

    public interface OnInsuredListener {
        fun requestforInsured()
        fun requestforclaim(fragment:InsuredFragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InsuredFragment.
         */
        var fragmentInsuredBinding: FragmentInsuredBinding? = null
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            InsuredFragment().apply {
                arguments = bundle
            }
    }
}