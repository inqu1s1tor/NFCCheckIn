package com.erminesoft.nfcpp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.User;

public class MyProfileFragment extends GenericFragment {

    private TextView firstName, lastName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstName = (TextView) view.findViewById(R.id.fragment_my_profile_first_name_field);
        lastName = (TextView) view.findViewById(R.id.fragment_my_profile_last_name_field);

        loadUserData();

        View.OnClickListener clicker = new Clicker();
        view.findViewById(R.id.fragment_my_profile_edit).setOnClickListener(clicker);


    }

    private void loadUserData() {
        User my = mActivityBridge.getUApplication().getDbBridge().getMe();
        Log.d("loadUserData", "!getFirstName = " + my.getFirstName() );

        firstName.setText(my.getFirstName());
        lastName.setText(my.getLastName());
    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }

    private final class Clicker implements View.OnClickListener{

        @Override
        public void onClick(View v) {
           switch (v.getId()){
               case R.id.fragment_my_profile_edit:
                   mActivityBridge.getFragmentLauncher().launchEditProfileFragment();
                   break;
           }
        }
    }
}
