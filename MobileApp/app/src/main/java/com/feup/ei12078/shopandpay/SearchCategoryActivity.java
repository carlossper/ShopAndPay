package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.feup.ei12078.shopandpay.adapters.SimpleTextListItemAdapter;
import com.feup.ei12078.shopandpay.data_classes.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCategoryActivity extends AppCompatActivity {

    private String token;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_category);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.user_token), null);

        String url = AppUtils.baseUrl + "categories/";

        if(queue == null)
            queue = Volley.newRequestQueue(this);

        //Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            List<Category> categoryList = new ArrayList<Category>();
                            ListView categoryListView = (ListView) findViewById(R.id.category_list_view);

                            JSONObject jsonObjectTemp = new JSONObject(response);
                            JSONArray jsonarray = jsonObjectTemp.getJSONArray("results");


                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                Category p = new Category(jsonobject.getString("id"), jsonobject.getString("name") + "'s");
                                categoryList.add(p);
                            }
                            SimpleTextListItemAdapter adapter = new SimpleTextListItemAdapter(categoryList, SearchCategoryActivity.this);
                            categoryListView.setAdapter(adapter);

                            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                @Override
                                public void onItemClick(AdapterView<?>adapter, View v, int position, long id){
                                    String catId = (String) adapter.getItemAtPosition(position);

                                    Intent intent = new Intent(SearchCategoryActivity.this, ProductListActivity.class);
                                    intent.putExtra("catId", catId);

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
