package com.example.suddinews

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.RelativeLayout
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
        val newsTitleTxt = bundle?.getString("NEWS_TITLE").toString()
        val im = bundle?.getString("IMAGE_URI").toString()
        val vi = bundle?.getString("VIDEO_URI").toString()
        val newsID=bundle?.getString("ID").toString()
        Log.d("Video extended", "$vi hello")
        val newsContentTxt = bundle?.getString("NEWS_CONTENT").toString()
        binding.content.text = newsContentTxt
        binding.header.text = newsTitleTxt

        if (vi.equals("null")) {
            Log.d("Video check img", "$vi")
            binding.graphicIM.visibility = View.VISIBLE
            binding.graphicVI.visibility = View.GONE
            Glide.with(this).load(Uri.parse(im)).into(binding.graphicIM)
        } else {
            binding.graphicIM.visibility = View.GONE
            binding.graphicVI.visibility = View.VISIBLE
            val params = binding.contentLine.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.BELOW, binding.graphicVI.id)
            binding.graphicVI.setVideoURI(Uri.parse(vi))
            val mc: MediaController = MediaController(this)
            mc.setAnchorView(binding.graphicVI)
            mc.setMediaPlayer(binding.graphicVI)
            binding.graphicVI.setMediaController(mc)
            binding.graphicVI.setOnPreparedListener { mp ->
                // Hide loading indicator once video is prepared
                binding.loadingProgressBar.visibility = View.GONE
                mp.start()
            }
            binding.graphicVI.setOnInfoListener { mp, what, extra ->
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    // Show loading indicator when buffering starts
                    binding.loadingProgressBar.visibility = View.VISIBLE
                } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    // Hide loading indicator when buffering ends
                    binding.loadingProgressBar.visibility = View.GONE
                }
                false
            }
            binding.graphicVI.start()
        }
    }

}