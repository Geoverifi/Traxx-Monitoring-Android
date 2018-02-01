package com.geoverifi.geoverifi.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.geoverifi.geoverifi.R;
import com.geoverifi.geoverifi.config.Variables;
import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.model.SubmissionAuditing;
import com.geoverifi.geoverifi.model.SubmissionPhotoAuditing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubmissionPhotosAuditingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubmissionPhotosAuditingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmissionPhotosAuditingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "photo_1";
    private static final String ARG_PARAM2 = "photo_2";
    private static final String ARG_SUBMISSION_ID  ="submission_id";

    List<SubmissionPhotoAuditing> submissionPhotoList = new ArrayList<>();
    DatabaseHandler db;

    // TODO: Rename and change types of parameters
    private String mPhoto1;
    private String mPhoto2;
    private int mSubmissionId;
    private SubmissionAuditing submission;

    private OnFragmentInteractionListener mListener;

    public SubmissionPhotosAuditingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubmissionPhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubmissionPhotosAuditingFragment newInstance(String param1, String param2, int submission_id) {
        SubmissionPhotosAuditingFragment fragment = new SubmissionPhotosAuditingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_SUBMISSION_ID, submission_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhoto1 = getArguments().getString(ARG_PARAM1);
            mPhoto2 = getArguments().getString(ARG_PARAM2);
            mSubmissionId = getArguments().getInt(ARG_SUBMISSION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_submission_photos, container, false);
        db = new DatabaseHandler(getContext());

        submissionPhotoList = db.getPhotoByAuditID(mSubmissionId);

        submission = db.getSubmissionauditing(mSubmissionId);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView galleryView = (RecyclerView) rootView.findViewById(R.id.rv_gallery);

        galleryView.setHasFixedSize(true);
        galleryView.setLayoutManager(layoutManager);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(getActivity(), submissionPhotoList);
        galleryView.setAdapter(adapter);

        galleryView.addOnItemTouchListener(new ImageGalleryAdapter.RecyclerTouchListener(getContext(), galleryView, new ImageGalleryAdapter.ClickListener(){

            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) submissionPhotoList);
                bundle.putInt("position", position);
                bundle.putString("user", submission.get_user_firstname() + " " + submission.get_user_lastname());
//
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

//        ImageView imgPhoto1 = (ImageView) rootView.findViewById(R.id.Photo1);
//        ImageView imgPhoto2 = (ImageView) rootView.findViewById(R.id.photo2);

//        Picasso.Builder builder = new Picasso.Builder(getContext());
//        builder.listener(new Picasso.Listener()
//        {
//            @Override
//            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
//            {
//                exception.printStackTrace();
//            }
//        });
//        builder.build()
//                .load(Variables.SERVER_URL + mPhoto1)
//                .placeholder(R.drawable.ic_refresh)
//                .error(R.drawable.ic_geo_verifi_splash_logo)
//                .into(imgPhoto1);
//        builder.build()
//                .load(Variables.SERVER_URL + mPhoto2)
//                .error(R.drawable.ic_geo_verifi_splash_logo)
//                .into(imgPhoto2);
//
//        imgPhoto1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Large_ImagePopup(getContext(), Variables.SERVER_URL + mPhoto1);
//            }
//        });
//
//        imgPhoto2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Large_ImagePopup(getContext(), Variables.SERVER_URL + mPhoto2);
//            }
//        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class ImageGalleryAdapter extends RecyclerView.Adapter<SubmissionPhotosAuditingFragment.ImageGalleryAdapter.MyViewHolder>{
        Activity activity;
        List<SubmissionPhotoAuditing> photos;
        public ImageGalleryAdapter(Activity a, List<SubmissionPhotoAuditing> photos){
            activity = a;
            this.photos = photos;
        }

        @Override
        public SubmissionPhotosAuditingFragment.ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View photoView = inflater.inflate(R.layout.view_submission_photo_layout, parent, false);
            SubmissionPhotosAuditingFragment.ImageGalleryAdapter.MyViewHolder viewHolder = new SubmissionPhotosAuditingFragment.ImageGalleryAdapter.MyViewHolder(photoView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(SubmissionPhotosAuditingFragment.ImageGalleryAdapter.MyViewHolder holder, int position) {
            SubmissionPhotoAuditing photo = photos.get(position);
            ImageView imageView = holder.mPhotoImageView;
//            Picasso.Builder builder = new Picasso.Builder(activity);
//            builder.listener(new Picasso.Listener() {
//                @Override
//                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
//                {
//                    exception.printStackTrace();
//                }
//            });
//
//            builder.build()
//                .load(Variables.SERVER_URL + photo.getThumb())
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.drawable.ic_geo_verifi_splash_logo)
//                .into(imageView);
            String url = "";
            if (photo.getThumb().isEmpty() || photo.getThumb().equals("null") || photo.getThumb().equals("")){
                url = Variables.SERVER_URL + photo.getPath();
            }else{
                url = Variables.SERVER_URL + photo.getThumb();
            }

            Glide.with(activity).load(url)
                    .error(R.drawable.ic_search_error)
                    .placeholder(R.drawable.ic_camera_synchronation)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }

        @Override
        public int getItemCount() {
            return photos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView mPhotoImageView;
            public MyViewHolder(View itemView) {
                super(itemView);
                mPhotoImageView = (ImageView) itemView.findViewById(R.id.thumbnail);
            }
        }

        public interface ClickListener {
            void onClick(View view, int position);

            void onLongClick(View view, int position);
        }

        public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
            private GestureDetector gestureDetector;
            private ImageGalleryAdapter.ClickListener clickListener;

            public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ImageGalleryAdapter.ClickListener clickListener) {
                this.clickListener = clickListener;
                gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                        }
                    }
                });
            }
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, rv.getChildPosition(child));
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        }
    }
}
