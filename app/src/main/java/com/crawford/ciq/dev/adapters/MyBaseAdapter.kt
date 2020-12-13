package com.crawford.ciq.dev.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.model.ClaimantResult
import kotlinx.android.synthetic.main.dialog_claimant_info.view.*
import kotlinx.android.synthetic.main.dialog_list_row.view.*


class MyBaseAdapter(context: Context) : BaseAdapter() {

    var mlist = ArrayList<ClaimantResult>()
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    fun addItems(mclaimantlist: ArrayList<ClaimantResult>) {
        mlist.addAll(mclaimantlist)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.dialog_list_row, parent, false)
        // rowView.tv_claimantname.text = "${mlist.get(position).contactType}: ${mlist.get(position).contactDetails}"

        rowView.tv_claimantname.text =
            "${mlist.get(position).firstName}"

        rowView.tv_claimantname.setOnClickListener {
            if (rowView.ll_listview.visibility == View.VISIBLE) {
                rowView.ll_listview.visibility = View.GONE
                rowView.tv_claimantname.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_claimant,
                    0,
                    R.drawable.right_chevron,
                    0
                )
            } else {
                rowView.ll_listview.visibility = View.VISIBLE
                rowView.tv_claimantname.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_claimant,
                    0,
                    R.drawable.down_chevron,
                    0
                )
            }
        }
        val myClaimantAdapter = MyClaimantAdapter(parent!!.context)
        rowView.lv_claimantinfo.adapter = myClaimantAdapter

        myClaimantAdapter.addItems(ArrayList(mlist.get(position).listcontactlist.filter {
            !it.contactType.equals(
                "EMAIL"
            )
        }))
        return rowView
    }

    override fun getItem(position: Int): Any {
        return mlist.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mlist.size
    }

    class MyClaimantAdapter(context: Context) : BaseAdapter() {
        private val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var mlist = ArrayList<ClaimantResult.ContactsResult>()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowView = inflater.inflate(R.layout.dialog_claimant_info, parent, false)
            var formatnumber = "+1 "
            val slitcontact = mlist.get(position).contactType.split(":")
           // val number: String = slitcontact[1].replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3")
            val number: String = PhoneNumberUtils.formatNumber(slitcontact[1],"US")
            formatnumber = formatnumber+number
            rowView.tv_claimantinfo.text ="${slitcontact[0]}: ${formatnumber}"
            rowView.tv_claimantinfo.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$formatnumber")
                parent!!.context.startActivity(callIntent)
            }
            return rowView
        }

        fun addItems(mclaimantlist: ArrayList<ClaimantResult.ContactsResult>) {
            mlist.addAll(mclaimantlist)
            notifyDataSetChanged()
        }

        override fun getItem(position: Int): Any {
            return mlist.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return mlist.size
        }

    }
}