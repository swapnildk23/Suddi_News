package com.example.suddinews

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class newsupload : AppCompatActivity() {
    val items = arrayOf(
        "Politics", "World", "Entertainment", "Sports", "Travel",
        "Technology", "Recent", "Business", "Health", "Video"
    )
    lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var news_title: EditText
    lateinit var news_content: EditText
    lateinit var btn_image: AppCompatButton
    lateinit var btn_preview: AppCompatButton
    lateinit var image_preview: ImageView
    lateinit var video_preview: VideoView
    lateinit var btn_video: AppCompatButton
    var selectedItem: String? = null
    var imageUri: Uri? = null
    var videoUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            image_preview.visibility=View.VISIBLE
            imageUri = data?.data
            image_preview.setImageURI(imageUri)
            video_preview.visibility = View.GONE // Hide video preview if any
        }
    }

    private val pickVideo = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            videoUri = data?.data
            video_preview.visibility=View.VISIBLE
            video_preview.setVideoURI(videoUri)
            video_preview.start()
            image_preview.visibility = View.GONE // Hide image preview if any
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsupload)
        BindUi()
        val adapter = ArrayAdapter<String>(this, R.layout.category_list_item, items)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnItemClickListener { parent: AdapterView<*>, view, position, id ->
            selectedItem = parent.getItemAtPosition(position).toString()
            if (selectedItem.equals("Video")) {
                btn_image.visibility = View.INVISIBLE
                btn_image.isEnabled = false
                btn_video.visibility = View.VISIBLE
                btn_video.isEnabled = true
            } else {
                btn_video.visibility = View.INVISIBLE
                btn_video.isEnabled = false
                btn_image.visibility = View.VISIBLE
                btn_image.isEnabled = true
            }
        }
        btn_image.setOnClickListener {
            if (checkPermission()) {
                openGallery()
            } else {
                requestPermission()
            }
        }
        btn_video.setOnClickListener {
            if (checkVideoPermission()) {
                openGalleryForVideos()
            } else {
                requestVideoPermission()
            }
        }
        autoCompleteTextView.setOnDismissListener {
            val enteredText = autoCompleteTextView.text.toString()
            if (!items.contains(enteredText)) {
                autoCompleteTextView.text = null
            }
        }
        btn_preview.setOnClickListener {
            Log.d("SI",selectedItem.toString())
            if (TextUtils.isEmpty(news_title.text.toString())) {
                Toast.makeText(this, "Please Enter News Title", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(news_content.text.toString())) {
                Toast.makeText(this, "Please Enter News Content", Toast.LENGTH_SHORT).show()
            } else if (selectedItem.isNullOrEmpty()) {
                Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
            } else {
                val newsTitleTxt = news_title.text.toString()
                val newsContentTxt = news_content.text.toString()
                if(selectedItem.equals("Video"))
                {
                    val bundle = Bundle().apply {
                        // Putting extras into the Bundle
                        putString("NEWS_TITLE", newsTitleTxt)
                        putString("SELECTED_CATEGORY", selectedItem.toString())
                        putString("NEWS_CONTENT", newsContentTxt)
                        putString("VIDEO_URI", videoUri.toString())
                    }
                    val intent = Intent(this, newspreview::class.java)
                        intent.putExtras(bundle)
                    startActivity(intent)
                }
                else{
                    val bundle = Bundle().apply {
                        // Putting extras into the Bundle
                        putString("NEWS_TITLE", newsTitleTxt)
                        putString("SELECTED_ITEM", selectedItem)
                        putString("NEWS_CONTENT", newsContentTxt)
                        putString("IMAGE_URI", imageUri.toString())
                    }

                    // Creating the Intent
                    val intent = Intent(this, newspreview::class.java)
                        // Putting the Bundle into the Intent
                        intent.putExtras(bundle)

                    // Starting the next activity with the intent
                    startActivity(intent)
                }
            }
        }
    }

    private fun BindUi() {
        autoCompleteTextView = findViewById(R.id.auto_complete_text)
        news_content = findViewById(R.id.new_news_content)
        news_title = findViewById(R.id.new_news_title)
        btn_image = findViewById(R.id.btn_upload_image)
        btn_preview = findViewById(R.id.btn_preview)
        image_preview = findViewById(R.id.image_preview)
        btn_video = findViewById(R.id.btn_upload_video)
        video_preview = findViewById(R.id.video_preview)
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkVideoPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_VIDEO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun requestVideoPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
            VIDEO_PERMISSION_REQUEST_CODE
        )
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }

    private fun openGalleryForVideos() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        pickVideo.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == VIDEO_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryForVideos()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
        private const val VIDEO_PERMISSION_REQUEST_CODE = 102
    }
}
