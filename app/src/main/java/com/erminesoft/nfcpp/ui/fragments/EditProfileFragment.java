package com.erminesoft.nfcpp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.SharedHelper;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.ui.dialogs.ConfirmationDialog;
import com.erminesoft.nfcpp.ui.dialogs.GenericDialog;
import com.erminesoft.nfcpp.ui.dialogs.PasswordDialog;
import com.erminesoft.nfcpp.ui.dialogs.UnsavedDataDialog;
import com.erminesoft.nfcpp.ui.launcher.DialogLauncher;
import com.erminesoft.nfcpp.util.SystemUtils;

import java.util.Observable;
import java.util.Observer;

public class EditProfileFragment extends GenericFragment {

    private EditText editFirstName, editLastName;
    private TextInputLayout tilFirstName, tilLastName;
    private TextView textPassword;
    private ImageView imagePassword;

    private Observer observer;
    private SharedHelper sharedHelper;

    private boolean isShowPassword = false;
    private boolean isChangeFields;
    private String password;
    private String errorUnableHost;
    private String errorRegistryUnableHost;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        errorUnableHost = getActivity().getResources().getString(R.string.error_unable_host);
        errorRegistryUnableHost = getActivity().getResources().getString(R.string.error_registry_unable_host);

        editFirstName = (EditText) view.findViewById(R.id.fragment_edit_profile_first_name_edit_field);
        editLastName = (EditText) view.findViewById(R.id.fragment_edit_profile_last_name_edit_field);

        tilFirstName = (TextInputLayout) view.findViewById(R.id.fragment_edit_profile_first_name_wrap);
        tilLastName = (TextInputLayout) view.findViewById(R.id.fragment_edit_profile_last_name_wrap);

        textPassword = (TextView) view.findViewById(R.id.fragment_edit_profile_password_view_field);
        imagePassword = (ImageView) view.findViewById(R.id.fragment_edit_profile_password_image_selector);

        View.OnClickListener clicker = new Clicker();
        view.findViewById(R.id.fragment_edit_profile_save_button).setOnClickListener(clicker);
        textPassword.setOnClickListener(clicker);
        imagePassword.setOnClickListener(clicker);

        editFirstName.addTextChangedListener(new TextWatcherFirstName());
        editLastName.addTextChangedListener(new TextWatcherLastName());

        sharedHelper = mActivityBridge.getUApplication().getSharedHelper();

        loadUserData();
    }

    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    private void loadUserData() {
        User my = mActivityBridge.getUApplication().getDbBridge().getMe();
        password = sharedHelper.getUserPassword();

        editFirstName.setText(my.getFirstName());
        editLastName.setText(my.getLastName());
        setPasswordField();
        isChangeFields = false;
    }

    private void buttonSavePressed() {
        String putFirstName = editFirstName.getText().toString();
        String putLastName = editLastName.getText().toString();
        String error;

        if (TextUtils.isEmpty(putFirstName)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_field);
            tilFirstName.setError(error);
            return;
        } else if (putFirstName.length() < 2) {
            error = getActivity().getResources().getString(R.string.message_error_size_firstname);
            tilFirstName.setError(error);
            return;
        } else {
            tilFirstName.setError("");
            tilFirstName.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(putLastName)) {
            error = getActivity().getResources().getString(R.string.message_error_empty_field);
            tilLastName.setError(error);
            return;
        } else if (putLastName.length() < 2) {
            error = getActivity().getResources().getString(R.string.message_error_size_firstname);
            tilLastName.setError(error);
            return;
        } else {
            tilLastName.setError("");
            tilLastName.setErrorEnabled(false);
        }

        showProgressDialog();
        BackendlessUser backendlessUser = Backendless.UserService.CurrentUser();
        backendlessUser.setProperty("lastName", putFirstName);
        backendlessUser.setProperty("firstName", putLastName);
        backendlessUser.setPassword(password);

        if (!SystemUtils.isNetworkConnected(getActivity())){
            error = getActivity().getResources().getString(R.string.message_error_check_internet);
            tilLastName.setError(error);
            return;
        }

        mActivityBridge.getUApplication().getNetBridge().updateMyUser(backendlessUser, new NetCallBack());
    }

    private void userSaved() {
        hideProgressDialog();
        sharedHelper.setUserPassword(password);
        getActivity().onBackPressed();
    }

    private void setPasswordField(){
        isChangeFields = true;
        if (isShowPassword){
            textPassword.setText(password);
        } else {
            textPassword.setText(getResources().getString(R.string.fragment_my_profile_password_field));
        }
    }

    private void changeDisplayPassword(){
        isShowPassword = !isShowPassword;
        setPasswordField();
    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }


    private final class TextWatcherFirstName implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            isChangeFields = true;
            String putFirstName = editFirstName.getText().toString();
            String error;
            if (TextUtils.isEmpty(putFirstName)) {
                error = getActivity().getResources().getString(R.string.message_error_empty_field);
                tilFirstName.setError(error);
            } else if (putFirstName.length() < 2) {
                error = getActivity().getResources().getString(R.string.message_error_size_firstname);
                tilFirstName.setError(error);
            } else {
                tilFirstName.setError("");
                tilFirstName.setErrorEnabled(false);
            }
        }
    }

    private final class TextWatcherLastName implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            isChangeFields = true;
            String putLastName = editLastName.getText().toString();
            String error;
            if (TextUtils.isEmpty(putLastName)) {
                error = getActivity().getResources().getString(R.string.message_error_empty_field);
                tilLastName.setError(error);
            } else if (putLastName.length() < 2) {
                error = getActivity().getResources().getString(R.string.message_error_size_firstname);
                tilLastName.setError(error);
            } else {
                tilLastName.setError("");
                tilLastName.setErrorEnabled(false);
            }
        }
    }





    private void errorSave(String message) {
        Log.d("errorSave", "!message = " + message);

        if (message.contains(errorUnableHost)) {
            String showMessage = errorRegistryUnableHost;
            showLongToast(showMessage);
            return;
        }

        showLongToast(message);
    }

    private void logout() {
        mActivityBridge.getUApplication().getDbBridge().clearAllData();
        mActivityBridge.getUApplication().getSharedHelper().sharedHelperClear();
        mActivityBridge.getUApplication().getNetBridge().userLogout();
        mActivityBridge.getFragmentLauncher().launchWelcomeFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                buttonBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void buttonBackPressed(){
        if (isChangeFields) {
            Bundle bundle = UnsavedDataDialog.buildArguments(getActivity().getResources().getString(R.string.lost_data_dialog));
            DialogLauncher.launchUnsavedDataDialog(getActivity(), new DialogListener(), bundle);
        } else {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mActivityBridge.getUApplication().getDbBridge().removeObserver(observer);
        observer = null;
    }

    private final class Clicker implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fragment_edit_profile_save_button:
                    buttonSavePressed();
                    break;

                case R.id.fragment_edit_profile_password_view_field:
                    Bundle bundle = PasswordDialog.buildArguments(password);
                    DialogLauncher.launchPasswordDialog(getActivity(), new DialogListener(), bundle);
                    break;

                case R.id.fragment_edit_profile_password_image_selector:
                    changeDisplayPassword();
                    break;
            }
        }
    }

    private final class DialogListener implements GenericDialog.DialogListener{

        @Override
        public void onOkPressed() {
            logout();
        }

        @Override
        public void onProceedPressed() {
            getActivity().onBackPressed();
        }

        @Override
        public void onPasswordChange(String newPassword) {
            password = newPassword;
            setPasswordField();
        }
    }


    private final class NetCallBack extends SimpleMainCallBack{

        @Override
        public void onError(String error) {
            errorSave(error);
        }

        @Override
        public void isUserAuthenticated(boolean isAuth) {
            if(!isAuth){
                hideProgressDialog();
                Bundle bundle = ConfirmationDialog.buildArguments(getActivity().getResources().getString(R.string.loggen_another_device));
                DialogLauncher.launchConfirmationDialog(getActivity(), new DialogListener(), bundle);
                return;
            }
        }
    }


    private final class DbObserver implements Observer{

        @Override
        public void update(Observable observable, Object data) {
            userSaved();
        }
    }


}
