package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class ProductListActivity extends AppCompatActivity {


    private String token;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Intent intent = getIntent();
        String catId = intent.getStringExtra("catId");

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.user_token), null);


        String url = AppUtils.baseUrl + "products-by-category/" + catId + "/";

        if(queue == null)
            queue = Volley.newRequestQueue(this);

        //Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<Product> productList = new ArrayList<Product>();
                            ListView productListView = (ListView) findViewById(R.id.product_list_view);

                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                Product p = new Product();
                                p.setId(jsonobject.getString("id"));
                                p.setName(jsonobject.getString("name"));
                                p.setImage_url(jsonobject.getString("image_url"));
                                p.setBrand(jsonobject.getString("brand"));
                                p.setCategory(jsonobject.getString("category"));
                                p.setPrice(jsonobject.getString("price"));
                                productList.add(p);
                            }
                            ProductListItemAdapter adapter = new ProductListItemAdapter(productList, ProductListActivity.this);
                            productListView.setAdapter(adapter);


                            productListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                @Override
                                public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                                    String prodId = (String) adapter.getItemAtPosition(position);

                                    Intent intent = new Intent(ProductListActivity.this, ProductActivity.class);
                                    Log.i("TAG6", prodId);
                                    intent.putExtra("id", prodId);

                                    startActivity(intent);
                                }
                            });


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
