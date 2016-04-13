package com.erminesoft.nfcpp.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.erminesoft.nfcpp.core.bridge.ActivityBridge;

public abstract class GenericFragment extends Fragment {

    protected ActivityBridge mActivityBridge;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBridge = (ActivityBridge) getActivity();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivityBridge.switchBackButtonVisibility(isBackButtonVisible());
    }

    protected void showShortToast(int resId) {
        showShortToast(getString(resId));
    }

    protected void showShortToast(String contentString) {
        Toast.makeText(getActivity(), contentString, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String contentString) {
        Toast.makeText(getActivity(), contentString, Toast.LENGTH_LONG).show();
    }

    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading....");
        }

        progressDialog.show();
    }

    protected abstract boolean isBackButtonVisible();

    protected void hideProgressDialog() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }


}
