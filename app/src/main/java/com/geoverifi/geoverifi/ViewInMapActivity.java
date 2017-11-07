package com.geoverifi.geoverifi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.model.MapItem;
import com.geoverifi.geoverifi.model.Submission;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.sharedpreference.Manager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class ViewInMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    private ClusterManager<MapItem> mClusterManager;
    DatabaseHandler db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_in_map);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("DATA ON MAP");

        db = new DatabaseHandler(this);
        user = Manager.getInstance(this).getUser();

        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpCluster();
    }

    private void setUpCluster(){
        mClusterManager = new ClusterManager<MapItem>(this, mMap);

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        List<Submission> submissions = new ArrayList<Submission>();
        if (user.get_user_type().equals("Admin")){
           submissions = db.allSubmissions();
        }else {
            submissions = db.allSubmissions(user.get_id());
        }

        for (int i = 0; i < submissions.size(); i++) {
            double lat = Double.parseDouble(submissions.get(i).get_latitude());
            double lng = Double.parseDouble(submissions.get(i).get_longitude());

            String title = submissions.get(i).get_brand() + " By " + submissions.get(i).get_media_owner();
            String offset = submissions.get(i).get_structure();

            MapItem offsetItem = new MapItem(lat, lng, title, offset);
            mClusterManager.addItem(offsetItem);
        }
    }
}
