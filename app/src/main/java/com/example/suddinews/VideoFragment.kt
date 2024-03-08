package com.example.suddinews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.suddinews.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind=DataBindingUtil.inflate<FragmentVideoBinding>(inflater,R.layout.fragment_video,container,false)
        bind.video.setVideoURI("https://www.youtube.com/live/Bn9dELPWcu8?feature=shared".toUri())
        return bind.root
    }
}