package com.example.revisionequipamiento

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class WhiteFragmentK : Fragment() {

    fun WhiteFragmentK() {
        // Required empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_blank, container, false)

        val rv = rootView.findViewById<View>(R.id.rv_recycler_view) as RecyclerView
        rv.setHasFixedSize(true)
        val adapter = MyAdapterK(arrayOf("manuel", "dfdf"))
        rv.adapter = adapter

        val llm = LinearLayoutManager(getActivity())
        rv.layoutManager = llm

        return rootView
    }


}