package com.example.suddinews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.suddinews.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind=DataBindingUtil.inflate<FragmentCategoryBinding>(inflater,R.layout.fragment_category,container,false)
        Toast.makeText(context, "CATEGORY", Toast.LENGTH_SHORT).show()

        return bind.root
    }
}