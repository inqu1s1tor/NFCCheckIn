package com.erminesoft.nfcpp.ui;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.core.bridge.ActivityBridge;
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


        android.support.v4.app.FragmentManager.OnBackStackChangedListener listener = new Clicker();

        getSupportFragmentManager().addOnBackStackChangedListener(listener);
        shouldDisplayHomeUp();
    }


    private void currentTime() {
        long hog = System.currentTimeMillis();
    }

    @Override
    public void setToolbarVisibility(int pVisibilityKey) {

    }

    @Override
    public void switchDrawerAvailability(boolean isEnable) {

    }

    @Override
    public NfcApplication getUApplication() {
        return application;
    }

    @Override
    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }


    public void shouldDisplayHomeUp(){
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    private final class Clicker implements android.support.v4.app.FragmentManager.OnBackStackChangedListener {

        @Override
        public void onBackStackChanged() {
            shouldDisplayHomeUp();
        }
    }
}

