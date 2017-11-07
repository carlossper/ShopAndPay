package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mDescriptionTextView;
    private TextView mBrandTextView;
    private TextView mCategoryTextView;
    private ImageView mProductImageView;
    private String productId;
    private String productName;
    private String productDescription;
    private String productBrand;
    private String productCategory;
    private String productURL;
    private String productPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        final String token = sharedPref.getString(getString(R.string.user_token), null);


        Intent intent = getIntent();

        productId = intent.getStringExtra("id");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = AppUtils.baseUrl + "products/" + productId;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            productName = json.getString("name");
                            productDescription = json.getString("description");
                            productBrand = json.getString("brand");
                            productCategory = json.getString("category");
                            productURL = json.getString("image_url");
                            productPrice = json.getString("price") + " €";


                            mNameTextView = (TextView) findViewById(R.id.product_product_name);
                            mNameTextView.setText(productName);

                            mPriceTextView = (TextView) findViewById(R.id.product_product_price);
                            mPriceTextView.setText(productPrice);

                            mDescriptionTextView = (TextView) findViewById(R.id.product_product_description);
                            mDescriptionTextView.setText(productDescription);

                            mBrandTextView = (TextView) findViewById(R.id.product_product_brand);
                            mBrandTextView.setText(productBrand);

                            mCategoryTextView = (TextView) findViewById(R.id.product_product_category);
                            mCategoryTextView.setText(productCategory);

                            mProductImageView = (ImageView) findViewById(R.id.product_product_image);
                            Picasso.with(getApplicationContext()).load(productURL).into(mProductImageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mTextView.setText("That didn't work!");
                NetworkResponse networkResponse = error.networkResponse;

                if(networkResponse == null){
                    //networkResponseDialog();
                    //TODO
                }
                else if (networkResponse != null && networkResponse.statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    // HTTP Status Code: 401 Unauthorized
                    //TODO
                }
                else{
                    //TODO
                }
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Token " + token);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
