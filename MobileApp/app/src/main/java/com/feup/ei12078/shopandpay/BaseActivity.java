package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import com.feup.ei12078.shopandpay.barcode.BarcodeCaptureActivity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    private UserLogoutTask mLogoutTask = null;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.user_token), null);


        Button mCameraButton = (Button) findViewById(R.id.base_scan_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraRedirect();
            }
        });

        Button mProductsButton = (Button) findViewById(R.id.base_product_button);
        mProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productRedirect();
            }
        });

        Button mCartButton = (Button) findViewById(R.id.base_cart_button);
        mCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingCartRedirect();
            }
        });
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }

    private void cameraRedirect(){
        Log.i("TAG","Camera button clicked");
        Intent mainIntent = new Intent(this, BarcodeCaptureActivity.class);
        this.startActivity(mainIntent);
    }

    private void shoppingCartRedirect(){
        Log.i("TAG","Cart button clicked");
        Intent mainIntent = new Intent(this, ShoppingCartActivity.class);
        this.startActivity(mainIntent);
    }

    private void productRedirect(){
        Log.i("TAG","Product button clicked");
        Intent mainIntent = new Intent(this, ProductActivity.class);
        mainIntent.putExtra("id","1");
        this.startActivity(mainIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            attemptLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void attemptLogout() {
        if (mLogoutTask != null) {
            return;
        }
        mLogoutTask = new UserLogoutTask();
        mLogoutTask.execute((Void) null);

    }


    public class UserLogoutTask extends AsyncTask<Void, Void, Boolean> {


        UserLogoutTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = AppUtils.baseUrl + "rest-auth/logout/";

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.user_token), MODE_PRIVATE).edit();
                    editor.putString(getString(R.string.user_token), "");
                    editor.apply();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8; ";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization", "Token " + token);

                    return params;
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
            };

            requestQueue.add(stringRequest);


            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLogoutTask = null;

            if (success) {
                Intent mainIntent = new Intent(BaseActivity.this, SplashActivity.class);
                startActivity(mainIntent);
                BaseActivity.this.finish();

            } else {
            }
        }

        @Override
        protected void onCancelled() {
            mLogoutTask = null;
        }
    }

}
