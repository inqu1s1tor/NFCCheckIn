package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.erminesoft.nfcpp.R;

/**
 * Created by Aleks on 10.03.2016.
 */
public class SignInFragment extends GenericFragment {

    private EditText signInLoginEt;
    private EditText signInPasswordEt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInLoginEt = (EditText)view.findViewById(R.id.signInloginUserET);
        signInPasswordEt = (EditText)view.findViewById(R.id.signUpPasswordUserET);

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.signInButton).setOnClickListener(listener);

    }

    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.signInButton:
                    mActivityBridge.getFragmentLauncher().launchMainFragment();
            }
        }
    }
}
