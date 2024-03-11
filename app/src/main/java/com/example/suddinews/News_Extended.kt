package com.example.suddinews

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.suddinews.databinding.ActivityNewsExtendedBinding

class News_Extended : AppCompatActivity() {

    private lateinit var binding: ActivityNewsExtendedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsExtendedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle? = intent.extras
        var newsTitleTxt = bundle?.getString("NEWS_TITLE").toString()
        var im = bundle?.getString("IMAGE_URI").toString()
        var newsContentTxt = bundle?.getString("NEWS_CONTENT").toString()
        binding.content.text=newsContentTxt
        binding.header.text=newsTitleTxt
        Glide.with(this).load(Uri.parse(im)).into(binding.graphic)
    }

}