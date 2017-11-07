package com.geoverifi.geoverifi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.geoverifi.geoverifi.adapter.AngleSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.IlluminationSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.MaterialSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.MediaOwnerAutoCompleteAdapter;
import com.geoverifi.geoverifi.adapter.RunUpSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.SizeSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.StructureSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.VisibilitySpinnerAdapter;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.fragments.LocationPreviewFragment;
import com.geoverifi.geoverifi.helper.DateHelper;
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
import com.geoverifi.geoverifi.provider.SubmissionProvider;
import com.geoverifi.geoverifi.service.GPSTracker;
import com.geoverifi.geoverifi.sharedpreference.Manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class MonitoringDataSubmission extends AppCompatActivity implements View.OnClickListener, LocationPreviewFragment.OnCompleteListener {
    GPSTracker gps;
    double latitude, longitude;
    DatabaseHandler db;
    AutoCompleteTextView autoCompleteTextView;

    int submission_id;

    List<Structure> structureList;
    List<Size> sizeList;
    List<Material> materialList;
    List<RunUp> runUpList;
    List<Illumination> illuminationList;
    List<Visibility> visibilityList;
    List<Angle> angleList;
    ArrayList<MediaOwner> mediaOwnerList;

    EditText txtSubmissionDate, etxBrand, etxTown, etxComment, etxSiteReferenceNumber;
    TextView txtMediaOwnerHidden, txtOtherHWHidden;

    Button btnContinue;

    String other_height= "0";
    String other_width = "0";

    Spinner media_size_spinner, material_type_spinner, run_up_spinner, illumination_spinner
            ,visibility_spinner, angle_spinner, structure_spinner;

    int day, month, year;

    User user;

    Formatter f = new Formatter();
    Context context = this;

    Submission submission;
    AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_data_submission);

        mAwesomeValidation = new AwesomeValidation(BASIC);

        gps = new GPSTracker(this);
        db= new DatabaseHandler(this);

        submission_id = getIntent().getIntExtra("submission_id", 0);

        if (submission_id == 0) {
            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

//                LocationPreviewFragment fragment = LocationPreviewFragment.newInstance();
//                Bundle bundle = new Bundle();
//                bundle.putDouble("latitude", latitude);
//                bundle.putDouble("longitude", longitude);
//                fragment.setArguments(bundle);
//                fragment.show(getFragmentManager(), "LocationPreviewDialog");
            } else {
                gps.showSettingsAlert();
            }
        }else{
            submission = db.draftSubmission(submission_id);

            latitude = Double.parseDouble(submission.get_latitude());
            longitude = Double.parseDouble(submission.get_longitude());
        }

        user = Manager.getInstance(context).getUser();

        getSupportActionBar().setTitle("Data Submission");
        getSupportActionBar().setSubtitle("Monitoring Data");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        media_size_spinner = (Spinner) findViewById(R.id.media_size);
        material_type_spinner  = (Spinner) findViewById(R.id.media_material_type);
        run_up_spinner = (Spinner) findViewById(R.id.media_run_up);
        illumination_spinner = (Spinner) findViewById(R.id.media_illumination);
        visibility_spinner = (Spinner) findViewById(R.id.media_visibility);
        angle_spinner = (Spinner) findViewById(R.id.media_angle);
        structure_spinner = (Spinner) findViewById(R.id.media_structure);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.input_media_owner);

        txtSubmissionDate = (EditText) findViewById(R.id.input_date);
        etxBrand = (EditText) findViewById(R.id.input_brand_advertiser);
        etxComment = (EditText) findViewById(R.id.input_other_comments);
        etxSiteReferenceNumber = (EditText) findViewById(R.id.site_reference_number);
        etxTown = (EditText) findViewById(R.id.input_town);

        txtMediaOwnerHidden = (TextView) findViewById(R.id.media_owner_hidden);
        txtOtherHWHidden = (TextView) findViewById(R.id.other_h_w);

        btnContinue = (Button) findViewById(R.id.continue_btn);

        sizeList = db.allSizes();
        materialList = db.allMaterials();
        runUpList = db.allRunUps();
        illuminationList = db.allIlluminations();
        visibilityList = db.allVisibilityTypes();
        angleList = db.allAngles();
        structureList = db.allStructures();
        mediaOwnerList = db.allMediaOwners();

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

        mAwesomeValidation.addValidation(this, R.id.input_town, RegexTemplate.NOT_EMPTY, R.string.err_town);
        mAwesomeValidation.addValidation(this, R.id.input_media_owner, RegexTemplate.NOT_EMPTY, R.string.err_media_owner);
        mAwesomeValidation.addValidation(this, R.id.input_brand_advertiser, RegexTemplate.NOT_EMPTY, R.string.err_brand_advertiser);
        btnContinue.setOnClickListener(this);

        txtSubmissionDate.setEnabled(false);
        txtSubmissionDate.setFocusable(false);

        if (latitude == 0.0 || longitude == 0.0){
            btnContinue.setVisibility(View.GONE);
            Toast.makeText(this, "Could not establish the exact coordinates. You will not be able to submit your data", Toast.LENGTH_LONG).show();
        }

        if (submission_id != 0){
            txtSubmissionDate.setText(submission.get_submission_date());
            etxBrand.setText(submission.get_brand());
            etxComment.setText(submission.get_other_comments());
            etxSiteReferenceNumber.setText(submission.get_site_reference_number());
            etxTown.setText(submission.get_town());
            txtMediaOwnerHidden.setText(submission.get_media_owner());

            if (!submission.get_media_size_other_width().equals("0") && !submission.get_media_size_other_height().equals("0")){
                other_height = submission.get_media_size_other_height();
                other_width = submission.get_media_size_other_width();

                txtOtherHWHidden.setText("Chosen Other: ("+ other_height + " x "+ other_width+")");
            }

            MediaOwner owner = new MediaOwner();
            if (!submission.get_media_owner().isEmpty()){
                owner = db.getMediaOwner(Integer.parseInt(submission.get_media_owner()));
                int media_owner_select = mediaOwnerList.indexOf(owner);
                autoCompleteTextView.setListSelection(media_owner_select);
                autoCompleteTextView.setText(owner.get_media_owner());
            }else{
                autoCompleteTextView.setText(submission.get_media_owner_name());
            }

            for (int i = 0; i < sizeList.size(); i++) {
                Size size = sizeList.get(i);
                if (size.get_id() == Integer.parseInt(submission.get_size()))
                    media_size_spinner.setSelection(i);
            }

            for (int i = 0; i < structureList.size(); i++) {
                Structure structure = structureList.get(i);
                if (structure.get_type_id() == Integer.parseInt(submission.get_structure()))
                    structure_spinner.setSelection(i);
            }

            for (int i = 0; i < materialList.size(); i++) {
                Material material = materialList.get(i);
                if (material.get_id() == Integer.parseInt(submission.get_material()))
                    material_type_spinner.setSelection(i);
            }

            for (int i = 0; i < runUpList.size(); i++) {
                RunUp runUp = runUpList.get(i);
                if (runUp.get_id() == Integer.parseInt(submission.get_run_up()))
                    run_up_spinner.setSelection(i);
            }

            for (int i = 0; i < illuminationList.size(); i++) {
                Illumination illumination = illuminationList.get(i);
                if (illumination.get_id() == Integer.parseInt(submission.get_illumination()))
                    illumination_spinner.setSelection(i);
            }

            for (int i = 0; i < visibilityList.size(); i++) {
                Visibility visibility = visibilityList.get(i);
                if (visibility.get_id() == Integer.parseInt(submission.get_visibility()))
                    visibility_spinner.setSelection(i);
            }

            for (int i = 0; i < angleList.size(); i++) {
                Angle angle = angleList.get(i);
                if (angle.get_id() == Integer.parseInt(submission.get_angle()))
                    angle_spinner.setSelection(i);
            }
        }

        callCalendar();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.continue_btn:
                if(mAwesomeValidation.validate()) {
                    if (submission_id != 0) {
                        updateDraft();
                    } else {
                        saveDraft();
                    }
                    Intent intent = new Intent(this, MonitoringSubmissionPhotosActivity.class);
                    intent.putExtra("submission_id", submission_id);
                    startActivity(intent);
                }else{
                    Toast.makeText(context, "You have some errors that you need to attend to", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private Submission getInput(){
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

        Submission inputSubmission = new Submission();

        inputSubmission.set_id(submission_id);
        inputSubmission.set_submission_date(txtSubmissionDate.getText().toString());
        inputSubmission.set_site_reference_number(etxSiteReferenceNumber.getText().toString());
        inputSubmission.set_brand(etxBrand.getText().toString());
        inputSubmission.set_media_owner(txtMediaOwnerHidden.getText().toString());
        inputSubmission.set_media_owner_name(autoCompleteTextView.getText().toString());
        inputSubmission.set_structure(String.valueOf(structure.get_type_id()));
        inputSubmission.set_town(etxTown.getText().toString());
        inputSubmission.set_size(String.valueOf(size.get_id()));
        inputSubmission.set_media_size_other_height(other_height);
        inputSubmission.set_media_size_other_width(other_width);
        inputSubmission.set_material(String.valueOf(material.get_id()));
        inputSubmission.set_run_up(String.valueOf(runup.get_id()));
        inputSubmission.set_illumination(String.valueOf(illumination.get_id()));
        inputSubmission.set_visibility(String.valueOf(visibility.get_id()));
        inputSubmission.set_angle(String.valueOf(angle.get_id()));
        inputSubmission.set_other_comments(etxComment.getText().toString());
        inputSubmission.set_latitude(String.valueOf(latitude));
        inputSubmission.set_longitude(String.valueOf(longitude));
        inputSubmission.set_user_id(user.get_id());
        inputSubmission.set_status(-1);

        return inputSubmission;
    }
    private void updateDraft(){
        db.updatedDraftSubmission(getInput());

        Toast.makeText(context, "Updated draft", Toast.LENGTH_SHORT).show();
    }

    private void saveDraft() {
        ContentValues values = new ContentValues();

//        values.put(DatabaseHandler.KEY_SUBMISSION_DATE, txtSubmissionDate.getText().toString());
//        values.put(DatabaseHandler.KEY_SITE_REFERENCE, etxSiteReferenceNumber.getText().toString());
//        values.put(DatabaseHandler.KEY_BRAND, etxBrand.getText().toString());
//        values.put(DatabaseHandler.KEY_MEDIA_OWNER_ID, txtMediaOwnerHidden.getText().toString());
//        values.put(DatabaseHandler.KEY_TOWN, etxTown.getText().toString());
//        values.put(DatabaseHandler.KEY_STRUCTURE_ID, structureList.get(structure_spinner.getSelectedItemPosition()).get_type_id());
//        values.put(DatabaseHandler.KEY_MEDIA_SIZE_ID, sizeList.get(media_size_spinner.getSelectedItemPosition()).get_id());
//        values.put(DatabaseHandler.KEY_MEDIA_SIZE_HEIGHT, other_height);
//        values.put(DatabaseHandler.KEY_MEDIA_SIZE_WIDTH, other_width);
//        values.put(DatabaseHandler.KEY_MATERIAL_TYPE_ID, materialList.get(material_type_spinner.getSelectedItemPosition()).get_id());
//        values.put(DatabaseHandler.KEY_RUN_UP_ID, runUpList.get(run_up_spinner.getSelectedItemPosition()).get_id());
//        values.put(DatabaseHandler.KEY_ILLUMINATION_ID, illuminationList.get(illumination_spinner.getSelectedItemPosition()).get_id());
//        values.put(DatabaseHandler.KEY_VISIBILITY_ID, visibilityList.get(visibility_spinner.getSelectedItemPosition()).get_id());
//        values.put(DatabaseHandler.KEY_ANGLE_ID, angleList.get(angle_spinner.getSelectedItemPosition()).get_id());
//        values.put(DatabaseHandler.KEY_OTHER_COMMENTS, etxComment.getText().toString());



        long id = db.addDraftSubmission(getInput());

        submission_id = Integer.valueOf(String.valueOf(id));

        Toast.makeText(context, "Saved to draft", Toast.LENGTH_SHORT).show();
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

            String date_string = getTodaysDate(day, month, year);
            txtSubmissionDate.setText(date_string);
        }
    };

    public void callCalendar() {
        Calendar c = Calendar.getInstance();


        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
//
//        String monthName = f.format("%tB",c).toString();

        String date_string = getTodaysDate(day, month, year);
        txtSubmissionDate.setText(date_string);
    }

    public String getTodaysDate(int day, int month, int year){
        String formatted_date = "";

        formatted_date = day + "/" + (month + 1) + "/" + year;
        return formatted_date;
    }

    @Override
    public void onComplete(String town) {
        etxTown.setText(town);
//        etxTown.setEnabled(false);
    }
}
