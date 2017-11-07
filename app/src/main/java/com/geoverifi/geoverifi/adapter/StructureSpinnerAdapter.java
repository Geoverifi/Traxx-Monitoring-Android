package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Structure;

import java.util.List;

/**
 * Created by chriz on 7/10/2017.
 */

public class StructureSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Structure> structureList;
    LayoutInflater inflater;

    public StructureSpinnerAdapter(Context ctx, List<Structure> structures){
        this.context = ctx;
        this.structureList = structures;
    }
    @Override
    public int getCount() {
        return structureList.size();
    }

    @Override
    public Object getItem(int position) {
        return structureList.get(position);
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
            convertView = inflater.inflate(R.layout.structure_spinner_item, null);

        Structure structure = structureList.get(position);

        TextView txtStructureID = (TextView) convertView.findViewById(R.id.structure_id);
        TextView txtStructure = (TextView) convertView.findViewById(R.id.structure_type);

        txtStructureID.setText(String.valueOf(structure.get_type_id()));
        txtStructure.setText(structure.get_type_name());

        return convertView;
    }
}
