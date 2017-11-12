package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

    private String token;

    private AddProductToCartTask mAddProductToCartTask = null;
    private RequestQueue queue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.user_token), null);


        Intent intent = getIntent();

        productId = intent.getStringExtra("id");


        String url = AppUtils.baseUrl + "product-details/" + productId + "/";

        if(queue == null)
            queue = Volley.newRequestQueue(this);

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





        Button mAddButton = (Button) findViewById(R.id.product_add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSendData();
            }
        });


    }

//#################################################


    private void attemptSendData() {
        if (mAddProductToCartTask != null) {
            return;
        }

        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            focusView.requestFocus();
        } else {
            mAddProductToCartTask = new AddProductToCartTask(productId);
            mAddProductToCartTask.execute((Void) null);
        }
    }

    public class AddProductToCartTask extends AsyncTask<Void, Void, Boolean> {

        private final String mProductId;

        AddProductToCartTask(String productId) {
            mProductId = productId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = AppUtils.baseUrl + "manage-cart/";
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("product", mProductId);
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("TAG", "Response: " + response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                            return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
                        } catch (Exception e) {
                            return Response.error(new ParseError(e));
                        }
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Token " + token);

                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAddProductToCartTask = null;

            if (success) {
                Intent mainIntent = new Intent(getApplicationContext(), BaseActivity.class);

                startActivity(mainIntent);
                ProductActivity.this.finish();


            } else {

            }


            if(!success){

            }
        }

        @Override
        protected void onCancelled() {
            mAddProductToCartTask = null;
        }
    }

}
