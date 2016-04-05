package com.erminesoft.nfcpp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.core.bridge.ActivityBridge;
import com.erminesoft.nfcpp.ui.dialogs.GenericDialog;
import com.erminesoft.nfcpp.ui.launcher.DialogLauncher;
import com.erminesoft.nfcpp.ui.launcher.FragmentLauncher;


public class MainActivity extends AppCompatActivity implements ActivityBridge {

    private NfcApplication application;
    private FragmentLauncher fragmentLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (NfcApplication) getApplication();
        fragmentLauncher = new FragmentLauncher(getSupportFragmentManager());
        fragmentLauncher.launchWelcomeFragment();


    }



    @Override
    public void switchBackButtonVisibility(boolean isVisible) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isVisible);
    }

    @Override
    public NfcApplication getUApplication() {
        return application;
    }

    @Override
    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }


    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}