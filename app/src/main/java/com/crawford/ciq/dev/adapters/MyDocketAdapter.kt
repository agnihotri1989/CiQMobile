package com.crawford.ciq.dev.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.model.DocketResult
import kotlinx.android.synthetic.main.item_recycler_progesscompletelast.view.*
import kotlinx.android.synthetic.main.item_recycler_progessfull.view.*
import kotlinx.android.synthetic.main.item_recycler_progessincomplete.view.*
import kotlinx.android.synthetic.main.item_recycler_progessincompletelast.view.*

class MyDocketAdapter(
    private val docketlist: ArrayList<DocketResult>,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return docketlist.size
    }

    fun addDockets(docketlist: List<DocketResult>) {
        this.docketlist.apply {
            clear()
            addAll(docketlist)
        }


    }

    override fun getItemViewType(position: Int): Int {
        if (docketlist[position].status == true) {
            if (position != (docketlist.size - 1)) {
                return 0
            } else {
                return 2
            }
        } else {

            if (position != (docketlist.size - 1)) {
                return 1
            } else {
                return 3
            }

        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHoldercomplete) {
            val myViewHoldercomplete = holder as MyViewHoldercomplete
            myViewHoldercomplete.bindItems(docketlist[position])
        } else if (holder is MyViewHolderincompletelast) {
            val myViewHolderdashed = holder as MyViewHolderincompletelast
            myViewHolderdashed.bindItems(docketlist[position])
        } else if (holder is MyViewHolderincomplete) {
            val myViewHolderdashed = holder as MyViewHolderincomplete
            myViewHolderdashed.bindItems(docketlist[position])
        } else if(holder is MyViewHoldercompletelast) {
            val myViewHolderincomplete = holder as MyViewHoldercompletelast
            myViewHolderincomplete.bindItems(docketlist[position])
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            0 -> {

                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_progesscompletelast, parent, false)
                MyViewHoldercomplete(view)
            }
            1->{
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_progessincompletelast, parent, false)
                MyViewHolderincomplete(view)

            }
            2->{
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_progesscompletelast, parent, false)
                MyViewHoldercompletelast(view)
            }
            3->{
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_progessincompletelast, parent, false)
                MyViewHolderincompletelast(view)
            }
            else -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_progessincompletelast, parent, false)
                MyViewHolderincompletelast(view)
            }
        }


    }


    class MyViewHoldercomplete(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(docketResult: DocketResult) {
            itemView.tv_docketcompletelast.text = docketResult.timeCode
        }
    }

    class MyViewHoldercompletelast(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(docketResult: DocketResult) {
            itemView.tv_docketcompletelast.text = docketResult.timeCode
        }
    }

    class MyViewHolderincomplete(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(docketResult: DocketResult) {
            itemView.tv_docketincompletelast.text = docketResult.timeCode
        }
    }

    class MyViewHolderincompletelast(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(docketResult: DocketResult) {
            itemView.tv_docketincompletelast.text = docketResult.timeCode
        }
    }
}