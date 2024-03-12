package com.example.suddinews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.suddinews.databinding.FragmentFavoritesBinding
import com.google.firebase.database.FirebaseDatabase

class FavoritesFragment : Fragment() {
    val firebase:FirebaseDatabase= FirebaseDatabase.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rn=firebase.reference.child("Categories")
       val bind=DataBindingUtil.inflate<FragmentFavoritesBinding>(inflater,R.layout.fragment_favorites,container,false)
        Toast.makeText(context, "Favorites", Toast.LENGTH_SHORT).show()
        return bind.root
    }
}