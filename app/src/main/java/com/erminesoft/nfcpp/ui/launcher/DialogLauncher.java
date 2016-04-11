package com.erminesoft.nfcpp.ui.launcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.erminesoft.nfcpp.ui.dialogs.ConfirmationDialog;
import com.erminesoft.nfcpp.ui.dialogs.GenericDialog;
import com.erminesoft.nfcpp.ui.dialogs.UnsavedDataDialog;
import com.erminesoft.nfcpp.ui.fragments.FragmentMain;

/**
 * Created by Aleks on 04.04.2016.
 */
public class DialogLauncher {

    public  static void launchUnsavedDataDialog(Activity activity, GenericDialog.DialogListener dialogListener, Bundle bundle) {
        UnsavedDataDialog dialog = new UnsavedDataDialog(activity, dialogListener, bundle);
        dialog.show();
    }

    public static void launchConfirmationDialog(Activity activity, GenericDialog.DialogListener dialogListener, Bundle bundle){
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(activity, dialogListener, bundle);
        confirmationDialog.show();
    }
}
