package com.erminesoft.nfcpp.ui.launcher;



import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.ui.fragments.AdminFragment;
import com.erminesoft.nfcpp.ui.fragments.CreateAndEditCardFragment;
import com.erminesoft.nfcpp.ui.fragments.DetailStatisticsFragment;
import com.erminesoft.nfcpp.ui.fragments.FragmentMain;
import com.erminesoft.nfcpp.ui.fragments.GenericFragment;
import com.erminesoft.nfcpp.ui.fragments.SignInFragment;
import com.erminesoft.nfcpp.ui.fragments.SignUpFragment;
import com.erminesoft.nfcpp.ui.fragments.StatisticsFragment;
import com.erminesoft.nfcpp.ui.fragments.TutorialFragment;
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
        launch(signInFragmnet, SignInFragment.class.getCanonicalName());
    }

    public void launchSignUpFragment(Boolean isTestLogin) {
        GenericFragment signUpFragment = new SignUpFragment();
        signUpFragment.setArguments(SignUpFragment.buildArguments(isTestLogin));
        launch(signUpFragment, SignInFragment.class.getCanonicalName());
    }

    public void launchMainFragment() {
        GenericFragment fragmentMain = new FragmentMain();
        launch(fragmentMain, null);
    }

    public void launchAdminMainFragment() {
        GenericFragment fragmentMain = new FragmentMain();
        launch(fragmentMain, FragmentMain.class.getCanonicalName());
    }

    public void launchStatisticsFragment(String objectId) {
        GenericFragment statisticsFragment = new StatisticsFragment();
        statisticsFragment.setArguments(StatisticsFragment.buildArguments(objectId));
        launch(statisticsFragment,StatisticsFragment.class.getCanonicalName());
    }

    public void launchAdminFragment() {
        GenericFragment adminFragmnent = new AdminFragment();
        launch(adminFragmnent, null);
    }

    public void launchDetailStatisticsFragment(Bundle bundle){
        GenericFragment detailStatisticsFragment = new DetailStatisticsFragment();
        detailStatisticsFragment.setArguments(bundle);
        launch(detailStatisticsFragment,StatisticsFragment.class.getCanonicalName());
    }

    public void launchCreateAndEditCardFragment(Bundle bundle) {
        GenericFragment createAndEditCardFragment = new CreateAndEditCardFragment();
        createAndEditCardFragment.setArguments(bundle);
        launch(createAndEditCardFragment, CreateAndEditCardFragment.class.getCanonicalName());
    }

    public void launchTutorialFragment(){
        TutorialFragment tutorialFragment = new TutorialFragment();
        launch(tutorialFragment, CreateAndEditCardFragment.class.getCanonicalName());
    }
}
