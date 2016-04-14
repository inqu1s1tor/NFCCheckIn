package com.erminesoft.nfcpp.ui.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;


public class PasswordDialog extends GenericDialog {

    private DialogListener dialogListener;
    private final Bundle bundle;
    private EditText editOldPassword, editNewPassword, editConfirmPassword;
    private TextInputLayout tilOldPassword, tilNewPassword, tilConfirmPassword;
    private String oldPassword;

    private String messageEmptyField;
    private String messageSizeField;
    private String messagePasswordNotMatch;
    private String messageOldPasswordIncorrect;

    public static Bundle buildArguments(String oldPassword) {
        Bundle bundle = new Bundle();
        bundle.putString(OLD_PASSWORD, oldPassword);
        return bundle;
    }

    public PasswordDialog(Context context, DialogListener clicker, Bundle bundle) {
        super(context);
        this.dialogListener = clicker;
        this.bundle = bundle;

        messageEmptyField = context.getResources().getString(R.string.message_error_empty_field);
        messageSizeField = context.getResources().getString(R.string.message_error_size_password);
        messagePasswordNotMatch = context.getResources().getString(R.string.message_error_password_not_match);
        messageOldPasswordIncorrect = context.getResources().getString(R.string.message_error_old_password_incorrect);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_password);

        editOldPassword = (EditText) findViewById(R.id.dialog_password_old_password_edit_field);
        editNewPassword = (EditText) findViewById(R.id.dialog_password_new_password_edit_field);
        editConfirmPassword = (EditText) findViewById(R.id.dialog_password_confirm_password_edit_field);

        tilOldPassword = (TextInputLayout) findViewById(R.id.dialog_password_old_password_wrap);
        tilNewPassword = (TextInputLayout) findViewById(R.id.dialog_password_new_password_wrap);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.dialog_password_confirm_password_wrap);

        editOldPassword.addTextChangedListener(new TextWatcherOldPassword());
        editNewPassword.addTextChangedListener(new TextWatcherNewPassword());
        editConfirmPassword.addTextChangedListener(new TextWatcherConfirmPassword());

        oldPassword = bundle.getString(OLD_PASSWORD);

        View.OnClickListener listener = new Clicker();
        findViewById(R.id.dialog_confirmation_button_ok).setOnClickListener(listener);
    }

    private void buttonOkPressed() {
        String putOldPassword = editOldPassword.getText().toString();
        String putNewPassword = editNewPassword.getText().toString();
        String putConfirmPassword = editConfirmPassword.getText().toString();
        String error;


        if (TextUtils.isEmpty(putOldPassword)) {
            error = messageEmptyField;
            tilOldPassword.setError(error);
            return;
        } else if (putOldPassword.length() < 8) {
            error = messageSizeField;
            tilOldPassword.setError(error);
            return;
        } else {
            tilOldPassword.setError("");
            tilOldPassword.setErrorEnabled(false);
        }

        if (!putOldPassword.equals(oldPassword)) {
            error = messageOldPasswordIncorrect;
            tilOldPassword.setError(error);
            return;
        }

        if (TextUtils.isEmpty(putNewPassword)) {
            error = messageEmptyField;
            tilNewPassword.setError(error);
            return;
        } else if (putNewPassword.length() < 8) {
            error = messageSizeField;
            tilNewPassword.setError(error);
            return;
        } else {
            tilNewPassword.setError("");
            tilNewPassword.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(putConfirmPassword)) {
            error = messageEmptyField;
            tilConfirmPassword.setError(error);
            return;
        } else if (putConfirmPassword.length() < 8) {
            error = messageSizeField;
            tilConfirmPassword.setError(error);
            return;
        } else {
            tilConfirmPassword.setError("");
            tilConfirmPassword.setErrorEnabled(false);
        }


        if (!putNewPassword.equals(putConfirmPassword)) {
            error = messagePasswordNotMatch;
            tilConfirmPassword.setError(error);
            return;
        }


        if (dialogListener != null) {
            Log.d("DialogPassw", "!putNewPassword = " + putNewPassword);
            dialogListener.onPasswordChange(putNewPassword);

            dismiss();
        }
    }


    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.dialog_confirmation_button_ok:
                    buttonOkPressed();
                    break;

            }
        }
    }

    private final class TextWatcherOldPassword implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String putOldPassword = editOldPassword.getText().toString();
            String error;

            if (TextUtils.isEmpty(putOldPassword)) {
                error = messageEmptyField;
                tilOldPassword.setError(error);
                return;
            } else if (putOldPassword.length() < 8) {
                error = messageSizeField;
                tilOldPassword.setError(error);
                return;
            } else {
                tilOldPassword.setError("");
                tilOldPassword.setErrorEnabled(false);
            }
        }
    }

    private final class TextWatcherNewPassword implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String putNewPassword = editNewPassword.getText().toString();
            String error;
            if (TextUtils.isEmpty(putNewPassword)) {
                error = messageEmptyField;
                tilNewPassword.setError(error);
                return;
            } else if (putNewPassword.length() < 8) {
                error = messageSizeField;
                tilNewPassword.setError(error);
                return;
            } else {
                tilNewPassword.setError("");
                tilNewPassword.setErrorEnabled(false);
            }
        }
    }

    private final class TextWatcherConfirmPassword implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String putConfirmPassword = editConfirmPassword.getText().toString();
            String error;

            if (TextUtils.isEmpty(putConfirmPassword)) {
                error = messageEmptyField;
                tilConfirmPassword.setError(error);
                return;
            } else if (putConfirmPassword.length() < 8) {
                error = messageSizeField;
                tilConfirmPassword.setError(error);
                return;
            } else {
                tilConfirmPassword.setError("");
                tilConfirmPassword.setErrorEnabled(false);
            }
        }
    }
}
