package com.crawford.ciq.dev.ui.home.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.ViewModelFactory
import com.crawford.ciq.dev.adapters.ClaimantExpandablelistAdapter
import com.crawford.ciq.dev.adapters.MyAdapter
import com.crawford.ciq.dev.adapters.MyBaseAdapter
import com.crawford.ciq.dev.adapters.MyDocketAdapter
import com.crawford.ciq.dev.api.ApiHelper
import com.crawford.ciq.dev.api.RetrofitBuilder
import com.crawford.ciq.dev.model.ClaimResult
import com.crawford.ciq.dev.model.ClaimantResult
import com.crawford.ciq.dev.model.DocketResult
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.ui.claimdetail.ClaimDetailActivity
import com.crawford.ciq.dev.ui.home.HomeActivity
import com.crawford.ciq.dev.ui.home.HomeViewModel
import com.crawford.ciq.dev.utils.AddresstoLatlong
import com.crawford.ciq.dev.utils.RecyclerTouchListener
import com.crawford.ciq.dev.utils.RecyclerTouchListener.OnRowClickListener
import com.crawford.ciq.dev.utils.Status.*
import com.crawford.ciq.dev.utils.Utilize
import kotlinx.android.synthetic.main.dialog_claimaintlist.*
import kotlinx.android.synthetic.main.fragment_claimlist.view.*
import kotlinx.coroutines.*
import org.jetbrains.anko.support.v4.act
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClaimListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClaimListFragment : Fragment(),
    RadioGroup.OnCheckedChangeListener, MyAdapter.OnDocketwithClaimListener,
    CoroutineScope by MainScope() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var getclaimlist: ArrayList<ClaimResult>? = null
    private var getclaimantlist: ArrayList<ClaimantResult>? = ArrayList()
    private lateinit var touchListener: RecyclerTouchListener
    private var mlocation: Location? = null
    val AdjusterID: String = "DRMONT"
    private lateinit var viewModel: HomeViewModel
    var mView: View? = null
    var rotatecount = 1

    private var onGetItemListener: OnGetItemListener? = null

    lateinit var myadapter: MyAdapter


    private var loading = true
    var pastVisiblesItems = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    var mclaimlist: ArrayList<MyClaimResult> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setUpViewModel()

        val homeActivity = activity as HomeActivity
        homeActivity.setUpObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGetItemListener) {
            onGetItemListener = context as OnGetItemListener
        } else {
            throw ClassCastException(
                "$context must implement OnGetItemListener"
            )
        }
    }

    fun getcliamlist(claimlist: List<MyClaimResult>) {

        retrieveList(ArrayList(claimlist), myadapter)
        mView!!.rcv_claimlist.visibility = View.VISIBLE
        mView!!.empty_view.visibility = View.GONE

        onGetItemListener!!.ongetitemcount(claimlist.size)
    }


    fun setupIcons() {

        // for radio buttons

        mView!!.fancy_radio_group.setOnCheckedChangeListener(this)


        mView!!.iv_assist.setOnClickListener {
            if (mView!!.iv_assist.tag.equals(resources.getString(R.string.outline))) {
                mView!!.iv_assist.setImageDrawable(resources.getDrawable(R.drawable.assist_filled))
                mView!!.iv_assist.tag = resources.getString(R.string.filled)
                myadapter.filter.filter("assist")
            } else {
                mView!!.iv_assist.setImageDrawable(resources.getDrawable(R.drawable.assist_outline))
                mView!!.iv_assist.tag = resources.getString(R.string.outline)
                myadapter.filter.filter("")
            }
        }

        mView!!.iv_alert.setOnClickListener {
            if (mView!!.iv_alert.tag.equals(resources.getString(R.string.outline))) {
                mView!!.iv_alert.setImageDrawable(resources.getDrawable(R.drawable.alert_filled))
                mView!!.iv_alert.tag = resources.getString(R.string.filled)
                myadapter.filter.filter("alert")
            } else {
                mView!!.iv_alert.setImageDrawable(resources.getDrawable(R.drawable.alert_outline))
                mView!!.iv_alert.tag = resources.getString(R.string.outline)
                myadapter.filter.filter("")
            }
        }

        mView!!.iv_flash.setOnClickListener {
            if (mView!!.iv_flash.tag.equals(resources.getString(R.string.outline))) {
                mView!!.iv_flash.setImageDrawable(resources.getDrawable(R.drawable.flash_filled))
                mView!!.iv_flash.tag = resources.getString(R.string.filled)
                myadapter.filter.filter("CAT")
            } else {
                mView!!.iv_flash.setImageDrawable(resources.getDrawable(R.drawable.flash_outline))
                mView!!.iv_flash.tag = resources.getString(R.string.outline)
                myadapter.filter.filter("")
            }
        }

        mView!!.iv_date.setOnClickListener {
            if (mView!!.iv_date.tag.equals(resources.getString(R.string.outline))) {
                mView!!.iv_date.setImageDrawable(resources.getDrawable(R.drawable.calender2))
                mView!!.iv_date.tag = (resources.getString(R.string.filled))
            } else {
                mView!!.iv_date.setImageDrawable(resources.getDrawable(R.drawable.calender1))
                mView!!.iv_date.tag = (resources.getString(R.string.outline))
            }
        }

        mView!!.iv_distance.setOnClickListener {
            if (mView!!.iv_distance.tag.equals(resources.getString(R.string.outline))) {
                mView!!.iv_distance.setImageDrawable(resources.getDrawable(R.drawable.location2))
                mView!!.iv_distance.tag = (resources.getString(R.string.filled))
            } else {
                mView!!.iv_distance.setImageDrawable(resources.getDrawable(R.drawable.location1))
                mView!!.iv_distance.tag = (resources.getString(R.string.outline))
            }
        }

        mView!!.iv_refresh.setOnClickListener {
            mView!!.rad_fc.isChecked = false
            mView!!.rad_fv.isChecked = false
            mView!!.rad_fr.isChecked = false
            mView!!.rad_er.isChecked = false
            mView!!.iv_refresh.animate().rotation(rotatecount * 360.0f).start()
            rotatecount = rotatecount + 1
            //    myadapter.refresh()
            myadapter.filter.filter("")
            mView!!.iv_assist.setImageResource(R.drawable.assist_outline)
            mView!!.iv_alert.setImageResource(R.drawable.alert_outline)
            mView!!.iv_flash.setImageResource(R.drawable.flash_outline)
            mView!!.iv_assist.tag = resources.getString(R.string.outline)
            mView!!.iv_alert.tag = resources.getString(R.string.outline)
            mView!!.iv_flash.tag = resources.getString(R.string.outline)
        }


    }

    fun controlheader(status: Boolean) {
        if (status) {
            mView!!.ll_header.visibility = View.VISIBLE
        } else {
            mView!!.ll_header.visibility = View.GONE
        }
    }

    fun sendlocation(mloc: Location?) {
        mlocation = mloc
        myadapter.getlocation(mloc)
    }

    fun sortbydistance(sortdistance: Boolean) {
        myadapter.sortbydistance(sortdistance)
    }

    fun sortbydate(sorttype: Boolean) {
        myadapter.sortbydate(sorttype)
    }

    fun filterbysearch(searchtext: String) {
        myadapter.filter.filter(searchtext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_claimlist, container, false)
        val claimlist = ArrayList<MyClaimResult>()
        val claimantResult = ArrayList<ClaimantResult>()

        setUpRecyclerview(v, claimlist, claimantResult)
        mView = v
        setupIcons()
        return v

    }

    fun setUpViewModel() {
        viewModel =
            ViewModelProviders.of(
                activity!!,
                ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
            )
                .get(HomeViewModel::class.java)
    }


    fun setUpRecyclerview(
        view: View,
        items: ArrayList<MyClaimResult>,
        itemsclaimant: ArrayList<ClaimantResult>
    ) {

        val mLayoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL, false
        )
        view.rcv_claimlist.layoutManager = mLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            view.rcv_claimlist.getContext(),
            DividerItemDecoration.VERTICAL
        )
        view.rcv_claimlist.addItemDecoration(dividerItemDecoration)
        myadapter = MyAdapter(items, itemsclaimant, view.context)
        myadapter.setonListener(this)
        view.rcv_claimlist.adapter = myadapter
        (view.rcv_claimlist.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false


//        view.rcv_claimlist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy);
//                visibleItemCount = mLayoutManager.getChildCount()
//                totalItemCount = mLayoutManager.getItemCount()
//                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
//                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
//                if (loading && dy > 0) {
//                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
//                        loading = false
//                        Log.v("...", "Last Item Wow !")
//
//                        try {
//                            myadapter.addUsers(mclaimlist.take(totalItemCount + 5))
//                            loading = true
//                        } catch (e: Exception) {
//
//                        }
//
//                    }
//                }
//
//            }
//        })


        setUpTouchListener(view, items, itemsclaimant, myadapter)

    }

    fun setUpTouchListener(
        v: View,
        items: ArrayList<MyClaimResult>,
        itemsclaimant: ArrayList<ClaimantResult>,
        myadapter: MyAdapter
    ) {
        touchListener = RecyclerTouchListener(activity, v.rcv_claimlist)

        touchListener
            .setClickable(object : OnRowClickListener {
                override fun onRowClicked(position: Int) {

                    val intent = Intent(activity, ClaimDetailActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable(
                        Utilize.CLAIMRESULT.name,
                        myadapter.claimlist.get(position)
                    )
                    intent.putExtras(bundle)
                    startActivity(intent)


                }

                override fun onIndependentViewClicked(
                    independentViewID: Int,
                    position: Int
                ) {
                }
            })
            .setSwipeOptionViews(R.id.delete_task, R.id.edit_task, R.id.message_task)
            .setSwipeable(
                R.id.rowFG, R.id.rowBG
            ) { viewID, position ->
                when (viewID) {
                    R.id.delete_task -> {
//                        items.drop(position)
//                        myadapter.setTaskList(items)
                        val url =
                            "http://maps.google.co.in/maps?q=${myadapter.claimlist.get(position).lossLocation}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                    R.id.edit_task -> {

                        val claimID = myadapter.claimlist.get(position).filenumber

                        showClaimantinforDialog(myadapter.claimlist.get(position), false)

                    }
                    R.id.message_task -> {

                        val claimID = myadapter.claimlist.get(position).filenumber


                        // showClaimantinforDialog(myadapter.claimlist.get(position), true)

                    }

                }
            }
        v.rcv_claimlist.addOnItemTouchListener(touchListener)
    }


    private fun retrieveList(claimlist: ArrayList<MyClaimResult>, myadapter: MyAdapter) {
        myadapter.apply {
            val iterate = claimlist.listIterator()
            while (iterate.hasNext()) {
                val it = iterate.next()
                val claimdate = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse(it.receiveDate)
                val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US)
                val currentDate = sdf.format(Date())
                val todaydate = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").parse(currentDate)
                val difference: Long = Math.abs(todaydate.getTime() - claimdate.getTime())
                val differenceDates = difference / (24 * 60 * 60 * 1000)
                val dayDifference = java.lang.Long.toString(differenceDates)
                if (myadapter.alertstatusondate(it, dayDifference.toLong())) {
                    it.alertstatus = "alert"
                } else {
                    it.alertstatus = ""
                }

                if (it.isAssist == true) {
                    it.claimstatus = "assist"
                } else {
                    it.claimstatus = ""
                }

                iterate.set(it)
            }
            mclaimlist.addAll(claimlist)
            // addUsers(mclaimlist.take(6))
            addUsers(mclaimlist)
            notifyDataSetChanged()
            Toast.makeText(activity,"We are calculating distance in background ,Please wait while we notify",Toast.LENGTH_LONG).show()


            launch {
                getdistanceofclaims()
            }
        }
    }



    private suspend fun getdistanceofclaims() {
        val getclaimlistwithdistance = getdistanceofclaimlist()
        withContext(Dispatchers.Main) {
            myadapter.apply {
                addUsers(getclaimlistwithdistance)
                notifyDataSetChanged()
                Toast.makeText(activity,"Distances are updated now , you can now sort claims by distance",Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun getdistanceofclaimlist(): ArrayList<MyClaimResult> {


        return withContext(Dispatchers.Default) {
            mclaimlist.forEach {

                try{
                    val addresstoLatlong = AddresstoLatlong(activity!!)
                    val latlong =
                        addresstoLatlong.getLocationFromAddress(it.lossLocation)
                    val distance = addresstoLatlong.getdistance(
                        mlocation!!.latitude,
                        mlocation!!.longitude,
                        latlong!!.latitude,
                        latlong!!.longitude
                    )

                    it.distance = distance
                }catch (e:Exception){

                }

            }

            return@withContext mclaimlist
        }


    }

//    private fun retrieveDocketlist(
//        dcktlist: List<DocketResult>,
//        myDocketAdapter: MyDocketAdapter, getadapterpsotion: Int
//    ) {
//
//        val docketlist = ArrayList<DocketResult>()
//        docketlist.addAll(dcktlist)
//        myDocketAdapter.apply {
//            val fcdocket = docketlist?.find { it.timeCode.equals("FC") }?.apply {
//                status = true
//                priority = 1
//            }
//            if (fcdocket == null) {
//                docketlist.add(DocketResult("FC", "", false, 1))
//            }
//            val fvdocket = docketlist?.find { it.timeCode.equals("FV") }?.apply {
//                status = true
//                priority = 2
//            }
//            if (fvdocket == null) {
//                docketlist.add(DocketResult("FV", "", false, 2))
//            }
//            val frdocket = docketlist?.find { it.timeCode.equals("FR") }?.apply {
//                status = true
//                priority = 3
//            }
//            if (frdocket == null) {
//                docketlist.add(DocketResult("FR", "", false, 2))
//            }
//            val erdocket = docketlist?.find { it.timeCode.equals("ER") }?.apply {
//                status = true
//                priority = 4
//            }
//            if (erdocket == null) {
//                docketlist.add(DocketResult("ER", "", false, 3))
//            }
//            var options = docketlist.filter {
//
//                it.timeCode.equals("FC") || it.timeCode.equals("FV") || it.timeCode.equals("FR") || it.timeCode.equals(
//                    "ER"
//                )
//            }.sortedBy { it.priority }
//            addDockets(options)
//            //notifyItemChanged(getadapterpsotion)
//            // notifyDataSetChanged()
//        }
//    }


    private fun retrieveclaimantlist(claimantlist: List<ClaimantResult>, getadapterpsotion: Int) {

        //   getclaimlist!!.get(getadapterpsotion).claimresult = claimantlist
        myadapter.apply {
            addclaimantlist(claimantlist)
            //   notifyDataSetChanged()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClaimListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    fun showClaimantinforDialog(myClaimResult: MyClaimResult, ismessagetype: Boolean) {
        val dialog = Dialog(activity!!, R.style.NewDialog)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_claimaintlist)
        val myBaseAdapter = MyBaseAdapter(activity!!)
        dialog.dialog_lv_claim.emptyView = dialog.dialog_empty_view
        dialog.expandableListView.emptyView = dialog.dialog_empty_view
        dialog.dialog_lv_claim.adapter = myBaseAdapter
        dialog.window!!.setDimAmount(0.5f)
        val window: Window = dialog.getWindow()!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        dialog.dialog_lv_claim.setOnItemClickListener { _, _, position, _ -> dialog.dismiss() }
        dialog.btn_cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = wlp

        viewModel.initContext(activity!!.applicationContext)
        val listcontacts = ArrayList<ClaimantResult>()

        //Expandable code
        val listData = HashMap<String, List<String>>()


        if (!myClaimResult.claimant1.isEmpty()) {
            val claimantcontacts = myClaimResult.claimant1Phone.split(",")
            //val contactlist = ArrayList<ClaimantResult.ContactsResult>()
            val contactlist = ArrayList<String>()
            claimantcontacts.forEach {
                //  val contactsResult = ClaimantResult.ContactsResult(it)
                contactlist.add(it)
            }
//            val claimantResult = ClaimantResult(contactlist, myClaimResult.claimant1)
//            listcontacts.add(claimantResult)

            //expandable code
            listData[myClaimResult.claimant1] = contactlist
        }

        if (!myClaimResult.claimant2.isEmpty()) {
            val claimantcontacts = myClaimResult.claimant2Phone.split(",")
            //  val contactlist = ArrayList<ClaimantResult.ContactsResult>()
            val contactlist = ArrayList<String>()
            claimantcontacts.forEach {
                // val contactsResult = ClaimantResult.ContactsResult(it)
                contactlist.add(it)
            }
//            val claimantResult = ClaimantResult(contactlist, myClaimResult.claimant2)
//            listcontacts.add(claimantResult)

            //expandable code
            listData[myClaimResult.claimant2] = contactlist
        }
        if (!myClaimResult.claimant3.isEmpty()) {
            val claimantcontacts = myClaimResult.claimant3Phone.split(",")
//            val contactlist = ArrayList<ClaimantResult.ContactsResult>()
            val contactlist = ArrayList<String>()
            claimantcontacts.forEach {

                contactlist.add(it)
            }
//            val claimantResult = ClaimantResult(contactlist, myClaimResult.claimant3)
//            listcontacts.add(claimantResult)
            //expandable code
            listData[myClaimResult.claimant3] = contactlist
        }
        if (!myClaimResult.claimant4.isEmpty()) {
            val claimantcontacts = myClaimResult.claimant4Phone.split(",")
            // val contactlist = ArrayList<ClaimantResult.ContactsResult>()
            val contactlist = ArrayList<String>()
            claimantcontacts.forEach {
//                val contactsResult = ClaimantResult.ContactsResult(it)
                contactlist.add(it)
            }
//            val claimantResult = ClaimantResult(contactlist, myClaimResult.claimant4)
//            listcontacts.add(claimantResult)
            //expandable code
            listData[myClaimResult.claimant4] = contactlist
        }
        if (!myClaimResult.claimant5.isEmpty()) {
            val claimantcontacts = myClaimResult.claimant5Phone.split(",")
            //val contactlist = ArrayList<ClaimantResult.ContactsResult>()
            val contactlist = ArrayList<String>()
            claimantcontacts.forEach {
//                val contactsResult = ClaimantResult.ContactsResult(it)
                contactlist.add(it)
            }
//            val claimantResult = ClaimantResult(contactlist, myClaimResult.claimant5)
//            listcontacts.add(claimantResult)
            //expandable code
            listData[myClaimResult.claimant5] = contactlist
        }
        myBaseAdapter.addItems(listcontacts)

        var expandablelistAdapter = ClaimantExpandablelistAdapter(
            activity!!.applicationContext,
            ArrayList(listData.keys) as ArrayList<String>,
            listData, ismessagetype
        )
        dialog.expandableListView!!.setAdapter(expandablelistAdapter)
        dialog.expandableListView!!.setOnGroupExpandListener { groupPosition ->

        }

        dialog.expandableListView!!.setOnGroupCollapseListener { groupPosition ->

        }

        dialog.expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            var getnumber =
                listData[(ArrayList(listData.keys) as ArrayList<String>)[groupPosition]]!!.get(
                    childPosition
                ).split(":")


            if (ismessagetype) {
//                val sms_uri = Uri.parse("smsto:+1${getnumber[1]}")
//                val sms_intent = Intent(Intent.ACTION_SENDTO, sms_uri)
//                sms_intent.putExtra("sms_body", "Type here")
//                startActivity(sms_intent)

                val smsIntent = Intent(Intent.ACTION_SENDTO)
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT)
                smsIntent.type = "vnd.android-dir/mms-sms"
                smsIntent.data = Uri.parse("sms:${getnumber[1]}")
                startActivity(smsIntent)
            } else {
                val u = Uri.parse("tel:" + getnumber[1])

                val i = Intent(Intent.ACTION_DIAL, u)

                try {
                    startActivity(i)
                } catch (s: SecurityException) {


                }
            }

            false
        }

    }


    public interface OnGetItemListener {

        fun ongetclaimlist()
        fun ongetitemcount(itemcount: Int)
    }

    @SuppressLint("ResourceType")
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val radio: RadioButton = mView!!.findViewById(checkedId)
        myadapter.filter.filter("")

        if (null != radio && checkedId > -1) {
            if (radio.text.equals("FC")) {
                myadapter.filter.filter("")
                myadapter.filter.filter("FC")
            }
            if (radio.text.equals("FV")) {
                myadapter.filter.filter("")
                myadapter.filter.filter("FV")
            }
            if (radio.text.equals("FR")) {
                myadapter.filter.filter("")
                myadapter.filter.filter("FR")
            }
            if (radio.text.equals("ER")) {
                myadapter.filter.filter("")
                myadapter.filter.filter("ER")
            }
        }
    }

    override fun updatedockets(
        claimResult: MyClaimResult,
        myDocketAdapter: MyDocketAdapter,
        rcv_dockets: RecyclerView,
        empty_view: TextView
    ) {
        rcv_dockets.visibility = View.VISIBLE
        empty_view.visibility = View.GONE
        (rcv_dockets.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        myDocketAdapter.apply {
            val docketlist = ArrayList<DocketResult>()
            var fcdocket: DocketResult? = null
            var fvdocket: DocketResult? = null
            var frdocket: DocketResult? = null
            var erdocket: DocketResult? = null
            if (!claimResult.fcCreatedDate.isEmpty()) {
                fcdocket = DocketResult("FC", "", true, 1)
            } else {
                fcdocket = DocketResult("FC", "", false, 1)
            }
            if (!claimResult.fvCreatedDate.isEmpty()) {
                fvdocket = DocketResult("FV", "", true, 2)
            } else {
                fvdocket = DocketResult("FV", "", false, 2)
            }
            if (!claimResult.frCreatedDate.isEmpty()) {
                frdocket = DocketResult("FR", "", true, 3)
            } else {
                frdocket = DocketResult("FR", "", false, 3)
            }
            if (!claimResult.erCreatedDate.isEmpty()) {
                erdocket = DocketResult("ER", "", true, 4)
            } else {
                erdocket = DocketResult("ER", "", false, 4)
            }
            docketlist.add(fcdocket)
            docketlist.add(fvdocket)
            docketlist.add(frdocket)
            docketlist.add(erdocket)
            val options = docketlist.sortedBy { it.priority }
            addDockets(options)
        }
    }

    override fun getclaimlistcount(itemcount: Int) {
        onGetItemListener!!.ongetitemcount(itemcount)
    }


}