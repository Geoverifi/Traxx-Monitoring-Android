package com.geoverifi.geoverifi.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.model.Submitter;

import java.util.List;

/**
 * Created by chriz on 10/5/2017.
 */

public class SubmitterSpinnerAdapter extends BaseAdapter {
    Context context;
    List<Submitter> submitterList;
    LayoutInflater inflater;

    public SubmitterSpinnerAdapter(Context context, List<Submitter> submitterList) {
        this.context = context;
        this.submitterList = submitterList;
    }

    @Override
    public int getCount() {
        return submitterList.size();
    }

    @Override
    public Object getItem(int i) {
        return submitterList.get(i);
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
            view = inflater.inflate(R.layout.angle_spinner_item, null);

        Submitter submitter = submitterList.get(i);

        TextView txtAngleID = (TextView) view.findViewById(R.id.angle_id);
        TextView txtAngle = (TextView) view.findViewById(R.id.angle);

        txtAngle.setText(submitter.getName());
        txtAngleID.setText(String.valueOf(submitter.getId()));

        return view;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
