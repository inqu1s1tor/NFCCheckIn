package com.erminesoft.nfcpp.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;

public class PageTutorialFragment extends Fragment {

    static final String TUTORIAL_PAGE_NUMBER = "tutorial_page_number";

    int pageNumber;

    static PageTutorialFragment newInstance(int page) {
        PageTutorialFragment pageFragment = new PageTutorialFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(TUTORIAL_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        pageNumber = getArguments().getInt(TUTORIAL_PAGE_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_page_tutorial, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.item_page_tutorial_image);
        TextView textTitle = (TextView) view.findViewById(R.id.item_page_tutorial_title);

        textTitle.setText("Page " + pageNumber);

        return view;
    }
}
