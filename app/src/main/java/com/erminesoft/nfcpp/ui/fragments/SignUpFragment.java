package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aleks on 10.03.2016.
 */
public class SignUpFragment extends GenericFragment {

    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText signUpLoginEt;
    private EditText signUpPasswordEt;

    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilLoginUser;
    private TextInputLayout tilPasswordUser;


    private Observer observer;

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

        tilFirstName = (TextInputLayout) view.findViewById(R.id.firstNameWrap);
        tilLastName = (TextInputLayout) view.findViewById(R.id.lastNameWrap);
        tilLoginUser = (TextInputLayout) view.findViewById(R.id.signUploginUserWrap);
        tilPasswordUser = (TextInputLayout) view.findViewById(R.id.signUpPasswordUserWrap);


        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.SignUpButton).setOnClickListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    @Override
    public void onStop() {
        super.onStop();
        mActivityBridge.getUApplication().getDbBridge().removeObserver(observer);
        observer = null;
    }

    private void buttonSignUpPressed() {
        String putFirstName = firstNameEt.getText().toString();
        String putLastName = lastNameEt.getText().toString();
        String putSignUpLoginEt = signUpLoginEt.getText().toString();
        String putSignUpPasswordEt = signUpPasswordEt.getText().toString();

        String error;

        if(TextUtils.isEmpty(putFirstName)) {
             error  = getActivity().getResources().getString(R.string.message_error_empty_firstname);
            tilFirstName.setError(error);
            return;
        } else {
            tilFirstName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(putLastName)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_lastname);
            tilLastName.setError(error);
            return;
        } else {
            tilLastName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(putSignUpLoginEt)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_login);
            tilLoginUser.setError(error);
            return;
        } else {
            tilLoginUser.setErrorEnabled(false);
        }

        if(TextUtils.isEmpty(putSignUpPasswordEt)) {
            error =  getActivity().getResources().getString(R.string.message_error_empty_password);
            tilPasswordUser.setError(error);
            return;
        } else {
            tilPasswordUser.setErrorEnabled(false);
        }

        showProgressDialog();
        NetBridge netBridge = mActivityBridge.getUApplication().getNetBridge();
        netBridge.registryUser(buildUser(putFirstName, putLastName, putSignUpLoginEt, putSignUpPasswordEt),
                new NetCallBack());
    }

    private BackendlessUser buildUser(String firstName, String lastName, String login, String password) {
        BackendlessUser user = new BackendlessUser();
        user.setPassword(password);
        user.setProperty("name", login);
        user.setProperty("firstName", firstName);
        user.setProperty("lastName", lastName);
        return user;
    }


    private final class NetCallBack extends SimpleMainCallBack {
        @Override
        public void onSuccess() {
            hideProgressDialog();
            mActivityBridge.getFragmentLauncher().launchMainFragment();
        }

        @Override
        public void onError(String error) {
            hideProgressDialog();
            showShortToast(error);
        }
    }

    private final class DbObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            Log.e("FL", "userAdded");
            hideProgressDialog();
            mActivityBridge.getUApplication().getNetBridge().autoLoginUser(new NetCallBack());
        }
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.SignUpButton:
                    buttonSignUpPressed();
            }
        }
    }
}
