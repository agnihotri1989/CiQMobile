package com.crawford.ciq.dev.ui.claimdetail.docket

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.adapters.AutocompleteAdapter
import com.crawford.ciq.dev.adapters.DocketListAdapter
import com.crawford.ciq.dev.adapters.ExpenseCodeAdapter
import com.crawford.ciq.dev.adapters.PayLoadRecyclerAdapter
import com.crawford.ciq.dev.databinding.DialogDocketdetailviewBinding
import com.crawford.ciq.dev.databinding.FragmentDocketBinding
import com.crawford.ciq.dev.databindingmodel.DocketUtils
import com.crawford.ciq.dev.model.*
import com.crawford.ciq.dev.utils.DatePickerUtils
import com.crawford.ciq.dev.utils.TimePickerUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dialog_add_docket.*
import kotlinx.android.synthetic.main.dialog_add_docket.tv_docketlossdate
import kotlinx.android.synthetic.main.dialog_docketdetailview.*
import kotlinx.android.synthetic.main.fragment_docket.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DocketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocketFragment : Fragment(), DocketListAdapter.onItemClickListner {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDocketBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_docket, container, false)
        val view = fragmentDocketBinding!!.root
        mView = view
        view.btn_adddocket.setOnClickListener {
            if (mclaimresult != null && mclaimantResult != null) showDialogaddDocket(
                mclaimresult!!,
                mclaimantResult!!
            ) else Toast.makeText(
                requireContext(),
                "Please wait we are updating",
                Toast.LENGTH_LONG
            ).show()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val docketApiResult = ArrayList<DocketApiResult>()
        setUpRecyclerView(mView!!, docketApiResult)
    }


    fun setUpRecyclerView(view: View, docketApiResult: List<DocketApiResult>) {
        val mLayoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL, false
        )
        view.rcv_docketlist.layoutManager = mLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            view.rcv_docketlist.getContext(),
            DividerItemDecoration.VERTICAL
        )
        view.rcv_docketlist.addItemDecoration(dividerItemDecoration)
        docketListAdapter =
            DocketListAdapter(view.context, ArrayList(docketApiResult), view.rcv_docketlist)
        view.rcv_docketlist.adapter =
            docketListAdapter
        (view.rcv_docketlist.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        ondocketlistener!!.requestfordockets(
            docketListAdapter, false)
        ondocketlistener!!.requestforclaimdata()
        ondocketlistener!!.requestforclaimsubscription()
        ondocketlistener!!.requestforclaimantdata()
        docketListAdapter.setonClickListner(this)
    }


    fun updatedocketwithnote(
        listnoteApiResult: ArrayList<NoteApiResult>,
        docketListAdapter: DocketListAdapter, position: Int
    ) {
        docketListAdapter.apply {
            updatenoteindocket(listnoteApiResult)
            notifyItemChanged(position)
        }

    }

    fun retrivedocketresult(
        listdocketApiResult: ArrayList<DocketApiResult>,
        docketListAdapter: DocketListAdapter
    ) {

        listdocketApiResult.sortByDescending { it.createdDate }
        docketListAdapter.apply {

            addDockets(listdocketApiResult)
            notifyDataSetChanged()
        }
        docketapiresult = listdocketApiResult

        val docketApiResult = listdocketApiResult.get(0)
        mDocketApiResult = docketApiResult

        val totaltimeunits = listdocketApiResult?.sumByDouble {
            it.timeUnits
        }
        val totaltimeamount = listdocketApiResult?.sumByDouble {
            it.timeAmount
        }

        val totalexpenseamount = listdocketApiResult?.sumByDouble {
            it.expenseAmount
        }


        val docketutils = DocketUtils(
            listdocketApiResult.size,
            totaltimeunits,
            totaltimeamount,
            totalexpenseamount
        )
        fragmentDocketBinding!!.docketutils = docketutils


    }

    fun getcliamlist(claimresult: ClaimResult) {
        mclaimresult = claimresult
        val mclre =
            mclaimresult
    }

    fun getclaimant(claimantResult: List<ClaimantApiResult>) {
        mclaimantResult = claimantResult
        val mclre =
            mclaimantResult
        //     fragmentClaimantBinding!!.claimant = claimantResult
        //claimantrecycleradapter!!.addClaiamant(ArrayList(claimantResult))

    }

    fun getclaimsubscription(claimantResult: List<ClaimSubscription>) {
        mclaimSubscriptiontResult = claimantResult
        val mclre =
            mclaimSubscriptiontResult
        //     fragmentClaimantBinding!!.claimant = claimantResult
        //claimantrecycleradapter!!.addClaiamant(ArrayList(claimantResult))

    }

    fun dismissDocketDialog() {
        mDialog!!.dismiss()
        ondocketlistener!!.requestfordockets(
            docketListAdapter, true)
    }

    fun retrivetimecoderesult(
        listtimecodeApiResult: TimeCodeDetailResult,
        payLoadRecyclerAdapter: PayLoadRecyclerAdapter
    ) {

        if (listtimecodeApiResult.templatePayload != null) {
            listtimecodeApiResult.templatePayload.templateFields?.find {
                it.templateFieldValue != null
            }?.viewtype = 1


            payLoadRecyclerAdapter.apply {
                addpayload(listtimecodeApiResult.templatePayload.templateFields)
                notifyDataSetChanged()
            }
        } else {
            payLoadRecyclerAdapter.apply {
                clear()
                notifyDataSetChanged()
            }
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDocketListener) {
            ondocketlistener = context as OnDocketListener
        } else {
            throw ClassCastException(
                "$context must implement OnGetItemListener"
            )
        }
    }


    companion object {
        var fragmentDocketBinding: FragmentDocketBinding? = null
        private var docketapiresult: ArrayList<DocketApiResult>? = null
        private var mclaimresult: ClaimResult? = null
        private var mclaimantResult: List<ClaimantApiResult>? = null
        private var mclaimSubscriptiontResult: List<ClaimSubscription>? = null
        private var mDocketApiResult: DocketApiResult? = null

        private var mDialog: Dialog? = null
        private var ondocketlistener: OnDocketListener? = null

        lateinit var docketListAdapter: DocketListAdapter
        lateinit var payloadrecycleradapter: PayLoadRecyclerAdapter

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DocketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DocketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun showDocketDetailView(docketApiResult: DocketApiResult) {
        val dialog = Dialog(ContextThemeWrapper(activity!!, R.style.DialogAnimation))
        val dialogDocketdetailviewBinding: DialogDocketdetailviewBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.dialog_docketdetailview,
                null,
                false
            );
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(dialogDocketdetailviewBinding.root)
        dialogDocketdetailviewBinding.docketapiresult = docketApiResult
        dialogDocketdetailviewBinding.claimapi =
            mclaimresult
        dialogDocketdetailviewBinding.claimsubscription = mclaimSubscriptiontResult!!.get(0)
        val arrayAdapter = mclaimantResult?.let { it }?.let {
            ArrayAdapter<ClaimantApiResult>(
                requireContext(), R.layout.custom_list_item,
                it
            )
        }
        dialog.lv_docketclaimant.adapter = arrayAdapter
        if (docketApiResult.templatePayload != null) {
            if (docketApiResult.templatePayload.templateFields != null) {
                val arrayAdaptertemplatepayload =
                    docketApiResult.templatePayload.templateFields?.let { it }?.let {
                        ArrayAdapter<DocketApiResult.TemplateFieldsResult>(
                            requireContext(), R.layout.custom_list_item,
                            it
                        )
                    }
                dialog.lv_templayplayload.adapter = arrayAdaptertemplatepayload
            }
        }


//        dialogDocketdetailviewBinding.noteapiresult =


        dialog.iv_dialogclose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        val window: Window = dialog.getWindow()!!
        window.setGravity(Gravity.BOTTOM)


//        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
//        lp.copyFrom(dialog.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        lp.gravity = Gravity.BOTTOM
//        lp.windowAnimations = R.style.DialogAnimation
//        dialog.window!!.setGravity(Gravity.BOTTOM)
//        dialog.window!!.attributes = lp

        window.setBackgroundDrawableResource(android.R.color.transparent)
        val width = (resources.displayMetrics.widthPixels * 1.00).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
        window.setLayout(width, height)
    }

    fun showDialogaddDocket(mclaimresult: ClaimResult, mclaimantResult: List<ClaimantApiResult>) {


        var mClaimantId: Int? = 0

        val dialog = Dialog(ContextThemeWrapper(activity!!, R.style.DialogAnimation))
        mDialog = dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_add_docket)

        var gettimecoderesult = ondocketlistener!!.gettimecode()
        var getexpensecoderesult = ondocketlistener!!.getexpensecode()


        var aa = mclaimantResult?.let {
            ArrayAdapter(
                requireContext(), R.layout.custom_simple_spinner_item,
                it
            )
        }
        aa!!.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item)

        with(dialog.spinner_claimant)
        {
            adapter = aa
            setSelection(0, false)

            prompt = "Choose your option"
            gravity = android.view.Gravity.CENTER

        }
        mClaimantId = mclaimantResult!!.get(0).claimantId
        dialog.spinner_claimant.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mClaimantId = mclaimantResult!!.get(position).claimantId
                    Toast.makeText(
                        parent!!.context,
                        mclaimantResult!!.get(position).getfullname(), Toast.LENGTH_LONG
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        dialog.tv_docketinsuredname.text =
            mclaimresult!!.insuredName + mclaimresult!!.insuredFirstName
        dialog.tv_docketlossdate.text = mclaimresult!!.retieveformatteddate(mclaimresult!!.lossDate)
        if (dialog.autocomplete_timedocket != null) {


            val adapter = AutocompleteAdapter(
                requireContext(),
                android.R.layout.simple_expandable_list_item_1,
                gettimecoderesult
            )
            dialog.autocomplete_timedocket.setAdapter(adapter)
            dialog.autocomplete_timedocket.threshold = 1

            payloadrecycleradapter =
                PayLoadRecyclerAdapter(ArrayList<TimeCodeDetailResult.templateFieldsResult>())
            dialog.rcv_payload.adapter =
                payloadrecycleradapter




            dialog.tv_doccketlossdate.setOnClickListener {


                val formatter =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                formatter.isLenient = false


                val lossdate: Date = formatter.parse(mclaimresult!!.lossDate)
                DatePickerUtils(requireContext()).setdate(dialog.tv_doccketlossdate, lossdate)
            }
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val currenttime = SimpleDateFormat("HH:mm:ss a", Locale.US).format(Date())
            val currentDate = sdf.format(Date())
            dialog.tv_doccketlossdate.text = currentDate
            dialog.tv_docketlosstime.text = currenttime
            dialog.tv_docketlosstime.setOnClickListener {
                TimePickerUtils(requireContext()).settime(dialog.tv_docketlosstime)
            }


            dialog.autocomplete_timedocket.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    dialog.autocomplete_timedocket.showDropDown()
                        return false
                }

            })


            dialog.autocomplete_timedocket.setOnItemClickListener() { parent, view, position, id ->
                val selectedPoi = parent.adapter.getItem(position) as TimeCodeResult?
                dialog.autocomplete_timedocket.setText(selectedPoi?.code)

                val editext_description = dialog.ed_timecodedescription as EditText
                editext_description.setText(selectedPoi?.description)
                if (!dialog.ed_timeamount.text.toString()
                        .isEmpty()
                ) dialog.ed_timeamount.setText("")
                if (!dialog.ed_timeunits.text.toString().isEmpty()) dialog.ed_timeunits.setText("")
                ondocketlistener!!.gettimecodedetails(selectedPoi!!.code,
                    payloadrecycleradapter
                )

                if (!dialog.ed_timeamount.text.toString()
                        .isEmpty()
                ) dialog.ed_timeamount.setText("")
                if (!dialog.ed_timeunits.text.toString().isEmpty()) dialog.ed_timeunits.setText("")
                if (selectedPoi?.serviceFee) {
                    dialog.ed_timeunits.isEnabled = false
                    dialog.ed_timeamount.isEnabled = true
                } else {
                    dialog.ed_timeunits.isEnabled = true
                    dialog.ed_timeamount.isEnabled = false
                }

            }

        }
        if (dialog.autocomplete_docket != null) {

            val adapter = ExpenseCodeAdapter(
                requireContext(),
                android.R.layout.simple_expandable_list_item_1,
                getexpensecoderesult
            )
            dialog.autocomplete_docket.setAdapter(adapter)

            dialog.autocomplete_docket.threshold = 1

            dialog.autocomplete_docket.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    dialog.autocomplete_docket.showDropDown()
                    return false
                }

            })
            dialog.autocomplete_docket.setOnItemClickListener { parent, view, position, id ->
                val selectedPoi = parent.adapter.getItem(position) as ExpenseCodeResult?
                dialog.autocomplete_docket.setText(selectedPoi?.code)
                val editext_description = dialog.ed_description as EditText
                editext_description.setText(selectedPoi?.description)
                if (!dialog.ed_amount.text.toString().isEmpty()) dialog.ed_amount.setText("")
                if (!dialog.ed_units.text.toString().isEmpty()) dialog.ed_units.setText("")
                if (!dialog.ed_rateunits.text.toString().isEmpty()) dialog.ed_rateunits.setText("")
                if (selectedPoi!!.defaultUnitRate > 0) {
                    dialog.ed_rateunits.isEnabled = true
                    var rperuvalue = selectedPoi!!.defaultUnitRate.toString()
                    dialog.ed_rateunits.setText(rperuvalue)
                    dialog.ed_units.isEnabled = true
                    dialog.ed_amount.isEnabled = false

                    dialog.ed_units.addTextChangedListener(object : TextWatcher {

                        override fun afterTextChanged(s: Editable?) {

                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            if (s.toString().isEmpty()) {
                                dialog.ed_amount.setText(s.toString())
                            } else {
                                var units: Double = dialog.ed_units.text.toString().toDouble()
                                var rateperunit: Double =
                                    dialog.ed_rateunits.text.toString().toDouble()
                                val calculatedamount = units * rateperunit
                                dialog.ed_amount.setText(calculatedamount.toString())
                            }

                        }
                    })

                } else {
                    dialog.ed_rateunits.isEnabled = false
                    dialog.ed_units.isEnabled = false
                    dialog.ed_amount.isEnabled = true
                }

            }


        }
        dialog.btn_docketsubmit.setOnClickListener {
            if (!dialog.autocomplete_timedocket.text.toString()
                    .isEmpty() || !dialog.autocomplete_docket.text.toString().isEmpty()
            ) {


                var templatePayload: JsonObject? = null
                if (payloadrecycleradapter.itemCount == 0) {
                    templatePayload = null
                } else {


                    templatePayload = gettemplatejsonobject(
                        dialog.autocomplete_timedocket.text.toString(),
                        payloadrecycleradapter.listtemplatefielddata
                    )
                }


                val finaloutput = submitdocketjson(
                    mDocketApiResult!!.claimID,
                    "0${mDocketApiResult!!.branchID.toString()}",
                    "${dialog.tv_doccketlossdate.text.toString()} ${dialog.tv_docketlosstime.text.toString()}"
                    ,
                    mDocketApiResult!!.adjusterID,
                    dialog.autocomplete_timedocket.text.toString(),
                    mClaimantId!!,
                    mDocketApiResult!!.docketRate,
                    0,
                    if (dialog.ed_amount.text.toString()
                            .isEmpty()
                    ) 0.0 else dialog.ed_amount.text.toString().toDouble(),
                    dialog.autocomplete_docket.text.toString(),
                    dialog.chkbx_dialogemployereimbursable.isChecked,
                    if (dialog.ed_rateunits.text.toString()
                            .isEmpty()
                    ) 0.0 else dialog.ed_rateunits.text.toString().toDouble(),
                    if (dialog.ed_units.text.toString()
                            .isEmpty()
                    ) 0 else dialog.ed_units.text.toString().toInt(),
                    dialog.ed_longdescription.text.toString(),
                    if (dialog.ed_timeamount.text.toString()
                            .isEmpty()
                    ) 0 else dialog.ed_timeamount.text.toString().toInt(),
                    if (dialog.ed_timeunits.text.toString()
                            .isEmpty()
                    ) 0 else dialog.ed_timeunits.text.toString().toInt(),
                    templatePayload
                )

                ondocketlistener!!.postjsonbodytodocket(finaloutput)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please provide atleast one docket",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
        dialog.btn_docketcancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        val window: Window = dialog.getWindow()!!
        window.setGravity(Gravity.BOTTOM)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        val width = (resources.displayMetrics.widthPixels * 1.00).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.80).toInt()
        window.setLayout(width, height)


//        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
//        lp.copyFrom(dialog.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        lp.gravity = Gravity.BOTTOM
//        lp.windowAnimations = R.style.DialogAnimation
//        dialog.window!!.setGravity(Gravity.BOTTOM)
//        dialog.window!!.attributes = lp


    }

    override fun onItemClick(docketApiResult: DocketApiResult) {
        showDocketDetailView(docketApiResult)
    }

    override fun requestfornote(ClaimId: Int, DocketId: Int, position: Int) {
        ondocketlistener!!.requestfornote(ClaimId, DocketId,
            docketListAdapter, position)
    }

    interface OnDocketListener {
        fun requestfordockets(docketListAdapter: DocketListAdapter, statusoflist: Boolean)
        fun requestfornote(
            ClaimId: Int,
            DocketId: Int,
            docketListAdapter: DocketListAdapter,
            position: Int
        )

        fun postjsonbodytodocket(jsonbody: JsonObject)
        fun requestforclaimdata()
        fun requestforclaimsubscription()
        fun requestforclaimantdata()
        fun gettimecode(): List<TimeCodeResult>
        fun getexpensecode(): List<ExpenseCodeResult>
        fun gettimecodedetails(code: String, payLoadRecyclerAdapter: PayLoadRecyclerAdapter)


    }

    fun submitdocketjson(
        claimID: Int,
        branchID: String,
        date: String,
        adjusterID: String,
        timeCode: String,
        claimantId: Int,
        docketRate: Double,
        claimAssistID: Int,
        expenseAmount: Double,
        expenseCode: String,
        expenseEmployeeReimbursable: Boolean,
        expenseRatePerUnit: Double,
        expenseUnits: Int,
        note: String,
        timeAmount: Int,
        timeUnits: Int,
        templatejsonobject: JsonObject?

    ): JsonObject {
        val parentjsonobject = JsonObject()
        parentjsonobject.addProperty("claimID", claimID)
        parentjsonobject.addProperty("branchID", branchID)
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val parser = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a")

        val output: String = formatter.format(parser.parse(date))
        parentjsonobject.addProperty("date", output)
        parentjsonobject.addProperty("adjusterID", adjusterID)
        parentjsonobject.addProperty("timeCode", timeCode)
        parentjsonobject.addProperty("claimantId", claimantId)
        parentjsonobject.addProperty("docketRate", docketRate)
        parentjsonobject.addProperty("claimAssistID", claimAssistID)
        parentjsonobject.addProperty("expenseAmount", expenseAmount)
        parentjsonobject.addProperty("expenseCode", expenseCode)
        parentjsonobject.addProperty("expenseEmployeeReimbursable", expenseEmployeeReimbursable)
        parentjsonobject.addProperty("expenseRatePerUnit", expenseRatePerUnit)
        parentjsonobject.addProperty("expenseUnits", expenseUnits)
        parentjsonobject.addProperty("note", note)
        parentjsonobject.addProperty("timeAmount", timeAmount)
        parentjsonobject.addProperty("timeUnits", timeUnits)
        parentjsonobject.add("templatePayload", templatejsonobject)


        return parentjsonobject
    }

    fun gettemplatejsonobject(
        templateCode: String, listtemplatefielddata: List<TimeCodeDetailResult.templateFieldsResult>
    ): JsonObject {

        val parentjsonobejct = JsonObject()
        parentjsonobejct.addProperty("templateCode", templateCode)
        val parentjsonarray = JsonArray()

        listtemplatefielddata.forEach {
            val childjsonopbject = JsonObject()
            childjsonopbject.addProperty("templateFieldId", it.templateFieldId)
            childjsonopbject.addProperty("templateFieldName", it.templateFieldName)
            childjsonopbject.add(
                "templateFieldValue",
                Gson().toJsonTree(it.templateFieldValue).asJsonArray
            )
            childjsonopbject.addProperty("sequenceId", it.sequenceId)
            childjsonopbject.addProperty("dataType", it.dataType)
            childjsonopbject.addProperty("sourceType", it.sourceType)

            parentjsonarray.add(childjsonopbject)
        }

        parentjsonobejct.add("templateFields", parentjsonarray)
        return parentjsonobejct


    }
}