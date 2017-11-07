package com.feup.ei12078.shopandpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.feup.ei12078.shopandpay.barcode.BarcodeCaptureActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        this.finish();
    }

    private void productRedirect(){
        Log.i("TAG","Product button clicked");
        Intent mainIntent = new Intent(this, ProductActivity.class);
        mainIntent.putExtra("id","1");
        this.startActivity(mainIntent);
        this.finish();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
