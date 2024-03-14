package com.example.suddinews

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentTransaction
import com.example.suddinews.databinding.ActivityNewsBinding



class News : AppCompatActivity() {
    private lateinit var bind: ActivityNewsBinding
    private lateinit var trans: FragmentTransaction
    lateinit var dwrTgl: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(bind.root)
        trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragments, RecentFragment()).commit()

        // Hide the action bar
        supportActionBar?.hide()

        dwrTgl = ActionBarDrawerToggle(this, bind.nd, R.string.open, R.string.close)
        bind.nd.addDrawerListener(dwrTgl)
        dwrTgl.syncState()

        bind.nv.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.about -> {
                    trans = supportFragmentManager.beginTransaction()
                    trans.replace(R.id.fragments, AboutFragment()).commit()
                    true
                }
                else -> true
            }
        }

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
                    true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (dwrTgl.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }
}
