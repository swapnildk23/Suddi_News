package com.example.suddinews

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        bind.politics.setOnClickListener{categoryClick(bind.politics)}
        bind.travel.setOnClickListener{categoryClick(bind.travel)}
        bind.technology.setOnClickListener{categoryClick(bind.technology)}
        bind.district.setOnClickListener{categoryClick(bind.district)}
        bind.world.setOnClickListener{categoryClick(bind.world)}
        bind.health.setOnClickListener{categoryClick(bind.health)}
        bind.sports.setOnClickListener{categoryClick(bind.sports)}
        bind.business.setOnClickListener{categoryClick(bind.business)}
        bind.entertainment.setOnClickListener{categoryClick(bind.entertainment)}
        return bind.root
    }
    public fun categoryClick(view: View) {
    val id=view.id
        val selectedCategory=when(id)
        {
            R.id.politics->"Politics"
            R.id.health->"Health"
            R.id.sports->"Sports"
            R.id.technology->"Technology"
            R.id.travel->"Travel"
            R.id.world->"World"
            R.id.entertainment->"Entertainment"
            R.id.district->"District"
            R.id.business->"Business"
            else -> {null}
        }
        Log.d("Selected Category","$selectedCategory")
        if (!selectedCategory.equals(null)){
            val bundle = Bundle().apply{
                putString("SELECTED_CATEGORY",selectedCategory)
                Log.d("Bundled category","$selectedCategory")
            }
            val intent = Intent(requireContext(),categorynews::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }


}