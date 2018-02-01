package com.geoverifi.geoverifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegThankyou extends AppCompatActivity implements View.OnClickListener {
    TextView thankyou;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regthankyou);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        thankyou = (TextView) findViewById(R.id.thankyou);

        registerBtn = (Button) findViewById(R.id.createAccountBtn);
        registerBtn.setOnClickListener(this);


        TextView HyperLink;
        Spanned Text;
        HyperLink = (TextView)findViewById(R.id.powered);

        Text = Html.fromHtml("<a href='http://geoverifi.com'>www.geoverifi.com</a>");

        HyperLink.setMovementMethod(LinkMovementMethod.getInstance());
        HyperLink.setText(Text);
    }

    private void registerUser(){
        startActivity(new Intent(RegThankyou.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccountBtn:
                registerUser();

                break;
        }
    }

}
