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
public class SignUpFragment extends GenericFragment {

    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText signUpLoginEt;
    private EditText signUpPasswordEt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEt = (EditText)view.findViewById(R.id.firstNameET);
        lastNameEt = (EditText)view.findViewById(R.id.lastNameET);
        signUpLoginEt = (EditText)view.findViewById(R.id.signUploginUserET);
        signUpPasswordEt = (EditText)view.findViewById(R.id.signUpPasswordUserET);
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {

            }
        }
    }
}
