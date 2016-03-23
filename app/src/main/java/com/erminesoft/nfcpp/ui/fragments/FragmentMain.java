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
import com.erminesoft.nfcpp.net.SyncService;
import com.erminesoft.nfcpp.ui.adapters.EventAdapter;
import com.erminesoft.nfcpp.util.CardFilterUtil;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.NfcUtil;
import com.erminesoft.nfcpp.util.SortUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D_H_M).format(curTime);
        currentTimeTv.setText(curStringDate);

        if (!initNFC()) {
            return;
        }

        initAdapter();
        getEventsFromDb();


        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.transferToStatisticsButton).setOnClickListener(listener);

//        SyncService.start(getActivity());
      //  mActivityBridge.getUApplication().getNetBridge().addNewEvent("B7449CB1", new NetCallback());
        SyncService.start(getActivity());
    }

    private void initAdapter() {
        eventsTodayList = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity(), eventsTodayList);
        listViewEvents.setAdapter(eventAdapter);
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

    private void loadTodayEventsList(List<Event> eventList) {
        if (eventList.size() <= 0) {
            return;
        }

        eventsTodayList.clear();
        long diffInMs = SortUtil.sortEventsOnTodayAndReturnTotalWorkingTime(eventList, eventsTodayList);
        todayTotalTv.setText(DateUtil.getDifferenceTime(diffInMs));
        eventAdapter.replaceNewData(eventsTodayList);
    }


    private void getEventsFromDb() {
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D).format(curTime);
        List<Event> eventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDate(curStringDate);
        Log.d("getEventsFromDb","eventList.size() = " + eventList.size());
        loadTodayEventsList(eventList);
    }

    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    private void doubleCheckInFilter(String cardId) {
        if (CardFilterUtil.isDoubleCheckIn(mActivityBridge.getUApplication().getDbBridge(), cardId)) {
            showShortToast("It is a double checkin!");
        } else {
            createNewEvent(cardId);
        }
    }

    private void createNewEvent(String cardId) {
        Event event = new Event();
        event.setIdCard(cardId);
        event.setCreationTime((int) (System.currentTimeMillis() / 1000));
        event.setIsSent(false);
        mActivityBridge.getUApplication().getDbBridge().saveEvent(event);

        Log.e("FM", "start sync");
//        SyncService.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (observer != null) {
            mActivityBridge.getUApplication().getDbBridge().removeObserver(observer);
            observer = null;
        }
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private final class NfcCallback implements NfcAdapter.ReaderCallback {

        @Override
        public void onTagDiscovered(Tag tag) {
            String idCard = NfcUtil.byteArrayToHexString(tag.getId());
            doubleCheckInFilter(idCard);
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
