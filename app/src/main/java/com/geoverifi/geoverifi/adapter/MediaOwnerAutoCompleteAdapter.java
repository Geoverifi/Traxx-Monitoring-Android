package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.MediaOwner;

import java.util.ArrayList;

/**
 * Created by chriz on 7/12/2017.
 */

public class MediaOwnerAutoCompleteAdapter extends ArrayAdapter<MediaOwner> {
    private ArrayList<MediaOwner> mediaOwners;
    private ArrayList<MediaOwner> allmediaOwners;
    private ArrayList<MediaOwner> suggestions;
    private int viewResourceId;

    public MediaOwnerAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<MediaOwner> mediaOwners) {
        super(context, resource, mediaOwners);
        this.mediaOwners = mediaOwners;
        this.allmediaOwners = (ArrayList<MediaOwner>) mediaOwners.clone();
        this.suggestions = new ArrayList<MediaOwner>();
        this.viewResourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }

        MediaOwner mediaOwner = mediaOwners.get(position);

        if (mediaOwner != null){
            TextView mediaOwnerLabel = (TextView) v.findViewById(R.id.media_owner_label);
            TextView mediaOwnerID = (TextView) v.findViewById(R.id.media_owner_id);

            mediaOwnerLabel.setText(mediaOwner.get_media_owner());
            mediaOwnerID.setText(String.valueOf(mediaOwner.get_id()));
        }
        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return ownerFilter;
    }

    Filter ownerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null){
                suggestions.clear();
                for (MediaOwner owner : allmediaOwners){
                    if (owner.get_media_owner().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(owner);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
            return new FilterResults();
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<MediaOwner> mediaOwnerList = (ArrayList<MediaOwner>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (MediaOwner owner : mediaOwnerList){
                    add(owner);
                }
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    };
}
