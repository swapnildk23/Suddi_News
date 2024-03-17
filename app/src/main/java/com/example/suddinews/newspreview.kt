package com.example.suddinews

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class newspreview : AppCompatActivity() {
    lateinit var newsTitleTxt: String
    lateinit var selectedItem: String
    lateinit var newsContentTxt: String
    lateinit var sh: SharedPreferences
    lateinit var news_title: TextView
    lateinit var news_content: TextView
    lateinit var btn_upload: AppCompatButton
    lateinit var image_preview: ImageView
    lateinit var video_preview: VideoView
    var videoUri: Uri? = null
    var imageUri: Uri? = null
    var nc: Long = 0
    var totalCount:Long=0
    val sr = Firebase.storage.reference
    val firedata: FirebaseDatabase = FirebaseDatabase.getInstance()
    var link: String = ""
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var internetTextView: TextView
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var internetConnected = false
    private val mainActivityStarted = false
    private lateinit var scrollView:ScrollView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newspreview)
        sh = getSharedPreferences(getString(R.string.shpref), Context.MODE_PRIVATE)
        BindUi()
        startCheckingInternetConnectivity()
        val databaseReference =firedata.reference
        val rn = firedata.reference.child("Categories")
        val bundle: Bundle? = intent.extras
        newsTitleTxt = bundle?.getString("NEWS_TITLE").toString()
        selectedItem = bundle?.getString("SELECTED_ITEM").toString()
        newsContentTxt = bundle?.getString("NEWS_CONTENT").toString()
        Log.d("Selected after bundle", selectedItem)
        if (selectedItem.equals("Video")) {
            val params = news_title.layoutParams as RelativeLayout.LayoutParams
            params.addRule(RelativeLayout.BELOW, R.id.admin_news_video)
            val videoUriString = bundle?.getString("VIDEO_URI")
            if (videoUriString == null) {
                Toast.makeText(this, "Video URI is null", Toast.LENGTH_SHORT).show()
            } else {
                videoUri = Uri.parse(videoUriString)
                image_preview.visibility = View.GONE
                video_preview.visibility = View.VISIBLE
                news_title.text = newsTitleTxt
                news_content.text = newsContentTxt
                val mediaController = MediaController(this)
                mediaController.setAnchorView(video_preview)
                video_preview.setMediaController(mediaController)
                video_preview.setVideoURI(videoUri)
                video_preview.start()
            }
        } else {
            val imageUriString = bundle?.getString("IMAGE_URI")
            if (imageUriString == null) {
                Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
            } else {
                imageUri = Uri.parse(imageUriString)
                video_preview.visibility = View.GONE
                image_preview.visibility = View.VISIBLE
                news_title.text = newsTitleTxt
                news_content.text = newsContentTxt
                image_preview.setImageURI(imageUri)
            }
        }

        btn_upload.setOnClickListener {
            val nref = rn.child(selectedItem)
            Log.d("SelectedItem inside btn", selectedItem)
            nref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        sh.edit().clear().apply()
                        nc = snapshot.childrenCount
                        val totalCountReference = snapshot.ref.parent?.parent?.child("TotalCount")

                        // Get the Task<DataSnapshot> for fetching TotalCount value
                        val totalCountTask = totalCountReference?.get()

                        // Wait for the task to complete synchronously
                        val totalCountSnapshot = Tasks.await(totalCountTask!!)

                        // Extract the value of TotalCount
                        totalCount = totalCountSnapshot.value.toString().toLong()
                        Log.d("TotalCount", totalCount.toString())
                        Log.d("nc after taken", "$nc")
                        link = "${selectedItem[0]}${selectedItem[1]}N${nc + 1}"
                        Log.d("Outside While", "$nc $link")
                        while (snapshot.hasChild(link)) {
                            nc++
                            link = "${selectedItem[0]}${selectedItem[1]}N${nc + 1}"
                            Log.d("Inside While", "$nc $link")
                        }
                        Log.d("link inside", "$link")
                        sh.edit().putString("addr", link).apply()

                        link = sh.getString("addr", "").toString()
                        Log.d("link", "$link")
                        Log.d("nc before link", "$nc $selectedItem")



                        Log.d("COUNT", "$link")
                        val ref = nref.child(link)
                        if (selectedItem == "Video") {
                            videoUri?.let { uri ->
                                sr.child(link).putFile(uri).addOnSuccessListener { uploadTask ->
                                    uploadTask.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                                        ref.child("VideoURI").setValue(downloadUri.toString())
                                        ref.child("ImageURI").setValue(downloadUri.toString())
                                    }.addOnFailureListener { exception ->
                                        Log.e(
                                            "Firebase Storage",
                                            "Error getting download URL: $exception"
                                        )
                                        // Handle failure to get download URL
                                    }
                                }.addOnFailureListener { exception ->
                                    Log.e("Firebase Storage", "Error uploading file: $exception")
                                    Toast.makeText(
                                        this@newspreview,
                                        "Error uploading file: $exception",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Handle failure to upload file
                                }
                            }
                        } else {
                            imageUri?.let { uri ->
                                sr.child(link).putFile(uri).addOnSuccessListener { uploadTask ->
                                    uploadTask.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                                        ref.child("ImageURI").setValue(downloadUri.toString())
                                    }.addOnFailureListener { exception ->
                                        Log.e(
                                            "Firebase Storage",
                                            "Error getting download URL: $exception"
                                        )
                                        // Handle failure to get download URL
                                    }
                                }.addOnFailureListener { exception ->
                                    Log.e("Firebase Storage", "Error uploading file: $exception")
                                    Toast.makeText(
                                        this@newspreview,
                                        "Error uploading file: $exception",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Handle failure to upload file
                                }
                            }
                        }
                        ref.child("Content").setValue(newsContentTxt)
                        ref.child("Header").setValue(newsTitleTxt)
                        Log.d("totalcount before", totalCount.toString())
                        totalCount += 1
                        ref.child("count").setValue(totalCount)
                        databaseReference.child("TotalCount").setValue(totalCount)
                        Log.d("totalcount after", totalCount.toString())
                    }
                    Toast.makeText(this@newspreview, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase Database", "Error accessing database: $error")
                    // Handle database access error
                }
            })
        }
    }
    private fun startCheckingInternetConnectivity() {
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                checkInternetConnectivity()
                handler.postDelayed(this, 1000) // Check every second
            }
        }
        handler.postDelayed(runnable, 1000) // Initial delay
    }

    private fun stopCheckingInternetConnectivity() {
        handler.removeCallbacks(runnable)
    }
    private fun checkInternetConnectivity() {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            var network: Network? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                network = connectivityManager.activeNetwork
            }
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            internetConnected =
                capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ))
            if (internetConnected && !mainActivityStarted) {
                stopCheckingInternetConnectivity() // Stop checking once internet connected
                loadingProgressBar!!.visibility = View.INVISIBLE
                internetTextView!!.visibility = View.INVISIBLE
                btn_upload.isEnabled=true
                scrollView.visibility=View.VISIBLE
            } else {
                loadingProgressBar!!.visibility = View.VISIBLE
                internetTextView!!.visibility = View.VISIBLE
                btn_upload.isEnabled=false
                scrollView.visibility=View.INVISIBLE
            }
        }
    }
    private fun BindUi() {
        news_content = findViewById(R.id.admin_news_content)
        news_title = findViewById(R.id.admin_news_header)
        btn_upload = findViewById(R.id.btn_admin_news_upload)
        image_preview = findViewById(R.id.admin_news_image)
        video_preview = findViewById(R.id.admin_news_video)
        loadingProgressBar = findViewById(R.id.loadingProgressBar3)
        internetTextView = findViewById(R.id.internetTextView3)
        scrollView=findViewById(R.id.scrollView)
    }
}
