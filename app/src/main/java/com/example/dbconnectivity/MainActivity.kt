package com.example.dbconnectivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var etPost : EditText
    private lateinit var btnPost : Button
    private lateinit var dbReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPost = findViewById(R.id.etPost)
        btnPost = findViewById(R.id.btnPost)

        // getting refernce to db
        dbReference = FirebaseDatabase.getInstance().getReference("Posts")
        btnPost.setOnClickListener {
            savePostdata()
            startActivity(Intent(this@MainActivity, ExploreActivity::class.java))
        }
        findViewById<Button>(R.id.exploreBtn).setOnClickListener {
            startActivity(Intent(this@MainActivity, ExploreActivity::class.java))
        }
    }

    private fun savePostdata() {
        //method to save all the data entered!!
        val postText = etPost.text.toString()
        if (postText.isEmpty()){
            etPost.error = "Please type something in order to post!"
            etPost.requestFocus()
        }

        val postId = dbReference.push().key!!

        val post = PostModel(postId, postText)
        dbReference.child(postId).setValue(post)
            .addOnSuccessListener {
                Toast.makeText(this, "posted successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{error ->
                Toast.makeText(this, "Error! ${error.message}", Toast.LENGTH_SHORT).show()

            }
    }
}