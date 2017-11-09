package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feup.ei12078.shopandpay.adapters.ProductListItemAdapter;
import com.feup.ei12078.shopandpay.data_classes.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartActivity extends AppCompatActivity {

    private String token;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.user_token), null);


        Intent intent = getIntent();
        String url = AppUtils.baseUrl + "cart-list/";

        if(queue == null)
            queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<Product> productList = new ArrayList<Product>();
                            ListView cartListView = (ListView) findViewById(R.id.shopping_cart_listview);

                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                Product p = new Product();
                                p.setName(jsonobject.getString("name"));
                                p.setImage_url(jsonobject.getString("image_url"));
                                p.setBrand(jsonobject.getString("brand"));
                                p.setCategory(jsonobject.getString("category"));
                                p.setCpid(jsonobject.getString("cpid"));
                                p.setPrice(jsonobject.getString("price"));
                                productList.add(p);
                            }
                            ProductListItemAdapter adapter = new ProductListItemAdapter(productList, ShoppingCartActivity.this);
                            cartListView.setAdapter(adapter);
                            /*
                            //JSONObject json = new JSONObject(response);
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
*/
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


        //ProductListItemAdapter adapter = new ProductListItemAdapter(productList, this);
       // cartListView.setAdapter(adapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }






}
