package com.feup.ei12078.shopandpay.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.feup.ei12078.shopandpay.AppUtils;
import com.feup.ei12078.shopandpay.R;
import com.feup.ei12078.shopandpay.data_classes.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andremachado on 08/11/2017.
 */

public class ProductListItemAdapter extends BaseAdapter {

    private final List<Product> products;
    private final Activity act;
    private RemoveProductFromCartTask mRemoveProductFromCartTask = null;
    private String token;


    public ProductListItemAdapter(List<Product> products, Activity act) {
        this.products = products;
        this.act = act;
        SharedPreferences sharedPref = act.getSharedPreferences(act.getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(act.getString(R.string.user_token), null);

    }


    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.product_list_item, parent, false);
        final Product product = products.get(position);

        TextView brand = (TextView) view.findViewById(R.id.product_list_item_brand);
        TextView name = (TextView) view.findViewById(R.id.product_list_item_name);
        TextView category = (TextView) view.findViewById(R.id.product_list_item_category);
        TextView price = (TextView) view.findViewById(R.id.product_list_item_price);
        Button deleteButton = (Button) view.findViewById(R.id.product_list_item_button);


        ImageView image = (ImageView) view.findViewById(R.id.product_list_item_image);

        name.setText(product.getName());
        brand.setText(product.getBrand());
        category.setText(product.getCategory());
        price.setText(product.getPrice() + " €");
        Picasso.with(act.getApplicationContext()).load(product.getImage_url()).into(image);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb=new AlertDialog.Builder(act);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        attemptDeleteData(Integer.parseInt(product.getCpid()), positionToRemove);
                    }});
                adb.show();
            }

        });


        return view;

    }



    //####################################
    //#######DELETE STUFF################
    //##################################

    private void attemptDeleteData(int cpid, int pos) {
        if (mRemoveProductFromCartTask != null) {
            return;
        }

        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            focusView.requestFocus();
        } else {
            mRemoveProductFromCartTask = new RemoveProductFromCartTask(cpid, pos);
            mRemoveProductFromCartTask.execute((Void) null);
        }
    }

    public class RemoveProductFromCartTask extends AsyncTask<Void, Void, Boolean> {

        private final int mCpid;
        private final int positionToRemove;

        RemoveProductFromCartTask(int mCpid, int positionToRemove) {
            this.mCpid = mCpid;
            this.positionToRemove = positionToRemove;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = AppUtils.baseUrl + "manage-cart/"+mCpid+"/";
            RequestQueue requestQueue = Volley.newRequestQueue(act.getApplicationContext());
            JSONObject jsonBody = new JSONObject();
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
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

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRemoveProductFromCartTask = null;

            if (success) {
                products.remove(positionToRemove);
                notifyDataSetChanged();
            } else {

            }


            if(!success){

            }
        }

        @Override
        protected void onCancelled() {
            mRemoveProductFromCartTask = null;
        }
    }


}
