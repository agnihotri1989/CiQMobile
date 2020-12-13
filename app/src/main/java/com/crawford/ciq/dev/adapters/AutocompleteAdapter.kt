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
import com.crawford.ciq.dev.model.TimeCodeResult

class AutocompleteAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private var alltimecode: List<TimeCodeResult>
) :
    ArrayAdapter<TimeCodeResult>(context, layoutResource, alltimecode),Filterable {

    private var malltimecode: List<TimeCodeResult> = alltimecode

    override fun getCount(): Int {
        return malltimecode.size
    }

    override fun getItem(p0: Int): TimeCodeResult? {
        return malltimecode.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return p0.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context)
            .inflate(layoutResource, parent, false) as TextView
        view.text = "${malltimecode[position].toString()}"
        return view
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                malltimecode = filterResults.values as List<TimeCodeResult>
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