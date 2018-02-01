package com.geoverifi.geoverifi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.config.Variables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText user_firstname, user_lastname, user_username, user_email, user_phone, user_password, user_confirm_password;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        user_firstname = (EditText) findViewById(R.id.input_firstname);
        user_lastname = (EditText) findViewById(R.id.input_lastname);
        user_username = (EditText) findViewById(R.id.input_username);
        user_email = (EditText) findViewById(R.id.input_email);
        user_phone = (EditText) findViewById(R.id.input_phone);
        user_password = (EditText) findViewById(R.id.input_password);
        user_confirm_password = (EditText) findViewById(R.id.input_confirm_password);
        registerBtn = (Button) findViewById(R.id.createAccountBtn);
        CheckBox check = findViewById(R.id.agreetopolicy);

        registerBtn.setOnClickListener(this);


    }

    private void registerUser(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating your account.Please Wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variables.BASE_URL + "API/createAccount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.hide();
                try {
                    JSONObject responseObj = new JSONObject(response);
                    Toast.makeText(RegisterActivity.this, responseObj.getString("message"), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, RegThankyou.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Log.e("Register Error", error.getMessage());
                Toast.makeText(RegisterActivity.this, "Could not complete your request. Please contact admin", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("user_firstname", user_firstname.getText().toString());
                params.put("user_lastname", user_lastname.getText().toString());
                params.put("user_email", user_email.getText().toString());
                params.put("user_phone", user_phone.getText().toString());
                params.put("user_username", user_username.getText().toString());
                params.put("user_password", user_password.getText().toString());
                params.put("user_confirm_password", user_confirm_password.getText().toString());

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createAccountBtn:
                registerUser();

                break;
        }
    }


        /*check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                registerUser();

            }
        });*/

}
