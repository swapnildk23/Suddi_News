package com.example.suddinews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suddinews.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RecentFragment : Fragment() {
    val firebase:FirebaseDatabase= FirebaseDatabase.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rn=firebase.reference.child("Categories")
        val bind=DataBindingUtil.inflate<FragmentHomeBinding>(inflater,R.layout.fragment_home,container,false)
        bind.rview.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        Toast.makeText(context, "RECENT", Toast.LENGTH_SHORT).show()
        val data=ArrayList<Dclass>()
        rn.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                for(ds in snapshot.children)
                    for(ns in ds.children)
                    {
                        data.add(Dclass(ns.child("ImageURI").value.toString(),ns.child("Header").value.toString(),ns.child("Content").value.toString(),ns.child("VideoURI").value.toString()))
                        Log.d("URI",ns.child("ImageURI").value.toString())
                        data.reverse()
                    }

                bind.rview.adapter= context?.let { MyAdapter(data, it.applicationContext) }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return bind.root
    }
}
