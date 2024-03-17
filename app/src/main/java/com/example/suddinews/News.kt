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
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class News : AppCompatActivity() {
    private lateinit var bind: ActivityNewsBinding
    private lateinit var trans: FragmentTransaction
    lateinit var dwrTgl: ActionBarDrawerToggle
    lateinit var userId: String
    private val firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()
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
        val dlgso=Dialog(this)
        dlgso.setContentView(R.layout.logout_dialog)
        val ys:TextView=dlgso.findViewById(R.id.yes)
        val no:TextView=dlgso.findViewById(R.id.no)
        val cls: TextView =dlg.findViewById(R.id.close)
        bind.drawer.setOnClickListener {
            bind.nd.openDrawer(GravityCompat.START)
        }
        val currentUser = firebaseAuth.currentUser;
        if (currentUser != null) {
            userId = currentUser.uid
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
                    dlgso.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                    dlgso.setCancelable(false)
                    no.setOnClickListener {
                        dlgso.dismiss()
                    }
                    ys.setOnClickListener {
                        // code for logout
//                        firebaseAuth.signOut()
                        dlgso.dismiss()
                        finishAffinity()
                    }
                    dlgso.show()
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
