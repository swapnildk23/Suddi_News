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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class News_Extended : AppCompatActivity() {

    private lateinit var binding: ActivityNewsExtendedBinding
    var isFavorite: Boolean = false
    lateinit var newsTitleTxt: String
    lateinit var im: String
    lateinit var vi: String
    lateinit var newsID: String
    lateinit var newsContentTxt: String
    lateinit var categoryName: String
    var newsNumber: Long = 0
    lateinit var userId: String
    lateinit var favoriteKey: String

    // Define a variable to hold the listener
    private var favoriteCheckListener: ((Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsExtendedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentUser = FirebaseAuth.getInstance().currentUser;
        if (currentUser != null) {
            userId = currentUser.uid
        }
        val bundle: Bundle? = intent.extras
        newsTitleTxt = bundle?.getString("NEWS_TITLE").toString()
        im = bundle?.getString("IMAGE_URI").toString()
        vi = bundle?.getString("VIDEO_URI").toString()
        newsID = bundle?.getString("ID").toString()
        Log.d("Video extended", "$vi hello")
        newsContentTxt = bundle?.getString("NEWS_CONTENT").toString()

        //  "Politics", "World", "Entertainment", "Sports", "Travel",
        //        "Technology", "District", "Business", "Health", "Video"
        categoryName = when (newsID.take(2)) {
            "En" -> "Entertainment"
            "Po" -> "Politics"
            "Wo" -> "World"
            "Sp" -> "Sports"
            "Tr" -> "Travel"
            "Te" -> "Technology"
            "Di" -> "District"
            "Bu" -> "Business"
            "He" -> "Health"
            "Vi" -> "Video"
            else -> ({ null }).toString()
        }
        val n = newsID.length
        newsNumber = newsID.substring(3, n).toLong()

        favoriteKey = categoryName + "_" + newsNumber
        Log.d("num cat", "$categoryName $newsNumber $newsID $favoriteKey")
        binding.content.text = newsContentTxt
        binding.header.text = newsTitleTxt
        if(categoryName=="Video"){
            val favoritesParams = binding.favoritesButton.layoutParams as RelativeLayout.LayoutParams
            favoritesParams.addRule(RelativeLayout.BELOW, binding.graphicVI.id)
            favoritesParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)

            // Set layout parameters for notfavoritesButton
            val notFavoritesParams = binding.notfavoritesButton.layoutParams as RelativeLayout.LayoutParams
            notFavoritesParams.addRule(RelativeLayout.BELOW, binding.graphicVI.id)
            notFavoritesParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)

        }
        checkFavorites { isFavorite ->
            this.isFavorite = isFavorite
            favoriteCheckListener?.invoke(isFavorite)
            Log.d("favorite value","$isFavorite")
            if (isFavorite) {
                binding.notfavoritesButton.visibility=View.INVISIBLE
                binding.favoritesButton.visibility=View.VISIBLE
            } else {
                binding.notfavoritesButton.visibility=View.VISIBLE
                binding.favoritesButton.visibility=View.INVISIBLE
            }
            binding.favoritesButton.setOnClickListener {
                this.isFavorite =false
                binding.favoritesButton.visibility=View.INVISIBLE
                binding.notfavoritesButton.visibility=View.VISIBLE
                val firedata=FirebaseDatabase.getInstance()
                val databaseReference=firedata.reference.child("Users").child(userId).child("favorites")
                databaseReference.child(favoriteKey).removeValue()
                Log.d("Removed","removed")
            }
            binding.notfavoritesButton.setOnClickListener{
                this.isFavorite=true
                binding.favoritesButton.visibility=View.VISIBLE
                binding.notfavoritesButton.visibility=View.INVISIBLE
                val firedata=FirebaseDatabase.getInstance()
                val databaseReference=firedata.reference.child("Users").child(userId).child("favorites")
                databaseReference.child(favoriteKey).setValue(newsID)
                Log.d("Added","added to fav")
            }
            if (vi.equals("null")) {
                Log.d("Video check img", "$vi")
                binding.graphicIM.visibility = View.VISIBLE
                binding.graphicVI.visibility = View.GONE
                Glide.with(this.applicationContext).load(Uri.parse(im)).into(binding.graphicIM)
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
    private fun checkFavorites(callback: (Boolean) -> Unit) {
        val firedata=FirebaseDatabase.getInstance()
        val databaseReference=firedata.reference.child("Users").child(userId).child("favorites")

        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                isFavorite= snapshot.hasChild(favoriteKey)
                Log.d("Children","${snapshot.children} $favoriteKey")
                Log.d("True val","$isFavorite")
                callback(isFavorite)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}