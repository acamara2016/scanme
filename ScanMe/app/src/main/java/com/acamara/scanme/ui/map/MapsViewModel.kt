package com.acamara.scanme.ui.map

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acamara.scanme.data_models.Product_model
import com.acamara.scanme.database.FirebaseHelper
import com.acamara.scanme.database.Offline_Handler
import com.acamara.scanme.ui.home.HomeViewModel


class MapsViewModel(application: Application) : AndroidViewModel(application) {
    private val fb = FirebaseHelper()
    private val offline_db = Offline_Handler(application.applicationContext)
    val productList : LiveData<List<Product_model>>
    init {
        if (isConnected(application.applicationContext)){
            productList = fb.getAtlanticSuperStore()
        }
        else
            productList = setValue(offline_db.readData())
    }

    private fun isConnected(context: Context): Boolean {
        var connected = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
        ) {
            connected = true
        }
        return connected
    }
    private fun setValue(list: ArrayList<Product_model>): LiveData<List<Product_model>>{
        var data = MutableLiveData<List<Product_model>>()
        data.value = list
        return data
    }
    private fun synchronize(list : LiveData<List<Product_model>>){
        for (i in 0 until list.value?.size!!) {
            offline_db.insertData(list.value!![i])
        }
    }


}