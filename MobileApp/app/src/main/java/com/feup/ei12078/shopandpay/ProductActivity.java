package com.feup.ei12078.shopandpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    private TextView mIdTextView;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();

        productId = intent.getStringExtra("id");

        mIdTextView = (TextView) findViewById(R.id.product_product_id);
        mIdTextView.setText(productId);

    }
}
