package com.erminesoft.nfcpp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erminesoft.nfcpp.R;

public class TutorialFragment extends GenericFragment{

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private int pageCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutorial, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = (ViewPager) view.findViewById(R.id.fragment_tutorial_viewpager);

        initAdapter();
    }


    private void initAdapter(){
        pageCount = 5;
        pagerAdapter = new TutorialFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }



    private class TutorialFragmentPagerAdapter extends FragmentPagerAdapter {

        public TutorialFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageTutorialFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return pageCount;
        }

    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }
}
