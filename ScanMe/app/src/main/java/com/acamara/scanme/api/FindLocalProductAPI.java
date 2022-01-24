package com.acamara.scanme.api;

import android.os.AsyncTask;
import android.util.Log;
import com.acamara.scanme.data_models.Product_model;
import com.acamara.scanme.database.FirebaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * FindLocalProductAPI.kt
 * handle the third search after the product is scanned and parsed a second time with PriceComparisonAPI.kt
 * This class takes in the title of an already scanned product and search and store new information found about the product
 * into the firebase slot.
 * Sample code from: https://developers.google.com/custom-search/v1/overview
 */

public class FindLocalProductAPI extends AsyncTask<URL, Integer, String> {
    private final String product_name, request_created_time, barcode;

    public FindLocalProductAPI(String barcode, String product_name, String request_created_time){
        this.barcode = barcode;
        this.product_name = product_name;
        this.request_created_time = request_created_time;
    }
    private static final String TAG = "searchApp";


    FirebaseHelper fb = new FirebaseHelper();
    static String result = null;
    Integer responseCode = null;
    String responseMessage = "";
    @Override
    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e){
            System.out.println("HTTP error "+e.toString());
        }
        try {
            responseCode = connection.getResponseCode();
            responseMessage = connection.getResponseMessage();
        } catch(IOException e){
            System.out.println(e);
        }
        try {

            if(responseCode == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                int limit = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                    limit++;
                }
                bufferedReader.close();
                connection.disconnect();
                result = sb.toString();
                return result;
            }else{
                result = "Error with API settings";
                return  result;
            }
        } catch (IOException e) {
            Log.e(TAG, "Http Response ERROR " + e.toString());
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        System.out.println("Progress update "+values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jObject = new JSONObject(s);
            if (jObject.getJSONArray("items").length()>0){
                JSONArray item = jObject.getJSONArray("items");
                if (jObject.getJSONArray("items")!=null){
                    System.out.println("This item was found near me");
                    FoundNearUser(
                            barcode,
                            product_name,
                            item.getJSONObject(0).getJSONObject("pagemap").getJSONArray("cse_thumbnail").getString(0)
                    );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void FoundNearUser(String barcode, String product_name, String image){
        Product_model product_model = new Product_model();
        product_model.setTitle(product_name);
        product_model.setBarcode(barcode);
        product_model.setDate_created(request_created_time);
        product_model.setType("All");
        product_model.setPhoto_url(""); //TODO replace the photo_url from template to the link from the GET request
        product_model.setCart_belonging("");
        fb.flagItemClose(barcode,product_name,image,"Atlantic Superstore");
        System.out.println("Found this product "+product_name);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Not executed yet");
    }
}
