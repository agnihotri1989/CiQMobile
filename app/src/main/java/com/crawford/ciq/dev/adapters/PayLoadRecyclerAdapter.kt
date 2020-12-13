package com.crawford.ciq.dev.adapters

import android.text.Editable
import android.text.TextWatcher
import android.text.method.TextKeyListener.clear
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.crawford.ciq.dev.R
import com.crawford.ciq.dev.model.DocketResult
import com.crawford.ciq.dev.model.TimeCodeDetailResult
import kotlinx.android.synthetic.main.item_docket_edittext.view.*
import kotlinx.android.synthetic.main.item_docket_spinner.view.*
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import java.util.Collections.addAll

class PayLoadRecyclerAdapter(val listtemplatefields: ArrayList<TimeCodeDetailResult.templateFieldsResult>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listtemplatefielddata = ArrayList<TimeCodeDetailResult.templateFieldsResult>()
    override fun getItemViewType(position: Int): Int {
        if (listtemplatefields.get(position).viewtype == 0) {
            return 0
        } else {
            return 1
        }
        return super.getItemViewType(position)
    }


    fun addpayload(listtemplatefields: List<TimeCodeDetailResult.templateFieldsResult>) {
        this.listtemplatefields.apply {
            clear()
            addAll(listtemplatefields)
        }
    }

    fun clear() {
        this.listtemplatefields.clear()
    }

    inner class MyTemplateFieldsEdittextViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun binditems(templateFieldsResult: TimeCodeDetailResult.templateFieldsResult) {
            itemView.tv_templateFieldName.text = templateFieldsResult.templateFieldName
            itemView.ed_templateFieldName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val templatefieldsresultdata = TimeCodeDetailResult.templateFieldsResult(
                        templateFieldsResult.templateFieldId,
                        templateFieldsResult.templateFieldName,
                        arrayListOf(s.toString()), templateFieldsResult.dataType,
                        templateFieldsResult.sequenceId, templateFieldsResult.sourceType, 0
                    )
                    if (listtemplatefielddata.size != 0) {


                        val mtemplafields = listtemplatefielddata?.find {
                            it.templateFieldName.equals(templateFieldsResult.templateFieldName)
                        }
                        if (mtemplafields != null) {
                         listtemplatefielddata?.find {
                                it.templateFieldName.equals(templateFieldsResult.templateFieldName)
                            }?.apply {
                                templateFieldId =templateFieldsResult.templateFieldId
                                templateFieldName = templateFieldsResult.templateFieldName
                                templateFieldValue =arrayListOf(s.toString())
                                dataType = templateFieldsResult.dataType
                                sequenceId= templateFieldsResult.sequenceId
                                sourceType = templateFieldsResult.sourceType
                                viewtype =0
                            }

                        }else
                        {
                            listtemplatefielddata.add(templatefieldsresultdata)
                        }


                    }else
                    {
                        listtemplatefielddata.add(templatefieldsresultdata)
                    }
                }

            })
        }
    }

    inner class MyTemplateFieldsSpinnerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun binditems(templatefieldsresult: TimeCodeDetailResult.templateFieldsResult) {
            itemView.tv_templateFieldNamespinner.text = templatefieldsresult.templateFieldName
            var aa = ArrayAdapter(
                itemView.context,
                android.R.layout.simple_spinner_item,
                templatefieldsresult.templateFieldValue
            )
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            with(itemView.spinner_templateFieldName)
            {
                adapter = aa
                setSelection(0, false)
                prompt = "Choose your option"
                gravity = Gravity.CENTER

            }

            itemView.spinner_templateFieldName.onItemSelectedListener {
                onItemSelected { adapterView, view, i, l ->
                    val templatefieldsresultdata = TimeCodeDetailResult.templateFieldsResult(
                        templatefieldsresult.templateFieldId,
                        templatefieldsresult.templateFieldName,
                        arrayListOf(templatefieldsresult.templateFieldValue.get(i)),
                        templatefieldsresult.dataType,
                        templatefieldsresult.sequenceId,
                        templatefieldsresult.sourceType,
                        1
                    )

                    if (listtemplatefielddata.size != 0) {

                        val mtemplafields = listtemplatefielddata?.find {
                            it.templateFieldName.equals(templatefieldsresult.templateFieldName)
                        }
                        if (mtemplafields != null) {
                            listtemplatefielddata?.find {
                                it.templateFieldName.equals(templatefieldsresult.templateFieldName)
                            }?.apply {
                                templateFieldId =templatefieldsresult.templateFieldId
                                templateFieldName = templatefieldsresult.templateFieldName
                                templateFieldValue =arrayListOf(templatefieldsresult.templateFieldValue.get(i))
                                dataType = templatefieldsresult.dataType
                                sequenceId= templatefieldsresult.sequenceId
                                sourceType = templatefieldsresult.sourceType
                                viewtype =0
                            }

                        }else
                        {
                            listtemplatefielddata.add(templatefieldsresultdata)
                        }
                    }else
                    {
                        listtemplatefielddata.add(templatefieldsresultdata)
                    }
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return when (viewType) {
            0 -> {

                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_docket_edittext, parent, false)
                MyTemplateFieldsEdittextViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_docket_spinner, parent, false)
                MyTemplateFieldsSpinnerViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return listtemplatefields.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyTemplateFieldsEdittextViewHolder) {

            val myTemplateFieldsEdittextViewHolder = holder as MyTemplateFieldsEdittextViewHolder
            myTemplateFieldsEdittextViewHolder.binditems(listtemplatefields.get(position))
        } else if (holder is MyTemplateFieldsSpinnerViewHolder) {

            val myTemplateFieldsSpinnerViewHolder = holder as MyTemplateFieldsSpinnerViewHolder
            myTemplateFieldsSpinnerViewHolder.binditems(listtemplatefields.get(position))
        }
    }

}