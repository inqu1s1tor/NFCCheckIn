package com.erminesoft.nfcpp.ui.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.ui.MainActivity;
import com.erminesoft.nfcpp.ui.launcher.FragmentLauncher;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Aleks on 10.03.2016.
 */
public class FragmentMain extends GenericFragment {

    private TextView currentTimeTv;
    private TextView todayEntryTv;
    private TextView todayExitTv;
    private TextView todayTotalTv;

    private NfcAdapter nfcAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentTimeTv = (TextView) view.findViewById(R.id.currentTime);
        todayEntryTv = (TextView)view.findViewById(R.id.todayEntry);
        todayExitTv = (TextView)view.findViewById(R.id.todayExit);
        todayTotalTv = (TextView)view.findViewById(R.id.totalTimeId);

        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat("MM-dd HH:mm").format(curTime);
        currentTimeTv.setText(curStringDate);

        goNfc();
        getTodayEvents();

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.transferToStatisticsButton).setOnClickListener(listener);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void goNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        Log.e("MA", "is adapter enabled = " + nfcAdapter.isEnabled());
        nfcAdapter.enableReaderMode(getActivity(), new NfcAdapter.ReaderCallback() {
            @Override
            public void onTagDiscovered(Tag tag) {
                Log.e("MA", "NFC TAG = " + tag.toString());

                byte[] cardIdArray = tag.getId();
                String idCard = byteArrayToHexString(cardIdArray);
                Log.d("nfc", "ID_Card = " + idCard);
                mActivityBridge.getUApplication().getNetBridge().addNewEvent(idCard, new NetCallback());

            }
        }, NfcAdapter.FLAG_READER_NFC_A, Bundle.EMPTY);

    }

    private static String byteArrayToHexString(byte[] bytes) {
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

    private void getTodayEvents() {
        long curTime = System.currentTimeMillis();
        String myId = mActivityBridge.getUApplication().getDbBridge().getMyUser().getObjectId();
        mActivityBridge.getUApplication().getNetBridge().getTodayEvents(myId, curTime, new NetCallback());
    }

    private void loadTodayScreen(List<Event> eventList){
        if (eventList.size() > 0) {
            String exit = new SimpleDateFormat("HH:mm").format(eventList.get(0).getCreated());

            String entry = "no data";
            if (eventList.size() > 1) {
                entry = new SimpleDateFormat("HH:mm").format(eventList.get(1).getCreated());
            }
            todayEntryTv.setText(entry);
            todayExitTv.setText(exit);
        }
    }

    private final class NetCallback extends SimpleMainCallBack {

        @Override
        public void onSuccess() {
            getTodayEvents();
        }

        @Override
        public void onSuccessGetEvents(List<Event> eventList) {
            Log.d("NetCallback", "onSuccessGetEvents");
            loadTodayScreen(eventList);
        }

        @Override
        public void onError(String error) {

        }
    }


    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.transferToStatisticsButton:
                    mActivityBridge.getFragmentLauncher().launchStatisticsFragment();
                    break;
            }

        }
    }

}
