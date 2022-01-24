package com.acamara.scanme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import com.acamara.scanme.database.FirebaseHelper
import com.acamara.scanme.getStarted.TermsConditions

class MainActivity : AppCompatActivity() {
    private val fb = FirebaseHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({updateUI()},2000)
    }
    fun updateUI(){

        if (fb.getUser()!=null){
            intent= Intent(this@MainActivity,Homepage::class.java)
            startActivity(intent)
            finish()
        }else if (fb.getUser()==null){
            intent= Intent(this@MainActivity,TermsConditions::class.java)
            startActivity(intent)
            finish()
        }

    }
}