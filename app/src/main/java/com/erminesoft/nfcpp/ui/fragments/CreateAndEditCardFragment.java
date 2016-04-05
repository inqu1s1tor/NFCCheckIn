package com.erminesoft.nfcpp.ui.fragments;

import android.annotation.TargetApi;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.ui.dialogs.GenericDialog;
import com.erminesoft.nfcpp.ui.dialogs.UnsavedDataDialog;
import com.erminesoft.nfcpp.ui.launcher.DialogLauncher;
import com.erminesoft.nfcpp.util.NfcUtil;

public class CreateAndEditCardFragment extends GenericFragment {

    private static final String CARD_ID = "card_id";

    private EditText nameEt;
    private EditText descriptionEt;
    private NfcAdapter nfcAdapter;
    private Card card;
    private EditText cardIdEt;

    private String editCardId;
    private String cardObjectId;


    public static Bundle buildArgs(String cardId) {
        Bundle bundle = new Bundle();
        bundle.putString(CARD_ID, cardId);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_new_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        nameEt = (EditText) view.findViewById(R.id.place_name_et);
        descriptionEt = (EditText) view.findViewById(R.id.description_et);
        cardIdEt = (EditText) view.findViewById(R.id.showIdcard);

        isBackButtonVisible();

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.save_new_place_button).setOnClickListener(listener);
        view.findViewById(R.id.button_cancel_card_edit).setOnClickListener(listener);

        if (!initNFC()) {
            return;
        }

        initNFC();
        extractExistCard();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("CEF", "home pressed");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }

    private void saveNewCard() {

        if (validationFields()) {
            card = new Card();

            if (editCardId == null) {
                card.setNameCard(nameEt.getText().toString());
                card.setDescriptionCard(descriptionEt.getText().toString());
                card.setIdCard(cardIdEt.getText().toString());
            } else {
                card.setNameCard(nameEt.getText().toString());
                card.setDescriptionCard(descriptionEt.getText().toString());
                card.setIdCard(cardIdEt.getText().toString());
                card.setObjectId(cardObjectId);
            }

            showProgressDialog();
            mActivityBridge.getUApplication().getNetBridge().addNewCard(card, new NetCallBack());

        }

    }

    private void extractExistCard() {
        Bundle bundle = getArguments();
        if (!bundle.isEmpty()) {
            editCardId = bundle.getString(CARD_ID);
            if (editCardId != null && !editCardId.isEmpty()) {
                Card editCard = mActivityBridge.getUApplication().getDbBridge().getCardById(editCardId);
                extractModeltoView(editCard);
            }
        }
    }

    private void extractModeltoView(Card editCard) {
        cardIdEt.setText(editCard.getIdCard());
        descriptionEt.setText(editCard.getDescriptionCard());
        nameEt.setText(editCard.getNameCard());
        cardObjectId = editCard.getObjectId();
    }

    private boolean validationFields() {
        Log.d("validationFields", "editCardId=" + editCardId);

        if (TextUtils.isEmpty(cardIdEt.getText().toString())) {
            String message = getActivity().getResources().getString(R.string.message_admin_no_added_card);
            showShortToast(message);
            return false;
        }

        if (editCardId == null && mActivityBridge.getUApplication().getDbBridge().containCardById(cardIdEt.getText().toString())) {
            String message = getActivity().getResources().getString(R.string.message_admin_card_already);
            showShortToast(message);
            return false;
        }

        if (TextUtils.isEmpty(nameEt.getText().toString())) {
            nameEt.setError("Input name");
            return false;
        }

        if (TextUtils.isEmpty(descriptionEt.getText().toString())) {
            descriptionEt.setError("Input description");
            return false;
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean initNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (nfcAdapter == null) {
            showShortToast("No NFC module");
            return false;
        }
        nfcAdapter.enableReaderMode(getActivity(), new NfcCallback(), NfcAdapter.FLAG_READER_NFC_A, Bundle.EMPTY);

        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private final class NfcCallback implements NfcAdapter.ReaderCallback {

        @Override
        public void onTagDiscovered(Tag tag) {
            final String cardIdFromTag = NfcUtil.byteArrayToHexString(tag.getId());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cardIdEt.setText(cardIdFromTag);
                }
            });

        }
    }

    private final class NetCallBack extends SimpleMainCallBack {

        @Override
        public void onSuccess() {
            hideProgressDialog();
            getActivity().onBackPressed();
        }

        @Override
        public void onError(String error) {
            hideProgressDialog();
            showShortToast(error);
        }
    }

    private final class DialogListener implements GenericDialog.DialogListener {

        @Override
        public void onOkPressed() {
            getActivity().onBackPressed();
        }
    }

    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_new_place_button:
                    saveNewCard();
                    break;
                case R.id.button_cancel_card_edit:
                    Bundle bundle = UnsavedDataDialog.buildArguments(getActivity().getResources().getString(R.string.lost_data_dialog));
                    DialogLauncher.launchUnsavedDataDialog(getActivity(), new DialogListener(), bundle);
            }

        }
    }
}
