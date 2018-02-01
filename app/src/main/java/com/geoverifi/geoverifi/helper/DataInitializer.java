package com.geoverifi.geoverifi.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.db.DatabaseHandler;
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
import com.geoverifi.geoverifi.model.SubmissionAuditing;
import com.geoverifi.geoverifi.model.TrafficQuantity;
import com.geoverifi.geoverifi.model.TrafficSpeed;
import com.geoverifi.geoverifi.model.Visibility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chriz on 10/4/2017.
 */

public class DataInitializer {
    private static Context context;
    private static Activity activity;
    private static DataInitializer mInstance;
    DatabaseHandler db;
    boolean SHOW_PROGRESS_FLAG = false;
    ProgressDialog progressDialog;

    public DataInitializer(Context context, Boolean showProgress){
        this.context = context;
        this.db = new DatabaseHandler(context);
        this.SHOW_PROGRESS_FLAG = showProgress;
        if (SHOW_PROGRESS_FLAG == true) {
            progressDialog = new ProgressDialog(context);
        }
    }

    public static synchronized DataInitializer getInstance(Context context, boolean showProgress){
        if (mInstance == null){
            mInstance = new DataInitializer(context, showProgress);
        }

        return mInstance;
    }

    public void initialize(){
        db.clearDatabase();
        if (db.getmainMenuCount() == 0 || db.allMediaOwners().size() == 0){
            // Add Menu Items
            setupMenuMain();
            setupMenuUtraxx();
            setupMenuItraxx();
            //getBaseData();
            //getAuditBaseData();
        }
    }

    public void setupMenuMain(){
        Menu menu = new Menu();
        menu.set_menu_title("uTraxx");
        menu.set_menu_icon(R.mipmap.ic_doc_add);
        menu.set_menu_slug("utraxx_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addMainMenu(menu);

        menu = new Menu();
        menu.set_menu_title("iTraxx");
        menu.set_menu_icon(R.mipmap.ic_doc_add);
        menu.set_menu_slug("itraxx_action");
        menu.set_menu_users("Admin, Super");
        db.addMainMenu(menu);

        menu = new Menu();
        menu.set_menu_title("My Account");
        menu.set_menu_icon(R.mipmap.ic_user_account);
        menu.set_menu_slug("my_account");
        menu.set_menu_users("Admin, Staff Member, Client, Super");

        db.addMainMenu(menu);

        menu = new Menu();
        menu.set_menu_title("Administrator");
        menu.set_menu_icon(R.mipmap.ic_dashboard);
        menu.set_menu_slug("administrator_section");
        menu.set_menu_users("Admin, Super");
        db.addMainMenu(menu);

    }
    public void setupMenuUtraxx(){
        Menu menu = new Menu();
        menu.set_menu_title("Sync Data");
        menu.set_menu_icon(R.mipmap.ic_doc_add);
        menu.set_menu_slug("sycn_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addUtraxxMenu(menu);

        menu = new Menu();
        menu.set_menu_title("View Offline submissions ");
        menu.set_menu_icon(R.mipmap.ic_doc_add);
        menu.set_menu_slug("offline_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addUtraxxMenu(menu);

        menu = new Menu();
        menu.set_menu_title("Add new data");
        menu.set_menu_icon(R.mipmap.ic_view);
        menu.set_menu_slug("enter_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addUtraxxMenu(menu);


        menu = new Menu();
        menu.set_menu_title("View Draft Submissions");
        menu.set_menu_icon(R.mipmap.ic_view);
        menu.set_menu_slug("draft_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addUtraxxMenu(menu);


        /*menu = new Menu();
        menu.set_menu_title("Reports");
        menu.set_menu_icon(R.mipmap.ic_reports);
        menu.set_menu_slug("view_reports_action");
        menu.set_menu_users("Admin, Client, Super");
        db.addUtraxxMenu(menu);*/

        menu = new Menu();
        menu.set_menu_title("Administrator");
        menu.set_menu_icon(R.mipmap.ic_dashboard);
        menu.set_menu_slug("administrator_section");
        menu.set_menu_users("Admin, Super");
        db.addUtraxxMenu(menu);

        menu = new Menu();
        menu.set_menu_title("My Account");
        menu.set_menu_icon(R.mipmap.ic_user_account);
        menu.set_menu_slug("my_account");
        menu.set_menu_users("Admin, Staff Member, Client, Super");

        db.addUtraxxMenu(menu);
    }
    public void setupMenuItraxx(){

        Menu menu = new Menu();
        menu.set_menu_title("Sync Data");
        menu.set_menu_icon(R.mipmap.ic_doc_add);
        menu.set_menu_slug("sycn_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addItraxxMenu(menu);

        menu = new Menu();
        menu.set_menu_title("View Offline submissions ");
        menu.set_menu_icon(R.mipmap.ic_doc_add);
        menu.set_menu_slug("offline_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addItraxxMenu(menu);


        menu = new Menu();
        menu.set_menu_title("Add new data");
        menu.set_menu_icon(R.mipmap.ic_view);
        menu.set_menu_slug("enter_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addItraxxMenu(menu);


        menu = new Menu();
        menu.set_menu_title("View Draft Submissions");
        menu.set_menu_icon(R.mipmap.ic_view);
        menu.set_menu_slug("draft_data_action");
        menu.set_menu_users("Admin, Staff Member, Super");
        db.addItraxxMenu(menu);

        /*menu = new Menu();
        menu.set_menu_title("Reports");
        menu.set_menu_icon(R.mipmap.ic_reports);
        menu.set_menu_slug("view_reports_action");
        menu.set_menu_users("Admin, Client, Super");
        db.addItraxxMenu(menu);*/

        menu = new Menu();
        menu.set_menu_title("Administrator");
        menu.set_menu_icon(R.mipmap.ic_dashboard);
        menu.set_menu_slug("administrator_section");
        menu.set_menu_users("Admin, Super");
        db.addItraxxMenu(menu);

        menu = new Menu();
        menu.set_menu_title("My Account");
        menu.set_menu_icon(R.mipmap.ic_user_account);
        menu.set_menu_slug("my_account");
        menu.set_menu_users("Admin, Staff Member, Client, Super");

        db.addItraxxMenu(menu);
    }

    private void showProgress(){
        if (SHOW_PROGRESS_FLAG != false){
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Fetching Data");
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
        }
    }

    private void hideProgress(){
        if (SHOW_PROGRESS_FLAG != false && progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void getBaseData(){
        String url = Variables.BASE_URL + "API/getBaseData";
        showProgress();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("DataFound:" + response);
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
                    hideProgress();

                } catch (JSONException e) {
                    hideProgress();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(context, "There was an error pulling data", Toast.LENGTH_LONG).show();
                System.out.println("====>" + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

    public void getAuditBaseData(){
        String url = Variables.BASE_URL + "API/getauditBaseData";
        showProgress();
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("DataFound:" + response);
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
                    JSONArray traffic_quality = dataObj.getJSONArray("traffic_quality");
                    JSONArray traffic_speed = dataObj.getJSONArray("traffic_speed");
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
                        submission.set_trafficquantity(submissionObj.getString("traffic_quality"));
                        submission.set_trafficspeed(submissionObj.getString("traffic_speed"));
                        db.addSubmissionAuditing(submission);
                    }
                    hideProgress();

                } catch (JSONException e) {
                    hideProgress();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                Toast.makeText(context, "There was an error pulling data", Toast.LENGTH_LONG).show();
                System.out.println("====>" + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}
