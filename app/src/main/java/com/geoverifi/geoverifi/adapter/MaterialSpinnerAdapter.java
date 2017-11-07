package com.geoverifi.geoverifi.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Material;
import com.geoverifi.geoverifi.model.Size;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class MaterialSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Material> materialList;
    LayoutInflater inflater;

    public MaterialSpinnerAdapter(Context ctx, List<Material> materialList){
        this.context = ctx;
        this.materialList = materialList;
        this.inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return materialList.size();
    }

    @Override
    public Object getItem(int position) {
        return materialList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MaterialSpinnerAdapter.ViewHolder spinnerHolder;

        if (convertView == null){
            spinnerHolder = new MaterialSpinnerAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.material_spinner_item, parent, false);

            spinnerHolder.txtMaterialID = (TextView) convertView.findViewById(R.id.material_id);
            spinnerHolder.txtMaterialName = (TextView) convertView.findViewById(R.id.material_name);

            convertView.setTag(spinnerHolder);
        }
        else{
            spinnerHolder = (MaterialSpinnerAdapter.ViewHolder)convertView.getTag();
        }


        Material material = materialList.get(position);

        spinnerHolder.txtMaterialID.setText(String.valueOf(material.get_id()));
        spinnerHolder.txtMaterialName.setText(String.valueOf(material.get_material()));

        return convertView;
    }

    class ViewHolder{
        TextView txtMaterialID, txtMaterialName;
    }
}
