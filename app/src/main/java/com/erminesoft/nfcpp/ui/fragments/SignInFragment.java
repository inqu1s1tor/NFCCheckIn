package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

        signInLoginEt.addTextChangedListener(new TextWatcherSignInLogin());
        signInPasswordEt.addTextChangedListener(new TextWatcherSignInPassword());
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
            error = getActivity().getResources().getString(R.string.message_error_empty_field);
            tilFirstName.setError(error);
            return;
        } else if (name.length() < 5) {
            error = getActivity().getResources().getString(R.string.message_error_size_login);
            tilFirstName.setError(error);
            return;
        } else {
            tilFirstName.setError("");
            tilFirstName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(password)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_field);
            tilPasswordUser.setError(error);
            return;
        } else if (password.length() < 8) {
            error = getActivity().getResources().getString(R.string.message_error_size_password);
            tilPasswordUser.setError(error);
            return;
        } else {
            tilPasswordUser.setError("");
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


    private final class TextWatcherSignInLogin implements TextWatcher{

        @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String name = signInLoginEt.getText().toString();
            String error;
            if (TextUtils.isEmpty(name)) {
                error = getActivity().getResources().getString(R.string.message_error_empty_field);
                tilFirstName.setError(error);
            } else if (name.length() < 5) {
                error = getActivity().getResources().getString(R.string.message_error_size_login);
                tilFirstName.setError(error);
            } else {
                tilFirstName.setError("");
                tilFirstName.setErrorEnabled(false);
            }
        }
    }

    private final class TextWatcherSignInPassword implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String password = signInPasswordEt.getText().toString();
            String error;
            if (TextUtils.isEmpty(password)) {
                error = getActivity().getResources().getString(R.string.message_error_empty_field);
                tilPasswordUser.setError(error);
            } else if (password.length() < 8) {
                error = getActivity().getResources().getString(R.string.message_error_size_password);
                tilPasswordUser.setError(error);
            } else {
                tilPasswordUser.setError("");
                tilPasswordUser.setErrorEnabled(false);
            }
        }
    }

    private void errorLogin(String message){
        if (message.contains(getResources().getString(R.string.error_invalid_login_password))){
            String showMessage = getResources().getString(R.string.error_invalid_login_password_please_register);
            showLongToast(showMessage);
            tilPasswordUser.setError(showMessage);
            return;
        }

        if (message.contains(getResources().getString(R.string.error_unable_host))){
            String showMessage = getResources().getString(R.string.error_login_unable_host);
            showLongToast(showMessage);
            tilPasswordUser.setError(showMessage);
            return;
        }

        showLongToast(message);
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
            errorLogin(error);
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
