package com.crawford.ciq.dev.adapters

import android.content.Context
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.model.DocketApiResult
import com.crawford.ciq.dev.model.NoteApiResult
import kotlinx.android.synthetic.main.item_recycler_docket.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class DocketListAdapter(
    private val context: Context,
    private val listdocketApiResult: ArrayList<DocketApiResult>, val recyclerView: RecyclerView
) : RecyclerView.Adapter<DocketListAdapter.MyDocketViewHolder>() {

    var mExpandedPosition: Int = -1
    private var onitemclicklistner: onItemClickListner? = null

    fun setonClickListner(onitemclick: onItemClickListner) {
        onitemclicklistner = onitemclick
    }

    inner class MyDocketViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {


        fun binditems(docketApiResult: DocketApiResult, position: Int) {
            itemView.tv_timecode.text = docketApiResult.timeCode
            itemView.tv_amount.text = docketApiResult.timeAmount.toString()

            itemView.tv_createddate.text = retieveformatteddate(docketApiResult.createdDate)
            itemView.tv_adjusterid.text = docketApiResult.adjusterID
            itemView.tv_desc.text = docketApiResult.timeCodeDescription
            itemView.tv_note.text =
                if (docketApiResult.noteDetails==null) "NA" else docketApiResult.noteDetails

            itemView.tv_expensecode.text = docketApiResult.expenseCode
            itemView.tv_expenseamount.text = docketApiResult.expenseAmount.toString()
            if (docketApiResult.expenseCode.isEmpty()) {
                itemView.rl_expense.visibility = View.GONE
            } else {
                itemView.rl_expense.visibility = View.VISIBLE

            }

            var isExpanded: Boolean = position == mExpandedPosition;

            itemView.ll_details.visibility = if (isExpanded) View.VISIBLE else View.GONE
            itemView.isActivated = isExpanded
            if (docketApiResult.noteDetails==null) {
                onitemclicklistner!!.requestfornote(
                    docketApiResult.claimID,
                    docketApiResult.id,
                    position
                )


            }


            itemView.setOnLongClickListener(OnLongClickListener { v ->
                mExpandedPosition = if (isExpanded) -1 else position
                TransitionManager.beginDelayedTransition(recyclerView)
                notifyItemChanged(position)
                true
            })
            itemView.setOnClickListener {
                onitemclicklistner!!.onItemClick(docketApiResult)
            }
        }


    }


    fun addDockets(listdocketApiResult: List<DocketApiResult>) {
        this.listdocketApiResult.apply {
            clear()
            addAll(listdocketApiResult)
        }

    }

    fun updatenoteindocket(listnoteApiResult: ArrayList<NoteApiResult>) {

        listdocketApiResult?.find {
            it.id == listnoteApiResult.get(0).docketId
        }?.noteDetails = listnoteApiResult.get(0).noteDetails
        val temp = listdocketApiResult

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDocketViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_recycler_docket,
            parent, false
        )

        return MyDocketViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listdocketApiResult.size
    }

    override fun onBindViewHolder(holder: MyDocketViewHolder, position: Int) {

        holder.binditems(listdocketApiResult[position], position)
    }

    interface onItemClickListner {
        fun onItemClick(docketApiResult: DocketApiResult)
        fun requestfornote(ClaimId: Int, DocketId: Int, position: Int)
    }

    fun retieveformatteddate(lossDate: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("MM/dd/yyyy HH:mm:ss a")
        val output: String = formatter.format(parser.parse(lossDate))
        return output
    }
}