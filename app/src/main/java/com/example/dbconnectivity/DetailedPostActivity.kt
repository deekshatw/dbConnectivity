package com.example.dbconnectivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class DetailedPostActivity : AppCompatActivity() {

    private lateinit var detailPostTv : TextView
    private lateinit var postComment : TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentList: ArrayList<CommentModel>
    private lateinit var adapter: CommentAdapter
    private lateinit var addComment : EditText
    private lateinit var editBtn : Button
    private lateinit var dbReferencePost: DatabaseReference
    private lateinit var dbReferenceComment: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_post)

        commentList = arrayListOf<CommentModel>()
        editBtn = findViewById(R.id.editBtn)
        detailPostTv = findViewById(R.id.detailPostTv)
        postComment = findViewById(R.id.postComment)
        addComment = findViewById(R.id.addComment)
        recyclerView = findViewById(R.id.commentsRecyclerView)
        detailPostTv.text = intent.getStringExtra("postText")
        val postId = intent.getStringExtra("postId")
        recyclerView.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        

       postComment.setOnClickListener {
           commentDataSave()
       }

        // creating comment under specific post
        dbReferencePost = FirebaseDatabase.getInstance().getReference("Posts/$postId")
        dbReferenceComment = dbReferencePost.child("Comments")
        getCommentsData()
    }

    private fun commentDataSave() {
        val commentText = addComment.text.toString()
        if(commentText.isEmpty()){
            addComment.error = "Please add a comment to post!"
            addComment.requestFocus()
        }
        val commentId = dbReferenceComment.push().key!!
        val comment = CommentModel(commentId,commentText)
        dbReferenceComment.child(commentId).setValue(comment)
            .addOnSuccessListener {
                Toast.makeText(this,"Successfully posted comment", Toast.LENGTH_SHORT).show()
                addComment.text.clear()
            }
    }

    private fun getCommentsData() {
        dbReferenceComment.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                commentList.clear()
                if (snapshot.exists()){
                    for (commentSnap in snapshot.children){
                        val commentData = commentSnap.getValue(CommentModel::class.java)
                        commentList.add(commentData!!)
                    }
                    adapter = CommentAdapter(commentList)
                    recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}