package de.glaeseker_tom.sportfestapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlacementFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public PlacementFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_placement, container, false);
        //sectionsPageAdapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        ViewPager viewPager = v.findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new PlacementFragmentContent(),"Gesamtliste");
        adapter.addFragment(new PlacementFragmentContent(),"Volleyball");
        viewPager.setAdapter(adapter);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
