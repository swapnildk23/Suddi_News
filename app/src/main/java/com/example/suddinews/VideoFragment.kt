package com.example.suddinews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.suddinews.databinding.FragmentVideoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class VideoFragment : Fragment() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebase: FirebaseDatabase
    private val data: ArrayList<Dclass> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bind=DataBindingUtil.inflate<FragmentVideoBinding>(inflater,R.layout.fragment_video,container,false)
        bind.rview.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        firebase = FirebaseDatabase.getInstance()
        databaseReference= firebase.reference.child("Categories")
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                data.clear()
                if(snapshot.hasChild("Video")){
                    for(videoRef in snapshot.child("Video").children)
                    {
                        data.add(
                            Dclass(
                                videoRef.child("ImageURI").value.toString(),
                                videoRef.child("Header").value.toString(),
                                videoRef.child("Content").value.toString(),
                                videoRef.child("VideoURI").value.toString(),
                                videoRef.key
                            )
                        )
                        Log.d("Video Id","${videoRef.key}")

                    }
                    data.reverse()
                    bind.rview.adapter= context?.let { MyAdapter(data, it.applicationContext) }
                }
                else{
                    bind.noVideosTxt.visibility=View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        return bind.root
    }
}