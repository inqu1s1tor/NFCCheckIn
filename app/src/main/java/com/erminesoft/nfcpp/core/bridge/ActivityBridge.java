package com.erminesoft.nfcpp.core.bridge;

import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.ui.launcher.FragmentLauncher;

/**
 * Created by Aleks on 09.03.2016.
 */
public interface ActivityBridge {
    void setToolbarVisibility(int pVisibilityKey);

    void switchDrawerAvailability(boolean isEnable);

    NfcApplication getUApplication();

    FragmentLauncher getFragmentLauncher();
}
