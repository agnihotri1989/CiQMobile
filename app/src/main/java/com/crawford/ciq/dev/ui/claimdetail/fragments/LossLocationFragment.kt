package com.crawford.ciq.dev.ui.claimdetail.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.databinding.FragmentInsuredBinding
import com.crawford.ciq.dev.databinding.FragmentLossLocationBinding
import com.crawford.ciq.dev.model.AddressApiResult
import com.crawford.ciq.dev.model.ClaimResult
import com.crawford.ciq.dev.model.InsuredAddressApiResult
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.ui.claimdetail.ClaimDetailActivity
import com.crawford.ciq.dev.utils.AddresstoLatlong
import com.crawford.ciq.dev.utils.Utilize
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_loss_location.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LossLocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LossLocationFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var myClaimResult: MyClaimResult? = null
    private var onlosslocationlistener: OnLossLocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myClaimResult = it.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult

        }
    }

    fun getcliamforlosslocation(claimresult: ClaimResult) {
        claimResult = claimresult
        fragmentLossLocationBinding!!.claim = claimResult
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
        fragmentLossLocationBinding!!.invalidateAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentLossLocationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_loss_location, container, false)
        val view = fragmentLossLocationBinding!!.root
        onlosslocationlistener!!.requestforclaimforlosslocation(LossLocationFragment())
        onlosslocationlistener!!.requestaddressfromclaim(LossLocationFragment())
        val spfrag: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment

        spfrag.getMapAsync(this)

        view.btn_direction.setOnClickListener {
            val claimDetailActivity = activity as ClaimDetailActivity
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=${claimDetailActivity.getmylatlngforlosslocation()!!.latitude},${claimDetailActivity.getmylatlngforlosslocation()!!.longitude}")
            )
            startActivity(intent)
        }

        view.btn_streetview.setOnClickListener {
            val claimDetailActivity = activity as ClaimDetailActivity
            val gmmIntentUri =
                Uri.parse("google.streetview:cbll=${claimDetailActivity.getmylatlngforlosslocation()!!.latitude},${claimDetailActivity.getmylatlngforlosslocation()!!.longitude}&cbp=0,30,0,0,-15")

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
// Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")

// Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }
        val myclaim = myClaimResult

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LossLocationFragment.
         */
        var fragmentLossLocationBinding: FragmentLossLocationBinding? = null
        private var claimResult: ClaimResult? = null
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            LossLocationFragment().apply {
                arguments = bundle
            }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLossLocationListener) {
            onlosslocationlistener = context as OnLossLocationListener
        } else {
            throw ClassCastException(
                "$context must implement OnGetItemListener"
            )
        }

    }

    public interface OnLossLocationListener {
        fun requestforclaimforlosslocation(fragment: LossLocationFragment)
        fun requestaddressfromclaim(fragment: LossLocationFragment)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val claimDetailActivity = activity as ClaimDetailActivity
        try {

            val markerOptions =
                MarkerOptions().position(claimDetailActivity.getmylatlngforlosslocation())
                    .title("I am here!")
            googleMap?.animateCamera(CameraUpdateFactory.newLatLng(claimDetailActivity.getmylatlngforlosslocation()))
            googleMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    claimDetailActivity.getmylatlngforlosslocation(),
                    19f
                )
            )
            googleMap?.addMarker(markerOptions)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "${googleMap}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

}