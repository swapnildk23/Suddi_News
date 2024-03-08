package com.example.suddinews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.suddinews.databinding.FragmentVideoBinding


class VideoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind=DataBindingUtil.inflate<FragmentVideoBinding>(inflater,R.layout.fragment_video,container,false)
        val html = "<iframe class=\"youtube-player\" style=\"border:none; width: 100%; height: 95%; padding:none; margin:none; background-color: #0.0;\" id=\"ytplayer\" type=\"text/html\" src=\"http://www.youtube.com/embed/O5er2XYojDQ"+ "?fs=0\" frameborder=\"0\">\n"+ "</iframe>"
        bind.video.settings.javaScriptEnabled = true
        bind.video.loadDataWithBaseURL("", html , "text/html",  "UTF-8", "")
        return bind.root
    }
}