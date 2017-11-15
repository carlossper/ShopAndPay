package com.feup.ei12078.shopandpay.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feup.ei12078.shopandpay.R;
import com.feup.ei12078.shopandpay.data_classes.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andremachado on 09/11/2017.
 */

public class ProductListItemAdapter extends BaseAdapter {


    private final List<Product> products;
    private final Activity act;
    private String token;

    String totalPrice;


    public ProductListItemAdapter(List<Product> products, Activity act) {
        this.products = products;
        this.act = act;
        SharedPreferences sharedPref = act.getSharedPreferences(act.getString(R.string.user_token), Context.MODE_PRIVATE);
        token = sharedPref.getString(act.getString(R.string.user_token), null);

        double total = 0;
        for(int i = 0 ; i < products.size() ; i++){
            total += Double.parseDouble(products.get(i).getPrice());
        }
        totalPrice = ""+total+" €";

    }



    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public String getItem(int position) {
        return products.get(position).getId();
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


        ImageView image = (ImageView) view.findViewById(R.id.product_list_item_image);

        name.setText(product.getName());
        brand.setText(product.getBrand());
        category.setText(product.getCategory());
        price.setText(product.getPrice() + " €");
        Picasso.with(act.getApplicationContext()).load(product.getImage_url()).into(image);


        return view;

    }

    public String getPrice() {
        return totalPrice;
    }
}