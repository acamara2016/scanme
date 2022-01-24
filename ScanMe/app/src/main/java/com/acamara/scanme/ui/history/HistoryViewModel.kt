package com.acamara.scanme.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acamara.scanme.data_models.Product_model
import com.acamara.scanme.database.FirebaseHelper

class HistoryViewModel(application: Application) : AndroidViewModel(application){
private val fb = FirebaseHelper()
    val historyList: LiveData<List<Product_model>>
    var tab_name : LiveData<List<String>>

    init {
        historyList = fb.getUserProducts(FirebaseHelper().getUser()?.uid.toString())
        tab_name = fb.getTabName()
    }
}