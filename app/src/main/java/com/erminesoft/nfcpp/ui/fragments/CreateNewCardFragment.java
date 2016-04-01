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
    private TextView cardId;
    private String idCard;


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
        cardId = (TextView) view.findViewById(R.id.showIdcard);

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.save_new_place_button).setOnClickListener(listener);

        if (!initNFC()) {
            return;
        }

        initNFC();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private RealmCard setNewCard() {
        RealmCard newRealmCard = new RealmCard();
        newRealmCard.setNameCard(nameEt.getText().toString());
        newRealmCard.setDescriptionCard(descriptionEt.getText().toString());
        newRealmCard.setIdCard(idCard);

        return newRealmCard;
    }

    private void saveNewCard() {
        if (validationFields()) {
            mActivityBridge.getUApplication().getNetBridge().addNewCard(setNewCard(), new NetCallBack());
        }
    }

    private boolean validationFields() {
        if (idCard == null) {
            String message = getActivity().getResources().getString(R.string.message_admin_no_added_card);
            showShortToast(message);
            return false;
        }

        List<RealmCard> cardList = mActivityBridge.getUApplication().getDbBridge().getAllCards();
        Log.d("validationFields", "cardList.size()=" + cardList.size());
        for (RealmCard rc : cardList) {
            if (rc.getIdCard().equals(idCard)) {
                String message = getActivity().getResources().getString(R.string.message_admin_card_already);
                showShortToast(message);
                return false;
            }
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
            idCard = NfcUtil.byteArrayToHexString(tag.getId());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cardId.setText(idCard);
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
            }

        }
    }
}
