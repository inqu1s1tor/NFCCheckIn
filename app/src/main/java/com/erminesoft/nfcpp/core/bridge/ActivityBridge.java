package com.erminesoft.nfcpp.core.bridge;

import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.ui.launcher.FragmentLauncher;

public interface ActivityBridge {

    void switchBackButtonVisibility(boolean isVisible);

    NfcApplication getUApplication();

    FragmentLauncher getFragmentLauncher();

}
