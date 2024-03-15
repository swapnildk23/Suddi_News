package com.example.suddinews

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class newspreview : AppCompatActivity() {
    lateinit var newsTitleTxt:String
    lateinit var selectedItem: String
    lateinit var newsContentTxt: String
    lateinit var  sh: SharedPreferences
    lateinit var news_title: TextView
    lateinit var news_content: TextView
    lateinit var btn_upload: AppCompatButton
    lateinit var image_preview: ImageView
    lateinit var video_preview: VideoView
    var videoUri: Uri? = null
    var imageUri: Uri? = null
    var nc:Long = 0
    val sr= Firebase.storage.reference
    val firedata:FirebaseDatabase=FirebaseDatabase.getInstance()
    var link:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newspreview)
        sh=getSharedPreferences(getString(R.string.shpref), Context.MODE_PRIVATE)
        BindUi()
        val rn=firedata.reference.child("Categories")
        val bundle: Bundle? = intent.extras
         newsTitleTxt = bundle?.getString("NEWS_TITLE").toString()
         selectedItem = bundle?.getString("SELECTED_ITEM").toString()
         newsContentTxt = bundle?.getString("NEWS_CONTENT").toString()
        Log.d("Selected after bundle", selectedItem)
        if(selectedItem.equals("Video"))
        {
            val params = news_title.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.BELOW, R.id.admin_news_video)
            val videoUriString = bundle?.getString("VIDEO_URI")
            if (videoUriString.equals(null))
            {
                Toast.makeText(this, "Its null", Toast.LENGTH_SHORT).show()
            }
            videoUri = Uri.parse(videoUriString)
            image_preview.visibility=View.GONE
            video_preview.visibility= View.VISIBLE
            news_title.text=newsTitleTxt
            news_content.text=newsContentTxt
            val mediaController = MediaController(this)
            mediaController.setAnchorView(video_preview)
            video_preview.setMediaController(mediaController)
            video_preview.setVideoURI(videoUri)
            video_preview.start()
            Toast.makeText(this,selectedItem,Toast.LENGTH_SHORT).show()
        }
        else{
            val imageUriString = bundle?.getString("IMAGE_URI")
            imageUri = Uri.parse(imageUriString)
            video_preview.visibility=View.GONE
            image_preview.visibility= View.VISIBLE
            news_title.text=newsTitleTxt
            news_content.text=newsContentTxt
            image_preview.setImageURI(imageUri)
            Toast.makeText(this,selectedItem,Toast.LENGTH_SHORT).show()
        }
        btn_upload.setOnClickListener{
            val nref=rn.child(selectedItem)
            Log.d("SelectedItem inside btn", selectedItem)
            nref.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    nc=snapshot.childrenCount
                    Log.d("nc after taken","$nc")
                    link="${selectedItem[0]}N${nc+1}"
                    while(snapshot.hasChild(link))
                    {
                        nc++
                        link="${selectedItem[0]}N${nc+1}"
                    }
                    Log.d("link inside","$link")
                    sh.edit().putString("addr",link).apply()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
            link= sh.getString("addr"," ").toString()
            Log.d("link","$link")
            Log.d("nc before link","$nc $selectedItem")
            Toast.makeText(this, "uploaded", Toast.LENGTH_SHORT).show()
            Log.d("COUNT","$link")
            val ref=nref.child(link)
            if(selectedItem.equals("Video"))
            {
                sr.child(link).putFile(Uri.parse(videoUri.toString())).addOnSuccessListener {
                    sr.child(link).downloadUrl.addOnSuccessListener {
                        ref.child("VideoURI").setValue("$it")
                        ref.child("ImageURI").setValue("$it")
                    }.addOnFailureListener {
                        ref.child("VideoURI").setValue("${sr.child(link).downloadUrl.result}")
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "${it}", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                sr.child(link).putFile(Uri.parse(imageUri.toString())).addOnSuccessListener {
                    sr.child(link).downloadUrl.addOnSuccessListener {
                        ref.child("ImageURI").setValue("$it")
                    }.addOnFailureListener {
                        ref.child("ImageURI").setValue("${sr.child(link).downloadUrl.result}")
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "${it}", Toast.LENGTH_SHORT).show()
                }
            }
            ref.child("Content").setValue(newsContentTxt)
            ref.child("Header").setValue(newsTitleTxt)
        }
    }
    private fun BindUi()
    {
        news_content=findViewById(R.id.admin_news_content)
        news_title=findViewById(R.id.admin_news_header)
        btn_upload=findViewById(R.id.btn_admin_news_upload)
        image_preview=findViewById(R.id.admin_news_image)
        video_preview=findViewById(R.id.admin_news_video)
    }
}