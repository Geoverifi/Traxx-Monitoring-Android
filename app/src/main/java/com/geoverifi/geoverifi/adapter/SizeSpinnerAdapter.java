package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Size;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class SizeSpinnerAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<Size> sizeList;

    public SizeSpinnerAdapter(Context ctx, List<Size> sizes){
        this.context = ctx;
        this.sizeList = sizes;
        this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return sizeList.size();
    }

    @Override
    public Object getItem(int position) {
        return sizeList.get(position);
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
            convertView = inflater.inflate(R.layout.size_spinner_item, parent, false);

            spinnerHolder.txtSizeName = (TextView) convertView.findViewById(R.id.size_name);
            spinnerHolder.txtSizeID = (TextView) convertView.findViewById(R.id.size_id);

            convertView.setTag(spinnerHolder);
        }
        else{
            spinnerHolder = (ViewHolder)convertView.getTag();
        }


        Size size = sizeList.get(position);

        spinnerHolder.txtSizeID.setText(String.valueOf(size.get_id()));
        spinnerHolder.txtSizeName.setText(String.valueOf(size.get_size()));

        return convertView;
    }

    class ViewHolder{
        TextView txtSizeName, txtSizeID;
    }
}
