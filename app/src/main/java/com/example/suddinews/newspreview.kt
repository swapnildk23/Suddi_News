package com.example.suddinews

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
    lateinit var news_title: TextView
    lateinit var news_content: TextView
    lateinit var btn_upload: AppCompatButton
    lateinit var btn_preview: AppCompatButton
    lateinit var image_preview: ImageView
    lateinit var video_preview: VideoView
    var videoUri: Uri? = null
    var imageUri: Uri? = null
    var nc:Long = 0
    val sr= Firebase.storage.reference
    val firedata:FirebaseDatabase=FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newspreview)
        BindUi()
        val rn=firedata.reference.child("Categories")
        val bundle: Bundle? = intent.extras
         newsTitleTxt = bundle?.getString("NEWS_TITLE").toString()
         selectedItem = bundle?.getString("SELECTED_ITEM").toString()
         newsContentTxt = bundle?.getString("NEWS_CONTENT").toString()
        if(selectedItem.equals("Video"))
        {
            val params = news_title.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.BELOW, R.id.admin_news_video)
            val videoUriString = bundle?.getString("VIDEO_URI")
            videoUri = Uri.parse(videoUriString)
            image_preview.visibility=View.GONE
            video_preview.visibility= View.VISIBLE
            news_title.text=newsTitleTxt
            news_content.text=newsContentTxt
            video_preview.setVideoURI(videoUri)
            video_preview.start()
        }
        else{
            val imageUriString = bundle?.getString("IMAGE_URI")
            imageUri = Uri.parse(imageUriString)
            video_preview.visibility=View.GONE
            image_preview.visibility= View.VISIBLE
            news_title.text=newsTitleTxt
            news_content.text=newsContentTxt
            image_preview.setImageURI(imageUri)
        }
        btn_upload.setOnClickListener{
            val nref=rn.child(selectedItem)
            nref.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    nc=snapshot.childrenCount
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
            Toast.makeText(this, "uploaded", Toast.LENGTH_SHORT).show()
            val link="${selectedItem[0]}N${nc+1}"
            val ref=nref.child(link)
            sr.child(link).putFile(Uri.parse(imageUri.toString())).addOnSuccessListener {
                sr.child(link).downloadUrl.addOnSuccessListener {
                        ref.child("ImageURI").setValue("$it")
                    }.addOnFailureListener {
                        ref.child("ImageURI").setValue("${sr.child(link).downloadUrl.result}")
                }
            }.addOnFailureListener{
                Toast.makeText(this, "${it}", Toast.LENGTH_SHORT).show()
            }
            ref.child("Content").setValue(newsContentTxt)
            ref.child("Header").setValue(newsTitleTxt)
            ref.child("VideoURI").setValue(videoUri.toString())
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