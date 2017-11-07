package com.geoverifi.geoverifi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.geoverifi.geoverifi.adapter.AngleSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.IlluminationSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.MaterialSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.MediaOwnerAutoCompleteAdapter;
import com.geoverifi.geoverifi.adapter.RunUpSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.SizeSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.StructureSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.VisibilitySpinnerAdapter;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.app.VolleyMultipartRequest;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.helper.InternetChecker;
import com.geoverifi.geoverifi.model.Angle;
import com.geoverifi.geoverifi.model.Illumination;
import com.geoverifi.geoverifi.model.Material;
import com.geoverifi.geoverifi.model.MediaOwner;
import com.geoverifi.geoverifi.model.RunUp;
import com.geoverifi.geoverifi.model.Size;
import com.geoverifi.geoverifi.model.Structure;
import com.geoverifi.geoverifi.model.Submission;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.model.Visibility;
import com.geoverifi.geoverifi.module.AppHelper;
import com.geoverifi.geoverifi.server.client.SubmissionClient;
import com.geoverifi.geoverifi.server.model.Response;
import com.geoverifi.geoverifi.service.GPSTracker;
import com.geoverifi.geoverifi.sharedpreference.Manager;
import com.geoverifi.geoverifi.util.FileUtils;
import com.geoverifi.geoverifi.util.Picture;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DataSubmissionActivity extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;

    List<Structure> structureList;
    List<Size> sizeList;
    List<Material> materialList;
    List<RunUp> runUpList;
    List<Illumination> illuminationList;
    List<Visibility> visibilityList;
    List<Angle> angleList;

    User user;

    Spinner media_size_spinner, material_type_spinner, run_up_spinner, illumination_spinner
            ,visibility_spinner, angle_spinner, structure_spinner;

    Button btnUploadPhoto1, btnUploadPhoto2, btnSubmit;
    ImageButton btnChooseDate;
    ImageView imgPhoto1, imgPhoto2;
    EditText txtSubmissionDate, etxBrand, etxTown, etxComment, etxSiteReferenceNumber;
    TextView txtMediaOwnerHidden, txtOtherHWHidden, txtPhoto1Path, txtPhoto2Path;

    AutoCompleteTextView autoCompleteTextView;

    static final int REQUEST_PHOTO_1_CAPTURE = 100;
    static final int REQUEST_PHOTO_2_CAPTURE = 101;
    String other_height= "0";
    String other_width = "0";
    String mCurrentPhotoPath;

    GPSTracker gps;

    int year, month, day;

    double latitude, longitude;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_submission);
        user = Manager.getInstance(context).getUser();

        gps = new GPSTracker(DataSubmissionActivity.this);
        etxTown = (EditText) findViewById(R.id.input_town);


        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Toast.makeText(context, String.valueOf(latitude), Toast.LENGTH_SHORT).show();

            if (InternetChecker.getInstance(context).isNetworkAvailable()) {
                Geocoder gcd = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longitude, 2);
                    if (addresses.size() > 0) {
                        String picked_town = addresses.get(0).getLocality();
                        etxTown.setText(picked_town);
                        etxTown.setEnabled(false);
                    } else {
                        Toast.makeText(context, "Could not get town A", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Log.e("LocationError", e.getMessage());
//                e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "There is no internet connection. Cannot get town", Toast.LENGTH_SHORT).show();
            }

            // \n is for new line

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        db = new DatabaseHandler(this);

        media_size_spinner = (Spinner) findViewById(R.id.media_size);
        material_type_spinner  = (Spinner) findViewById(R.id.media_material_type);
        run_up_spinner = (Spinner) findViewById(R.id.media_run_up);
        illumination_spinner = (Spinner) findViewById(R.id.media_illumination);
        visibility_spinner = (Spinner) findViewById(R.id.media_visibility);
        angle_spinner = (Spinner) findViewById(R.id.media_angle);
        structure_spinner = (Spinner) findViewById(R.id.media_structure);

        btnUploadPhoto1 = (Button) findViewById(R.id.btn_upload_photo_1);
        btnUploadPhoto2 = (Button) findViewById(R.id.btn_upload_photo_2);
        btnChooseDate = (ImageButton) findViewById(R.id.pick_date);
        btnSubmit = (Button) findViewById(R.id.submit_button);

        imgPhoto1 = (ImageView) findViewById(R.id.img_photo_1);
        imgPhoto2 = (ImageView) findViewById(R.id.img_photo_2);

        txtSubmissionDate = (EditText) findViewById(R.id.input_date);
        etxBrand = (EditText) findViewById(R.id.input_brand_advertiser);
        etxComment = (EditText) findViewById(R.id.input_other_comments);
        etxSiteReferenceNumber = (EditText) findViewById(R.id.site_reference_number);

        txtMediaOwnerHidden = (TextView) findViewById(R.id.media_owner_hidden);
        txtOtherHWHidden = (TextView) findViewById(R.id.other_h_w);
        txtPhoto1Path = (TextView) findViewById(R.id.photo1path);
        txtPhoto2Path = (TextView) findViewById(R.id.photo2path);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_media_owner);

        txtSubmissionDate.setEnabled(false);
        txtSubmissionDate.setFocusable(false);

        sizeList = db.allSizes();
        materialList = db.allMaterials();
        runUpList = db.allRunUps();
        illuminationList = db.allIlluminations();
        visibilityList = db.allVisibilityTypes();
        angleList = db.allAngles();
        structureList = db.allStructures();
        ArrayList<MediaOwner> mediaOwnerList = db.allMediaOwners();

        SizeSpinnerAdapter adapter = new SizeSpinnerAdapter(this, sizeList);
        MaterialSpinnerAdapter materialSpinnerAdapter = new MaterialSpinnerAdapter(this, materialList);
        RunUpSpinnerAdapter runUpSpinnerAdapter = new RunUpSpinnerAdapter(this, runUpList);
        IlluminationSpinnerAdapter illuminationSpinnerAdapter = new IlluminationSpinnerAdapter(this, illuminationList);
        VisibilitySpinnerAdapter visibilitySpinnerAdapter = new VisibilitySpinnerAdapter(this, visibilityList);
        AngleSpinnerAdapter angleSpinnerAdapter = new AngleSpinnerAdapter(this, angleList);
        StructureSpinnerAdapter structureSpinnerAdapter = new StructureSpinnerAdapter(this, structureList);
        MediaOwnerAutoCompleteAdapter mediaOwnerAutoCompleteAdapter = new MediaOwnerAutoCompleteAdapter(this, R.layout.media_owner_list_item, mediaOwnerList);


        media_size_spinner.setAdapter(adapter);
        material_type_spinner.setAdapter(materialSpinnerAdapter);
        run_up_spinner.setAdapter(runUpSpinnerAdapter);
        illumination_spinner.setAdapter(illuminationSpinnerAdapter);
        visibility_spinner.setAdapter(visibilitySpinnerAdapter);
        angle_spinner.setAdapter(angleSpinnerAdapter);
        structure_spinner.setAdapter(structureSpinnerAdapter);
        autoCompleteTextView.setAdapter(mediaOwnerAutoCompleteAdapter);

        getSupportActionBar().setTitle("DATA SUBMISSION");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnUploadPhoto1.setOnClickListener(this);
        btnUploadPhoto2.setOnClickListener(this);
        btnChooseDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mediaOwnerText = (TextView) view.findViewById(R.id.media_owner_label);
                TextView mediaOwnerIDText = (TextView) view.findViewById(R.id.media_owner_id);

                txtMediaOwnerHidden.setText(mediaOwnerIDText.getText().toString());
                autoCompleteTextView.setText(mediaOwnerText.getText().toString());
            }
        });

        media_size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView txtMediaSize = (TextView) view.findViewById(R.id.size_name);
                String media_size = txtMediaSize.getText().toString();
                if (media_size.equals("Other (H x W)")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("New Size");
                    LayoutInflater li = LayoutInflater.from(context);
                    View newSizeView = li.inflate(R.layout.other_media_size_view, null);

                    alertDialog.setView(newSizeView);

                    final EditText etxHeight = (EditText) newSizeView.findViewById(R.id.input_height);
                    final EditText etxWidth = (EditText) newSizeView.findViewById(R.id.input_width);

                    if (!other_height.equals("0")){
                        etxHeight.setText(other_height);
                        etxWidth.setText(other_width);
                    }

                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            other_height = etxHeight.getText().toString();
                            other_width = etxWidth.getText().toString();
                            txtOtherHWHidden.setText("Chosen Other: ("+ other_height + " x "+ other_width+")");
                            txtOtherHWHidden.setVisibility(View.VISIBLE);
                        }
                    });

                    alertDialog.setNegativeButton("Cancel", null);
                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                }else{
                    txtOtherHWHidden.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        callCalendar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload_photo_1:
                dispatchTakePictureIntent(REQUEST_PHOTO_1_CAPTURE);
                break;
            case R.id.btn_upload_photo_2:
                dispatchTakePictureIntent(REQUEST_PHOTO_2_CAPTURE);
                break;
            case R.id.pick_date:
                showDialog(999);
                break;
            case R.id.submit_button:

                if (InternetChecker.getInstance(context).isNetworkAvailable() == false){
                    saveoffline();
                    Toast.makeText(context, "Submission saved on device. It will be submitted as soon as you go online", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, OfflineDataActivity.class));
                    finish();
                }else{
                    final ProgressDialog pDialog = new ProgressDialog(this);
                    pDialog.setMessage("Uploading Data");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    btnSubmit.setEnabled(false);
                    btnSubmit.setText("Please wait...");
                    submit_data(pDialog);
                }
                break;
        }
    }

    public class MyViewListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            // Code to undo the user's last action
        }
    }

    private void saveoffline(){
        final String photo1_path = txtPhoto1Path.getText().toString();
        final String photo2_path = txtPhoto2Path.getText().toString();

        final Bitmap bmp_photo_1 = decodeFile(photo1_path);
        final Bitmap bmp_photo_2 = decodeFile(photo2_path);

        Uri tempPhoto1Uri = Picture.getImageUri(context, bmp_photo_1);
        Uri tempPhoto2Uri = Picture.getImageUri(context, bmp_photo_2);

        final String real_photo_1_path = getRealPathFromURI(tempPhoto1Uri);
        final String real_photo_2_path = getRealPathFromURI(tempPhoto2Uri);

        int selected_structure_position = structure_spinner.getSelectedItemPosition();
        int selected_media_size = media_size_spinner.getSelectedItemPosition();
        int selected_material = material_type_spinner.getSelectedItemPosition();
        int selected_run_up = run_up_spinner.getSelectedItemPosition();
        int selected_illumination_type = illumination_spinner.getSelectedItemPosition();
        int selected_visibility = visibility_spinner.getSelectedItemPosition();
        int selected_angle = angle_spinner.getSelectedItemPosition();

        final Structure structure = structureList.get(selected_structure_position);
        final Size size = sizeList.get(selected_media_size);
        final Material material = materialList.get(selected_material);
        final RunUp runup = runUpList.get(selected_run_up);
        final Illumination illumination = illuminationList.get(selected_illumination_type);
        final Visibility visibility = visibilityList.get(selected_visibility);
        final Angle angle = angleList.get(selected_angle);

        Submission submission = new Submission();

        submission.set_submission_date(txtSubmissionDate.getText().toString());
        submission.set_site_reference_number(etxSiteReferenceNumber.getText().toString());
        submission.set_photo_1(real_photo_1_path);
        submission.set_photo_2(real_photo_2_path);
        submission.set_brand(etxBrand.getText().toString());
        submission.set_media_owner(txtMediaOwnerHidden.getText().toString());
        submission.set_structure(String.valueOf(structure.get_type_id()));
        submission.set_town(etxTown.getText().toString());
        submission.set_size(String.valueOf(size.get_id()));
        submission.set_media_size_other_height(other_height);
        submission.set_media_size_other_width(other_width);
        submission.set_material(String.valueOf(material.get_id()));
        submission.set_run_up(String.valueOf(runup.get_id()));
        submission.set_illumination(String.valueOf(illumination.get_id()));
        submission.set_visibility(String.valueOf(visibility.get_id()));
        submission.set_angle(String.valueOf(angle.get_id()));
        submission.set_other_comments(etxComment.getText().toString());
        submission.set_latitude(String.valueOf(latitude));
        submission.set_longitude(String.valueOf(longitude));
        submission.set_user_id(user.get_id());
        submission.set_status(0);

        db.addOfflineSubmission(submission);
    }

    private void submit_data(final ProgressDialog pDialog) {


        final String photo1_path = txtPhoto1Path.getText().toString();
        final String photo2_path = txtPhoto2Path.getText().toString();

        final Bitmap bmp_photo_1 = decodeFile(photo1_path);
        final Bitmap bmp_photo_2 = decodeFile(photo2_path);

        Uri tempPhoto1Uri = Picture.getImageUri(context, bmp_photo_1);
        Uri tempPhoto2Uri = Picture.getImageUri(context, bmp_photo_2);

        final String real_photo_1_path = getRealPathFromURI(tempPhoto1Uri);
        final String real_photo_2_path = getRealPathFromURI(tempPhoto2Uri);

        int selected_structure_position = structure_spinner.getSelectedItemPosition();
        int selected_media_size = media_size_spinner.getSelectedItemPosition();
        int selected_material = material_type_spinner.getSelectedItemPosition();
        int selected_run_up = run_up_spinner.getSelectedItemPosition();
        int selected_illumination_type = illumination_spinner.getSelectedItemPosition();
        int selected_visibility = visibility_spinner.getSelectedItemPosition();
        int selected_angle = angle_spinner.getSelectedItemPosition();

        final Structure structure = structureList.get(selected_structure_position);
        final Size size = sizeList.get(selected_media_size);
        final Material material = materialList.get(selected_material);
        final RunUp runup = runUpList.get(selected_run_up);
        final Illumination illumination = illuminationList.get(selected_illumination_type);
        final Visibility visibility = visibilityList.get(selected_visibility);
        final Angle angle = angleList.get(selected_angle);

//        com.geoverifi.geoverifi.server.model.Submission submission = new com.geoverifi.geoverifi.server.model.Submission();
//        submission.setAngle(angle.get_id());
//        submission.setBrand(etxBrand.getText().toString());
//        submission.setIllumination_type_id(illumination.get_id());
//        submission.setLatitude(latitude);
//        submission.setLongitude(longitude);
//        submission.setMaterial_type_id(material.get_id());
//        submission.setMedia_owner(txtMediaOwnerHidden.getText().toString());
//        submission.setMedia_size(size.get_id());
//        submission.setMedia_size_other_height(Integer.parseInt(other_height));
//        submission.setMedia_size_other_width(Integer.parseInt(other_width));
//        submission.setOther_comments(etxComment.getText().toString());
//        submission.setRun_up_id(runup.get_id());
//        submission.setStructure_id(structure.get_type_id());
//        submission.setSubmission_date(txtSubmissionDate.getText().toString());
//        submission.setTown(etxTown.getText().toString());
//        submission.setUser_id(user.get_id());
//        submission.setVisibility(visibility.get_id());

        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("submission_date", createPartFromString(txtSubmissionDate.getText().toString()));
        params.put("site_reference_number", createPartFromString(etxSiteReferenceNumber.getText().toString()));
        params.put("brand", createPartFromString(etxBrand.getText().toString()));
        params.put("media_owner", createPartFromString(txtMediaOwnerHidden.getText().toString()));
        params.put("structure_id", createPartFromString(String.valueOf(structure.get_type_id())));
        params.put("town", createPartFromString(etxTown.getText().toString()));
        params.put("media_size", createPartFromString(String.valueOf(size.get_id())));
        params.put("media_size_other_height", createPartFromString(other_height));
        params.put("media_size_other_width", createPartFromString(other_width));
        params.put("material_type_id", createPartFromString(String.valueOf(material.get_id())));
        params.put("run_up_id", createPartFromString(String.valueOf(runup.get_id())));
        params.put("illumination_type_id", createPartFromString(String.valueOf(illumination.get_id())));
        params.put("visibility", createPartFromString(String.valueOf(visibility.get_id())));
        params.put("angle", createPartFromString(String.valueOf(angle.get_id())));
        params.put("other_comments", createPartFromString(etxComment.getText().toString()));
        params.put("latitude", createPartFromString(String.valueOf(latitude)));
        params.put("longitude", createPartFromString(String.valueOf(longitude)));
        params.put("user_id", createPartFromString(String.valueOf(user.get_id())));


        Retrofit.Builder builder = new Retrofit.Builder()
                                    .baseUrl(Variables.BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        SubmissionClient client = retrofit.create(SubmissionClient.class);
        Call<com.geoverifi.geoverifi.server.model.Response> call = client.createSubmission(
                params,
                prepareFilePart("photo_1", tempPhoto1Uri),
                prepareFilePart("photo_2", tempPhoto2Uri)
        );

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                pDialog.dismiss();
                btnSubmit.setEnabled(true);
                btnSubmit.setText("SUBMIT DATA");
                System.out.println(response.raw());
                if (response.body().isStatus()) {
                    finish();
                    startActivity(getIntent());
//                    System.out.println(response.raw());
                    Toast.makeText(DataSubmissionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    saveoffline();
                    Toast.makeText(DataSubmissionActivity.this, "Could not connect to server. Data stored offline", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(DataSubmissionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                saveoffline();
                Toast.makeText(DataSubmissionActivity.this, "Could not connect to server. Data stored offline", Toast.LENGTH_LONG).show();
            }
        });


//        String url = Variables.BASE_URL + "API/addSubmission";
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                pDialog.dismiss();
//                String resultResponse = new String(response.data);
//                Log.e("RESPONSE", resultResponse);
//                try {
//                    JSONObject resultJSON = new JSONObject(resultResponse);
//                    if (resultJSON.getBoolean("status") == true){
//                        Toast.makeText(context, resultJSON.getString("message"), Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(context, ViewSubmissionsActivity.class));
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
//                NetworkResponse networkResponse = error.networkResponse;
//                String errorMessage = "Unknown error";
//                if (networkResponse == null) {
//                    if (error.getClass().equals(TimeoutError.class)) {
//                        errorMessage = "Request timeout";
//                    } else if (error.getClass().equals(NoConnectionError.class)) {
//                        errorMessage = "Failed to connect server";
//                    }
//                }else{
//                    String result = new String(networkResponse.data);
//                    try {
//                        JSONObject response = new JSONObject(result);
//                        String message = response.getString("errors");
//                        Log.e("Error Message", message);
//
//                        if (networkResponse.statusCode == 404) {
//                            errorMessage = "Resource not found";
//                        } else if (networkResponse.statusCode == 401) {
//                            errorMessage = message+" Please login again";
//                        } else if (networkResponse.statusCode == 400) {
//                            errorMessage = message+ " Check your inputs";
//                        } else if (networkResponse.statusCode == 500) {
//                            errorMessage = message+" Something is getting wrong";
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.i("Error", errorMessage);
//                new AlertDialog.Builder(DataSubmissionActivity.this)
//                        .setTitle("Error")
//                        .setMessage(errorMessage)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setPositiveButton(android.R.string.ok, null).show();
//                error.printStackTrace();
//                saveoffline();
//                Toast.makeText(context, "Your data was saved offline", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("submission_date", txtSubmissionDate.getText().toString());
//                params.put("brand", etxBrand.getText().toString());
//                params.put("media_owner", txtMediaOwnerHidden.getText().toString());
//                params.put("structure_id", String.valueOf(structure.get_type_id()));
//                params.put("town", etxTown.getText().toString());
//                params.put("media_size", String.valueOf(size.get_id()));
//                params.put("media_size_other_height", other_height);
//                params.put("media_size_other_width", other_width);
//                params.put("material_type_id", String.valueOf(material.get_id()));
//                params.put("run_up_id", String.valueOf(runup.get_id()));
//                params.put("illumination_type_id", String.valueOf(illumination.get_id()));
//                params.put("visibility", String.valueOf(visibility.get_id()));
//                params.put("angle", String.valueOf(angle.get_id()));
//                params.put("other_comments", etxComment.getText().toString());
//                params.put("latitude", String.valueOf(latitude));
//                params.put("longitude", String.valueOf(longitude));
//                params.put("user_id", String.valueOf(user.get_id()));
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() throws AuthFailureError {
//                Map<String, DataPart> params = new HashMap<>();
//
//                Drawable drawable_photo_1 = Drawable.createFromPath(real_photo_1_path);
//                Drawable drawable_photo_2 = Drawable.createFromPath(real_photo_2_path);
//
//                String mimeType_photo_1 = Picture.getMimeType(photo1_path);
//                String mimeType_photo_2 = Picture.getMimeType(photo2_path);
//
//                params.put("photo_1", new DataPart(photo1_path, AppHelper.getFileDataFromDrawable(getBaseContext(), drawable_photo_1), mimeType_photo_1));
//                params.put("photo_2", new DataPart(photo2_path, AppHelper.getFileDataFromDrawable(getBaseContext(), drawable_photo_2), mimeType_photo_2));
//
//                return params;
//            }
//        };
//
//        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(volleyMultipartRequest);
//        AppController.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    @NonNull
    private RequestBody createPartFromString(String string){
        return RequestBody.create(
                MultipartBody.FORM, string
        );
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri){
        File file = FileUtils.getFile(this, fileUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void dispatchTakePictureIntent(int code){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Uri photoURI = null;
            if (photoFile != null && android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                photoURI = FileProvider.getUriForFile(this,"com.geoverifi.geoverifi.provider", photoFile);
            }else if(photoFile != null && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                photoURI = FileUtils.getUri(photoFile);
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, code);
        }else{
            Toast.makeText(this, "Get Package Manager is Null", Toast.LENGTH_SHORT).show();
        }
    }

    public void callCalendar() {
        Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        txtSubmissionDate.setText(new StringBuilder()
                .append(day).append("/")
                .append(month + 1).append("/")
                .append(year));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 999:
                return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int dayOfMonth) {
            year = selectedYear;
            month = selectedMonth;
            day = dayOfMonth;

            txtSubmissionDate.setText(new StringBuilder()
                    .append(day).append("/")
                    .append(month + 1).append("/")
                    .append(year));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(data, PackageManager.MATCH_DEFAULT_ONLY);
//            for (ResolveInfo resolveInfo : resInfoList){
//                String packageName = resolveInfo.activityInfo.packageName;
//                context.grantUriPermission(packageName, data.getData(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }
//        }
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_PHOTO_1_CAPTURE || requestCode == REQUEST_PHOTO_2_CAPTURE){
                if (requestCode == REQUEST_PHOTO_1_CAPTURE){
                    imgPhoto1.setImageBitmap(decodeFile(mCurrentPhotoPath));
                    txtPhoto1Path.setText(mCurrentPhotoPath);
                    if (txtPhoto2Path.getText().toString().equals("")){
                        Toast.makeText(context, "Taking Photo 2", Toast.LENGTH_SHORT).show();
                        btnUploadPhoto2.performClick();
                    }
                }else{
                    imgPhoto2.setImageBitmap(decodeFile(mCurrentPhotoPath));
                    txtPhoto2Path.setText(mCurrentPhotoPath);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1000;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }
}
