package com.geoverifi.geoverifi.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.geoverifi.geoverifi.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by chriz on 11/1/2017.
 */

public class ConfirmLocationFragment extends DialogFragment {
    private Double latitude, longitude;
    Geocoder geocoder;
    public static ConfirmLocationFragment newInstance() {

        Bundle args = new Bundle();

        ConfirmLocationFragment fragment = new ConfirmLocationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View titleView = View.inflate(getActivity(), R.layout.location_review_title, null);
        final View view = View.inflate(getActivity(), R.layout.fragment_confirm_location, null);

        latitude = getArguments().getDouble("latitude");
        longitude = getArguments().getDouble("longitude");

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        TextView txtTitleText = (TextView) titleView.findViewById(R.id.title_text);
        final TextView txtLocationText = (TextView) view.findViewById(R.id.location_text);
        final EditText etxLatitude = (EditText) view.findViewById(R.id.input_latitude);
        final EditText etxLongitude = (EditText) view.findViewById(R.id.input_longitude);
        Button btnGetDetails = (Button) view.findViewById(R.id.get_location_details);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);

        etxLatitude.setText(String.valueOf(latitude));
        etxLongitude.setText(String.valueOf(longitude));

        btnGetDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses.size() > 0){
                        String town = addresses.get(0).getLocality();
                        String county = addresses.get(0).getAdminArea();
                        txtLocationText.setText(town + ", " + county);
                        txtLocationText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                } catch (IOException e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        txtTitleText.setText("Confirm the location");
        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setCustomTitle(titleView)
                .setView(view)
                .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onComplete(true, Double.parseDouble(etxLatitude.getText().toString()), Double.parseDouble(etxLongitude.getText().toString()));
                    }
                })
                .create();
    }

    public static interface OnCompleteListener{
        public abstract void onComplete(boolean confirmed, Double latitude, Double longitude);
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
}
