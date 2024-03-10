package com.example.suddinews

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatButton

class newspreview : AppCompatActivity() {

    var newsTitleTxt:String?=null
    var selectedItem: String? = null
    var newsContentTxt: String? = null
    lateinit var news_title: TextView
    lateinit var news_content: TextView
    lateinit var btn_upload: AppCompatButton
    lateinit var btn_preview: AppCompatButton
    lateinit var image_preview: ImageView
    lateinit var video_preview: VideoView
    var videoUri: Uri? = null
    var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newspreview)
        BindUi()
        val bundle: Bundle? = intent.extras
         newsTitleTxt = bundle?.getString("NEWS_TITLE")
         selectedItem = bundle?.getString("SELECTED_CATEGORY")
         newsContentTxt = bundle?.getString("NEWS_CONTENT")
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