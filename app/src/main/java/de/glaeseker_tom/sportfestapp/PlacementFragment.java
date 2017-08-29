package de.glaeseker_tom.sportfestapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlacementFragment extends Fragment {

  //  private OnFragmentInteractionListener mListener;
    private SectionsPageAdapter adapter;
    private int[] imageResId = {
            R.drawable.ic_tab_soccer,
            R.drawable.ic_tab_volleyball,
            R.drawable.ic_tab_badminton,
            R.drawable.ic_tab_basketball,
            R.drawable.ic_tab_hockey
    };

    public PlacementFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_placement, container, false);
        adapter = new SectionsPageAdapter(getChildFragmentManager());
        ViewPager viewPager = v.findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        for(int i = 0; i < tabLayout.getTabCount(); i++){
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }
        return v;
    }

    private void setupViewPager(ViewPager viewPager){
        adapter.addFragment(new PlacementFragmentContent("fußball"),"");
        adapter.addFragment(new PlacementFragmentContent("volleyball"),"");
        adapter.addFragment(new PlacementFragmentContent("badminton"),"");
        adapter.addFragment(new PlacementFragmentContent("basketball"),"");
        adapter.addFragment(new PlacementFragmentContent("hockey"),"");
        //adapter.addFragment(new PlacementFragmentContent(),"");
        /*   adapter.addFragment(new PlacementFragmentContent(),"Gesamtliste");
        adapter.addFragment(new PlacementFragmentContent(),"Volleyball");
        adapter.addFragment(new PlacementFragmentContent(),"Badminton");
        adapter.addFragment(new PlacementFragmentContent(),"Hockey");
        adapter.addFragment(new PlacementFragmentContent(),"Fußball");
        */
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

/*
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }*/
}
