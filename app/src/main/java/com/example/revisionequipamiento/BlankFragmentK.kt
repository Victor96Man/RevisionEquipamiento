package com.example.revisionequipamiento

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class BlankFragmentK : Fragment() {

    fun BlankFragmentK() {

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
        val adapter = MyAdapterK(arrayOf("Example One", "Example Two", "Example Three", "Example Four", "Example Five", "Example Six", "Example Seven"))
        rv.adapter = adapter

        val llm = LinearLayoutManager(activity)
        rv.layoutManager = llm

        return rootView
    }

}// Required empty public constructor