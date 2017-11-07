package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Angle;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class AngleSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Angle> angleList;
    LayoutInflater inflater;

    public  AngleSpinnerAdapter(Context ctx, List<Angle> angles){
        this.context = ctx;
        this.angleList = angles;
    }
    @Override
    public int getCount() {
        return this.angleList.size();
    }

    @Override
    public Object getItem(int position) {
        return angleList.get(position);
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
            convertView = inflater.inflate(R.layout.angle_spinner_item, null);

        Angle angle = angleList.get(position);

        TextView txtAngleID = (TextView) convertView.findViewById(R.id.angle_id);
        TextView txtAngle = (TextView) convertView.findViewById(R.id.angle);

        txtAngleID.setText(String.valueOf(angle.get_id()));
        txtAngle.setText(String.valueOf(angle.get_angle()));

        return convertView;
    }
}
