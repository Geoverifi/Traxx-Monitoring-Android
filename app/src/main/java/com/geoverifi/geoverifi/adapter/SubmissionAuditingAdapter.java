package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.helper.DateHelper;
import com.geoverifi.geoverifi.model.SubmissionAuditing;
import com.geoverifi.geoverifi.model.User;
import com.geoverifi.geoverifi.sharedpreference.Manager;

import java.util.List;

/**
 * Created by chriz on 7/17/2017.
 */

public class SubmissionAuditingAdapter extends RecyclerView.Adapter<SubmissionAuditingAdapter.MyViewHolder> implements Filterable{
    List<SubmissionAuditing> submissionList;
    List<SubmissionAuditing> filteredList;
    Context context;
    User user;
    private CustomFilter mFilter;

    public SubmissionAuditingAdapter(Context c, List<SubmissionAuditing> submissions){
        this.submissionList = submissions;
        this.filteredList = submissions;
        this.context = c;
        user = Manager.getInstance(c).getUser();
        mFilter = new CustomFilter(SubmissionAuditingAdapter.this, submissions);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.submission_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubmissionAuditing submission = this.submissionList.get(position);

        String submission_date = DateHelper.FormatDateString(submission.get_submission_date(), "yyyy-MM-dd", "dd.MM.Y");
        String submission_time = DateHelper.FormatDateString(submission.get_created_at(), "yyyy-MM-dd H:m:s", "H:mm");
//        String submission_date = submission.get_submission_date();
        holder.txtSubmissionDate.setText(submission_date);
        holder.txtBrandMediaOwner.setText(submission.get_brand() + " " + submission.get_media_owner());
        holder.txtStructureSize.setText(submission.get_structure() + " " + submission.get_size());
        holder.txtSubmissionTime.setText(submission_time);
        if (user.get_user_type().equals("Admin")) {
            String username = submission.get_user_firstname() + " " + submission.get_user_lastname();
            if (user.get_id() == submission.get_user_id()){
                username = "You";
            }
            holder.txtUser.setText("Submitted By: " + username);
        }else{
            holder.txtUser.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return submissionList.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtBrandMediaOwner, txtStructureSize, txtSubmissionDate, txtUser, txtSubmissionTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtBrandMediaOwner = (TextView) itemView.findViewById(R.id.brand_media_owner);
            txtStructureSize = (TextView) itemView.findViewById(R.id.structure_size);
            txtSubmissionDate = (TextView) itemView.findViewById(R.id.submission_date);
            txtUser = (TextView) itemView.findViewById(R.id.user);
            txtSubmissionTime = (TextView) itemView.findViewById(R.id.submission_time);
        }
    }

    public class CustomFilter extends Filter {

        private SubmissionAuditingAdapter mAdapter;
        List<SubmissionAuditing> subs;
        private CustomFilter(SubmissionAuditingAdapter adapter, List<SubmissionAuditing> subs){
            super();
            mAdapter = adapter;
            this.subs = subs;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            DatabaseHandler db = new DatabaseHandler(context);
            List<SubmissionAuditing> submissions = db.allSubmissionsauditing();
            if (constraint.length() == 0) {
                filteredList.addAll(submissions);
            }else{
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (SubmissionAuditing submission : submissions){
                    if (submission.get_brand().toLowerCase().contains(filterPattern) || submission.get_media_owner().toLowerCase().contains(filterPattern)){
                        filteredList.add(submission);
                    }
                }
            }

            System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
