package com.example.revisionequipamiento.Adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.example.revisionequipamiento.R
import java.util.*




class PostsAdapter( context :Context,val posts: ArrayList<String>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    val mCallback: CallbackInterface? = context as CallbackInterface


    interface CallbackInterface {


        fun onHandleSelection(imagen: ImageButton)
    }
    class PostsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var ImagenButton: ImageButton
        var observaciones: EditText

        init {
            ImagenButton = itemview.findViewById<View>(R.id.ft_foto_ibt) as ImageButton
            observaciones = itemview.findViewById<View>(R.id.ft_observaciones_edt) as EditText
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_foto, parent, false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {

        holder.ImagenButton.setOnClickListener{
            if(mCallback != null){
                mCallback.onHandleSelection(holder.ImagenButton)
            }

        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }


}