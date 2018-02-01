package com.geoverifi.geoverifi;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.fragments.ConfirmLocationFragment;
import com.geoverifi.geoverifi.fragments.ReviewDataAuditingFragment;
import com.geoverifi.geoverifi.fragments.ReviewPhotoAuditingFragment;
import com.geoverifi.geoverifi.model.SubmissionAuditing;
import com.geoverifi.geoverifi.provider.SubmissionAuditingProvider;
import com.geoverifi.geoverifi.service.GPSTracker;

public class MonitoringSubmissionReviewAuditingActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ReviewDataAuditingFragment.OnFragmentInteractionListener, ReviewPhotoAuditingFragment.OnFragmentInteractionListener, ConfirmLocationFragment.OnCompleteListener {
    int submission_id,data_type_sides, submission_idb,submission_idc,newuser_id,draft;
    String side,newtown,newbrand,newsubmission_date,newreference_number,newmedia_owner,newmedia_owner_name,newstructure,newsize,newmedia_size_other_height,newmedia_size_other_width,newmaterial,newrun_up,newillumination,newvisibility,newangle;


    double newlatitude,newlongitude;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean confirmed = false;
    private double longitude, latitude;
    DatabaseHandler db;
    GPSTracker gps;

    SubmissionAuditing submission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditing_submission_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gps = new GPSTracker(this);

        db = new DatabaseHandler(this);

        getSupportActionBar().setTitle("Auditing");
        getSupportActionBar().setSubtitle("Review your submission");

        submission_id = getIntent().getIntExtra("submission_id", 0);

        data_type_sides = getIntent().getIntExtra("data_type_sides", 0);
        side = getIntent().getStringExtra("side");
        newtown = getIntent().getStringExtra("newtown");
        newsubmission_date = getIntent().getStringExtra("newsubmission_date");
        newreference_number = getIntent().getStringExtra("newreference_number");
        newmedia_owner = getIntent().getStringExtra("newmedia_owner");
        newmedia_owner_name = getIntent().getStringExtra("newmedia_owner_name");
        newstructure = getIntent().getStringExtra("newstructure");
        newsize = getIntent().getStringExtra("newsize");
        newmedia_size_other_height = getIntent().getStringExtra("newmedia_size_other_height");
        newmedia_size_other_width = getIntent().getStringExtra("newmedia_size_other_width");
        newmaterial = getIntent().getStringExtra("newmaterial");
        newrun_up = getIntent().getStringExtra("newrun_up");
        newillumination = getIntent().getStringExtra("newillumination");
        newvisibility = getIntent().getStringExtra("newvisibility");
        newangle = getIntent().getStringExtra("newangle");
        newlatitude = getIntent().getDoubleExtra("newlatitude", 0.0);
        newlongitude = getIntent().getDoubleExtra("newlongitude", 0.0);
        newuser_id = getIntent().getIntExtra("newuser_id", 0);
        newbrand = getIntent().getStringExtra("newbrand");
        draft =  getIntent().getIntExtra("newdraft", 0);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        tabLayout.addTab(tabLayout.newTab().setText("Auditing Data"));
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

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
                 if(side == "A" && data_type_sides==2 || side.equals("A") && data_type_sides==2 || side == null && data_type_sides==2){
                     //save data for side A


                     if(draft==1){
                         if (confirmed) {
                             ContentValues values = new ContentValues();

                             values.put(DatabaseHandler.KEY_STATUS, 0);

                             getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                             Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                             //startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                             //finish();
                         } else {
                             showConfirmLocation();
                         }
                         ContentValues values = new ContentValues();
                         saveDraftB();
                         values.put(DatabaseHandler.KEY_STATUS, -1);
                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_idb)});

                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Lets load page 2 other tabs", Toast.LENGTH_SHORT).show();


                         Intent intent = new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class);
                         intent.putExtra("submission_id", submission_idb);
                         intent.putExtra("data_type_sides", data_type_sides);
                         intent.putExtra("side", "B");
                         intent.putExtra("newdraft", "1");
                         startActivity(intent);
                         finish();
                     }else{
                         if (confirmed) {
                             ContentValues values = new ContentValues();

                             values.put(DatabaseHandler.KEY_STATUS, 0);

                             getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                             Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                             //startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                             //finish();


                         } else {
                             showConfirmLocation();
                         }
                         if(data_type_sides==2){
                             ContentValues values = new ContentValues();
                             saveDraftB();
                             values.put(DatabaseHandler.KEY_STATUS, -1);
                             getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_idb)});

                             Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Lets load page 2 other tabs", Toast.LENGTH_SHORT).show();


                             Intent intent = new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class);
                             intent.putExtra("submission_id", submission_idb);
                             intent.putExtra("data_type_sides", data_type_sides);
                             intent.putExtra("side", "B");
                             intent.putExtra("newdraft", "1");
                             startActivity(intent);
                             finish();
                         }
                     }

                     //create draft for side B
                 }
                else if(side=="B"  && data_type_sides==2 || side.equals("B")  && data_type_sides==2){
                    // save data for B

                     if (confirmed) {
                         ContentValues values = new ContentValues();

                         values.put(DatabaseHandler.KEY_STATUS, 0);

                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                         startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                         finish();
                     } else {
                         showConfirmLocation2();
                     }


                     // clear
                 }
                 else if(side == "A" && data_type_sides==3 || side.equals("A") && data_type_sides==3 || side==null && data_type_sides==3){

                    //create draft for side B


                     if(draft==1){

                         //save data for side A

                         if (confirmed) {
                             ContentValues values = new ContentValues();

                             values.put(DatabaseHandler.KEY_STATUS, 0);

                             getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                             Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                             //startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                             //finish();
                         } else {
                             showConfirmLocation();
                         }
                         ContentValues values = new ContentValues();
                         saveDraftB();
                         values.put(DatabaseHandler.KEY_STATUS, -1);
                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_idb)});

                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Lets load page 2 other tabs", Toast.LENGTH_SHORT).show();


                         Intent intent = new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class);
                         intent.putExtra("submission_id", submission_idb);
                         intent.putExtra("data_type_sides", data_type_sides);
                         intent.putExtra("side", "B");
                         intent.putExtra("newdraft", "1");
                         startActivity(intent);
                         finish();
                     }else{
                         //save data for side A


                         if (confirmed) {
                             ContentValues values = new ContentValues();

                             values.put(DatabaseHandler.KEY_STATUS, 0);

                             getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                             Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                             startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                             finish();
                         } else {
                             showConfirmLocation();
                         }

                         ContentValues values = new ContentValues();
                         saveDraftB();
                         values.put(DatabaseHandler.KEY_STATUS, -1);
                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_idb)});

                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Lets load page 2 other tabs", Toast.LENGTH_SHORT).show();


                         Intent intent = new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class);
                         intent.putExtra("submission_id", submission_idb);
                         intent.putExtra("data_type_sides", data_type_sides);
                         intent.putExtra("side", "B");
                         intent.putExtra("newdraft", "1");
                         startActivity(intent);
                         finish();
                     }
                }
                else if(side=="B"  && data_type_sides==3 || side.equals("B")  && data_type_sides==3){
                    // save data for B

                     if (confirmed) {
                         ContentValues values = new ContentValues();

                         values.put(DatabaseHandler.KEY_STATUS, 0);

                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                         //startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                         //finish();
                     } else {
                         showConfirmLocation2();
                     }


                    // create draft for c
                     if(draft==1){

                         ContentValues values = new ContentValues();
                         saveDraftC();
                         values.put(DatabaseHandler.KEY_STATUS, -1);
                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_idc)});

                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Lets load page 2 other tabs", Toast.LENGTH_SHORT).show();


                         Intent intent = new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class);
                         intent.putExtra("submission_id", submission_idc);
                         intent.putExtra("data_type_sides", data_type_sides);
                         intent.putExtra("side", "C");
                         intent.putExtra("newdraft", "1");
                         startActivity(intent);
                         finish();
                     }
                }else if(side=="C"  && data_type_sides==3 || side.equals("C")  && data_type_sides==3){
                     // save data for C

                     if (confirmed) {
                         ContentValues values = new ContentValues();

                         values.put(DatabaseHandler.KEY_STATUS, 0);

                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                         startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                         finish();
                     } else {
                         showConfirmLocation2();
                     }
                     // clear
                 }else{
                     if (confirmed) {
                         ContentValues values = new ContentValues();

                         values.put(DatabaseHandler.KEY_STATUS, 0);

                         getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});


                         Toast.makeText(MonitoringSubmissionReviewAuditingActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();


                         startActivity(new Intent(MonitoringSubmissionReviewAuditingActivity.this, MonitoringDataSubmissionAuditing.class));
                         finish();
                     } else {
                         showConfirmLocation();
                     }

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


    private void showConfirmLocation2() {
        ConfirmLocationFragment fragment = ConfirmLocationFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", newlatitude);
        bundle.putDouble("longitude", newlongitude);
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

        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, MonitoringDataSubmissionAuditing.class);
                intent.putExtra("submission_id", submission_id);
                intent.putExtra("side", side);
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

        getContentResolver().update(SubmissionAuditingProvider.CONTENT_URI, values, DatabaseHandler.KEY_ID + "=?", new String[]{String.valueOf(submission_id)});
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

            switch (position) {
                case 0:
                    ReviewDataAuditingFragment reviewDataFragment = new ReviewDataAuditingFragment();
                    reviewDataFragment.setArguments(bundle);
                    return reviewDataFragment;
                case 1:
                    ReviewPhotoAuditingFragment reviewPhotoFragment = new ReviewPhotoAuditingFragment();
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


    private void saveDraftB() {

        long id = db.addDraftSubmissionAuditing(getInputB());

        submission_idb = Integer.valueOf(String.valueOf(id));

        Toast.makeText(this, "Saved to draft b", Toast.LENGTH_SHORT).show();}

    private void updateDraftB(){


        db.updatedDraftSubmissionAuditing(getInputB());

        Toast.makeText(this, "Updated draft b", Toast.LENGTH_SHORT).show();
    }


    private SubmissionAuditing getInputB(){


        SubmissionAuditing inputSubmission = new SubmissionAuditing();

        Log.d("b", String.valueOf(submission_idb));

        inputSubmission.set_id(submission_idb);
        inputSubmission.set_submission_date(newsubmission_date);
        inputSubmission.set_site_reference_number(newreference_number);
        inputSubmission.set_brand(newbrand);
        inputSubmission.set_media_owner(newmedia_owner);
        inputSubmission.set_media_owner_name(newmedia_owner_name);
        inputSubmission.set_structure(newstructure);
        inputSubmission.set_town(newtown);
        inputSubmission.set_size(newsize);
        inputSubmission.set_media_size_other_height(newmedia_size_other_height);
        inputSubmission.set_media_size_other_width(newmedia_size_other_width);
        inputSubmission.set_material(newmaterial);
        inputSubmission.set_run_up(newrun_up);
        inputSubmission.set_illumination(newillumination);
        inputSubmission.set_visibility(newvisibility);
        if(data_type_sides == 1){
            inputSubmission.set_angle("2");
        }
        else if(data_type_sides == 2 || data_type_sides == 3){
            inputSubmission.set_angle("1");
        }
        else{
            inputSubmission.set_angle("2");
        }
        inputSubmission.set_other_comments("");
        inputSubmission.set_latitude(String.valueOf(newlatitude));
        inputSubmission.set_longitude(String.valueOf(newlongitude));
        inputSubmission.set_user_id(newuser_id);
        inputSubmission.set_status(-1);
        inputSubmission.set_side("B");
        inputSubmission.set_parentid(submission_id);

        return inputSubmission;
    }

    private SubmissionAuditing getInputC(){


        SubmissionAuditing inputSubmission = new SubmissionAuditing();

        inputSubmission.set_submission_date(newsubmission_date);
        inputSubmission.set_site_reference_number(newreference_number);
        inputSubmission.set_brand(newbrand);
        inputSubmission.set_media_owner(newmedia_owner);
        inputSubmission.set_media_owner_name(newmedia_owner_name);
        inputSubmission.set_structure(newstructure);
        inputSubmission.set_town(newtown);
        inputSubmission.set_size(newsize);
        inputSubmission.set_media_size_other_height(newmedia_size_other_height);
        inputSubmission.set_media_size_other_width(newmedia_size_other_width);
        inputSubmission.set_material(newmaterial);
        inputSubmission.set_run_up(newrun_up);
        inputSubmission.set_illumination(newillumination);
        inputSubmission.set_visibility(newvisibility);
        if(data_type_sides == 1) {
            inputSubmission.set_angle("2");
        }
        else if(data_type_sides == 2 || data_type_sides == 3){
            inputSubmission.set_angle("1");
        }

        else{
            inputSubmission.set_angle("2");
        }
        inputSubmission.set_other_comments("");
        inputSubmission.set_latitude(String.valueOf(newlatitude));
        inputSubmission.set_longitude(String.valueOf(newlongitude));
        inputSubmission.set_user_id(newuser_id);
        inputSubmission.set_status(-1);
        inputSubmission.set_side("C");
        inputSubmission.set_parentid(submission_id);

        return inputSubmission;
    }


    private void saveDraftC() {
        ContentValues values = new ContentValues();
        //querry database

        long id = db.addDraftSubmissionAuditing(getInputC());

        submission_id = Integer.valueOf(String.valueOf(id));

        Toast.makeText(this, "Saved to draftc", Toast.LENGTH_SHORT).show();}

    private void updateDraftC(){


        db.updatedDraftSubmissionAuditing(getInputC());

        Toast.makeText(this, "Updated draft", Toast.LENGTH_SHORT).show();
    }

}
