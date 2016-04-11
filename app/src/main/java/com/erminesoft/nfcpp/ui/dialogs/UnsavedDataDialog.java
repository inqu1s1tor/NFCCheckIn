package com.erminesoft.nfcpp.ui.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;

/**
 * Created by Aleks on 04.04.2016.
 */
public class UnsavedDataDialog extends GenericDialog {

    private GenericDialog.DialogListener dialogListener;
    private final Bundle bundle;

    public static Bundle buildArguments(String title){
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_TEXT, title);
        return bundle;
    }

    public UnsavedDataDialog(Context context, GenericDialog.DialogListener clicker, Bundle bundle) {
        super(context);
        this.dialogListener = clicker;
        this.bundle = bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_unsaved_data);

        TextView textTitle = (TextView) findViewById(R.id.dialog_unsavedData);

        String title = bundle.getString(TITLE_TEXT);
        if(TextUtils.isEmpty(title)){
            title = getContext().getString(R.string.app_name);
        }

        textTitle.setText(title);

        View.OnClickListener listener = new Clicker();

        findViewById(R.id.dialog_button_ok).setOnClickListener(listener);
        findViewById(R.id.dialog_button_cancel).setOnClickListener(listener);
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.dialog_button_ok:

                    if (dialogListener != null) {
                        dialogListener.onOkPressed();
                        dismiss();
                    }

                    break;

                case R.id.dialog_button_cancel:
                    dismiss();
                    break;
            }
        }
    }
}
