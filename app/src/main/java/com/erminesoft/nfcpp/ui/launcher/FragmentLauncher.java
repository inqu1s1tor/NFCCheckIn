package com.erminesoft.nfcpp.ui.launcher;



import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.ui.fragments.FragmentMain;
import com.erminesoft.nfcpp.ui.fragments.GenericFragment;
import com.erminesoft.nfcpp.ui.fragments.SignInFragment;
import com.erminesoft.nfcpp.ui.fragments.SignUpFragment;
import com.erminesoft.nfcpp.ui.fragments.WelcomeFragment;

/**
 * Created by Aleks on 09.03.2016.
 */
public class FragmentLauncher {

    private final FragmentManager fragmentManager;

    public FragmentLauncher(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private void launch (GenericFragment genericFragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(!TextUtils.isEmpty(tag)) {
            fragmentTransaction.addToBackStack(tag);
        }

        fragmentTransaction.replace(R.id.containerFragment, genericFragment, tag );
        fragmentTransaction.commitAllowingStateLoss();
    }



    public void launchWelcomeFragment() {
        GenericFragment fragmentWelcome = new WelcomeFragment();
        launch(fragmentWelcome, null);
    }

    public void launchSignInfragment() {
        GenericFragment signInFragmnet = new SignInFragment();
        launch(signInFragmnet, null);
    }

    public void launchSignUpFragment() {
        GenericFragment signUpFragment = new SignUpFragment();
        launch(signUpFragment, null);
    }

    public void launchMainFragment() {
        GenericFragment fragmentMain = new FragmentMain();
        launch(fragmentMain, null);
    }


}
