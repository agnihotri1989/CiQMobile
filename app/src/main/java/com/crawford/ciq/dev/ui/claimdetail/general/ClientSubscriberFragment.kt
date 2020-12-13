package com.crawford.ciq.dev.ui.claimdetail.general

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.databinding.FragmentClientSubscriberBinding
import com.crawford.ciq.dev.model.ClaimSubscription
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.utils.Utilize

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClientSubscriberFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClientSubscriberFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var myClaimResult: MyClaimResult? = null
    private var claimSubscription: ClaimSubscription? = null
    lateinit var mview: View
    private var onclientsubscriberlistener: onClientSubscriberListener? = null

    var fragmentClientSubscriberBinding: FragmentClientSubscriberBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            myClaimResult = it.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult

        }
    }

    fun getclientSbscriber(claimSubscription: ClaimSubscription) {
        fragmentClientSubscriberBinding!!.claimsubscription = claimSubscription
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentClientSubscriberBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_client_subscriber, container, false)

        val view = fragmentClientSubscriberBinding!!.root
        fragmentClientSubscriberBinding!!.myclaim = myClaimResult

        onclientsubscriberlistener!!.requestforcleintsubscriber()
        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is onClientSubscriberListener) {
            onclientsubscriberlistener = context as onClientSubscriberListener
        } else {
            throw ClassCastException(
                "$context must implement OnGetItemListener"
            )
        }

    }

    public interface onClientSubscriberListener {
        fun requestforcleintsubscriber()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClientSubscriberFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ClientSubscriberFragment().apply {
                arguments = bundle
            }
    }
}