package com.example.revisionequipamiento.Adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.example.revisionequipamiento.R
import java.util.*




class PostsAdapter( context :Context,val posts: ArrayList<String>) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    val mCallback: CallbackInterface? = context as CallbackInterface


    interface CallbackInterface {


        fun onHandleSelectionImage(imagen: ImageButton, position:Int)
        fun onHandleSelectionEditext(obs: EditText, position:Int)
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
                mCallback.onHandleSelectionImage(holder.ImagenButton,holder.adapterPosition)
            }

        }
        holder.observaciones.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if(mCallback != null){
                    mCallback.onHandleSelectionEditext(holder.observaciones ,holder.adapterPosition)
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(mCallback != null){
                    mCallback.onHandleSelectionEditext(holder.observaciones ,holder.adapterPosition)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if(mCallback != null){
                    mCallback.onHandleSelectionEditext(holder.observaciones ,holder.adapterPosition)
                }
            }
        })
    }

    override fun getItemCount(): Int {
        return posts.size
    }


}