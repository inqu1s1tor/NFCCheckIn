package com.erminesoft.nfcpp.ui.fragments;

import android.annotation.TargetApi;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by Aleks on 31.03.2016.
 */
public class CreateNewCardFragment extends GenericFragment {
    private EditText nameEt;
    private EditText descriptionEt;
    private NfcAdapter nfcAdapter;
    private TextView cardId;

    private RealmCard newRealmCard;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_new_card, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEt = (EditText)view.findViewById(R.id.place_name_et);
        descriptionEt = (EditText)view.findViewById(R.id.description_et);
        cardId = (TextView)view.findViewById(R.id.showIdcard);

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
        newRealmCard = new RealmCard();
    }

    private void setNewCard() {
        newRealmCard.setNameCard(nameEt.getText().toString());
        newRealmCard.setDescriptionCard(descriptionEt.getText().toString());
    }

    private void saveNewCard() {
        mActivityBridge.getUApplication().getNetBridge().addNewCard(newRealmCard, new NetCallBack());
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
            String idCard = NfcUtil.byteArrayToHexString(tag.getId());
            cardId.setText(idCard);

            newRealmCard.setIdCard(idCard);
            setNewCard();
        }
    }

    private final class NetCallBack extends SimpleMainCallBack {

    }

    private final class Clicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.save_new_place_button:
                    saveNewCard();
                    getActivity().onBackPressed();
                    break;
            }

        }
    }
}
