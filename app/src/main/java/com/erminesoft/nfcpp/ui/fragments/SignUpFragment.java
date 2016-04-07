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
import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.bridge.NetBridge;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.User;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aleks on 10.03.2016.
 */
public class SignUpFragment extends GenericFragment {

    private final static String IS_TEST_LOGIN = "is_test_login";

    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText signUpLoginEt;
    private EditText signUpPasswordEt;

    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilLoginUser;
    private TextInputLayout tilPasswordUser;

    private Observer observer;
    private boolean isTestLogin = false;


    public static Bundle buildArguments(Boolean isTestLogin) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IS_TEST_LOGIN, isTestLogin);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstNameEt = (EditText) view.findViewById(R.id.firstNameET);
        lastNameEt = (EditText) view.findViewById(R.id.lastNameET);
        signUpLoginEt = (EditText) view.findViewById(R.id.signUploginUserET);
        signUpPasswordEt = (EditText) view.findViewById(R.id.signUpPasswordUserET);

        tilFirstName = (TextInputLayout) view.findViewById(R.id.firstNameWrap);
        tilLastName = (TextInputLayout) view.findViewById(R.id.lastNameWrap);
        tilLoginUser = (TextInputLayout) view.findViewById(R.id.signUploginUserWrap);
        tilPasswordUser = (TextInputLayout) view.findViewById(R.id.signUpPasswordUserWrap);

        Bundle bundle = getArguments();
        if (bundle != null) {
            isTestLogin = (boolean) bundle.getSerializable(IS_TEST_LOGIN);
        }

        if (isTestLogin){
            lastNameEt.setVisibility(View.GONE);
            signUpLoginEt.setVisibility(View.GONE);
            signUpPasswordEt.setVisibility(View.GONE);
        }


        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.SignUpButton).setOnClickListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    private void buttonSignUpPressed() {
        String putFirstName = firstNameEt.getText().toString();
        String putLastName = lastNameEt.getText().toString();
        String putSignUpLoginEt = signUpLoginEt.getText().toString();
        String putSignUpPasswordEt = signUpPasswordEt.getText().toString();

        String error;

        if (TextUtils.isEmpty(putFirstName)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_firstname);
            tilFirstName.setError(error);
            return;
        } else {
            if (putFirstName.length() < 2){
                error = getActivity().getResources().getString(R.string.message_error_size_firstname);
                tilFirstName.setError(error);
                return;
            } else {
                tilFirstName.setErrorEnabled(false);
            }

        }

        if (!isTestLogin) {
            if (TextUtils.isEmpty(putLastName)) {
                error = getActivity().getResources().getString(R.string.message_error_empty_lastname);
                tilLastName.setError(error);
                return;
            } else {
                if (putLastName.length() < 2){
                    error = getActivity().getResources().getString(R.string.message_error_size_firstname);
                    tilLastName.setError(error);
                    return;
                } else {
                    tilLastName.setErrorEnabled(false);
                }
            }

            if (TextUtils.isEmpty(putSignUpLoginEt)) {
                error = getActivity().getResources().getString(R.string.message_error_empty_login);
                tilLoginUser.setError(error);
                return;
            } else {
                if (putSignUpLoginEt.length() < 5){
                    error = getActivity().getResources().getString(R.string.message_error_size_login);
                    tilLoginUser.setError(error);
                    return;
                } else {
                    tilLoginUser.setErrorEnabled(false);
                }
            }

            if (TextUtils.isEmpty(putSignUpPasswordEt)) {
                error = getActivity().getResources().getString(R.string.message_error_empty_password);
                tilPasswordUser.setError(error);
                return;
            } else {
                if (putSignUpPasswordEt.length() < 8){
                    error = getActivity().getResources().getString(R.string.message_error_size_login);
                    tilPasswordUser.setError(error);
                    return;
                } else {
                    tilPasswordUser.setErrorEnabled(false);
                }
            }
        }


        if (isTestLogin) {
            User testUser = new User();
            testUser.setName(putFirstName);
            testUser.setLastName("test-user");
            testUser.setEmail("test-user");
            testUser.setObjectId(getActivity().getString(R.string.objectid_test_user));
            testUser.setUserRoles("testUser,");

            SharedHelper sharedHelper = mActivityBridge.getUApplication().getSharedHelper();
            sharedHelper.setUserName(putFirstName);
            sharedHelper.setUserPassword("testUser");
            mActivityBridge.getUApplication().getDbBridge().setMyUser(testUser);
        } else {
            showProgressDialog();
            NetBridge netBridge = mActivityBridge.getUApplication().getNetBridge();
            netBridge.registryUser(buildUser(putFirstName, putLastName, putSignUpLoginEt, putSignUpPasswordEt), new NetCallBack());
        }
    }

    private BackendlessUser buildUser(String firstName, String lastName, String login, String password) {
        BackendlessUser user = new BackendlessUser();
        user.setPassword(password);
        user.setProperty("name", login);
        user.setProperty("firstName", firstName);
        user.setProperty("lastName", lastName);
        return user;
    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }

    private void goEntry(){
        if (isTestLogin) {
            mActivityBridge.getFragmentLauncher().launchMainFragment();
        } else {
            hideProgressDialog();
            mActivityBridge.getUApplication().getNetBridge().autoLoginUser(new NetCallBack());
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
            goEntry();
        }
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.SignUpButton:
                    buttonSignUpPressed();
            }
        }
    }
}
