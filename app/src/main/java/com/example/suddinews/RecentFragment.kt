package com.example.suddinews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suddinews.databinding.FragmentHomeBinding

class RecentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind=DataBindingUtil.inflate<FragmentHomeBinding>(inflater,R.layout.fragment_home,container,false)
        bind.rview.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        Toast.makeText(context, "RECENT", Toast.LENGTH_SHORT).show()
        val data=ArrayList<Dclass>()


        return bind.root
    }

}