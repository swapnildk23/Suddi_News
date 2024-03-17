package com.example.suddinews

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class categorynews : AppCompatActivity() {
    lateinit var rviewC:RecyclerView
    lateinit var noNewsTxt:TextView
    lateinit var categoryName:TextView
    lateinit var selectedCategory: String
    val firedatabase:FirebaseDatabase=FirebaseDatabase.getInstance()
    private val data: ArrayList<Dclass> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorynews)
        BindUi()
        rviewC.layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        val databaseReference:DatabaseReference=firedatabase.reference

        val bundle:Bundle?=intent.extras
        selectedCategory=bundle?.getString("SELECTED_CATEGORY").toString()
        Log.d("Category in news","$selectedCategory")
        val categoryReference:DatabaseReference=databaseReference.child("Categories").child(selectedCategory)
        if (!selectedCategory.equals(null)){
            categoryName.text=selectedCategory+" News"
            categoryReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    data.clear()
                    if(snapshot.hasChildren())
                    {
                        for (eachNews in snapshot.children){
                            val header=eachNews.child("Header").value.toString()
                            lateinit var img:String
                            val content=eachNews.child("Content").value.toString()
                            val vid:String?=null
                            val newsId=eachNews.key
                            Log.d("Category NewsID","$newsId")
                            if (eachNews.hasChild("ImageURI"))
                            {
                                img=eachNews.child("ImageURI").value.toString()
                            }
                            else{
                                img= null.toString()
                            }
                            val count=eachNews.child("count").value.toString().toLong()
                            val dataclass = Dclass(img, header, content, vid,newsId,count)
                            data.add(dataclass)
                        }
                        rviewC.adapter=MyAdapter(data,applicationContext)
                    }else{
                        noNewsTxt.visibility= View.VISIBLE
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }
    private fun BindUi() {
        rviewC=findViewById(R.id.rviewCategory)
        noNewsTxt=findViewById(R.id.noCategoryNewsTxt)
        categoryName=findViewById(R.id.category_header)
    }
}