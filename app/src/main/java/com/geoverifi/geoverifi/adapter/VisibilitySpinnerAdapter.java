package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Visibility;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class VisibilitySpinnerAdapter extends BaseAdapter {
    Context context;
    List<Visibility> visibilityList;
    LayoutInflater inflater;

    public VisibilitySpinnerAdapter(Context ctx, List<Visibility> visibilities){
        this.context = ctx;
        this.visibilityList = visibilities;
        this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return visibilityList.size();
    }

    @Override
    public Object getItem(int position) {
        return visibilityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.visibility_spinner_item, null);

        Visibility visibility = visibilityList.get(position);

        TextView txtVisibilityID = (TextView) convertView.findViewById(R.id.visibility_id);
        TextView txtVisibilityType = (TextView) convertView.findViewById(R.id.visibility_type);

        txtVisibilityID.setText(String.valueOf(visibility.get_id()));
        txtVisibilityType.setText(visibility.get_visibility());

        return convertView;
    }
}
