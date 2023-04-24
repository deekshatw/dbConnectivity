package com.example.dbconnectivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ExploreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postList: ArrayList<PostModel>
    private lateinit var adapter: PostAdapter
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        recyclerView = findViewById(R.id.recyclerView)
        findViewById<Button>(R.id.buttonPost2).setOnClickListener {
            startActivity(Intent(this@ExploreActivity, MainActivity::class.java))
        }

        postList = arrayListOf<PostModel>()

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        getPostsdata()
    }

    private fun getPostsdata() {
        dbReference = FirebaseDatabase.getInstance().getReference("Posts")
        dbReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                if (snapshot.exists()){
                    for (postSnap in snapshot.children){
                        val postData = postSnap.getValue(PostModel::class.java)
                        postList.add(postData!!)
                    }
                    adapter = PostAdapter(postList)
                    recyclerView.adapter = adapter

                    adapter.setOnItemClickListener(object : PostAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@ExploreActivity, DetailedPostActivity::class.java)
                            intent.putExtra("postId", postList[position].postId)
                            intent.putExtra("postText", postList[position].postText)
                            startActivity(intent)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}