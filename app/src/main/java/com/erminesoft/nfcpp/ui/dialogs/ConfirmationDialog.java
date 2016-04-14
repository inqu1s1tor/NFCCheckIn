package com.erminesoft.nfcpp.ui.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.erminesoft.nfcpp.R;


public class ConfirmationDialog extends GenericDialog {

    private TextView textTitile;
    private DialogListener dialogListener;
    private final Bundle bundle;

    public static Bundle buildArguments(String title){
        Bundle bundle = new Bundle();
        bundle.putString(TITLE_TEXT, title);
        return bundle;
    }

    public ConfirmationDialog(Context context, DialogListener clicker, Bundle bundle) {
        super(context);
        this.dialogListener = clicker;
        this.bundle = bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirmation);
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        textTitile = (TextView) findViewById(R.id.dialog_confirmation_title);

        String title = bundle.getString(TITLE_TEXT);
        textTitile.setText(title);

        View.OnClickListener listener = new Clicker();
        findViewById(R.id.dialog_confirmation_button_ok).setOnClickListener(listener);
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.dialog_confirmation_button_ok:
                    if (dialogListener != null) {
                        dialogListener.onProceedPressed();
                        dismiss();
                    }
                    break;

            }
        }
    }
}
