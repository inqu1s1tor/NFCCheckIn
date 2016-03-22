package com.erminesoft.nfcpp.ui.fragments;

import android.annotation.TargetApi;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.EventsToday;
import com.erminesoft.nfcpp.ui.MainActivity;
import com.erminesoft.nfcpp.ui.adapters.EventAdapter;
import com.erminesoft.nfcpp.ui.launcher.FragmentLauncher;
import com.erminesoft.nfcpp.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aleks on 10.03.2016.
 */
public class FragmentMain extends GenericFragment {

    private TextView currentTimeTv;
    private TextView todayTotalTv;
    private ListView listViewEvents;

    private NfcAdapter nfcAdapter;
    private EventAdapter eventAdapter;
    private List<EventsToday> eventsTodayList;
    private Observer observer;

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
        todayTotalTv = (TextView) view.findViewById(R.id.todayTotal);
        listViewEvents = (ListView) view.findViewById(R.id.list_events);


        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(curTime);
        currentTimeTv.setText(curStringDate);

        goNfc();
        initAdapter();
        getEventsFromDb();


        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.transferToStatisticsButton).setOnClickListener(listener);
    }

    private void initAdapter(){
        eventsTodayList = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity(), eventsTodayList);
        listViewEvents.setAdapter(eventAdapter);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void goNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        if (nfcAdapter == null) {
            Log.e("MA", "No NFC");
            return;
        }
        Log.e("MA", "is adapter enabled = " + nfcAdapter.isEnabled());
        nfcAdapter.enableReaderMode(getActivity(), new NfcAdapter.ReaderCallback() {
            @Override
            public void onTagDiscovered(Tag tag) {
                Log.e("MA", "NFC TAG = " + tag.toString());

                byte[] cardIdArray = tag.getId();
                String idCard = byteArrayToHexString(cardIdArray);
                Log.d("nfc", "ID_Card = " + idCard);

                addNewEvent(idCard);

            }
        }, NfcAdapter.FLAG_READER_NFC_A, Bundle.EMPTY);

    }

    private void addNewEvent(String idCard) {
//        mActivityBridge.getUApplication().getNetBridge().addNewEvent(idCard, new NetCallback());
        long now = System.currentTimeMillis();
        int timeUnix = (int) (System.currentTimeMillis() / 1000);

        Event newEvent = new Event();
        newEvent.setIdCard(idCard);
        newEvent.setCreationTime(timeUnix);
        mActivityBridge.getUApplication().getDbBridge().saveEvent(newEvent);

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


    private void loadTodayEventsList(List<Event> eventList) {
        if (eventList.size() > 0) {
            eventsTodayList.clear();
            long entryLong = 0;
            long exitLong = 0;
            long diffInMs = 0;

            for (int i = 1; i <= eventList.size(); i++) {
                if (i % 2 == 0) { // 2
                    exitLong = ((long) eventList.get(i - 1).getCreationTime() * (long) 1000);

                    Log.d("loadTodayEvents", "exitLong = " + exitLong);
                    diffInMs = diffInMs + (exitLong - entryLong);
                    eventsTodayList.add(new EventsToday(DateUtil.dateToFormatString(entryLong),
                            DateUtil.dateToFormatString(exitLong),
                            DateUtil.getDifferenceTime(exitLong - entryLong),
                            false));
                } else {  //1
                    entryLong = ((long) eventList.get(i - 1).getCreationTime() * (long) 1000);
                    if (i == eventList.size()) {
                        Date curDate = new Date(System.currentTimeMillis());
                        eventsTodayList.add(new EventsToday(DateUtil.dateToFormatString(entryLong),
                                " --:-- ",
                                DateUtil.getDifferenceTime(curDate.getTime() - entryLong),
                                false));

                        diffInMs = diffInMs + (curDate.getTime() - entryLong);
                    }
                }
            }

            todayTotalTv.setText(DateUtil.getDifferenceTime(diffInMs));

            Log.d("loadTodayEventsList", "eventsTodayList.size() = " + eventsTodayList.size());
            eventAdapter.replaceNewData(eventsTodayList);

        }
    }


    private void getEventsFromDb() {
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat("yyyy-MM-dd").format(curTime);
        List<Event> eventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDate(curStringDate);
        loadTodayEventsList(eventList);
    }

    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    @Override
    public void onStop() {
        super.onStop();
        mActivityBridge.getUApplication().getDbBridge().removeObserver(observer);
        observer = null;
    }

    private final class NetCallback extends SimpleMainCallBack {

        @Override
        public void onSuccess() {
//            getEventsFromBd();
        }

        @Override
        public void onSuccessGetEvents(List<Event> eventList) {
            Log.d("NetCallback", "onSuccessGetEvents");
//            loadTodayScreen(eventList);
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

    private final class DbObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            Log.e("FM", "update");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getEventsFromDb();
                }
            });
        }


    }

}
