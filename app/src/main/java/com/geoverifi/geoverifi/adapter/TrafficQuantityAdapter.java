package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.TrafficQuantity;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class TrafficQuantityAdapter extends BaseAdapter {
    Context context;
    List<TrafficQuantity> quantityList;
    LayoutInflater inflater;

    public TrafficQuantityAdapter(Context ctx, List<TrafficQuantity> quantities){
        this.context = ctx;
        this.quantityList = quantities;
        this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return quantityList.size();
    }

    @Override
    public Object getItem(int position) {
        return quantityList.get(position);
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
            convertView = inflater.inflate(R.layout.traffic_qualities_spinner, null);

        TrafficQuantity trafficQuantity = quantityList.get(position);

        TextView txtAngleID = (TextView) convertView.findViewById(R.id.trafficquality_id);
        TextView txtAngle = (TextView) convertView.findViewById(R.id.trafficquality_name);

        txtAngleID.setText(String.valueOf(trafficQuantity.get_id()));
        txtAngle.setText(String.valueOf(trafficQuantity.get_traffic_quantity()));

        return convertView;
    }
}

