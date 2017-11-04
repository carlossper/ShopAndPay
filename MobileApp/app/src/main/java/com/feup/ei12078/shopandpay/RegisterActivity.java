package com.feup.ei12078.shopandpay;

import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Campos User
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etAddr = (EditText) findViewById(R.id.etAddr);
        final EditText etNIF = (EditText) findViewById(R.id.etNIF);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPass = (EditText) findViewById(R.id.etPass);//generate a 368 bit RSA key pair
        final EditText etPass2 = (EditText) findViewById(R.id.etPass2);//generate a 368 bit RSA key pair

        // Campos Cartão
        final RadioGroup rgCard = (RadioGroup) findViewById(R.id.rgCard);
        final int rbSel = rgCard.getCheckedRadioButtonId();
    }

}
