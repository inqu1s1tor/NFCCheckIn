package com.erminesoft.nfcpp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.ui.dialogs.ConfirmationDialog;
import com.erminesoft.nfcpp.ui.dialogs.GenericDialog;
import com.erminesoft.nfcpp.ui.launcher.DialogLauncher;

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

        View.OnClickListener clicker = new Clicker();
        view.findViewById(R.id.fragment_my_profile_edit).setOnClickListener(clicker);

        loadUserData();
        checkAuthUser();
    }

    private void loadUserData() {
        User my = mActivityBridge.getUApplication().getDbBridge().getMe();
        firstName.setText(my.getFirstName());
        lastName.setText(my.getLastName());
    }

    private void checkAuthUser(){
        mActivityBridge.getUApplication().getNetBridge().isUserAuthenticated(new NetCallBack());
    }

    private void logout() {
        mActivityBridge.getUApplication().getDbBridge().clearAllData();
        mActivityBridge.getUApplication().getSharedHelper().sharedHelperClear();
        mActivityBridge.getUApplication().getNetBridge().userLogout();
        mActivityBridge.getFragmentLauncher().launchWelcomeFragment();
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

    private final class NetCallBack extends SimpleMainCallBack{
        @Override
        public void isUserAuthenticated(boolean isAuth) {
            if(!isAuth){
                hideProgressDialog();
                Bundle bundle = ConfirmationDialog.buildArguments(getActivity().getResources().getString(R.string.loggen_another_device));
                DialogLauncher.launchConfirmationDialog(getActivity(), new DialogListener(), bundle);
            }
        }
    }

    private final class DialogListener implements GenericDialog.DialogListener{

        @Override
        public void onOkPressed() {

        }

        @Override
        public void onProceedPressed() {
            logout();
        }

        @Override
        public void onPasswordChange(String newPassword) {

        }
    }
}
