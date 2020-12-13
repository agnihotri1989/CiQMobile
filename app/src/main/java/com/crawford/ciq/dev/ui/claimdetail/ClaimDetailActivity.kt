package com.crawford.ciq.dev.ui.claimdetail

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.ViewModelFactory
import com.crawford.ciq.dev.adapters.ClaimantRecyclerAdapter
import com.crawford.ciq.dev.adapters.CustomPagerAdapter
import com.crawford.ciq.dev.adapters.DocketListAdapter
import com.crawford.ciq.dev.adapters.PayLoadRecyclerAdapter
import com.crawford.ciq.dev.api.ApiHelper
import com.crawford.ciq.dev.api.RetrofitBuilder
import com.crawford.ciq.dev.interfaces.IOnBackPressed
import com.crawford.ciq.dev.model.*
import com.crawford.ciq.dev.ui.BaseActivity
import com.crawford.ciq.dev.ui.claimdetail.general.*
import com.crawford.ciq.dev.ui.claimdetail.docket.DocketFragment
import com.crawford.ciq.dev.ui.home.HomeActivity
import com.crawford.ciq.dev.utils.AddresstoLatlong
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.crawford.ciq.dev.utils.Status
import com.crawford.ciq.dev.utils.Utilize
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_claim_detail.*
import kotlinx.android.synthetic.main.fragment_claimdetail.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClaimDetailActivity : BaseActivity(), ClaimInfoFragment.onClaimInfoListener,
    ClientSubscriberFragment.onClientSubscriberListener,
    ClaimantFragment.onClaimantListener,
    InsuredFragment.OnInsuredListener,
    DocketFragment.OnDocketListener,
    LossLocationFragment.OnLossLocationListener {

    lateinit var fragment: Fragment
    private lateinit var viewModel: ClaimDetailViewModel
    lateinit var AdjusterID: String
    var claimID: Int = 0
    private var myLatlngforlosslocation: LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim_detail)

        val myclaim = intent.extras!!.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult

        GlobalScope.launch {
            myLatlngforlosslocation =
                AddresstoLatlong(this@ClaimDetailActivity).getLocationFromAddress(myclaim!!.lossLocation)
        }
        CiQSharedprefrences.init(this)

        AdjusterID =
            if (myclaim.isAssist) myclaim.leadAdjuster else CiQSharedprefrences.getAdjusterId!!
        setUpfragment(savedInstanceState)
        setUpViewModel()
        //  setUpObservers()

        iv_navigate_back.setOnClickListener {
            onBackPressed()
        }
        iv_home.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

    }

    fun getseraializedata(): Bundle {
        val myclaim = intent.extras!!.getSerializable(Utilize.CLAIMRESULT.name) as MyClaimResult
        claimID = myclaim.filenumber
        return intent.extras!!

//        val fragment = ClaimDetailFragment.newInstance(
//            myadapter.claimlist.get(position).filenumber,
//            myadapter.claimlist.get(position).isAssist
//        )
//
//        homeActivity.supportFragmentManager.beginTransaction()
//            .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
//            .commit()
    }

    fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
            )
                .get(ClaimDetailViewModel::class.java)
        viewModel.initContext(this, claimID)
    }


    private fun retrieveListtofragment(users: List<ClaimResult>) {
        if (fragment is ClaimDetailFragment) {
            val claimfrag = fragment as ClaimDetailFragment
            claimfrag.getcliamlist(users[0])
        }


    }

    private fun retrieveclaimtolosslocationfragment(
        users: List<ClaimResult>,
        fragment: LossLocationFragment
    ) {
        if (fragment is LossLocationFragment) {

            fragment.getcliamforlosslocation(users[0])
        }


    }

    private fun retrieveListtoDocketfragment(users: List<ClaimResult>) {

        val claimfrag =
            DocketFragment()
        claimfrag.getcliamlist(users[0])


    }

    private fun retrieveclaimtoInsuredfragment(
        users: List<ClaimResult>,
        fragment: InsuredFragment
    ) {


        fragment.getclaim(users[0])


    }

    private fun retrieveinfotoClaimInfofragmentforpolicy(users: List<ClaimSubscription>) {
        if (fragment is ClaimDetailFragment) {
            val claimfrag = fragment as ClaimDetailFragment
            claimfrag.forpolicyinfo(users[0])
        }
    }

    private fun retrieveListtoClaimSubscriptionfragment(users: List<ClaimSubscription>) {
        if (fragment is ClaimDetailFragment) {
            val claimfrag = fragment as ClaimDetailFragment
            claimfrag.getclientsubscriber(users[0])
        }
    }

    private fun retrieveListtoInsuredfragment(users: List<InsuredAddressApiResult>) {
        if (fragment is ClaimDetailFragment) {
            val claimfrag = fragment as ClaimDetailFragment
            claimfrag.getinsured(users[0])
        }
    }

    private fun retrieveListtoLossLocationfragment(
        users: List<AddressApiResult>,
        fragment: LossLocationFragment
    ) {
        if (fragment is LossLocationFragment) {
            val claimfrag = fragment as LossLocationFragment
            claimfrag.retriveaddressfrominsured(users[0])
        }
    }

    private fun retrieveListtoClaiminfofragment(
        users: List<AddressApiResult>,
        fragment: ClaimInfoFragment
    ) {
        if (fragment is ClaimInfoFragment) {
            val claimfrag = fragment as ClaimInfoFragment
            claimfrag.retriveaddressfrominsured(users[0])
        }
    }

    private fun retrieveListtoClaimantfragment(
        users: List<AddressApiResult>,
        claimantRecyclerAdapter: ClaimantRecyclerAdapter, fragment: ClaimantFragment, positon: Int
    ) {
        if (fragment is ClaimantFragment) {
            val claimfrag = fragment as ClaimantFragment
            claimfrag.retriveclaimantaddressfromaddressapi(
                users[0],
                claimantRecyclerAdapter,
                positon
            )
        }
    }

    private fun retrieveListtoDocketfragment(
        listdocketapiresult: List<DocketApiResult>,
        docketListAdapter: DocketListAdapter
    ) {
        val claimfrag =
            DocketFragment()
        claimfrag.retrivedocketresult(ArrayList(listdocketapiresult), docketListAdapter)
    }

    private fun retrieveNoteDetailtoDocketfragment(
        listnoteapiresult: List<NoteApiResult>,
        docketListAdapter: DocketListAdapter, positon: Int
    ) {
        val claimfrag =
            DocketFragment()
        claimfrag.updatedocketwithnote(ArrayList(listnoteapiresult), docketListAdapter, positon)
    }

    private fun retrievepostdocketresponseDocketfragment(
        listdocketapiresult: PostDocketApiResult
    ) {

        Toast.makeText(this@ClaimDetailActivity, "Docket Submitted Successfully", Toast.LENGTH_LONG)
            .show()
        val claimfrag =
            DocketFragment()
        claimfrag.dismissDocketDialog()
        //claimfrag.retrivedocketresult(ArrayList(listdocketapiresult))
    }

    private fun retrievetimecodedetaillisttoDocketfragment(
        listtimecodedetailist: TimeCodeDetailResult, payLoadRecyclerAdapter: PayLoadRecyclerAdapter
    ) {
        val claimfrag =
            DocketFragment()
        claimfrag.retrivetimecoderesult(listtimecodedetailist, payLoadRecyclerAdapter)
    }

    private fun retrieveListtoClaimantfragment(users: List<ClaimantApiResult>) {
        if (fragment is ClaimDetailFragment) {
            val claimfrag = fragment as ClaimDetailFragment
            claimfrag.getclaimant(users)
        }
    }

    private fun retrieveClaimasubscriptiontoDocketfragment(users: List<ClaimSubscription>) {

        val claimfrag =
            DocketFragment()
        claimfrag.getclaimsubscription(users)

    }

    private fun retrieveClaimanttoDocketfragment(users: List<ClaimantApiResult>) {

        val claimfrag =
            DocketFragment()
        claimfrag.getclaimant(users)

    }

    fun setUpfragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragment = ClaimDetailFragment.newInstance(getseraializedata())
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()

        }
    }


    override fun requestforclaimlist() {

        viewModel.getClaims(AdjusterID, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveListtofragment(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestclaimsubsforpolicy() {

        viewModel.getClaimSubscription(AdjusterID, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveinfotoClaimInfofragmentforpolicy(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestaddressforclaiminfo(fragment: ClaimInfoFragment) {
        viewModel.getclaimandclaimantaddresslist(0, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrieveListtoClaiminfofragment(users, fragment)
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestforcleintsubscriber() {

        viewModel.getClaimSubscription(AdjusterID, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveListtoClaimSubscriptionfragment(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })

    }

    override fun requestforclaimant() {

        viewModel.getclaimant(claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveListtoClaimantfragment(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun ongetaddressclaimant(
        claimantid: Int,
        claimantRecyclerAdapter: ClaimantRecyclerAdapter, fragment: ClaimantFragment, positon: Int
    ) {
        viewModel.getclaimandclaimantaddresslist(claimantid, 0).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrieveListtoClaimantfragment(
                                        users,
                                        claimantRecyclerAdapter,
                                        fragment,
                                        positon
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }


    override fun requestforInsured() {


        viewModel.getinsuredaddress(claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveListtoInsuredfragment(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestforclaim(fragment: InsuredFragment) {
        viewModel.getClaims(AdjusterID, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrieveclaimtoInsuredfragment(
                                        users,
                                        fragment
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestfordockets(docketListAdapter: DocketListAdapter, statusoflist: Boolean) {

        viewModel.getDockets(claimID, statusoflist).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrieveListtoDocketfragment(
                                        users,
                                        docketListAdapter
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestfornote(
        ClaimId: Int,
        DocketId: Int,
        docketListAdapter: DocketListAdapter,
        positon: Int
    ) {
        viewModel.getnote(ClaimId, DocketId).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrieveNoteDetailtoDocketfragment(
                                        users,
                                        docketListAdapter, positon
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun postjsonbodytodocket(jsonbody: JsonObject) {

        val mprogressdailog = ProgressDialog(this@ClaimDetailActivity)
        mprogressdailog.setTitle("Sending")
        mprogressdailog.setMessage("Please wait for  a while")
        mprogressdailog.show()
        viewModel.getpostdocketresult(jsonbody).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        mprogressdailog.dismiss()
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrievepostdocketresponseDocketfragment(
                                        users
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        mprogressdailog.dismiss()
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestforclaimdata() {
        viewModel.getClaims(AdjusterID, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveListtoDocketfragment(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestforclaimsubscription() {
        viewModel.getClaimSubscription(AdjusterID, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveClaimasubscriptiontoDocketfragment(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestforclaimantdata() {
        viewModel.getclaimant(claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users -> retrieveClaimanttoDocketfragment(users) })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun gettimecode(): ArrayList<TimeCodeResult> {
        var retrivietimecode = ArrayList<TimeCodeResult>()

        viewModel.returntimecode().observe(this, Observer {
            it?.let {
                retrivietimecode = ArrayList(it)
            }
        })

//        viewModel.gettimecodelist().observe(this, Observer {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        resource.data?.let { users ->
//                            users.observe(
//                                this,
//                                Observer { users ->
//                                    retrivietimecode = ArrayList(users)
//                                })
//
//                        }
//                    }
//                    Status.ERROR -> {
//                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
//                            .show()
//                    }
//                    Status.LOADING -> {
//
//                    }
//                }
//            }
//        })
        return retrivietimecode
    }

    override fun getexpensecode(): ArrayList<ExpenseCodeResult> {
        var retrivieexpensecode = ArrayList<ExpenseCodeResult>()
        viewModel.returnexpensecode().observe(this, Observer {
            it?.let {
                retrivieexpensecode = ArrayList(it)
            }
        })
//        viewModel.getexpensecodelist().observe(this, Observer {
//            it?.let { resource ->
//                when (resource.status) {
//                    Status.SUCCESS -> {
//                        resource.data?.let { users ->
//                            users.observe(
//                                this,
//                                Observer { users ->
//                                    retrivieexpensecode = ArrayList(users)
//                                })
//
//                        }
//                    }
//                    Status.ERROR -> {
//                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
//                            .show()
//                    }
//                    Status.LOADING -> {
//
//                    }
//                }
//            }
//        })
        return retrivieexpensecode
    }

    override fun gettimecodedetails(code: String, payLoadRecyclerAdapter: PayLoadRecyclerAdapter) {
        viewModel.gettimecodedetaillist(code).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrievetimecodedetaillisttoDocketfragment(
                                        users,
                                        payLoadRecyclerAdapter
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    public fun getmylatlngforlosslocation(): LatLng {
        return myLatlngforlosslocation!!
    }

    override fun requestforclaimforlosslocation(fragment: LossLocationFragment) {
        viewModel.getClaims(AdjusterID, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrieveclaimtolosslocationfragment(
                                        users,
                                        fragment
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun requestaddressfromclaim(fragment: LossLocationFragment) {
        viewModel.getclaimandclaimantaddresslist(0, claimID).observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { users ->
                            users.observe(
                                this,
                                Observer { users ->
                                    retrieveListtoLossLocationfragment(
                                        users,
                                        fragment
                                    )
                                })

                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(this@ClaimDetailActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    Status.LOADING -> {

                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.container)
        val clmdetailfragment = fragment as ClaimDetailFragment
        val adapter = clmdetailfragment.mView.viewPager.adapter as CustomPagerAdapter
        val activityFragment =adapter.getItem(clmdetailfragment.mView.viewPager.currentItem)
            (activityFragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
                super.onBackPressed()
            }
    }
}