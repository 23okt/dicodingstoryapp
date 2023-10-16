package com.example.storymeow.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storymeow.data.response.ListStoryItem
import com.example.storymeow.databinding.ActivityDetailBinding


@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = intent.getParcelableExtra<ListStoryItem>(DETAIL_STORY) as ListStoryItem
        setupAction(detail)
        
        supportActionBar?.show()
        supportActionBar?.title = "Detail Story"
    }

    private fun setupAction(detail: ListStoryItem){
        Glide.with(applicationContext)
            .load(detail.photoUrl)
            .into(binding.imageDetail)
        binding.nameDetail.text = detail.name
        binding.descDetail.text = detail.description
    }
    companion object{
        const val DETAIL_STORY = "detail_story"
    }
}