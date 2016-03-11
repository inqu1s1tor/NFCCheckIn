package com.erminesoft.nfcpp.ui.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.ui.MainActivity;
import com.erminesoft.nfcpp.ui.launcher.FragmentLauncher;

/**
 * Created by Aleks on 10.03.2016.
 */
public class FragmentMain extends GenericFragment {

    private NfcAdapter nfcAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
            Log.e("MA", "is adapter enabled = " + nfcAdapter.isEnabled());
            nfcAdapter.enableReaderMode(getActivity(), new NfcAdapter.ReaderCallback() {
                @Override
                public void onTagDiscovered(Tag tag) {
                    Log.e("MA", "NFC TAG = " + tag.toString());

                    byte[] cardIdArray = tag.getId();
                    ByteArrayToHexString(cardIdArray);

//                cardID.setText(ByteArrayToHexString(cardIdArray));
                }
            }, NfcAdapter.FLAG_READER_NFC_A, Bundle.EMPTY);

        }



    private static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);

    }

    private void currentTime() {
        long hog = System.currentTimeMillis();

    }

}
