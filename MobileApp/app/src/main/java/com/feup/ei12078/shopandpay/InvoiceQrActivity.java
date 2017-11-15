package com.feup.ei12078.shopandpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InvoiceQrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_qr);

        Intent intent = getIntent();
        String invId = intent.getStringExtra("id");

        TextView mIdTextView = (TextView) findViewById(R.id.invoice_qr_number);
        mIdTextView.setText(invId);

        String url = "http://api.qrserver.com/v1/create-qr-code/?color=000000&bgcolor=FFFFFF&data=" + invId + "&qzone=1&margin=0&size=900x900&ecc=L";

        ImageView mQrImageView = (ImageView) findViewById(R.id.invoice_qr_image);
        Picasso.with(getApplicationContext()).load(url).into(mQrImageView);
    }
}
