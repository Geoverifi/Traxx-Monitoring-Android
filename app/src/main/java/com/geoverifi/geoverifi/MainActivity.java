package com.geoverifi.geoverifi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.geoverifi.geoverifi.adapter.RecyclerViewAdapter;
import com.geoverifi.geoverifi.app.MyApp;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.helper.DataInitializer;
import com.geoverifi.geoverifi.model.Menu;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.service.GPSTracker;
import com.geoverifi.geoverifi.sharedpreference.Manager;
import com.squareup.otto.Bus;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Bus bus = MyApp.getBus();
    User user;
    List<Menu> menuList;
    TextView loggedIn;
    DatabaseHandler db;

    String[] perms = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = Manager.getInstance(this).getUser();
        if (user.get_firstname() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }

        gps = new GPSTracker(MainActivity.this);

        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
        String usertype = user.get_user_type();

        getSupportActionBar().setTitle("HOME");

        loggedIn = (TextView) findViewById(R.id.logged_in_as);
        loggedIn.setText("Logged In As: " + user.get_firstname() + " " + user.get_lastname());
        RecyclerView rv = (RecyclerView)findViewById(R.id.menu_recycler_view);
        GridLayoutManager llm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(llm);

        db = new DatabaseHandler(this);
        menuList = db.Mainmenu(usertype);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(menuList, this, this);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to logout?")
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Account Manager definition
                                AccountManager accountManager = (AccountManager) MainActivity.this.getSystemService(ACCOUNT_SERVICE);

                                // loop through all accounts to remove them
                                Account[] accounts = accountManager.getAccounts();
                                for (int index = 0; index < accounts.length; index++) {
                                    if (accounts[index].type.intern() == "com.geoverifi.geoverifi")
                                        accountManager.removeAccount(accounts[index], null, null);
                                }
                                Manager.getInstance(MainActivity.this).clear();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(true)
                        .show();
                break;

            case R.id.offline_data:
                startActivity(new Intent(this, OfflineDataActivity.class));
                break;
            case R.id.refresh_data:
                DataInitializer.getInstance(this, true).initialize();
                break;

            case R.id.draft_submission:
                startActivity(new Intent(this, DraftSubmissionsActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(permsRequestCode){
            case 200:
                boolean coarse_location_accepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                boolean fine_location_accepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                boolean write_external_accepted = grantResults[2]==PackageManager.PERMISSION_GRANTED;
                break;
        }
    }
}
