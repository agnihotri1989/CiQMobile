package com.crawford.ciq.dev.ui.home

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.ViewModelFactory
import com.crawford.ciq.dev.api.ApiHelper
import com.crawford.ciq.dev.api.RetrofitBuilder
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.ui.home.fragments.ClaimListFragment
import com.crawford.ciq.dev.ui.home.fragments.RecentFragment
import com.crawford.ciq.dev.utils.CiQSharedprefrences
import com.crawford.ciq.dev.utils.Status
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity(), ClaimListFragment.OnGetItemListener {

    lateinit var fragment: Fragment
    var status: Boolean = true
    var sortwith: Boolean = true
    var sortdistance: Boolean = true
    protected var mLastLocation: Location? = null
    private var mLatitudeLabel: String? = null
    private var mLongitudeLabel: String? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var viewModel: HomeViewModel
    lateinit var AdjusterID: String



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        CiQSharedprefrences.init(this)
        AdjusterID = CiQSharedprefrences.getAdjusterId!!
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper())
        setUpViewModel()
        setUpnavigationbar()
        setUpfragment(savedInstanceState)
        // setUpObservers()

        getLastLocation()

        iv_filter.setOnClickListener {
            var f = fragment
            if (f is ClaimListFragment) {
                f = fragment as ClaimListFragment
                f.controlheader(status)

                if (status) {
                    iv_filter.setImageDrawable(resources.getDrawable(R.drawable.filter_filled))
                } else {
                    iv_filter.setImageDrawable(resources.getDrawable(R.drawable.filter_outline))
                }

                status = !status

            }

        }
        edit_query.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var ff = fragment
                if (ff is ClaimListFragment) {
                    ff = fragment as ClaimListFragment
                    ff.filterbysearch(s.toString())
                }
            }
        })

        iv_sort.setOnClickListener { showsortDialog() }

    }

    fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
            )
                .get(HomeViewModel::class.java)
    }


    fun setUpObservers() {

        viewModel.initContext(this)
        viewModel.getmyClaims(AdjusterID.toUpperCase()).observe(this, Observer {
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
                        Toast.makeText(this@HomeActivity, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {

                    }

                }
            }
        })
    }


    private fun retrieveListtofragment(users: List<MyClaimResult>) {
        if (fragment is ClaimListFragment) {
            val claimfrag = fragment as ClaimListFragment
            claimfrag.getcliamlist(users)
        }
    }

    fun setUpnavigationbar() {
        setSupportActionBar(toolbar)
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun setUpfragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            fragment = ClaimListFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                .commit()

        }
    }


    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_myclaims -> {
                    fragment =
                        ClaimListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_recent -> {
                    fragment =
                        RecentFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_drafts -> {
                    fragment =
                        RecentFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_reports -> {
                    fragment =
                        RecentFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
//                R.id.navigation_threedots -> {
//                    fragment = RecentFragment()
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
//                        .commit()
//                    return@OnNavigationItemSelectedListener true
//                }
            }
            false
        }


    fun showsortDialog() {


        if (ll_datedistance.visibility == View.VISIBLE) {
            ll_datedistance.visibility = View.GONE
        } else {
            ll_datedistance.visibility = View.VISIBLE
        }


        rl_date.setOnClickListener {
            var ff = fragment
            if (ff is ClaimListFragment) {
                ff = fragment as ClaimListFragment

                ff.sortbydate(sortwith)
                sortwith = !sortwith
            }
            iv_datearrow.visibility = View.VISIBLE
            iv_distancearrow.visibility = View.INVISIBLE

            if (iv_datearrow.tag.equals("down")) {
                iv_datearrow.setImageDrawable(resources.getDrawable(R.drawable.up_arrow))
                iv_datearrow.tag = resources.getString(R.string.up)

            } else {
                iv_datearrow.setImageDrawable(resources.getDrawable(R.drawable.down_arrow))
                iv_datearrow.tag = resources.getString(R.string.down)
            }

            ll_datedistance.visibility = View.GONE
        }
        rl_distance.setOnClickListener {
            var f = fragment
            if (f is ClaimListFragment) {
                f = fragment as ClaimListFragment

                f.sortbydistance(sortdistance)
                sortdistance = !sortdistance
            }
            iv_datearrow.visibility = View.INVISIBLE
            iv_distancearrow.visibility = View.VISIBLE
            if (iv_distancearrow.tag.equals("down")) {

                iv_distancearrow.setImageDrawable(resources.getDrawable(R.drawable.up_arrow))
                iv_distancearrow.tag = resources.getString(R.string.up)

            } else {
                iv_distancearrow.setImageDrawable(resources.getDrawable(R.drawable.down_arrow))
                iv_distancearrow.tag = resources.getString(R.string.down)
            }
            ll_datedistance.visibility = View.GONE
        }

    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        val locale: String =
            getResources().getConfiguration().locale.getDisplayCountry()
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result
                    var f = fragment
                    if (f is ClaimListFragment) {
                        f = fragment as ClaimListFragment
                        f.sendlocation(mLastLocation)
//                        if (arrayListOf<String>("USA,U.S., US,United States,Usa,usa").contains(locale.toLowerCase())) {
//                            f.sendlocation(mLastLocation)
//                        } else {
//                            val mloc = Location("")
//
//                            mloc.latitude = 40.6971494
//                            mloc.longitude = -74.2598661
//                            mLastLocation = mloc
//                            f.sendlocation(mLastLocation)
//                        }

                    }
                } else {

                }
            }

    }

    override fun ongetclaimlist() {
        setUpObservers()
    }

    override fun ongetitemcount(itemcount: Int) {
        tv_totalcount.setText("Total: ${itemcount}")
    }


}