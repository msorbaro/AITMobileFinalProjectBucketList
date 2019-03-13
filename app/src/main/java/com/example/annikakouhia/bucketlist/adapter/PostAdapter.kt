package com.example.annikakouhia.bucketlist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import com.example.annikakouhia.bucketlist.R
import com.example.annikakouhia.bucketlist.data.BucketList
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.row_post.view.*
import android.content.Intent
import com.example.annikakouhia.bucketlist.SingleList


class PostAdapter(var context: Context, var uid:String) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var postsList = mutableListOf<BucketList>()
    private var postKeys = mutableListOf<String>()
    private var lastPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.row_post, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postsList.get(holder.adapterPosition)

        holder.tvAuthor.append("Authors:  \n")

        for(i in 0..post.authors.size-2){ holder.tvAuthor.append(post.authors.get(i)+ " + ") }

        holder.tvAuthor.append(post.authors.get(post.authors.size-1))
        holder.tvTitle.text =  post.title
        holder.btnDelete.setOnClickListener { removePost(holder.adapterPosition) }

        holder.btnView.setOnClickListener {
            val intent = Intent(context,SingleList::class.java)
            intent.putExtra(context.getString(R.string.CURRLIST), holder.tvTitle.text.toString())
            context.startActivity(intent)
        }

        setAnimation(holder.itemView, position)
    }

    fun addPost(post: BucketList, key: String) {
        postsList.add(post)
        postKeys.add(key)
        notifyDataSetChanged()
    }

    private fun removePost(index: Int) {
        FirebaseFirestore.getInstance().collection(context.getString(R.string.bucketlist)).document(
                postKeys[index]
        ).delete()

        postsList.removeAt(index)
        postKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    fun removePostByKey(key: String) {
        val index = postKeys.indexOf(key)
        if (index != -1) {
            postsList.removeAt(index)
            postKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context,
                    android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }


    class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {

        val tvAuthor: TextView = itemView.tvAuthor
        val tvTitle: TextView = itemView.tvTitle
        val btnDelete: Button = itemView.btnDelete
        val btnView: Button = itemView.btnView
    }

}