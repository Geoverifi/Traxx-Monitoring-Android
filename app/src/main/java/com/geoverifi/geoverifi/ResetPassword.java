package com.geoverifi.geoverifi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {
    EditText input_code;
    EditText input_confirm_password;
    EditText input_password;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        input_code = (EditText) findViewById(R.id.input_code);
        input_password = (EditText) findViewById(R.id.input_password);
        input_confirm_password = (EditText) findViewById(R.id.input_confirm_password);
        registerBtn = (Button) findViewById(R.id.createAccountBtn);

        registerBtn.setOnClickListener(this);

    }

    private void registerUser(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Creating your account.Please Wait...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Variables.BASE_URL + "API/resetpassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.hide();
                try {
                    JSONObject responseObj = new JSONObject(response);
                    Toast.makeText(ResetPassword.this, responseObj.getString("message"), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ResetPassword.this, LoginActivity.class));
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
                Toast.makeText(ResetPassword.this, "Could not complete your request. Please contact admin", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("confirm_password", input_password.getText().toString());
                params.put("user_confirm_password", input_confirm_password.getText().toString());
                params.put("code", input_code.getText().toString());
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
