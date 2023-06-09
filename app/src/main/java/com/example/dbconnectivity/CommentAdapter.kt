package com.example.dbconnectivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter(private val commentList: ArrayList<CommentModel>)
    : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.comments_list, parent, false)
        return CommentViewHolder(itemview)
    }

    override fun getItemCount(): Int {
       return commentList.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = commentList[position]
        holder.commentText.text = currentComment.commentText
    }

    class CommentViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        val commentText : TextView = itemview.findViewById(R.id.commentsTv)
    }
}