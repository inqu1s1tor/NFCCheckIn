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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.RealmCard;
import com.erminesoft.nfcpp.ui.dialogs.GenericDialog;
import com.erminesoft.nfcpp.ui.dialogs.UnsavedDataDialog;
import com.erminesoft.nfcpp.ui.launcher.DialogLauncher;
import com.erminesoft.nfcpp.util.NfcUtil;

import java.util.List;

/**
 * Created by Aleks on 31.03.2016.
 */
public class CreateAndEditCardFragment extends GenericFragment {

    private static final String CARD_ID = "card_id";

    private EditText nameEt;
    private EditText descriptionEt;
    private NfcAdapter nfcAdapter;
    private RealmCard realmCard;
    private EditText cardId;


    public static Bundle buildArgs(String cardId){
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

        nameEt = (EditText) view.findViewById(R.id.place_name_et);
        descriptionEt = (EditText) view.findViewById(R.id.description_et);
        cardId = (EditText) view.findViewById(R.id.showIdcard);

        changeStateOfBackButton();

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.save_new_place_button).setOnClickListener(listener);
        view.findViewById(R.id.button_cancel_card_edit).setOnClickListener(listener);

        if (!initNFC()) {
            return;
        }

        initNFC();
        extractExistCard();
    }

    private void saveNewCard() {

        if (validationFields()) {
            mActivityBridge.getUApplication().getNetBridge().addNewCard(realmCard, new NetCallBack());
        }

        realmCard.setNameCard(nameEt.getText().toString());
        realmCard.setDescriptionCard(descriptionEt.getText().toString());
        realmCard.setIdCard(cardId.getText().toString());
    }

    private void extractExistCard() {
        Bundle bundle = getArguments();
        if(bundle.isEmpty()) {
            realmCard = new RealmCard();
        } else {
            String cardId = bundle.getString(CARD_ID);

            if (cardId == null && cardId.isEmpty()) {
                realmCard = new RealmCard();
            } else {
                realmCard = mActivityBridge.getUApplication().getDbBridge().getCardById(cardId);
                extractModeltoView(realmCard);
            }
        }
    }

    private void extractModeltoView(RealmCard realmCard){
        cardId.setText(realmCard.getIdCard());
        descriptionEt.setText(realmCard.getDescriptionCard());
        nameEt.setText(realmCard.getNameCard());
    }

    private boolean validationFields() {
        if (TextUtils.isEmpty(realmCard.getIdCard())) {
            String message = getActivity().getResources().getString(R.string.message_admin_no_added_card);
            showShortToast(message);
            return false;
        }

        if (mActivityBridge.getUApplication().getDbBridge().containCardById(realmCard.getIdCard())) {
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

    @Override
    protected void changeStateOfBackButton() {
        mActivityBridge.switchBackButtonVisibility(true);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private final class NfcCallback implements NfcAdapter.ReaderCallback {

        @Override
        public void onTagDiscovered(Tag tag) {
            final String cardIdFromTag = NfcUtil.byteArrayToHexString(tag.getId());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    realmCard.setIdCard(cardIdFromTag);
                    cardId.setText(cardIdFromTag);
                }
            });

        }
    }

    private final class NetCallBack extends SimpleMainCallBack {

        @Override
        public void onSuccess() {
            getActivity().onBackPressed();
        }

        @Override
        public void onError(String error) {
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
