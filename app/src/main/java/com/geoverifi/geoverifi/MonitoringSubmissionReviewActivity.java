package com.geoverifi.geoverifi;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.fragments.ConfirmLocationFragment;
import com.geoverifi.geoverifi.fragments.LocationPreviewFragment;
import com.geoverifi.geoverifi.fragments.ReviewDataFragment;
import com.geoverifi.geoverifi.fragments.ReviewPhotoFragment;
import com.geoverifi.geoverifi.provider.SubmissionProvider;
import com.geoverifi.geoverifi.service.GPSTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MonitoringSubmissionReviewActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ReviewDataFragment.OnFragmentInteractionListener, ReviewPhotoFragment.OnFragmentInteractionListener, ConfirmLocationFragment.OnCompleteListener {
    int submission_id;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean confirmed = false;
    private double longitude, latitude;
    DatabaseHandler db;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_submission_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gps = new GPSTracker(this);

        db = new DatabaseHandler(this);

        getSupportActionBar().setTitle("SUBMISSION");
        getSupportActionBar().setSubtitle("Review your submission");

        submission_id = getIntent().getIntExtra("submission_id", 0);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setText("Submission Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            showConfirmLocation();
            fab.setVisibility(View.VISIBLE);
        }else{
            gps.showSettingsAlert();
            fab.setVisibility(View.GONE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                db.changeOfflineStatus(submission_id, 0);
                if (confirmed) {
                    ContentValues values = new ContentValues();

                    values.put(DatabaseHandler.KEY_STATUS, 0);

                    getContentResolver().update(SubmissionProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});
                    Toast.makeText(MonitoringSubmissionReviewActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(MonitoringSubmissionReviewActivity.this, MonitoringDataSubmission.class));
                    finish();
                }else{
                    showConfirmLocation();
                }
            }
        });
    }

    private void showConfirmLocation() {
        ConfirmLocationFragment fragment = ConfirmLocationFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        fragment.setArguments(bundle);
        fragment.show(getFragmentManager(), "LocationPreviewDialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                Intent intent = new Intent(this, MonitoringDataSubmission.class);
                intent.putExtra("submission_id", submission_id);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onComplete(boolean confirmed, Double latitude, Double longitude) {
        ContentValues values = new ContentValues();

        values.put(DatabaseHandler.KEY_LATITUDE, latitude);
        values.put(DatabaseHandler.KEY_LONGITUDE, longitude);

        getContentResolver().update(SubmissionProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});
        this.confirmed = confirmed;
    }

    class Pager extends FragmentStatePagerAdapter{
        int tabCount = 1;
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("submission_id", submission_id);
            switch (position){
                case 0:
                    ReviewDataFragment reviewDataFragment = new ReviewDataFragment();
                    reviewDataFragment.setArguments(bundle);
                    return reviewDataFragment;
                case 1:
                    ReviewPhotoFragment reviewPhotoFragment = new ReviewPhotoFragment();
                    reviewPhotoFragment.setArguments(bundle);
                    return reviewPhotoFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

}
