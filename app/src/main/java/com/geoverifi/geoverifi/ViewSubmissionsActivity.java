package com.geoverifi.geoverifi;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geoverifi.geoverifi.adapter.SubmissionAdapter;
import com.geoverifi.geoverifi.app.AppController;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.fragments.FilterFragment;
import com.geoverifi.geoverifi.model.Submission;
import com.geoverifi.geoverifi.model.SubmissionPhoto;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.sharedpreference.Manager;
import com.geoverifi.geoverifi.util.ClickListener;
import com.geoverifi.geoverifi.util.DividerItemDecoration;
import com.geoverifi.geoverifi.util.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewSubmissionsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    final Context context = this;
    User user;

    FrameLayout loadingLayout, emptyLayout;
    SwipeRefreshLayout dataLayout;
    private RecyclerView recyclerView;

    private SubmissionAdapter adapter;
    List<Submission> submissionList = new ArrayList<Submission>();
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_submissions);

        db = new DatabaseHandler(this);

        loadingLayout = (FrameLayout) findViewById(R.id.loading_layout);
        emptyLayout = (FrameLayout) findViewById(R.id.no_submissions);
        dataLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.submission_recycler_view);

        user = Manager.getInstance(context).getUser();

        loadingLayout.setVisibility(View.GONE);
        if(user.get_user_type().equals("Admin") || user.get_user_type().equals("Super")){
            submissionList = db.allSubmissions();
        }else{
            submissionList = db.allSubmissions(user.get_id());
        }


        if(submissionList.size() > 0){
            dataLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }else{
            dataLayout.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        adapter = new SubmissionAdapter(this, submissionList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Submission submission = submissionList.get(position);
                Intent intent = new Intent(ViewSubmissionsActivity.this, SubmissionActivity.class);
                intent.putExtra("submission_id", String.valueOf(submission.get_id()));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        dataLayout.setOnRefreshListener(this);
        dataLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryLight);

        getSupportActionBar().setTitle("Submissions");
        getSupportActionBar().setSubtitle(submissionList.size() + " submissions");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_submission_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(ViewSubmissionsActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.filter:
                FilterFragment fragment = FilterFragment.newInstance();
                fragment.show(getFragmentManager(), "dialog");
                break;
            case R.id.view_in_map:
                startActivity(new Intent(ViewSubmissionsActivity.this, ViewInMapActivity.class));
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_in_down);
                break;
            case R.id.refresh:
                fetchData();
                break;
        }
        return true;
    }

    void fetchData(){
        loadingLayout.setVisibility(View.VISIBLE);
        dataLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);
        getSupportActionBar().setSubtitle("Fetching...");
        String user_id = null;
        if (!user.get_user_type().equals("Admin")){
            user_id = String.valueOf(user.get_id());
        }

        String url = Variables.BASE_URL + "API/getSubmissions/" + user_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadingLayout.setVisibility(View.GONE);
                    if (dataLayout.isRefreshing()){
                        dataLayout.setRefreshing(false);
                    }
                    if (response.getBoolean("status") == true){
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        dataLayout.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);

                        JSONArray dataArray = response.getJSONArray("data");
                        JSONArray photosArray = response.getJSONArray("photos");
                        if(dataArray.length() != 0){
                            db.clearSubmissions();
                            submissionList.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject submissionObj = dataArray.getJSONObject(i);
                                Submission submission = new Submission();

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

                                submission.set_parentid(submissionObj.getInt("parentid"));
                                submission.set_side(submissionObj.getString("side"));
                                db.addSubmission(submission);
                                submissionList.add(submission);
                            }

                            for (int i = 0; i < photosArray.length(); i++) {
                                JSONObject submissionPhotoObj = photosArray.getJSONObject(i);
                                SubmissionPhoto photo = new SubmissionPhoto();

                                photo.setId(submissionPhotoObj.getInt("id"));
                                photo.setName(submissionPhotoObj.getString("name"));
                                photo.setSubmission_id(submissionPhotoObj.getInt("submission_id"));
                                photo.setThumb(submissionPhotoObj.getString("image_thumb"));
                                photo.setPath(submissionPhotoObj.getString("image_url"));
                                photo.setCreated_at(submissionPhotoObj.getString("uploaded_at"));

                                db.addPhoto(photo);
                            }
                            adapter.notifyDataSetChanged();
                            getSupportActionBar().setSubtitle(submissionList.size() + " submissions");
                        }else{
                            dataLayout.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            getSupportActionBar().setSubtitle("No submissions");
                        }

                    }else{
                        getSupportActionBar().setSubtitle("No submissions");
                        db.clearSubmissions();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public void onRefresh() {
        fetchData();
    }
}
