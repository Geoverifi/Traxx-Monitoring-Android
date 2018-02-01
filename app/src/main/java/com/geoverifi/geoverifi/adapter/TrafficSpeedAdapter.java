package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.TrafficSpeed;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class TrafficSpeedAdapter extends BaseAdapter {
    Context context;
    List<TrafficSpeed> speedList;
    LayoutInflater inflater;

    public TrafficSpeedAdapter(Context ctx, List<TrafficSpeed> speeds){
        this.context = ctx;
        this.speedList = speeds;
        this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return speedList.size();
    }

    @Override
    public Object getItem(int position) {
        return speedList.get(position);
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
            convertView = inflater.inflate(R.layout.traffic_speed_spinner, null);

        TrafficSpeed trafficSpeed = speedList.get(position);

        TextView txtAngleID = (TextView) convertView.findViewById(R.id.trafficspeed_id);
        TextView txtAngle = (TextView) convertView.findViewById(R.id.trafficspeed_name);

        txtAngleID.setText(String.valueOf(trafficSpeed.get_id()));
        txtAngle.setText(String.valueOf(trafficSpeed.get_traffic_speed()));

        return convertView;
    }
}

