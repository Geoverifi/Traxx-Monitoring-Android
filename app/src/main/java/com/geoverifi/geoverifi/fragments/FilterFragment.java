package com.geoverifi.geoverifi.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.adapter.MediaOwnerSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.SizeSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.StructureSpinnerAdapter;
import com.geoverifi.geoverifi.adapter.SubmitterSpinnerAdapter;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.model.MediaOwner;
import com.geoverifi.geoverifi.model.Size;
import com.geoverifi.geoverifi.model.Structure;
import com.geoverifi.geoverifi.model.Submitter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chriz on 10/5/2017.
 */

public class FilterFragment extends DialogFragment {
    Spinner user_spinner, media_owner_spinner, structure_spinner, media_size_spinner;

    List<Structure> structureList;
    List<Size> sizeList;
    ArrayList<MediaOwner> mediaOwnerList;
    List<Submitter> submitterList;

    public static FilterFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = View.inflate(getActivity(), R.layout.fragment_filter_layout, null);

        DatabaseHandler db = new DatabaseHandler(getActivity());

        user_spinner = (Spinner) view.findViewById(R.id.submitted_by);
        media_owner_spinner = (Spinner) view.findViewById(R.id.media_owner);
        structure_spinner = (Spinner) view.findViewById(R.id.media_structure);
        media_size_spinner = (Spinner) view.findViewById(R.id.media_size);

        structureList = db.allStructures();
        sizeList = db.allSizes();
        mediaOwnerList = db.allMediaOwners();
        submitterList = db.allSubmitters();

        StructureSpinnerAdapter structureSpinnerAdapter = new StructureSpinnerAdapter(getActivity(), structureList);
        SizeSpinnerAdapter adapter = new SizeSpinnerAdapter(getActivity(), sizeList);
        MediaOwnerSpinnerAdapter mediaOwnerSpinnerAdapter = new MediaOwnerSpinnerAdapter(getActivity(), mediaOwnerList);
        SubmitterSpinnerAdapter submitterSpinnerAdapter = new SubmitterSpinnerAdapter(getActivity(), submitterList);

        structure_spinner.setAdapter(structureSpinnerAdapter);
        media_size_spinner.setAdapter(adapter);
        media_owner_spinner.setAdapter(mediaOwnerSpinnerAdapter);
        user_spinner.setAdapter(submitterSpinnerAdapter);


        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle("Filter")
                .setView(view)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }
}
