package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {



    /** Duration of wait **/
    private int SPLASH_DISPLAY_LENGTH = 2000;
    private boolean isTokenValid = false;
    //private final String baseUrl = "http://10.0.2.2:8000/";
    private String pk, username, first_name, last_name, email;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        final String token = sharedPref.getString(getString(R.string.user_token), null);

        //Log.d("TAG", "TOKEN: "+ token);
        //final String token = "9a9829f1061ffe8f1aafb7642be28c069bc1ae5f X";

        setContentView(R.layout.activity_splash);

        //startMenuActivity();

        if(token!=null){
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AppUtils.baseUrl + "rest-auth/user/";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                pk = json.getString("pk");
                                username = json.getString("username");
                                first_name = json.getString("first_name");
                                last_name = json.getString("last_name");
                                email = json.getString("email");

                                Log.d("TAG", "test: " + pk+ username+ first_name+ last_name+ email);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            isTokenValid = true;
                            startMenuActivity();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // mTextView.setText("That didn't work!");
                    NetworkResponse networkResponse = error.networkResponse;

                    if(networkResponse == null){
                        networkResponseDialog();
                    }
                    else if (networkResponse != null && networkResponse.statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        // HTTP Status Code: 401 Unauthorized
                        Log.e("TAG", "ErrSplash: 403");
                        isTokenValid = false;
                        startMenuActivity();
                    }
                    else{
                        Log.e("TAG", "Other Error");
                        isTokenValid = false;
                        startMenuActivity();
                    }
                    isTokenValid = false;
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
        else {
            startMenuActivity();
        }

    }

    private void networkResponseDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage("Server not responding. Please make sure you have an internet connection.").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask ();
                }
                else{
                    finishAffinity();
                }
            }
        });
        alertDialogBuilder.show();
    }

    private void startMenuActivity(){
         /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                if(!isTokenValid) {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
                else{
                    Intent mainIntent = new Intent(SplashActivity.this, BaseActivity.class);

                    mainIntent.putExtra("pk",pk);
                    mainIntent.putExtra("first_name",first_name);
                    mainIntent.putExtra("last_name",last_name);
                    mainIntent.putExtra("username",username);
                    mainIntent.putExtra("email",email);

                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
