package com.acamara.scanme.getStarted

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.acamara.scanme.R
import com.acamara.scanme.getStarted.adapter.CustomPagerAdapter
import com.acamara.scanme.registration.ui.Registration
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.android.synthetic.main.activity_terms_conditions.*


class TermsConditions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_conditions)
        val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.dots_indicator)
        viewPager.adapter = CustomPagerAdapter(this)
        dotsIndicator.setViewPager(viewPager)
        intent = Intent(this@TermsConditions,  Registration::class.java)
        on_boarding_btn.setOnClickListener { startActivity(intent) }
    }
}