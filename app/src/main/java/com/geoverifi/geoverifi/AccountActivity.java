package com.geoverifi.geoverifi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.sharedpreference.Manager;
import com.geoverifi.geoverifi.util.CircularNetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {
    User user;

    EditText etxFirstName, etxLastName, etxEmail, etxPhoneNumber, etxAbout, etxNewPassword, etxConfirmPassword;
    TextView txtAccountType;
    CircularNetworkImageView userImageView;

    final Context context = this;

    private int HIDE_MENU = 1;
    private int input_status = 0;

    ImageLoader loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        user = Manager.getInstance(this).getUser();
        loader = AppController.getInstance().getImageLoader();

        etxFirstName = (EditText) findViewById(R.id.input_firstname);
        etxLastName = (EditText) findViewById(R.id.input_lastname);
        etxPhoneNumber = (EditText) findViewById(R.id.input_phone);
        etxEmail = (EditText) findViewById(R.id.input_email);
        etxAbout = (EditText) findViewById(R.id.input_about);
        etxNewPassword = (EditText) findViewById(R.id.input_password);
        etxConfirmPassword = (EditText) findViewById(R.id.input_confirm_password);
        txtAccountType = (TextView) findViewById(R.id.account_type_label);
        userImageView = (CircularNetworkImageView) findViewById(R.id.user_image);

        etxFirstName.setText(user.get_firstname());
        etxLastName.setText(user.get_lastname());
        etxEmail.setText(user.get_email());
        etxPhoneNumber.setText(user.get_phone());
        etxAbout.setText(user.get_user_about());
        txtAccountType.setText(user.get_user_type());
        userImageView.setImageUrl(Variables.BASE_URL + user.get_photo(), loader);


        etxFirstName.addTextChangedListener(fieldValidatorTextWatcher);
        etxLastName.addTextChangedListener(fieldValidatorTextWatcher);
        etxEmail.addTextChangedListener(fieldValidatorTextWatcher);
        etxPhoneNumber.addTextChangedListener(fieldValidatorTextWatcher);
        etxAbout.addTextChangedListener(fieldValidatorTextWatcher);
        etxNewPassword.addTextChangedListener(fieldValidatorTextWatcher);
        etxConfirmPassword.addTextChangedListener(fieldValidatorTextWatcher);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_menu, menu);
        if (HIDE_MENU == 1) {
            menu.findItem(R.id.save).setVisible(false);
            menu.findItem(R.id.reset).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                new Intent(AccountActivity.this, MainActivity.class);
                finish();
                break;
            case R.id.save:
                saveChanges();
                break;

            case R.id.reset:
                resetData();
                break;

            case R.id.logout:
                new AlertDialog.Builder(AccountActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to logout?")
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Manager.getInstance(AccountActivity.this).clear();
                                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(true)
                        .show();
                break;
        }
        return true;
    }

    private void saveChanges() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("PASSWORD");

        LayoutInflater li = LayoutInflater.from(context);
        View passwordView = li.inflate(R.layout.enter_password, null);

        alertDialog.setView(passwordView);

        final EditText passwordText = (EditText) passwordView.findViewById(R.id.input_password);

        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog pDialog = new ProgressDialog(context);
                pDialog.setMessage("Updating Details... Please wait");
                pDialog.setCancelable(false);
                pDialog.show();
                final String firstname = etxFirstName.getText().toString();
                final String lastname = etxLastName.getText().toString();
                final String email = etxEmail.getText().toString();
                final String phone = etxPhoneNumber.getText().toString();
                final String about = etxAbout.getText().toString();
                final String new_password = etxNewPassword.getText().toString();
                final String confirm_password = etxConfirmPassword.getText().toString();

                user.set_firstname(firstname);
                user.set_lastname(lastname);
                user.set_email(email);
                user.set_phone(phone);
                user.set_user_about(about);

                Manager.getInstance(AccountActivity.this).storeUser(user);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Variables.BASE_URL + "API/updateUser", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            if (responseObj.getBoolean("status") == true){
                                Toast.makeText(AccountActivity.this, "Successfully updated details", Toast.LENGTH_SHORT).show();

                                etxConfirmPassword.setText("");
                                etxNewPassword.setText("");
                                etxFirstName.requestFocus();
                                HIDE_MENU = 1;
                                invalidateOptionsMenu();
                            }else{
                                Toast.makeText(AccountActivity.this, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("user_firstname", firstname);
                        params.put("user_lastname", lastname);
                        params.put("user_email", email);
                        params.put("user_phone", phone);
                        params.put("user_about", about);
                        params.put("id", String.valueOf(user.get_id()));
                        if (new_password.equals(confirm_password)){
                            params.put("user_password", new_password);
                        }
                        params.put("current_password", passwordText.getText().toString());

                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(stringRequest);
            }
        });

        alertDialog.setNegativeButton("Exit", null);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    private void resetData(){
        new AlertDialog.Builder(AccountActivity.this)
                .setTitle("Confirmation")
                .setMessage("All changes you made will be lost")
                .setIcon(R.drawable.ic_warning)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etxFirstName.setText(user.get_firstname());
                        etxLastName.setText(user.get_lastname());
                        etxEmail.setText(user.get_email());
                        etxPhoneNumber.setText(user.get_phone());
                        etxAbout.setText(user.get_user_about());
                    }
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(true)
                .show();
    }

    TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            HIDE_MENU = -1;
            int passcheck = 1;
            String firstname = etxFirstName.getText().toString();
            String lastname = etxLastName.getText().toString();
            String email = etxEmail.getText().toString();
            String phone = etxPhoneNumber.getText().toString();
            String about = etxAbout.getText().toString();
            String new_password = etxNewPassword.getText().toString();
            String confirm_password = etxConfirmPassword.getText().toString();
            if(TextUtils.isEmpty(new_password) || TextUtils.isEmpty(confirm_password) || !new_password.equals(confirm_password)){
                passcheck = 0;
            }
            if (firstname.equals(user.get_firstname()) && lastname.equals(user.get_lastname()) && email.equals(user.get_email()) && phone.equals(user.get_phone()) && about.equals(user.get_user_about()) && passcheck == 0){
                HIDE_MENU = 1;
            }else{
                HIDE_MENU = 0;
            }
            invalidateOptionsMenu();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
