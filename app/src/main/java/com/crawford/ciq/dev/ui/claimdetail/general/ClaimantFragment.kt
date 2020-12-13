package com.crawford.ciq.dev.ui.claimdetail.general

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.adapters.ClaimantRecyclerAdapter
import com.crawford.ciq.dev.databinding.FragmentClaimantBinding
import com.crawford.ciq.dev.model.*
import com.crawford.ciq.dev.utils.Utilize
import kotlinx.android.synthetic.main.fragment_claimant.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClaimantFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClaimantFragment : Fragment(), ClaimantRecyclerAdapter.OnClaimantAddressListener {
    // TODO: Rename and change types of parameters
    private var myClaimResult: MyClaimResult? = null
    private var claimantResult: ClaimantResult? = null
    private var onclaimantlistener: onClaimantListener? = null
    lateinit var mview: View
    var fragmentClaimantBinding: FragmentClaimantBinding? = null
    lateinit var claimantrecycleradapter: ClaimantRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myClaimResult = it.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult

        }
    }

    fun getclaimant(claimantResult: List<ClaimantApiResult>) {

        //     fragmentClaimantBinding!!.claimant = claimantResult
        claimantrecycleradapter!!.addClaiamant(ArrayList(claimantResult))

    }

    fun retriveclaimantaddressfromaddressapi(
        claiamntaddresslist: AddressApiResult,
        claimantRecyclerAdapter: ClaimantRecyclerAdapter,
        position: Int
    ) {
      //  claimantRecyclerAdapter.updateaddressinclaimant(claiamntaddresslist)
        claimantRecyclerAdapter.apply {
            updateaddressinclaimant(claiamntaddresslist)
            notifyItemChanged(position)   }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentClaimantBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_claimant, container, false)
        val view = fragmentClaimantBinding!!.root
        val dividerItemDecoration = DividerItemDecoration(
            view.rcv_claimant.getContext(),
            DividerItemDecoration.VERTICAL
        )
        view.rcv_claimant.addItemDecoration(dividerItemDecoration)
        val claimantapiresultlist = ArrayList<ClaimantApiResult>()

        claimantrecycleradapter = ClaimantRecyclerAdapter(claimantapiresultlist, myClaimResult!!)
        claimantrecycleradapter!!.setonClickListner(this)
        fragmentClaimantBinding!!.adapter = claimantrecycleradapter
        onclaimantlistener!!.requestforclaimant()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is onClaimantListener) {
            onclaimantlistener = context as onClaimantListener
        } else {
            throw ClassCastException(
                "$context must implement OnGetItemListener"
            )
        }
    }

    public interface onClaimantListener {
        fun requestforclaimant()
        fun ongetaddressclaimant(
            claimantid: Int,
            claimantrecycleradapter: ClaimantRecyclerAdapter,
            fragment: ClaimantFragment, position: Int
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClaimantFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ClaimantFragment().apply {
                arguments = bundle
            }
    }

    override fun ongetaddressclaimant(claimantid: Int, position: Int) {
        onclaimantlistener!!.ongetaddressclaimant(
            claimantid,
            claimantrecycleradapter,
            ClaimantFragment(),
            position
        )
    }
}