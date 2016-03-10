package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.SharedHelper;

/**
 * Created by Aleks on 10.03.2016.
 */
public class WelcomeFragment extends GenericFragment {

    private TextView loginUserTv;
    private TextView registerUserTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginUserTv = (TextView) view.findViewById(R.id.text_view_login);
        registerUserTv = (TextView) view.findViewById(R.id.text_view_registry);

        View.OnClickListener listener = new Clicker();
        loginUserTv.setOnClickListener(listener);
        registerUserTv.setOnClickListener(listener);
    }

    private void checkData() {
        SharedHelper sharedHelper = mActivityBridge.getUApplication().getSharedHelper();
        if (TextUtils.isEmpty(sharedHelper.getUserName())) {
            loginUserTv.setVisibility(View.VISIBLE);
            registerUserTv.setVisibility(View.VISIBLE);
        }else {
            showProgressDialog();

        }
    }


    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_view_login:
                    mActivityBridge.getFragmentLauncher().launchSignInfragment();
                    break;
                case R.id.text_view_registry:
                    mActivityBridge.getFragmentLauncher().launchSignUpFragment();
                    break;
            }


        }
    }
}
