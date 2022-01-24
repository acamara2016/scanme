package com.acamara.scanme.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acamara.scanme.data_models.Product_model
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class FirebaseHelper internal constructor() {
    private var mAuth: FirebaseAuth
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("scanned_items")


    fun getUser() : FirebaseUser? {
        return mAuth.currentUser
    }
    fun flagItemClose(barcode: String,product_name:String,product_image:String,location:String){
        myRef.child(getUser()?.uid.toString()).child(location).child(product_name).setValue(product_image)
        AlreadyFoundAround(barcode)
    }
    fun AlreadyFoundAround(barcode: String){
        myRef.child(getUser()?.uid.toString()).child(barcode).child("foundNear").setValue("true")
    }
    fun storeItemToUserAccount(pro:Product_model,uid: String){
        myRef.child(""+uid).child(pro.barcode).setValue(pro);
    }
    fun storePhotoURL(photo_url:String,barcode: String,uid: String){
        myRef.child(""+uid).child(barcode).child("photo_url").setValue(photo_url);
    }
    fun storeBestPrice(price:String,barcode: String,uid: String){
        myRef.child(""+uid).child(barcode).child("price").setValue(price);
    }
    fun storeExternalLink(link:String,barcode: String,uid: String){
        myRef.child(""+uid).child(barcode).child("link").setValue(link)
    }
    fun storeParsedTitle(title:String,barcode: String,uid: String){
        myRef.child(""+uid).child(barcode).child("title").setValue(title);
    }
    fun storeItem(pro: Product_model){
        myRef.child("cache").child(pro.barcode).setValue(pro);
    }
    fun updateProductType(upc:String,type:String){
        myRef.child(getUser()?.uid.toString()).child(upc).child("type").setValue(type)
    }
    fun addNewCategory(type:String){
        myRef.child(getUser()?.uid.toString()).child("categories").child(""+type).setValue("Found")
    }
    fun updateCacheTitle(title:String,barcode:String){
        myRef.child("cache").child(barcode).child("title").setValue(title)
    }
    fun updateFirebase(image_link:String,title:String,barcode:String,uid: String){
        System.out.println("Updating the title FIREBASE")
        myRef.child(""+uid).child(barcode).child("title").setValue(title)
        myRef.child(""+uid).child(barcode).child("photo_url").setValue(image_link)
    }
    fun sendCategoryPreference(category:String){
        myRef.child(""+getUser()?.uid.toString()).child("preferences").child(""+category).setValue("Picked")
    }
    fun getProductFromCacheByBarcode(barcode:String):Product_model{
        var data = MutableLiveData<List<Product_model>>()
        val ref = FirebaseDatabase.getInstance().reference.child("scanned_items").child("cache").child(barcode)
        val list: ArrayList<Product_model> = ArrayList<Product_model>()
        var product = Product_model()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var barcode: String = dataSnapshot.child("barcode").value.toString()
                var date_created: String = dataSnapshot.child("date_created").value.toString()
                var photo_url: String = dataSnapshot.child("photo_url").value.toString()
                var type: String = dataSnapshot.child("type").value.toString()
                var cart_belonging: String = dataSnapshot.child("cart_belonging").value.toString()
                var title: String = dataSnapshot.child("title").value.toString()
                var price: String = dataSnapshot.child("price").value.toString()
                var link: String = dataSnapshot.child("link").value.toString()
                val found:String = dataSnapshot.child("found").value.toString()
                val snippet:String = dataSnapshot.child("snippet").value.toString()
                val second_parsed:String = dataSnapshot.child("second_parsed").value.toString()
                product = Product_model("", title, price,  barcode,  photo_url, date_created, type, cart_belonging,link,found,snippet,second_parsed)
                list.add(product)
                data.value=list
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return product
    }
    fun getUserProducts(uid: String): LiveData<List<Product_model>> {
        var data = MutableLiveData<List<Product_model>>()
        val ref = FirebaseDatabase.getInstance().reference.child("scanned_items").child(uid)
        val list: ArrayList<Product_model> = ArrayList<Product_model>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (userSnapshot in dataSnapshot.children) {
                    if (!userSnapshot.key.equals("categories") && !userSnapshot.key.equals("preferences") && !userSnapshot.key.equals("Atlantic Superstore")){
                    var barcode: String = userSnapshot.child("barcode").value.toString()
                    var date_created: String = userSnapshot.child("date_created").value.toString()
                    var photo_url: String = userSnapshot.child("photo_url").value.toString()
                    var type: String = userSnapshot.child("type").value.toString()
                    var cart_belonging: String = userSnapshot.child("cart_belonging").value.toString()
                    var title: String = userSnapshot.child("title").value.toString()
                    var price: String = userSnapshot.child("price").value.toString()
                    var link: String = userSnapshot.child("link").value.toString()
                    val found:String = userSnapshot.child("foundNear").value.toString()
                    val snippet:String = userSnapshot.child("snippet").value.toString()
                    val second_parsed:String = userSnapshot.child("second_parsed").value.toString()
                    var product = Product_model("", title, price,  barcode,  photo_url, date_created, type, cart_belonging,link,found,snippet,second_parsed)

                    if (!product.title.isNullOrBlank())
                        list.add(product)
                    }
                }
                data.value = list
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return data
    }
    fun getAtlanticSuperStore(): LiveData<List<Product_model>>{
        var data = MutableLiveData<List<Product_model>>()
        val ref = FirebaseDatabase.getInstance().reference.child("scanned_items").child(getUser()?.uid.toString()).child("Atlantic Superstore")
        val list: ArrayList<Product_model> = ArrayList<Product_model>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (userSnapshot in dataSnapshot.children) {
                        val key:String = userSnapshot.key.toString()
                        System.out.println(key)
                        System.out.println(userSnapshot.value.toString().substringAfter(":\"").substringBefore("\",\"width"))
                        var product = Product_model(
                            "",
                            userSnapshot.key.toString(),
                            "",
                            "",
                            userSnapshot.value.toString().substringAfter(":\"").substringBefore("\",\"width"),
                            "",
                            "", "","","")

                        if (!product.title.isNullOrBlank())
                            list.add(product)
                }
                data.value = list
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return data
    }
    fun logout(){
        mAuth.signOut()
    }

    fun getTabName(): LiveData<List<String>> {
        var data = MutableLiveData<List<String>>()
        val ref = FirebaseDatabase.getInstance().reference.child("scanned_items").child(getUser()?.uid.toString()).child("categories")
        val list: ArrayList<String> = ArrayList<String>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    var category: String = userSnapshot.key.toString()
                    list.add(category)
                }
                data.value = list
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return data
    }
    init {
        mAuth = FirebaseAuth.getInstance()
    }
}