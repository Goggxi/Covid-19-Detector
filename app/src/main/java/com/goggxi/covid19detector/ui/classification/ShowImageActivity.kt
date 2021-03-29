package com.goggxi.covid19detector.ui.classification

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.databinding.ActivityShowImageBinding
import com.goggxi.covid19detector.ui.adapter.RetrieveImageAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.annotations.NotNull

class ShowImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowImageBinding
    private lateinit var mUploads: ArrayList<ImageUpload>
    private lateinit var mAdapter: RetrieveImageAdapter

    private val root = FirebaseDatabase.getInstance().getReference("ImageClassification")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        binding = ActivityShowImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        listImage()
    }

    override fun onOptionsItemSelected(@NotNull item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listImage() {
        binding.recyclerViewClassification.setHasFixedSize(true)
        binding.recyclerViewClassification.layoutManager = LinearLayoutManager(this)
        mUploads = ArrayList()

        root.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    val upload: ImageUpload? = postSnapshot.getValue(ImageUpload::class.java)
                    mUploads.add(upload!!)
                }
                mAdapter = RetrieveImageAdapter(this@ShowImageActivity ,mUploads)
                binding.recyclerViewClassification.adapter = mAdapter
                binding.progressCircle.visibility = View.INVISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ShowImageActivity , databaseError.message, Toast.LENGTH_SHORT).show()
                binding.progressCircle.visibility = View.INVISIBLE
            }
        })
    }
}