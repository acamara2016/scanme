package com.acamara.scanme.ui.product

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.CheckBox
import com.acamara.scanme.Homepage
import com.acamara.scanme.R
import com.acamara.scanme.api.PriceComparisonAPI
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.Request

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val intent: Intent = this.intent
        supportActionBar?.hide()
        if(!intent.getStringExtra("image").isNullOrBlank())
            Picasso.get().load(intent.getStringExtra("image")).into(product_image_view)
        barcode_view.text = intent.getStringExtra("title")
        toolbar3.title= intent.getStringExtra("title")
        product_price_view.text = intent.getStringExtra("price")
        val termsLink ="<html><a href=\""+intent.getStringExtra("link")+"\">Order online (Click here to find the best deal online)</a>"
        go_to_link.setText(Html.fromHtml(termsLink))
        go_to_link.setMovementMethod(LinkMovementMethod.getInstance())
        if (intent.getStringExtra("image").isNullOrBlank())
        {
            GlobalScope.async {
                PriceComparisonAPI().getProduct(barcode_view,intent.getStringExtra("barcode").toString())
            }
        }
        if(intent.getStringExtra("in_store").equals("true"))
            textView9.visibility = View.VISIBLE
        val intent2 = Intent(this@ProductActivity, Homepage::class.java)
        toolbar3.setNavigationOnClickListener { startActivity(intent2) }
        /**
         * https://developers.google.com/maps/documentation/urls/android-intents#kotlin_5
         */
        val gmmIntentUri = Uri.parse("geo:44.6464,-63.57291?q=Atlantic Superstore")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        textView9.setOnClickListener {
            startActivity(mapIntent)
        }


    }

}