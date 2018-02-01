package com.geoverifi.geoverifi.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geoverifi.geoverifi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by chriz on 10/16/2017.
 */

public class LocationPreviewAuditingFragment extends DialogFragment {
    Geocoder geocoder;
    List<Address> addresses;
    String town;

    private Double latitude, longitude;
    public static LocationPreviewAuditingFragment newInstance(){
        Bundle args = new Bundle();

        LocationPreviewAuditingFragment fragment = new LocationPreviewAuditingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = View.inflate(getActivity(), R.layout.fragment_location_review, null);
        View titleView = View.inflate(getActivity(), R.layout.location_review_title, null);

        ListView listView = (ListView) view.findViewById(R.id.location_details_list);

        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        latitude = getArguments().getDouble("latitude");
        longitude = getArguments().getDouble("longitude");

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0){
                LocationDetailsAdapter adapter = new LocationDetailsAdapter(getLocationDetails(addresses));
                listView.setAdapter(adapter);
            }
        } catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setCustomTitle(titleView)
                .setView(view)
                .setPositiveButton("YES, PROCEED", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onComplete(town);
                    }
                })
                .setNegativeButton("REFRESH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getActivity(), getActivity().getClass()));
                        getActivity().finish();
                    }
                })
                .create();
    }

    public static interface OnCompleteListener{
        public abstract void onComplete(String town);
    }

    private OnCompleteListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    public List<Loc> getLocationDetails(List<Address> addressList){
        List<Loc> locationList = new ArrayList<>();
        Loc location = new Loc();

        location.setTitle("County");
        location.setDetail(addressList.get(0).getAdminArea());
        locationList.add(location);

        town = addressList.get(0).getLocality();
        location = new Loc();
        location.setTitle("Town");
        location.setDetail(addressList.get(0).getLocality());
        locationList.add(location);

        String addressline = addressList.get(0).getAddressLine(0);
        if (!addressline.isEmpty()){
            location = new Loc();
            location.setTitle("Address");
            location.setDetail(addressline);
            locationList.add(location);
        }

        return locationList;
    }

    class Loc{
        private String title, detail;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    class LocationDetailsAdapter extends BaseAdapter{
        List<Loc> details;
        LayoutInflater inflater;
        LocationDetailsAdapter(List<Loc> details){
            this.details = details;
        }

        @Override
        public int getCount() {
            return details.size();
        }

        @Override
        public Object getItem(int i) {
            return details.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (inflater == null)
                inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null)
                view = inflater.inflate(R.layout.data_item_row, null);

            TextView txtData = (TextView) view.findViewById(R.id.data_item);
            TextView txtTitle = (TextView) view.findViewById(R.id.title_item);

//            txtData.setTextColor(getActivity().getResources().getColor(android.R.color.white));
//            txtTitle.setTextColor(getActivity().getResources().getColor(android.R.color.white));

            Loc loc = details.get(i);
            txtData.setText(loc.getDetail());
            txtTitle.setText(loc.getTitle());

            return view;
        }
    }
}
