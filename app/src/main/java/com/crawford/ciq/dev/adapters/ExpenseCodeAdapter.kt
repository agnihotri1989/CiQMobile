package com.crawford.ciq.dev.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.crawford.ciq.dev.model.ExpenseCodeResult
import com.crawford.ciq.dev.model.TimeCodeResult

class ExpenseCodeAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val alltimecode: List<ExpenseCodeResult>
) :
    ArrayAdapter<ExpenseCodeResult>(context, layoutResource, alltimecode), Filterable {

    private var mallepensecode: List<ExpenseCodeResult> = alltimecode

    override fun getCount(): Int {
        return mallepensecode.size
    }

    override fun getItem(p0: Int): ExpenseCodeResult? {
        return mallepensecode.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return p0.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context)
            .inflate(layoutResource, parent, false) as TextView
        view.text = "${mallepensecode[position].toString()}"
        return view
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mallepensecode = filterResults.values as List<ExpenseCodeResult>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    alltimecode
                else
                    alltimecode.filter {
                        it.code.toLowerCase().startsWith(queryString)
                    }
                return filterResults
            }
        }
    }
}