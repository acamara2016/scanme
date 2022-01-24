package com.acamara.scanme.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.acamara.scanme.data_models.Product_model
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val DATABASENAME = "Product.db"
val TABLENAME = "Products"
val COL_NAME = "title"
val COL_BARCODE = "barcode"
val COL_PHOTO_URL = "photo_url"
val COL_DATE_CREATED = "date_created"
val COL_CART_BELONGING = "cart_belonging"
val COL_PRODUCT_TYPE = "type"
val COL_ID = "id"
class Offline_Handler(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
        1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLENAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_NAME + " VARCHAR(256)," + COL_BARCODE + " VARCHAR(256),"  + COL_PHOTO_URL + " VARCHAR(256),"  + COL_DATE_CREATED + " VARCHAR(256)," + COL_PRODUCT_TYPE + " VARCHAR(256),"+ COL_CART_BELONGING+" VARCHAR(256))"
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //onCreate(db);
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertData(pro: Product_model) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)
        contentValues.put(COL_NAME, pro.title)
        contentValues.put(COL_BARCODE, pro.barcode)
        contentValues.put(COL_PHOTO_URL, pro.photo_url)
        contentValues.put(COL_DATE_CREATED, formatted)
        contentValues.put(COL_PRODUCT_TYPE, "All")
        contentValues.put(COL_CART_BELONGING, "All")
        val result = database.insert(TABLENAME, null, contentValues)
        if (result == (0).toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateData(pro: Product_model, id: String):
            Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)
        contentValues.put(COL_NAME, pro.title)
        contentValues.put(COL_BARCODE, pro.barcode)
        contentValues.put(COL_PHOTO_URL, pro.photo_url)
        contentValues.put(COL_DATE_CREATED, formatted)
        contentValues.put(COL_PRODUCT_TYPE, "All")
        contentValues.put(COL_CART_BELONGING, "All")
        db.update(TABLENAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun markToFavorite(pro: Product_model, id: String):
            Boolean {
        val db = this.writableDatabase
        println("Marking note to favorites")
        val contentValues = ContentValues()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)
        contentValues.put(COL_NAME, pro.title)
        contentValues.put(COL_BARCODE, pro.barcode)
        contentValues.put(COL_PHOTO_URL, pro.photo_url)
        contentValues.put(COL_DATE_CREATED, formatted)
        contentValues.put(COL_PRODUCT_TYPE, "All")
        contentValues.put(COL_CART_BELONGING, "All")
        db.update(TABLENAME, contentValues, "ID = ?", arrayOf(id))
        return true
    }
    fun readData(): ArrayList<Product_model> {
        val list: ArrayList<Product_model> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val pro = Product_model()
                pro.id = result.getString(result.getColumnIndex(COL_ID)).toString()
                pro.title = result.getString(result.getColumnIndex(COL_NAME))
                pro.barcode = result.getString(result.getColumnIndex(COL_BARCODE))
                pro.photo_url = result.getString(result.getColumnIndex(COL_PHOTO_URL)).toString()
                pro.type = result.getString(result.getColumnIndex(COL_PRODUCT_TYPE)).toString()
                pro.cart_belonging = result.getString(result.getColumnIndex(COL_CART_BELONGING)).toString()
                pro.date_created = result.getString(result.getColumnIndex(COL_DATE_CREATED)).toString()
                list.add(pro)
            }
            while (result.moveToNext())
        }
        return list
    }
    fun readByCategory(category: String): ArrayList<Product_model> {
        val list: ArrayList<Product_model> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val pro = Product_model()
                pro.id = result.getString(result.getColumnIndex(COL_ID)).toString()
                pro.title = result.getString(result.getColumnIndex(COL_NAME))
                pro.barcode = result.getString(result.getColumnIndex(COL_BARCODE))
                pro.photo_url = result.getString(result.getColumnIndex(COL_PHOTO_URL)).toString()
                pro.type = result.getString(result.getColumnIndex(COL_PRODUCT_TYPE)).toString()
                pro.cart_belonging = result.getString(result.getColumnIndex(COL_CART_BELONGING)).toString()
                pro.date_created = result.getString(result.getColumnIndex(COL_DATE_CREATED)).toString()
                if (pro.type.equals(category))
                    list.add(pro)
            }
            while (result.moveToNext())
        }
        return list
    }
    fun readDataByID(id:String): ArrayList<Product_model> {
        val list: ArrayList<Product_model> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val pro = Product_model()
                pro.id = result.getString(result.getColumnIndex(COL_ID)).toString()
                pro.title = result.getString(result.getColumnIndex(COL_NAME))
                pro.barcode = result.getString(result.getColumnIndex(COL_BARCODE))
                pro.photo_url = result.getString(result.getColumnIndex(COL_PHOTO_URL)).toString()
                pro.type = result.getString(result.getColumnIndex(COL_PRODUCT_TYPE)).toString()
                pro.cart_belonging = result.getString(result.getColumnIndex(COL_CART_BELONGING)).toString()
                pro.date_created = result.getString(result.getColumnIndex(COL_DATE_CREATED)).toString()
                if (pro.id.equals(id))
                    list.add(pro)
            }
            while (result.moveToNext())
        }
        return list
    }
}