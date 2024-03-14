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
    import com.example.suddinews.databinding.FragmentFavoritesBinding
    import com.google.firebase.database.DataSnapshot
    import com.google.firebase.database.DatabaseError
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.database.ValueEventListener
    import com.google.firebase.ktx.Firebase

    class FavoritesFragment : Fragment() {

        private lateinit var databaseReference: DatabaseReference
        private lateinit var firebase:FirebaseDatabase
        private lateinit var userId:String
        private val data: ArrayList<Dclass> = ArrayList()
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
           val bind=DataBindingUtil.inflate<FragmentFavoritesBinding>(inflater,R.layout.fragment_favorites,container,false)
            bind.rview.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            Toast.makeText(context, "Favorites", Toast.LENGTH_SHORT).show()
            firebase = FirebaseDatabase.getInstance()
            databaseReference= firebase.reference

            userId = "bZlvpiTIckO0eGUe9o7plvdISf12"
            val favoritesRef = databaseReference.child("Users").child("bZlvpiTIckO0eGUe9o7plvdISf12").child("favorites")

            val favoritesMap = hashMapOf<String, String>()
            favoritesRef.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(childSnapshot in snapshot.children) {
                        val key = childSnapshot.key as String
                        val value = childSnapshot.value as String?
                        if (key != null && value != null) {
                            favoritesMap[key] = value
                        }
                        Log.d("FirebaseData", "$favoritesMap")
                    }
                    for((category,newsId) in favoritesMap)
                    {
                        Log.d("FireData1","$category $newsId")
                        val newsRef=databaseReference.child("Categories")
                        newsRef.addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if(snapshot.hasChild(category)) {
                                    if (snapshot.child(category).hasChild(newsId)) {
                                        val header = snapshot.child(category).child(newsId)
                                            .child("Header").value.toString()
                                        val content = snapshot.child(category).child(newsId)
                                            .child("Content").value.toString()
                                        var img: String? = null
                                        var vid: String? = null
                                        if (snapshot.child(category).child(newsId)
                                                .hasChild("ImageURI")
                                        ) {
                                            img = snapshot.child(category).child(newsId)
                                                .child("ImageURI").value.toString()
                                        }
                                        if (snapshot.child(category).child(newsId)
                                                .hasChild("VideoURI")
                                        ) {
                                            vid = snapshot.child(category).child(newsId)
                                                .child("VideoURI").value.toString()
                                        }
                                        val dataclass = Dclass(img, header, content, vid,newsId)

                                        data.add(dataclass)
                                        bind.rview.adapter =
                                            context?.let { MyAdapter(data, it.applicationContext) }
                                        Log.d("DataClassElement1", "$data")
                                    }
                                    else{
                                        bind.nofavoritesTxt.visibility=View.VISIBLE
                                    }
                                }
                                else{
                                    bind.nofavoritesTxt.visibility=View.VISIBLE
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("FirebaseData", "Error fetching news: ${error.message}")
                            }
                        })
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseData", "Error: ${error.message}")
                }
            })
            return bind.root
        }

    }