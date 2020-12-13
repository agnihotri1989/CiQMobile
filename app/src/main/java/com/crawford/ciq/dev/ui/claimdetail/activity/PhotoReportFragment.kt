package com.crawford.ciq.dev.ui.claimdetail.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crawford.ciq.dev.R
import kotlinx.android.synthetic.main.fragment_claimlist.view.*
import kotlinx.android.synthetic.main.fragment_photo_report.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoReportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_photo_report, container, false)
        val mLayoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL, false
        )
        view.rcv_photoreport.layoutManager = mLayoutManager
        val dividerItemDecoration = DividerItemDecoration(
            view.rcv_photoreport.getContext(),
            DividerItemDecoration.VERTICAL
        )
        view.rcv_photoreport.addItemDecoration(dividerItemDecoration)
        view.rcv_photoreport.adapter = MyPhotoRecyclerAdapter()

        view.iv_plusaction.setOnClickListener {
            if(view.mvc_actions.visibility == View.GONE)
            {
                view.mvc_actions.visibility == View.VISIBLE
            }else
            {
                view.mvc_actions.visibility == View.GONE
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotoReportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    inner class MyPhotoRecyclerAdapter: RecyclerView.Adapter<MyPhotoRecyclerAdapter.MyPhotoViewHolder>(){
        inner class MyPhotoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPhotoViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.item_recycler_photoreport,
                parent, false
            )

            return MyPhotoViewHolder(v)
        }

        override fun getItemCount(): Int {
           return 10
        }

        override fun onBindViewHolder(holder: MyPhotoViewHolder, position: Int) {

        }
    }
}