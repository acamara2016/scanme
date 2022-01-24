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
import java.time.format.DateTimeFormatter;
/**
 * InitialSearchAPI.kt
 * handle the first search of a scanned product, this request only return the name and picture of a product, so the result need to be pass into PriceComparisonAPI.kt 
 * This class takes in the upc of a newly scanned product and search and store new information found about the product
 * into the firebase slot.
 * Sample code from: https://developers.google.com/custom-search/v1/overview
 */
public class InitialSearchAPI extends AsyncTask<URL, Integer, String> {
    private final String barcode;
    public InitialSearchAPI(String barcode){
        this.barcode = barcode;
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
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                int limit = 0;
                while ((line = rd.readLine()) != null) {
                    sb.append(line + "\n");
                    limit++;
                }
                rd.close();
                connection.disconnect();
                result = sb.toString();
                return result;
            }else{
                Log.e(TAG, "Error with the API");
                result = "Error";
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
        String jsonString =s ; //assign your JSON String here

        try {
            JSONObject jObject = new JSONObject(s);
            sendResultToFirebase(barcode,
                    "",
                    "",
                    jObject.getJSONArray("items").getJSONObject(0).get("title").toString(),
                    jObject.getJSONArray("items").getJSONObject(0).get("link").toString(),
                    "",
                    jObject.getJSONArray("items").getJSONObject(0).get("snippet").toString()
                    );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void sendResultToFirebase(String barcode, String photo_url, String price, String product_name, String product_link, String product_type,String snippet){
        Product_model pm = new Product_model();
        pm.setTitle(product_name);
        pm.setBarcode(barcode);
        pm.setPhoto_url(photo_url);
        pm.setLink(product_link);
        pm.setType(product_type);
        pm.setSnippet(snippet);
        //TODO implement Java for catching current date
        fb.storeItem(pm);
        fb.storeItemToUserAccount(pm,fb.getUser().getUid().toString());
        fb.storeBestPrice(price,barcode,fb.getUser().getUid().toString());
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("Not executed yet");
    }
}
