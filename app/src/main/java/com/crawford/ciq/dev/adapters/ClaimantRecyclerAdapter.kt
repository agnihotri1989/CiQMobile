package com.crawford.ciq.dev.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.databinding.ItemRcvClaimantBinding
import com.crawford.ciq.dev.model.*
import kotlinx.android.synthetic.main.item_recycler_progesscompletelast.view.*


class ClaimantRecyclerAdapter(
    private val claimantapiresultlist: ArrayList<ClaimantApiResult>,
    val myclaim: MyClaimResult
) :
    RecyclerView.Adapter<ClaimantRecyclerAdapter.ClaimantViewHolder>() {
    private var onclaimantaddresslistener: OnClaimantAddressListener? = null
    fun setonClickListner(onitemclick: OnClaimantAddressListener) {
        onclaimantaddresslistener = onitemclick
    }

    inner class ClaimantViewHolder(private val itemRcvClaimantBinding: ItemRcvClaimantBinding) :
        RecyclerView.ViewHolder(itemRcvClaimantBinding.root) {
        fun bindItems(claimantapiresult: ClaimantApiResult,position: Int) {
            itemRcvClaimantBinding.claimant = claimantapiresult
//            itemRcvClaimantBinding.myclaim = myclaim
            if (claimantapiresult.city == null) {
                onclaimantaddresslistener!!.ongetaddressclaimant(claimantapiresult.claimantId,position)
            }
            itemRcvClaimantBinding.executePendingBindings()

        }
    }

    fun addClaiamant(claimantapiresultlist: ArrayList<ClaimantApiResult>) {
        this.claimantapiresultlist.apply {
            clear()
            addAll(claimantapiresultlist)
            notifyDataSetChanged()
        }
    }

    fun updateaddressinclaimant(listaddressApiResult: AddressApiResult) {

        claimantapiresultlist?.find {
            it.claimantId == listaddressApiResult.claimantId
        }?.apply {
            address1 = listaddressApiResult.address1
            address2 = listaddressApiResult.address2
            city = listaddressApiResult.city
            province = listaddressApiResult.province
            country = listaddressApiResult.country
            postalCode = listaddressApiResult.postalCode
        }




    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimantViewHolder {
        val binding: ItemRcvClaimantBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_rcv_claimant, parent, false
        )
        return ClaimantViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return claimantapiresultlist.size
    }


    interface OnClaimantAddressListener {
        fun ongetaddressclaimant(claimantid: Int,position: Int)
    }

    override fun onBindViewHolder(holder: ClaimantViewHolder, position: Int) {
        holder.bindItems(claimantapiresultlist.get(position),position)

    }
}