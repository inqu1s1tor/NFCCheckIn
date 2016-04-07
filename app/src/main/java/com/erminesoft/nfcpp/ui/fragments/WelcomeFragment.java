package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.User;

/**
 * Created by Aleks on 10.03.2016.
 */
public class WelcomeFragment extends GenericFragment {

    private Button loginUserTv;
    private TextView registerUserTv;
    private TextView testLoginTv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginUserTv = (Button) view.findViewById(R.id.text_view_login);
        registerUserTv = (TextView) view.findViewById(R.id.text_view_registry);
        testLoginTv = (TextView) view.findViewById(R.id.text_view_test_login);


        View.OnClickListener listener = new Clicker();
        loginUserTv.setOnClickListener(listener);
        registerUserTv.setOnClickListener(listener);
        testLoginTv.setOnClickListener(listener);

        checkData();
    }

    private void checkData() {
        User user = mActivityBridge.getUApplication().getDbBridge().getMe();
        if (user == null) {
            loginUserTv.setVisibility(View.VISIBLE);
            registerUserTv.setVisibility(View.VISIBLE);
            testLoginTv.setVisibility(View.VISIBLE);
        } else {
            if (user.getUserRoles().contains("Admin")) {  // TODO
                mActivityBridge.getFragmentLauncher().launchAdminFragment();
            } else {
                mActivityBridge.getFragmentLauncher().launchMainFragment();
            }
        }
    }

    @Override
    protected boolean isBackButtonVisible() {
        return false;
    }


    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_view_login:
                    mActivityBridge.getFragmentLauncher().launchSignInfragment();
                    break;
                case R.id.text_view_registry:
                    mActivityBridge.getFragmentLauncher().launchSignUpFragment(false);
                    break;
                case R.id.text_view_test_login:
                    mActivityBridge.getFragmentLauncher().launchSignUpFragment(true);
                    break;
            }


        }
    }
}
