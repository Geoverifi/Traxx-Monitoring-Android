package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Material;
import com.geoverifi.geoverifi.model.RunUp;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class RunUpSpinnerAdapter extends BaseAdapter {
    Context context;
    List<RunUp> runUpList;
    LayoutInflater inflater;
    public RunUpSpinnerAdapter(Context ctx, List<RunUp> runUps){
        this.context = ctx;
        this.runUpList = runUps;
        this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return runUpList.size();
    }

    @Override
    public Object getItem(int position) {
        return runUpList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RunUpSpinnerAdapter.ViewHolder spinnerHolder;

        if (convertView == null){
            spinnerHolder = new RunUpSpinnerAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.run_up_spinner_item, parent, false);

            spinnerHolder.txtRunUpID = (TextView) convertView.findViewById(R.id.run_up_id);
            spinnerHolder.txtRunUp = (TextView) convertView.findViewById(R.id.run_up);

            convertView.setTag(spinnerHolder);
        }
        else{
            spinnerHolder = (RunUpSpinnerAdapter.ViewHolder)convertView.getTag();
        }


        RunUp runUp = runUpList.get(position);

        spinnerHolder.txtRunUpID.setText(String.valueOf(runUp.get_id()));
        spinnerHolder.txtRunUp.setText(String.valueOf(runUp.get_run_up()));
        return convertView;
    }

    class ViewHolder{
        TextView txtRunUpID, txtRunUp;
    }
}
