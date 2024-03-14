package com.example.suddinews

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.suddinews.databinding.ActivityNewsBinding


@Suppress("DEPRECATION")
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
        dwrTgl = ActionBarDrawerToggle(this, bind.nd, R.string.open, R.string.close)
        bind.nd.addDrawerListener(dwrTgl)
        dwrTgl.syncState()
        val dlg = Dialog(this)
        dlg.setContentView(R.layout.dialog_box)
        val cls: TextView =dlg.findViewById(R.id.close)
        bind.drawer.setOnClickListener {
            bind.nd.openDrawer(GravityCompat.START)
        }
        bind.nv.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.about->{
                    dlg.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                    dlg.setCancelable(false)
                    cls.setOnClickListener {
                        dlg.dismiss()
                    }
                    dlg.show()
                    true
                }
                R.id.logout->{
                    //code for logout logic
                    true
                }
                else -> {
                    dlg.dismiss()
                    true
                }
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
}
