package com.crawford.ciq.dev.adapters

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.model.DummyContent
import com.google.android.material.button.MaterialButton
import org.jetbrains.anko.textColor

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MygeneralRecyclerViewAdapter(
    private val values: List<DummyContent.DummyItem>, val activity: Activity
) : RecyclerView.Adapter<MygeneralRecyclerViewAdapter.ViewHolder>() {
    var selectedPosition = 0
    private var onitemclicklistner: onItemClickListner? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_general, parent, false)
        return ViewHolder(view)
    }

    fun setonClickListner(onitemclick: onItemClickListner) {
        onitemclicklistner = onitemclick
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        if (selectedPosition == position) {
          //  holder.btn_action.setTextColor(holder.btn_action.resources.getColor(R.color.color_tabhighlighted))

            holder.btn_action.textColor = ContextCompat.getColor(holder.btn_action.context,R.color.color_tabhighlighted)
            holder.ll_redbg.visibility = View.VISIBLE
        } else {
         //   holder.btn_action.setTextColor(holder.btn_action.resources.getColor(R.color.colorgrayscale))

            holder.btn_action.textColor =ContextCompat.getColor(holder.btn_action.context,R.color.color_unselected)
            holder.ll_redbg.visibility = View.INVISIBLE
        }
        holder.btn_action.text = item.content
        holder.ll_action.setOnClickListener {
            onitemclicklistner!!.onItemClick(item.content)
            selectedPosition = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btn_action: TextView = view.findViewById(R.id.btn_action)
        val ll_action: LinearLayout = view.findViewById(R.id.ll_action)
        val ll_redbg: LinearLayout = view.findViewById(R.id.ll_redbg)
    }

    interface onItemClickListner {
        fun onItemClick(content: String)
    }
}