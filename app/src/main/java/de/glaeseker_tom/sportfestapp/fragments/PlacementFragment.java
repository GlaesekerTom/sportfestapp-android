package de.glaeseker_tom.sportfestapp.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.glaeseker_tom.sportfestapp.R;
import de.glaeseker_tom.sportfestapp.fragments.PlacementFragmentContent;

public class PlacementFragment extends Fragment {

    private SectionsPageAdapter adapter;
    private String serverUrl;
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
        if (getArguments() != null) {
            serverUrl = getArguments().getString("serverUrl");
        }
        adapter = new SectionsPageAdapter(getChildFragmentManager());
        ViewPager viewPager = v.findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = v.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        String[] sports = {"fußball", "volleyball", "badminton", "basketball", "hockey"};
        Bundle bundle = new Bundle();
        bundle.putString("serverUrl", serverUrl);
        PlacementFragmentContent frag;
        for (int i = 0; i < sports.length; i++) {
            frag = new PlacementFragmentContent(sports[i]);
            frag.setArguments(bundle);
            adapter.addFragment(frag);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }


    private class SectionsPageAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            notifyDataSetChanged();
        }

        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }
}