package com.geoverifi.geoverifi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.helper.CustomRequest;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.sharedpreference.Manager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginBtn;
    TextView txtEmail;
    TextView txtPassword;
    TextView txtRegister;
    TextView forgotpassword;

    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.signIn);
        txtEmail = (TextView) findViewById(R.id.input_email);
        txtPassword = (TextView) findViewById(R.id.input_password);
        txtRegister = (TextView) findViewById(R.id.registerBtn);
        forgotpassword = (TextView) findViewById(R.id.forgotpasswordtxt);

        loginBtn.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);


        TextView HyperLink;
        Spanned Text;
        HyperLink = (TextView)findViewById(R.id.powered);

        Text = Html.fromHtml("<a href='http://geoverifi.com'>www.geoverifi.com</a>");

        HyperLink.setMovementMethod(LinkMovementMethod.getInstance());
        HyperLink.setText(Text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signIn:
                attemptLogin();
                break;
            case R.id.registerBtn:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.forgotpasswordtxt:
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
                break;
        }
    }

    private void attemptLogin() {
        final String username = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "Username Cannot be Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Variables.BASE_URL + "API/login/" + username + "/" + password;

        final ProgressDialog pdialog = new ProgressDialog(this);
        pdialog.setMessage("Authenticating");
        pdialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pdialog.hide();
                Log.e("RESPONSE", response);
                try {
                    JSONObject responseObj = new JSONObject(response);
                    if (responseObj.getBoolean("status") == true){
                        JSONObject userObj = responseObj.getJSONObject("data");

                        User user = new User();

                        user.set_id(userObj.getInt("id"));
                        user.set_firstname(userObj.getString("user_firstname"));
                        user.set_lastname(userObj.getString("user_lastname"));
                        user.set_email(userObj.getString("user_email"));
                        user.set_photo(userObj.getString("user_image"));
                        user.set_phone(userObj.getString("user_phone"));
                        user.set_user_type(userObj.getString("user_type"));
                        user.set_user_about(userObj.getString("user_about"));

                        CreateSyncAccount(c, user);

                        Manager.getInstance(LoginActivity.this).storeUser(user);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, responseObj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdialog.hide();
                Log.e("RESPONSE", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("user_username", username);
                params.put("user_password", password);

                return params;
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_username", username);
        params.put("user_password", password);

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, url, params , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("status") == true){

                    }else{
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static Account CreateSyncAccount(Context context, User user) {
        Account newAccount = new Account(user.get_firstname() + " " + user.get_lastname(), context.getString(R.string.account_type));
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        Bundle userdata = new Bundle();
        userdata.putInt("user_id", user.get_id());
        userdata.putString("firstname", user.get_firstname());
        userdata.putString("firstname", user.get_lastname());

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount;
    }
}
