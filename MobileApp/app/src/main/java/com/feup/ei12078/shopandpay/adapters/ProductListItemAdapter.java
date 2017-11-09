package com.feup.ei12078.shopandpay.adapters;

/**
 * Created by andremachado on 09/11/2017.
 */

public class ProductListItemAdapter {//extends BaseAdapter {

    /*
    private final List<Product> products;
    private final Activity act;
    private CartProductListItemAdapter.RemoveProductFromCartTask mRemoveProductFromCartTask = null;
    private String token;
    private TextView totalPriceView;


    public ProductListItemAdapter(List<Product> products, Activity act, TextView totalPriceView) {
        this.products = products;
        this.act = act;
        this.totalPriceView = totalPriceView;
        SharedPreferences sharedPref = act.getSharedPreferences(act.getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(act.getString(R.string.user_token), null);

        double total = 0;
        for(int i = 0 ; i < products.size() ; i++){
            total += Double.parseDouble(products.get(i).getPrice());
        }
        totalPriceView.setText(""+total+" €");

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
    public void notifyDataSetChanged() {
        double total = 0;
        for(int i = 0 ; i < products.size() ; i++){
            total += Double.parseDouble(products.get(i).getPrice());
        }
        totalPriceView.setText(""+total+" €");


        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.cart_list_item, parent, false);
        final Product product = products.get(position);

        TextView brand = (TextView) view.findViewById(R.id.cart_product_list_item_brand);
        TextView name = (TextView) view.findViewById(R.id.cart_product_list_item_name);
        TextView category = (TextView) view.findViewById(R.id.cart_product_list_item_category);
        TextView price = (TextView) view.findViewById(R.id.cart_product_list_item_price);
        Button deleteButton = (Button) view.findViewById(R.id.cart_product_list_item_button);


        ImageView image = (ImageView) view.findViewById(R.id.cart_product_list_item_image);

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
            mRemoveProductFromCartTask = new CartProductListItemAdapter.RemoveProductFromCartTask(cpid, pos);
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
        */
    }