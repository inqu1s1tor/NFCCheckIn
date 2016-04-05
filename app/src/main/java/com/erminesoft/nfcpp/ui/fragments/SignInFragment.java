package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.User;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aleks on 10.03.2016.
 */
public class SignInFragment extends GenericFragment {

    private EditText signInLoginEt;
    private EditText signInPasswordEt;
    private TextInputLayout tilFirstName;
    private TextInputLayout tilPasswordUser;
    private Observer observer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInLoginEt = (EditText) view.findViewById(R.id.signInloginUserET);
        signInPasswordEt = (EditText) view.findViewById(R.id.signInPasswordUserET);

        tilFirstName = (TextInputLayout) view.findViewById(R.id.firstNameWrap);
        tilPasswordUser = (TextInputLayout) view.findViewById(R.id.signInPasswordUserWrap);

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.signInButton).setOnClickListener(listener);


    }

    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;

    }

    private void buttonSignInPressed() {
        String name = signInLoginEt.getText().toString();
        String password = signInPasswordEt.getText().toString();

        String error;

        if (TextUtils.isEmpty(name)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_login);
            tilFirstName.setError(error);
            return;
        } else {
            tilFirstName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(password)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_password);
            tilPasswordUser.setError(error);
            return;
        } else {
            tilPasswordUser.setErrorEnabled(false);
        }

        showProgressDialog();
        mActivityBridge.getUApplication().getNetBridge().loginUser(name, password, new NetCallBack());
    }

    private void checkMode(User user) {
        if (user.getUserRoles().contains("Admins")) { // TODO
            mActivityBridge.getFragmentLauncher().launchAdminFragment();
        } else {
            mActivityBridge.getFragmentLauncher().launchMainFragment();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mActivityBridge.getUApplication().getDbBridge().removeObserver(observer);
        observer = null;
    }

    private final class NetCallBack extends SimpleMainCallBack {
        @Override
        public void onError(String error) {
            hideProgressDialog();
            showShortToast(error);
            tilPasswordUser.setError(error);
        }
    }

    private final class DbObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            hideProgressDialog();
            User user = mActivityBridge.getUApplication().getDbBridge().getMe();
            checkMode(user);
        }
    }

    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signInButton:
                    buttonSignInPressed();
            }
        }
    }
}
