package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.MediaOwner;

import java.util.ArrayList;

/**
 * Created by chriz on 10/5/2017.
 */

public class MediaOwnerSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<MediaOwner> mediaOwnerArrayList;
    LayoutInflater inflater;

    public MediaOwnerSpinnerAdapter(Context context, ArrayList<MediaOwner> mediaOwnerArrayList) {
        this.context = context;
        this.mediaOwnerArrayList = mediaOwnerArrayList;
    }

    @Override
    public int getCount() {
        return mediaOwnerArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mediaOwnerArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.media_owner_spinner_item, null);

        MediaOwner mediaOwner = mediaOwnerArrayList.get(i);

        TextView txtMediaOwner = (TextView) view.findViewById(R.id.media_owner);
        TextView txtMediaOwnerID = (TextView) view.findViewById(R.id.media_owner_id);

        txtMediaOwner.setText(mediaOwner.get_media_owner());
        txtMediaOwnerID.setText(String.valueOf(mediaOwner.get_id()));

        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
