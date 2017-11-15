package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private PaymentTask mPaymentTask = null;

    private String token;
    private String totalPrice;
    private String cardNumber;
    private EditText mCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        totalPrice = intent.getStringExtra("total");

        TextView mTotalPrice = (TextView) findViewById(R.id.payment_total_price);
        mTotalPrice.setText(totalPrice);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.user_token), null);


        Button mPayButton = (Button) findViewById(R.id.payment_button);
        mPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });

    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("^[0-9]+$");
    }

    private void pay() {
        if (mPaymentTask != null) {
            return;
        }

        mCardNumber = (EditText) findViewById(R.id.payment_card_number_holder);
        cardNumber = mCardNumber.getText().toString();

        mCardNumber.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(cardNumber) || !isNumeric(cardNumber) || cardNumber.length() != 14) {
            mCardNumber.setError(getString(R.string.error_invalid_password));
            focusView = mCardNumber;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mPaymentTask = new PaymentTask(cardNumber);
            mPaymentTask.execute((Void) null);
        }


    }


    public class PaymentTask extends AsyncTask<Void, Void, Boolean> {

        private final String mCard;


        PaymentTask(String card) {
            mCard = card;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = AppUtils.baseUrl + "process-payment/";
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("card-number", mCard);
                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i("VOLLEY2", response);
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
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Token " + token);

                        return params;
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
            } catch (JSONException e) {
                e.printStackTrace();

            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mPaymentTask = null;

            if(!success){
                mCardNumber.setError("Invalid card Number");
                mCardNumber.requestFocus();
            }
            else{
                Intent mainIntent = new Intent(getApplicationContext(), BaseActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mPaymentTask = null;
        }
    }

}
