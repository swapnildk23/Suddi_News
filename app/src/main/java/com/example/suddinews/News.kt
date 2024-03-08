package com.example.suddinews

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.example.suddinews.databinding.ActivityNewsBinding

@Suppress("DEPRECATION")
class News : AppCompatActivity() {
    private lateinit var bind: ActivityNewsBinding
    private lateinit var trans: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.activity_news)
        trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragments, RecentFragment()).commit()
        bind.bn.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.recent -> {
                    trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.fragments, RecentFragment()).commit()
                    true
                }
                R.id.category -> {
                    trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.fragments, CategoryFragment()).commit()
                    true
                }
                R.id.video -> {
                    trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.fragments, VideoFragment()).commit()
                    true
                }
                else -> {
                    trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.fragments, FavoritesFragment()).commit()
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }
}
