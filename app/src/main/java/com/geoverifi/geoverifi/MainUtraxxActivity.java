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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geoverifi.geoverifi.adapter.RecyclerViewutraxxAdapter;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.app.MyApp;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.helper.DataInitializer;
import com.geoverifi.geoverifi.model.Angle;
import com.geoverifi.geoverifi.model.Illumination;
import com.geoverifi.geoverifi.model.Material;
import com.geoverifi.geoverifi.model.MaterialStatus;
import com.geoverifi.geoverifi.model.MediaOwner;
import com.geoverifi.geoverifi.model.Menu;
import com.geoverifi.geoverifi.model.RunUp;
import com.geoverifi.geoverifi.model.Size;
import com.geoverifi.geoverifi.model.Structure;
import com.geoverifi.geoverifi.model.StructureCondition;
import com.geoverifi.geoverifi.model.Submission;
import com.geoverifi.geoverifi.model.SubmissionPhoto;
import com.geoverifi.geoverifi.model.SubmissionAuditing;
import com.geoverifi.geoverifi.model.TrafficQuantity;
import com.geoverifi.geoverifi.model.TrafficSpeed;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.model.Visibility;
import com.geoverifi.geoverifi.service.GPSTracker;
import com.geoverifi.geoverifi.sharedpreference.Manager;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainUtraxxActivity extends AppCompatActivity {
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
            startActivity(new Intent(MainUtraxxActivity.this, LoginActivity.class));
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }

        gps = new GPSTracker(MainUtraxxActivity.this);

        if(!gps.canGetLocation()){
            gps.showSettingsAlert();
        }
        String usertype = user.get_user_type();

        getSupportActionBar().setTitle("uTraxx");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loggedIn = (TextView) findViewById(R.id.logged_in_as);
        loggedIn.setText("Logged In As: " + user.get_firstname() + " " + user.get_lastname());
        RecyclerView rv = (RecyclerView)findViewById(R.id.menu_recycler_view);
        GridLayoutManager llm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(llm);

        db = new DatabaseHandler(this);
        menuList = db.MenuUtraxx(usertype);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        RecyclerViewutraxxAdapter adapter = new RecyclerViewutraxxAdapter(menuList, this, this);
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
            case android.R.id.home:
                Intent intent = new Intent(MainUtraxxActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                new AlertDialog.Builder(MainUtraxxActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to logout?")
                        .setIcon(R.drawable.ic_warning)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Account Manager definition
                                AccountManager accountManager = (AccountManager) MainUtraxxActivity.this.getSystemService(ACCOUNT_SERVICE);

                                // loop through all accounts to remove them
                                Account[] accounts = accountManager.getAccounts();
                                for (int index = 0; index < accounts.length; index++) {
                                    if (accounts[index].type.intern() == "com.geoverifi.geoverifi")
                                        accountManager.removeAccount(accounts[index], null, null);
                                }
                                Manager.getInstance(MainUtraxxActivity.this).clear();
                                startActivity(new Intent(MainUtraxxActivity.this, LoginActivity.class));
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


    public void getBaseData(final DatabaseHandler db){
        String url = Variables.BASE_URL + "API/getBaseData";
        final TextView splash_waiting = (TextView) findViewById(R.id.splash_wait);

        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject dataObj = response.getJSONObject("data");

                    JSONArray mediaOwners = dataObj.getJSONArray("media_owners");
                    JSONArray structures = dataObj.getJSONArray("structures");
                    JSONArray angles =  dataObj.getJSONArray("angles");
                    JSONArray illuminations = dataObj.getJSONArray("illuminations");
                    JSONArray materials = dataObj.getJSONArray("material_types");
                    JSONArray material_status = dataObj.getJSONArray("material_status");
                    JSONArray run_ups = dataObj.getJSONArray("run_ups");
                    JSONArray sizes = dataObj.getJSONArray("sizes");
                    JSONArray structure_conditions = dataObj.getJSONArray("structure_conditions");
                    JSONArray visibility_types = dataObj.getJSONArray("visibility_types");
                    JSONArray submissions = dataObj.getJSONArray("submissions");
                    JSONArray submission_photos = dataObj.getJSONArray("submission_photos");

                    for (int i = 0; i < mediaOwners.length(); i++) {
                        MediaOwner owner = new MediaOwner();
                        JSONObject ownerObj = mediaOwners.getJSONObject(i);

                        owner.set_id(ownerObj.getInt("id"));
                        owner.set_media_owner(ownerObj.getString("media_owner"));

                        db.addMediaOwner(owner);
                    }
                    for (int i = 0; i < structures.length(); i++) {
                        Structure structure = new Structure();
                        JSONObject structureObj = structures.getJSONObject(i);

                        structure.set_type_id(structureObj.getInt("type_id"));
                        structure.set_type_name(structureObj.getString("type_name"));
                        structure.set_type_photos(structureObj.getInt("type_photos"));
                        structure.set_material_type(structureObj.getString("type_media_type"));
                        structure.set_type_sides(structureObj.getInt("type_sides"));

                        db.addStructure(structure);
                    }
                    for (int i = 0; i < angles.length(); i++) {
                        Angle angle = new Angle();
                        JSONObject angleObj = angles.getJSONObject(i);

                        angle.set_id(angleObj.getInt("id"));
                        angle.set_angle(angleObj.getString("angle"));

                        db.addAngle(angle);
                    }


                    for (int i = 0; i < illuminations.length(); i++) {
                        Illumination illumination = new Illumination();
                        JSONObject illuminationObj = illuminations.getJSONObject(i);

                        illumination.set_id(illuminationObj.getInt("id"));
                        illumination.set_type(illuminationObj.getString("type"));

                        db.addIllumination(illumination);
                    }

                    for (int i = 0; i < materials.length(); i++) {
                        Material material = new Material();
                        JSONObject materialObj = materials.getJSONObject(i);

                        material.set_id(materialObj.getInt("id"));
                        material.set_material(materialObj.getString("type"));

                        db.addMaterial(material);
                    }

                    for (int i = 0; i < material_status.length(); i++) {
                        MaterialStatus status = new MaterialStatus();
                        JSONObject statusObj = material_status.getJSONObject(i);

                        status.set_id(statusObj.getInt("id"));
                        status.set_status(statusObj.getString("status"));

                        db.addMaterialStatus(status);
                    }

                    for (int i = 0; i < run_ups.length(); i++) {
                        RunUp runup = new RunUp();
                        JSONObject runupObj = run_ups.getJSONObject(i);

                        runup.set_id(runupObj.getInt("id"));
                        runup.set_run_up(runupObj.getString("run_up"));

                        db.addRunUp(runup);
                    }

                    for (int i = 0; i < sizes.length(); i++) {
                        Size size = new Size();
                        JSONObject sizeObj = sizes.getJSONObject(i);

                        size.set_id(sizeObj.getInt("id"));
                        size.set_size(sizeObj.getString("size"));

                        db.addSizes(size);
                    }

                    for (int i = 0; i < structure_conditions.length(); i++) {
                        StructureCondition condition = new StructureCondition();
                        JSONObject conditionObj = structure_conditions.getJSONObject(i);

                        condition.set_id(conditionObj.getInt("id"));
                        condition.set_condition(conditionObj.getString("condition"));

                        db.addStructureCondition(condition);
                    }

                    for (int i = 0; i < visibility_types.length(); i++) {
                        Visibility visibility = new Visibility();
                        JSONObject visibilityObj = visibility_types.getJSONObject(i);

                        visibility.set_id(visibilityObj.getInt("id"));
                        visibility.set_visibility(visibilityObj.getString("type"));

                        db.addVisibility(visibility);
                    }

                    for (int i = 0; i < submissions.length(); i++) {
                        Submission submission = new Submission();
                        JSONObject submissionObj = submissions.getJSONObject(i);

                        submission.set_id(submissionObj.getInt("id"));
                        submission.set_user_id(submissionObj.getInt("user_id"));
                        submission.set_submission_date(submissionObj.getString("submission_date"));
                        submission.set_brand(submissionObj.getString("brand"));
                        submission.set_media_owner(submissionObj.getString("media_owner"));
                        submission.set_town(submissionObj.getString("town"));
                        submission.set_structure(submissionObj.getString("type_name"));
                        submission.set_size(submissionObj.getString("size"));
                        submission.set_media_size_other_height(submissionObj.getString("media_size_height"));
                        submission.set_media_size_other_width(submissionObj.getString("media_size_width"));
                        submission.set_material(submissionObj.getString("material"));
                        submission.set_run_up(submissionObj.getString("run_up"));
                        submission.set_illumination(submissionObj.getString("illumination"));
                        submission.set_visibility(submissionObj.getString("visibility"));
                        submission.set_angle(submissionObj.getString("angle"));
                        submission.set_other_comments(submissionObj.getString("other_comments"));
                        submission.set_user_firstname(submissionObj.getString("user_firstname"));
                        submission.set_user_lastname(submissionObj.getString("user_lastname"));
                        submission.set_latitude(submissionObj.getString("latitude"));
                        submission.set_longitude(submissionObj.getString("longitude"));
                        submission.set_photo_1(submissionObj.getString("photo_1"));
                        submission.set_photo_2(submissionObj.getString("photo_2"));
                        submission.set_created_at(submissionObj.getString("created_at"));
                        db.addSubmission(submission);
                    }

                    for (int i = 0; i < submission_photos.length(); i++) {
                        SubmissionPhoto photo = new SubmissionPhoto();
                        JSONObject submissionPhotoObj = submission_photos.getJSONObject(i);

                        photo.setId(submissionPhotoObj.getInt("id"));
                        photo.setName(submissionPhotoObj.getString("name"));
                        photo.setSubmission_id(submissionPhotoObj.getInt("submission_id"));
                        photo.setThumb(submissionPhotoObj.getString("image_thumb"));
                        photo.setPath(submissionPhotoObj.getString("image_url"));
                        photo.setCreated_at(submissionPhotoObj.getString("uploaded_at"));

                        db.addPhoto(photo);
                    }

                    // startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    //finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainUtraxxActivity.this, "There was an error pulling data", Toast.LENGTH_LONG).show();
                System.out.println("====>" + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    public void getBaseauditingData(final DatabaseHandler db){
        String urls = Variables.BASE_URL + "API/getBaseauditingData";
        //final TextView splash_waiting = (TextView) findViewById(R.id.splash_wait);

        JsonObjectRequest request = new JsonObjectRequest(urls, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject dataObj1 = response.getJSONObject("data");

                    JSONArray submissions = dataObj1.getJSONArray("submissions");
                    JSONArray traffic_quality = dataObj1.getJSONArray("traffic_quantity");
                    JSONArray traffic_speed = dataObj1.getJSONArray("traffic_speed");

                    //JSONArray auditing_photos = dataObj1.getJSONArray("auditing_photos");


                    for (int i = 0; i < traffic_quality.length(); i++) {
                        TrafficQuantity trafficQuantity = new TrafficQuantity();
                        JSONObject traffic_qualityObj = traffic_quality.getJSONObject(i);

                        trafficQuantity.set_id(traffic_qualityObj.getInt("id"));
                        trafficQuantity.set_traffic_quantity(traffic_qualityObj.getString("traffic_quantity"));

                        db.addTrafficQuantity(trafficQuantity);
                    }

                    for (int i = 0; i < traffic_speed.length(); i++) {
                        TrafficSpeed trafficSpeed = new TrafficSpeed();
                        JSONObject traffic_speedObj = traffic_speed.getJSONObject(i);

                        trafficSpeed.set_id(traffic_speedObj.getInt("id"));
                        trafficSpeed.set_traffic_speed(traffic_speedObj.getString("traffic_speed"));

                        db.addTrafficSpeed(trafficSpeed);
                    }


                    for (int i = 0; i < submissions.length(); i++) {
                        SubmissionAuditing submission = new SubmissionAuditing();
                        JSONObject submissionObj = submissions.getJSONObject(i);

                        submission.set_id(submissionObj.getInt("id"));
                        submission.set_user_id(submissionObj.getInt("user_id"));
                        submission.set_submission_date(submissionObj.getString("submission_date"));
                        submission.set_brand(submissionObj.getString("brand"));
                        submission.set_media_owner(submissionObj.getString("media_owner"));
                        submission.set_town(submissionObj.getString("town"));
                        submission.set_structure(submissionObj.getString("type_name"));
                        submission.set_size(submissionObj.getString("size"));
                        submission.set_media_size_other_height(submissionObj.getString("media_size_height"));
                        submission.set_media_size_other_width(submissionObj.getString("media_size_width"));
                        submission.set_material(submissionObj.getString("material"));
                        submission.set_run_up(submissionObj.getString("run_up"));
                        submission.set_illumination(submissionObj.getString("illumination"));
                        submission.set_visibility(submissionObj.getString("visibility"));
                        submission.set_angle(submissionObj.getString("angle"));
                        submission.set_other_comments(submissionObj.getString("other_comments"));
                        submission.set_user_firstname(submissionObj.getString("user_firstname"));
                        submission.set_user_lastname(submissionObj.getString("user_lastname"));
                        submission.set_latitude(submissionObj.getString("latitude"));
                        submission.set_longitude(submissionObj.getString("longitude"));
                        submission.set_photo_1(submissionObj.getString("photo_1"));
                        submission.set_photo_2(submissionObj.getString("photo_2"));
                        submission.set_created_at(submissionObj.getString("created_at"));
                        submission.set_trafficquantity(submissionObj.getString("traffic_quantity"));
                        submission.set_trafficspeed(submissionObj.getString("traffic_speed"));
                        db.addSubmissionAuditing(submission);
                    }

                    /*for (int i = 0; i < auditing_photos.length(); i++) {
                        SubmissionPhotoAuditing photo = new SubmissionPhotoAuditing();
                        JSONObject submissionPhotoObj =auditing_photos.getJSONObject(i);

                        photo.setId(submissionPhotoObj.getInt("id"));
                        photo.setName(submissionPhotoObj.getString("name"));
                        photo.setSubmission_id(submissionPhotoObj.getInt("submission_id"));
                        photo.setThumb(submissionPhotoObj.getString("image_thumb"));
                        photo.setPath(submissionPhotoObj.getString("image_url"));
                        photo.setCreated_at(submissionPhotoObj.getString("uploaded_at"));

                        db.addAuditingPhoto(photo);
                    }*/


                    startActivity(new Intent(MainUtraxxActivity.this, MainActivity.class));
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainUtraxxActivity.this, "There was an error pulling data", Toast.LENGTH_LONG).show();
                System.out.println("====>" + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}


