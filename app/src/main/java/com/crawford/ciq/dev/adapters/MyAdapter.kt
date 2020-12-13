package com.crawford.ciq.dev.adapters

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.model.ClaimantResult
import com.crawford.ciq.dev.model.MyClaimResult
import com.crawford.ciq.dev.utils.AddresstoLatlong
import com.crawford.ciq.dev.utils.OnSwipeTouchListener
import kotlinx.android.synthetic.main.list_row_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class MyAdapter(
    var claimlist: ArrayList<MyClaimResult>,
    private var claimantlist: ArrayList<ClaimantResult>,
    private val context: Context
) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(), Filterable {
    var claimoriginalist = ArrayList<MyClaimResult>()
    var mlocation: Location? = null
    lateinit var onDocketwithClaimListener: OnDocketwithClaimListener
    fun getlocation(location: Location?) {
        mlocation = location
    }

    override fun getItemCount(): Int {
        return claimlist.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(claimlist[position], onDocketwithClaimListener, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.list_row_item,
            parent, false
        )

        return MyViewHolder(v)
    }

    fun addclaimantlist(claimantlist: List<ClaimantResult>) {
        this.claimantlist.apply {
            clear()
            addAll(claimantlist)
        }
    }

    fun addUsers(claimlist: List<MyClaimResult>) {
        this.claimlist.apply {
            clear()
            addAll(claimlist)
        }
        this.claimoriginalist.apply {
            clear()
            addAll(claimlist)
        }
        sortbydate(false)
    }


    fun setonListener(onDocketwithClaimListener: OnDocketwithClaimListener) {
        this.onDocketwithClaimListener = onDocketwithClaimListener
    }

    //    override fun onViewRecycled(holder: MyViewHolder) {
//        super.onViewRecycled(holder)
//        val view =  holder.itemView as BothSideCoordinatorLayout
//        view.sync()
//    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {

            }
        }

        fun bindItems(
            claimResult: MyClaimResult,
            onDocketwithClaimListener: OnDocketwithClaimListener, position: Int
        ) {


            itemView.rowFG.setOnTouchListener(object : OnSwipeTouchListener(itemView.context) {


                override fun onSwipeLeft() {
                    Toast.makeText(itemView.context, "left", Toast.LENGTH_LONG).show()
                }

                override fun onSwipeRight() {
                    Toast.makeText(itemView.context, "right", Toast.LENGTH_LONG).show()
                }

            })
            itemView.tv_filenumber.text = "FILE#: ${claimResult.filenumber}"
            var indexlocation: List<String> = claimResult.lossLocation.split(",").map { it.trim() }
            // itemView.tv_losslocation.text = "${indexlocation[1]}, ${indexlocation[2]}".capitalize()
            itemView.tv_losslocation.text =
                "${claimResult.lossLocationCity}, ${claimResult.lossLocationProvince}".capitalize()
            if (claimResult.claimTypeCode.trim().equals("CAT")) {
                itemView.img_cat.setImageResource(R.drawable.flash_filled)
            } else {
                itemView.img_cat.setImageResource(0)
            }
            if (claimResult.alertstatus.equals("alert")) {
                itemView.img_alertstatus.visibility = View.VISIBLE
            } else {
                itemView.img_alertstatus.visibility = View.GONE
            }
            if (claimResult.claimstatus.equals("assist")) {
                itemView.img_claimstatus.setImageResource(R.drawable.assist_filled)
                ImageViewCompat.setImageTintList(
                    itemView.img_claimstatus,
                    ColorStateList.valueOf(itemView.resources.getColor(R.color.colorbg))
                )
            } else {
                itemView.img_claimstatus.setImageResource(0)
            }
            itemView.tv_claimtypecode.text = "${claimResult.claimTypeCode}"
            itemView.tv_rcvddate.text = "${claimResult.receiveDate}"
//            need to be done on phone

            itemView.tv_miles.text =
                if (claimResult.distance == 0) "0 mi" else "${claimResult.distance}" + " mi"


//            if (claimResult.distance == 0) {
//                GlobalScope.launch {
//                    val addresstoLatlong = AddresstoLatlong(itemView.context)
//                    val latlong =
//                        addresstoLatlong.getLocationFromAddress(claimResult.lossLocation)
//                    val distance = addresstoLatlong.getdistance(
//                        mlocation!!.latitude,
//                        mlocation!!.longitude,
//                        latlong!!.latitude,
//                        latlong!!.longitude
//                    )
//
//                    claimResult.distance = distance
//
//                    (itemView.context as Activity).runOnUiThread {
//                        itemView.tv_miles.text =
//                            if (distance == 0) "0 mi" else "${claimlist[position].distance}" + " mi"
//                        //  notifyItemChanged(position)
//                        notifyDataSetChanged()
//                    }
//                }
//            } else {
//                itemView.tv_miles.text = "${claimResult.distance}" + " mi"
//                // notifyItemChanged(position)
//            }


            //itemView.tv_miles.text = "755.36 mi"
//              itemView.tv_miles.text = "${claimResult.distance} mi"

            itemView.tv_firstlastname.text =
                returnclaimant(claimResult)

            // println("for ${claimResult.claimant1} is ${claimResult.claimant2}")
            itemView.tv_catostrophicloss.text = "${claimResult.catastrophicLossCode}"
            itemView.tv_carriername.text = "${claimResult.organizationCode}"

            itemView.tv_carriername.text = "${claimResult.organizationCode}"
            //    val docketAdapter = MyDocketAdapter(returndocketlist(), itemView.context)
            val docketAdapter = MyDocketAdapter(ArrayList(), itemView.context)
            itemView.rcv_dockets.layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL, false
            )
            itemView.rcv_dockets.adapter = docketAdapter
            onDocketwithClaimListener.updatedockets(
                claimResult,
                docketAdapter,
                itemView.rcv_dockets,
                itemView.tv_emptyview
            )
            onDocketwithClaimListener.getclaimlistcount(itemCount)
        }
    }

    interface OnDocketwithClaimListener {


        fun updatedockets(
            claimResult: MyClaimResult, myDocketAdapter: MyDocketAdapter,
            recyclerView: RecyclerView, emptyview: TextView
        )

        fun getclaimlistcount(itemcount: Int)
    }


    fun sortbydate(sortype: Boolean) {

        if (sortype) {
            var sortedlist = ArrayList<MyClaimResult>()
            claimlist.sortBy { it.receiveDate }
            sortedlist.addAll(claimlist)
            claimlist.clear()
            claimlist.addAll(sortedlist)
            notifyItemRangeChanged(0, claimlist.size)
        } else {
            var sortedlist = ArrayList<MyClaimResult>()
            claimlist.sortByDescending { it.receiveDate }
            sortedlist.addAll(claimlist)
            claimlist.clear()
            claimlist.addAll(sortedlist)
            notifyItemRangeChanged(0, claimlist.size)
        }

    }

    fun refresh() {
        claimlist.clear()
        claimlist.addAll(claimoriginalist)
        notifyDataSetChanged()
    }

    fun sortbydistance(sortistance: Boolean) {

        if (sortistance) {
            var sortedlist = ArrayList<MyClaimResult>()
            claimlist.sortBy { it.distance }
//            claimlist.sortWith(Comparator { lhs, rhs ->
//                if (lhs.distance > rhs.distance) 1 else if (lhs.distance < rhs.distance) -1 else 0
//            })

            sortedlist.addAll(claimlist)
//            claimlist.clear()
            claimlist = sortedlist
            // notifyItemRangeChanged(0, claimlist.size)
            notifyDataSetChanged()
        } else {
            var sortedlist = ArrayList<MyClaimResult>()
            claimlist.sortByDescending { it.distance }
//            claimlist.sortWith(Comparator { lhs, rhs ->
//                if (lhs.distance < rhs.distance) 1 else if (lhs.distance > rhs.distance) -1 else 0
//            })

            sortedlist.addAll(claimlist)
            claimlist = sortedlist
            notifyItemRangeChanged(0, claimlist.size)
        }

    }


    fun returnclaimant(myClaimResult: MyClaimResult): String {
        var count = 0
        var finalclaimat = ""
        if (!myClaimResult.claimant1.trim().isEmpty()) {
            finalclaimat = "C1: ${myClaimResult.claimant1}"
        }
        if (!myClaimResult.claimant2.trim().isEmpty()) {
            count = ++count
        }
        if (!myClaimResult.claimant3.trim().isEmpty()) {
            count = ++count
        }
        if (!myClaimResult.claimant4.trim().isEmpty()) {
            count = ++count
        }
        if (!myClaimResult.claimant5.trim().isEmpty()) {
            count = ++count
        }

        if (count == 0) {
            return finalclaimat
        } else {
            return "${finalclaimat}, +${count}"
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charsearch = constraint.toString()
                if (charsearch.isEmpty()) {
                    claimlist = claimoriginalist
                } else {
                    if (charsearch.equals("CAT")) {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.claimTypeCode.trim().equals(charsearch)) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    } else if (charsearch.equals("assist")) {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.claimstatus.equals(charsearch)) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    } else if (charsearch.equals("alert")) {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.alertstatus.equals(charsearch)) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    } else if (charsearch.equals("FC")) {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.fcCreatedDate.isEmpty()) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    } else if (charsearch.equals("FV")) {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.fvCreatedDate.isEmpty()) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    } else if (charsearch.equals("FR")) {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.frCreatedDate.isEmpty()) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    } else if (charsearch.equals("ER")) {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.erCreatedDate.isEmpty()) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    } else {
                        val resultclaimlist = ArrayList<MyClaimResult>()
                        claimlist.forEach {
                            if (it.claimant1.toLowerCase(Locale.ROOT).contains(charsearch) ||
                                it.lossLocation.toLowerCase(Locale.ROOT).contains(charsearch) ||
                                it.filenumber.toString().contains(charsearch)
                            ) {
                                resultclaimlist.add(it)
                            }
                        }
                        claimlist = resultclaimlist
                    }
                }

                val filterresults = FilterResults()
                filterresults.values = claimlist
                return filterresults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                claimlist = results?.values as ArrayList<MyClaimResult>
                notifyDataSetChanged()
            }
        }
    }

    fun alertstatusondate(claimResult: MyClaimResult, days: Long): Boolean {
        if (claimResult.fcCreatedDate.isEmpty()) {
            if (days > 1) {
                return true
            }
        }
        if (claimResult.fvCreatedDate.isEmpty()) {
            if (days > 3) {
                return true
            }
        }
        if (claimResult.frCreatedDate.isEmpty()) {
            if (days > 7) {
                return true
            }
        }
        if (claimResult.erCreatedDate.isEmpty()) {
            if (days > 5) {
                return true
            }
        }
        return false
    }


}