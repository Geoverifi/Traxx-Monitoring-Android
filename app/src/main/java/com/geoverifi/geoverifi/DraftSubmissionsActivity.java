package com.geoverifi.geoverifi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.model.MediaOwner;
import com.geoverifi.geoverifi.model.Size;
import com.geoverifi.geoverifi.model.Structure;
import com.geoverifi.geoverifi.model.Submission;

import java.util.List;

import static android.view.View.GONE;

public class DraftSubmissionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    DatabaseHandler db;
    List<Submission> submissionList;
    LinearLayout emptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_submissions);

        listView = (ListView) findViewById(R.id.draft_submissions_list);
        emptyLayout = (LinearLayout) findViewById(R.id.empty);

        getSupportActionBar().setTitle("Your Drafts");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHandler(this);

        submissionList = db.allDraftSubmissions();

        updateUI();
        DraftListAdapter adapter = new DraftListAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    public void updateUI(){
        if (submissionList.size() > 0){
            listView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(GONE);
            return;
        }

        emptyLayout.setVisibility(View.VISIBLE);
        listView.setVisibility(GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Submission submission = submissionList.get(i);

        Intent intent = new Intent(this, MonitoringDataSubmission.class);
        intent.putExtra("submission_id", submission.get_id());
        startActivity(intent);
        finish();
    }

    public class DraftListAdapter extends BaseAdapter{
        Activity activity;
        LayoutInflater inflater;
        public DraftListAdapter(Activity activity){
            this.activity = activity;
        }
        @Override
        public int getCount() {
            return submissionList.size();
        }

        @Override
        public Object getItem(int i) {
            return submissionList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null)
                view = inflater.inflate(R.layout.draft_submission_row_item, null);

            Submission submission = submissionList.get(i);

            TextView txtBrand = (TextView) view.findViewById(R.id.brand_media_owner);
            TextView txtStructureSize = (TextView) view.findViewById(R.id.structure_size);
            TextView txtSubmissionDate = (TextView) view.findViewById(R.id.submission_date);

            MediaOwner owner = new MediaOwner();
            Structure structure = new Structure();
            Size size = new Size();
            if (!submission.get_media_owner().isEmpty()) {
                owner = db.getMediaOwner(Integer.parseInt(submission.get_media_owner()));
            }

            if (!submission.get_structure().isEmpty()) {
                structure = db.getStructure(Integer.parseInt(submission.get_structure()));
            }

            if (!submission.get_size().isEmpty()) {
                size = db.getSize(Integer.parseInt(submission.get_size()));
            }

            txtBrand.setText(submission.get_brand() + " by " + owner.get_media_owner());
            txtStructureSize.setText(structure.get_type_name() + size.get_size());
            txtSubmissionDate.setText(submission.get_submission_date());
            return view;
        }
    }
}
