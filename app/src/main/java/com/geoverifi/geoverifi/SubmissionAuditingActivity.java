package com.geoverifi.geoverifi;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.geoverifi.geoverifi.db.DatabaseHandler;
import com.geoverifi.geoverifi.fragments.SubmissionDetailsAuditingFragment;
import com.geoverifi.geoverifi.fragments.SubmissionLocationAuditingFragment;
import com.geoverifi.geoverifi.fragments.SubmissionPhotosAuditingFragment;
import com.geoverifi.geoverifi.model.SubmissionAuditing;

public class SubmissionAuditingActivity extends AppCompatActivity implements SubmissionDetailsAuditingFragment.OnFragmentInteractionListener, SubmissionLocationAuditingFragment.OnFragmentInteractionListener, SubmissionPhotosAuditingFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    String _id;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    SubmissionAuditing submissionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        this._id = this.getIntent().getStringExtra("submission_id");
        DatabaseHandler db = new DatabaseHandler(this);
        submissionData = db.getSubmissionauditing(Integer.parseInt(this._id));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Auditing");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Bundle bundle = new Bundle();
            switch (position){
                case 0:
                    bundle.putParcelable("data", submissionData);

                    SubmissionDetailsAuditingFragment fragment = new SubmissionDetailsAuditingFragment();
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    bundle.putDouble("latitude", Double.parseDouble(submissionData.get_latitude()));
                    bundle.putDouble("longitude", Double.parseDouble(submissionData.get_longitude()));

                    SubmissionLocationAuditingFragment submissionLocationFragment = new SubmissionLocationAuditingFragment();
                    submissionLocationFragment.setArguments(bundle);
                    return submissionLocationFragment;
                case 2:
                    bundle.putString("photo_1", submissionData.get_photo_1());
                    bundle.putString("photo_2", submissionData.get_photo_2());
                    bundle.putInt("submission_id", submissionData.get_id());

                    SubmissionPhotosAuditingFragment submissionPhotosFragment = new SubmissionPhotosAuditingFragment();
                    submissionPhotosFragment.setArguments(bundle);

                    return submissionPhotosFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DETAILS";
                case 1:
                    return "MAP";
                case 2:
                    return "PHOTOS";
            }
            return null;
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter{
        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new SubmissionDetailsAuditingFragment();
                case 1:
                    return new SubmissionLocationAuditingFragment();
                case 2:
                    return new SubmissionPhotosAuditingFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
