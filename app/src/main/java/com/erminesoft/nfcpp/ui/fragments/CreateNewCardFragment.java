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
import com.erminesoft.nfcpp.util.NfcUtil;

import java.util.List;

/**
 * Created by Aleks on 31.03.2016.
 */
public class CreateNewCardFragment extends GenericFragment {
    private EditText nameEt;
    private EditText descriptionEt;
    private NfcAdapter nfcAdapter;
    private RealmCard card;
    private EditText cardId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        card = new RealmCard();
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

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.save_new_place_button).setOnClickListener(listener);
        view.findViewById(R.id.button_cancel_card_edit).setOnClickListener(listener);

        if (!initNFC()) {
            return;
        }

        initNFC();
    }

    private void saveNewCard() {

        if (validationFields()) {
            mActivityBridge.getUApplication().getNetBridge().addNewCard(card, new NetCallBack());
        }

        card.setNameCard(nameEt.getText().toString());
        card.setDescriptionCard(descriptionEt.getText().toString());
        card.setIdCard(cardId.getText().toString());
    }

    private boolean validationFields() {
        if (TextUtils.isEmpty(card.getIdCard())) {
            String message = getActivity().getResources().getString(R.string.message_admin_no_added_card);
            showShortToast(message);
            return false;
        }

        if (mActivityBridge.getUApplication().getDbBridge().containCardById(card.getIdCard())) {
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
                    card.setIdCard(cardIdFromTag);
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

    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save_new_place_button:
                    saveNewCard();
                    break;
                case R.id.button_cancel_card_edit:
                    getActivity().onBackPressed();
            }

        }
    }
}
