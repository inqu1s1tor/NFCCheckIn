package com.erminesoft.nfcpp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Aleks on 04.04.2016.
 */
public abstract class GenericDialog extends Dialog {

    protected static final String TITLE_TEXT = "title";

    public GenericDialog(Context context) {
        super(context);
    }

    public GenericDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected GenericDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public interface DialogListener {

        void onOkPressed();
    }
}
