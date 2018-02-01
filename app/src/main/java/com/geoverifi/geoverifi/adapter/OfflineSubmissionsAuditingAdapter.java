package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.model.MediaOwner;
import com.geoverifi.geoverifi.model.SubmissionAuditing;

import java.util.List;

/**
 * Created by chriz on 7/23/2017.
 */

public class OfflineSubmissionsAuditingAdapter extends RecyclerView.Adapter<OfflineSubmissionsAuditingAdapter.MyViewHolder> {
    Context context;
    List<SubmissionAuditing> offlineList;
    public OfflineSubmissionsAuditingAdapter(Context context, List<SubmissionAuditing> offlineList){
        this.context = context;
        this.offlineList = offlineList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offlineauditing_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            SubmissionAuditing submission = this.offlineList.get(position);

            DatabaseHandler db = new DatabaseHandler(context);
            MediaOwner mediaOwner = new MediaOwner();
            if (!submission.get_media_owner().equals("")) {
                mediaOwner = db.getMediaOwner(Integer.parseInt(submission.get_media_owner()));
            }

            holder.txtSubmissionDate.setText(submission.get_submission_date());
            holder.txtBrandMediaOwner.setText(submission.get_brand() + " " + mediaOwner.get_media_owner());
            holder.txtTown.setText(submission.get_town());
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return this.offlineList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtBrandMediaOwner, txtTown,  txtCounty, txtSubmissionDate, txtStructure;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtBrandMediaOwner = (TextView) itemView.findViewById(R.id.brand_media_owner);
            txtTown = (TextView) itemView.findViewById(R.id.town);
            txtSubmissionDate = (TextView) itemView.findViewById(R.id.submission_date);
        }
    }
}
