package com.example.suddinews

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.suddinews.databinding.ActivityNewsBinding

@Suppress("DEPRECATION")
class News : AppCompatActivity() {
    val bind: ActivityNewsBinding =DataBindingUtil.setContentView<ActivityNewsBinding>(this,R.layout.activity_news)
    val trans=supportFragmentManager.beginTransaction()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val trans=supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragments,RecentFragment())
        bind.bn.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.recent->{ trans.replace(bind.fragments.id,RecentFragment())
                                            true}
                R.id.category->{trans.replace(R.id.fragments,CategoryFragment())
                                            true}
                R.id.video->{trans.replace(bind.fragments.id,RecentFragment())
                                            true}
                else -> { Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                                true }
            }
        }

    }
}