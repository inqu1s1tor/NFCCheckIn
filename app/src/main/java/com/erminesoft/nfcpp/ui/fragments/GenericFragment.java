package com.erminesoft.nfcpp.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.erminesoft.nfcpp.core.bridge.ActivityBridge;

/**
 * Created by Aleks on 09.03.2016.
 */
public abstract class GenericFragment extends Fragment {

    protected ActivityBridge mActivityBridge;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityBridge = (ActivityBridge)getActivity();
    }

    protected void showSrotToast(int resId) {
        showSrotToast(getString(resId));
    }

    protected void showSrotToast(String contentString) {
        Toast.makeText(getActivity(), contentString, Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading....");
        }

        progressDialog.show();
    }

    protected void hideProgressDialog() {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}
