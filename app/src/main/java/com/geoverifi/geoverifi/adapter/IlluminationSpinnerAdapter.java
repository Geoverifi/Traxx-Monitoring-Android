package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Illumination;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class IlluminationSpinnerAdapter extends BaseAdapter{
    Context context;
    List<Illumination> illuminationList;
    LayoutInflater inflater;

    public IlluminationSpinnerAdapter(Context ctx, List<Illumination> illuminations){
        this.context = ctx;
        this.illuminationList = illuminations;
        this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return illuminationList.size();
    }

    @Override
    public Object getItem(int position) {
        return illuminationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder spinnerHolder;

        if (convertView == null){
            spinnerHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.illumination_spinner_item, parent, false);

            spinnerHolder.txtIlluminationID = (TextView) convertView.findViewById(R.id.illumination_id);
            spinnerHolder.txtIlluminationType = (TextView) convertView.findViewById(R.id.illumination_type);

            convertView.setTag(spinnerHolder);
        }
        else{
            spinnerHolder = (ViewHolder)convertView.getTag();
        }

        Illumination illumination = illuminationList.get(position);

        spinnerHolder.txtIlluminationID.setText(String.valueOf(illumination.get_id()));
        spinnerHolder.txtIlluminationType.setText(illumination.get_type());

        return convertView;
    }

    class ViewHolder{
        TextView txtIlluminationID, txtIlluminationType;
    }
}
