package com.geoverifi.geoverifi;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.geoverifi.geoverifi.adapter.OfflineSubmissionsAdapater;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.app.VolleyMultipartRequest;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.helper.InternetChecker;
import com.geoverifi.geoverifi.model.Submission;
import com.geoverifi.geoverifi.module.AppHelper;
import com.geoverifi.geoverifi.server.client.SubmissionClient;
import com.geoverifi.geoverifi.util.DividerItemDecoration;
import com.geoverifi.geoverifi.util.FileUtils;
import com.geoverifi.geoverifi.util.Picture;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfflineDataActivity extends AppCompatActivity {
    Context context = this;
    RecyclerView offlineRecyclerView;
    List<Submission> submissionList = new ArrayList<Submission>();
    OfflineSubmissionsAdapater offlineSubmissionsAdapater;
    int upload = 0;
    DatabaseHandler db;
    LinearLayout emptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_data);

        db = new DatabaseHandler(context);

        offlineRecyclerView = (RecyclerView) findViewById(R.id.all_offline_submissions);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        offlineRecyclerView.setLayoutManager(mLayoutManager);
        offlineRecyclerView.setItemAnimator(new DefaultItemAnimator());
        offlineRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        emptyLayout = (LinearLayout) findViewById(R.id.empty);

        submissionList = db.allOfflineSubmissions();
        updateUI();
        offlineSubmissionsAdapater = new OfflineSubmissionsAdapater(context, submissionList);
        offlineRecyclerView.setAdapter(offlineSubmissionsAdapater);

        getSupportActionBar().setTitle(getResources().getString(R.string.waiting));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.offline_submission_menu, menu);
        return true;
    }

    private void updateUI(){
        if (submissionList.size() > 0){
            offlineRecyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            return;
        }
        offlineRecyclerView.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int notification_id = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
//            case R.id.action_upload:
//                if (InternetChecker.getInstance(context).isNetworkAvailable() == true) {
//                    upload = 1;
//                    mBuilder.setContentTitle("Traxx Offline Data")
//                            .setContentText("Uploading Data...")
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setProgress(0, 0, true);
//                    notificationManager.notify(notification_id, mBuilder.build());
//                    List<Submission> offlinesubmissionList = new ArrayList<Submission>();
//                    offlinesubmissionList = db.allOfflineSubmissions();
//                    for (int i = 0; i < submissionList.size(); i++) {
//                        Submission submission = submissionList.get(i);
//                        submitOfflineData(submission, i);
//                    }
//                    mBuilder.setContentText("Upload complete")
//                            // Removes the progress bar
//                            .setProgress(0,0,false);
//                    notificationManager.notify(notification_id, mBuilder.build());
//                }else{
//                    Snackbar.make(findViewById(R.id.offline_coordinator_layout), R.string.not_connected,
//                            Snackbar.LENGTH_SHORT)
//                            .show();
//                }
//                break;
//            case R.id.action_stop:
//                notificationManager.cancel(notification_id);
//                break;
        }
        return true;
    }

    private void submitOfflineData(final Submission s, final int index){
        String url = Variables.BASE_URL + "API/addSubmission";
        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("submission_date", createPartFromString(s.get_submission_date()));
        params.put("site_reference_number", createPartFromString(s.get_site_reference_number()));
        params.put("brand", createPartFromString(s.get_brand()));
        params.put("media_owner", createPartFromString(s.get_media_owner()));
        params.put("structure_id", createPartFromString(s.get_structure()));
        params.put("town", createPartFromString(s.get_town()));
        params.put("media_size", createPartFromString(s.get_size()));
        params.put("media_size_other_height", createPartFromString(s.get_media_size_other_height()));
        params.put("media_size_other_width", createPartFromString(s.get_media_size_other_width()));
        params.put("material_type_id", createPartFromString(s.get_material()));
        params.put("run_up_id", createPartFromString(s.get_run_up()));
        params.put("illumination_type_id", createPartFromString(s.get_illumination()));
        params.put("visibility", createPartFromString(s.get_visibility()));
        params.put("angle", createPartFromString(s.get_angle()));
        params.put("other_comments", createPartFromString(s.get_other_comments()));
        params.put("latitude", createPartFromString(s.get_latitude()));
        params.put("longitude", createPartFromString(s.get_longitude()));
        params.put("user_id", createPartFromString(String.valueOf(s.get_user_id())));


        final Bitmap bmp_photo_1 = decodeFile(s.get_photo_1());
        final Bitmap bmp_photo_2 = decodeFile(s.get_photo_2());

        Uri tempPhoto1Uri = Picture.getImageUri(context, bmp_photo_1);
        Uri tempPhoto2Uri = Picture.getImageUri(context, bmp_photo_2);

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

        call.enqueue(new Callback<com.geoverifi.geoverifi.server.model.Response>() {
            @Override
            public void onResponse(Call<com.geoverifi.geoverifi.server.model.Response> call, retrofit2.Response<com.geoverifi.geoverifi.server.model.Response> response) {
                if (response.body().isStatus()) {
                    db.deleteOfflineSubmission(s.get_id());
                    submissionList.remove(index);
                    offlineRecyclerView.removeViewAt(index);
                    offlineSubmissionsAdapater.notifyItemRemoved(index);
                    offlineSubmissionsAdapater.notifyItemRangeChanged(index, submissionList.size());
                    offlineSubmissionsAdapater.notifyDataSetChanged();
                }else{
                    Toast.makeText(OfflineDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.geoverifi.geoverifi.server.model.Response> call, Throwable t) {
                Toast.makeText(OfflineDataActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                db.deleteOfflineSubmission(s.get_id());
//                submissionList.remove(index);
//                offlineRecyclerView.removeViewAt(index);
//                offlineSubmissionsAdapater.notifyItemRemoved(index);
//                offlineSubmissionsAdapater.notifyItemRangeChanged(index, submissionList.size());
//                offlineSubmissionsAdapater.notifyDataSetChanged();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "There was an error pushing some offline data", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("submission_date", s.get_submission_date());
//                params.put("brand", s.get_brand());
//                params.put("media_owner", s.get_media_owner());
//                params.put("structure_id", s.get_structure());
//                params.put("town", s.get_town());
//                params.put("media_size", s.get_size());
//                params.put("media_size_other_height", s.get_media_size_other_height());
//                params.put("media_size_other_width", s.get_media_size_other_width());
//                params.put("material_type_id", s.get_material());
//                params.put("run_up_id", s.get_run_up());
//                params.put("illumination_type_id", s.get_illumination());
//                params.put("visibility", s.get_visibility());
//                params.put("angle", s.get_angle());
//                params.put("other_comments", s.get_other_comments());
//                params.put("latitude", s.get_latitude());
//                params.put("longitude", s.get_longitude());
//                params.put("user_id", String.valueOf(s.get_user_id()));
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() throws AuthFailureError {
//                Map<String, DataPart> params = new HashMap<>();
//                String photo1_path = s.get_photo_1();
//                String photo2_path = s.get_photo_2();
//
//                Drawable drawable_photo_1 = Drawable.createFromPath(photo1_path);
//                Drawable drawable_photo_2 = Drawable.createFromPath(photo2_path);
//
//                String mimeType_photo_1 = Picture.getMimeType(photo1_path);
//                String mimeType_photo_2 = Picture.getMimeType(photo2_path);
//
//                params.put("photo_1", new DataPart(photo1_path, AppHelper.getFileDataFromDrawable(getBaseContext(), drawable_photo_1), mimeType_photo_1));
//                params.put("photo_2", new DataPart(photo2_path, AppHelper.getFileDataFromDrawable(getBaseContext(), drawable_photo_2), mimeType_photo_2));
//                return params;
//            }
//        };
//
//        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().addToRequestQueue(volleyMultipartRequest);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (submissionList.size() > 0 && upload == 1){
            Snackbar.make(findViewById(R.id.offline_coordinator_layout), R.string.cannot_go_back,
                    Snackbar.LENGTH_SHORT)
                    .show();
        }else{
            super.onBackPressed();
        }
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
