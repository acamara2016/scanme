package com.acamara.scanme.api

import android.view.View
import com.acamara.scanme.data_models.Product_model
import com.acamara.scanme.database.FirebaseHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * PriceComparisonAPI.kt
 * handle the second search after the product is scanned and initially search with InitialSearchAPI
 * This class takes in the upc code of an already scanned product and search and store new information found about the product
 * into the firebase slot.
 * Sample code from: https://rapidapi.com/blog/build-android-app-with-api/
 */

class PriceComparisonAPI {
    var fb = FirebaseHelper()

    suspend fun getProduct(view: View, upc: String) {
        try {
            val result = GlobalScope.async {
                api("https://ebay-com.p.rapidapi.com/products/" + upc,upc)
            }.await()
            onResponse(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun onResponse(result: String?) {
        try {
            val resultJson = JSONObject(result)
            System.out.println(resultJson)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    protected fun SendResultToFirebase(barcode: String?,photo_url:String?,price:String?,product_name: String?,product_link:String?,product_type:String?) {
        val product_model = Product_model()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)
        product_model.title = product_name!!
        product_model.barcode = barcode!!
        //product_model.date_created = request_created_time
        product_model.type = product_type!!
        product_model.date_created = formatted!!
        product_model.photo_url = photo_url!!
        product_model.second_parsed = "true"
        product_model.cart_belonging = ""
        product_model.link = product_link!!
        fb.storeItem(product_model) //send to cache
        fb.storeItemToUserAccount(
            product_model,
            fb.getUser()?.getUid().toString()
        ) //send to user's private cache
        fb.storeBestPrice(price!!,barcode,fb.getUser()?.uid.toString())
    }
    private fun api(apiUrl: String,upc:String):String?{
        var result: String? = ""
        val url: URL;
        var connection: HttpURLConnection? = null
        try {
            url = URL(apiUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("x-rapidapi-host", "ebay-com.p.rapidapi.com")
            connection.setRequestProperty(
                "x-rapidapi-key",
                "41741a2d22msh52ecd516bf463a7p11023bjsn62a91702f4f0"
            )
            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded")
            connection.requestMethod = "GET"
            val `in` = connection.inputStream
            val reader = InputStreamReader(`in`)
            // read the response data
            var data = reader.read()
            while (data != -1) {
                val current = data.toChar()
                result += current
                data = reader.read()
            }

            var jsonObject  = JSONObject(result)
            var jsonObject2  = JSONObject(result)
            var best_price = jsonObject.get("FormattedBestPrice").toString()
            var offers = jsonObject.getJSONArray("Offers")
            var media = jsonObject.getJSONObject("Media")
            var category = jsonObject2.getJSONArray("ProductCategories")
            var parsed_title = jsonObject.get("Title").toString()
            System.out.println(category[0].toString())
            var jsonObject3 = JSONObject(category[0].toString())
            var jsonObject4 = JSONObject(offers[0].toString())
            var category_parsed = jsonObject3.get("FullName").toString()
            var category_parsed_2 = ""
            val ch = '>'

            for (i in 0..category_parsed.length - 1) {
                if (ch == category_parsed[i]) {
                    break
                }else
                    category_parsed_2+=category_parsed[i]
            }
            /**
             * Add new category to user's account
             */
            fb.addNewCategory(category_parsed_2)
            SendResultToFirebase(upc,
                    media.get("XImage").toString(),
                    best_price,
                    parsed_title,
                    jsonObject4.getString("Link").toString(),
                    category_parsed_2
            )

            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
