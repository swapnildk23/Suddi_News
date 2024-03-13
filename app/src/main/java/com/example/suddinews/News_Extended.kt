package com.example.suddinews

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.suddinews.databinding.ActivityNewsExtendedBinding

class News_Extended : AppCompatActivity() {

    private lateinit var binding: ActivityNewsExtendedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsExtendedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle: Bundle? = intent.extras
        val newsTitleTxt = bundle?.getString("NEWS_TITLE").toString()
        val im = bundle?.getString("IMAGE_URI").toString()
        val vi=bundle?.getString("VIDEO_URI").toString()
        val newsContentTxt = bundle?.getString("NEWS_CONTENT").toString()
        if(im.isNotEmpty()) {
            binding.graphicIM.isVisible=true
            binding.graphicIM.isEnabled=true
            binding.graphicVI.isVisible=false
            binding.graphicVI.isEnabled=false
            binding.content.text = newsContentTxt
            binding.header.text = newsTitleTxt
            Glide.with(this).load(Uri.parse(im)).into(binding.graphicIM)
        }
        else if (bundle?.containsKey("VIDEO_URI") == true){
            Log.d("VID",bundle?.containsKey("VIDEO_URI").toString())
            binding.graphicIM.isVisible=false
            binding.graphicVI.isVisible=true
            binding.graphicVI.isEnabled=true
            binding.graphicIM.isEnabled=false
            binding.graphicVI.setVideoURI(Uri.parse(vi))
            val mc : MediaController =MediaController(this)
            mc.setAnchorView(binding.graphicVI)
            mc.setMediaPlayer(binding.graphicVI)
            binding.graphicVI.setMediaController(mc)
            binding.graphicVI.start()
        }
    }

}