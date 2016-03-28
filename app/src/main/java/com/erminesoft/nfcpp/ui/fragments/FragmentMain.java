package com.erminesoft.nfcpp.ui.fragments;

import android.annotation.TargetApi;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.model.EventsToday;
import com.erminesoft.nfcpp.net.SyncService;
import com.erminesoft.nfcpp.ui.adapters.EventAdapter;
import com.erminesoft.nfcpp.util.CardFilterUtil;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.NfcUtil;
import com.erminesoft.nfcpp.util.SortUtil;
import com.erminesoft.nfcpp.util.SystemUtils;

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

        loadEventsFromBackendless();
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

    private void loadTodayEventsList(List<RealmEvent> realmEventList) {
        if (realmEventList.size() <= 0) {
            return;
        }

        eventsTodayList.clear();
        long diffInMs = SortUtil.sortEventsOnTodayAndReturnTotalWorkingTime(realmEventList, eventsTodayList);
        todayTotalTv.setText(DateUtil.getDifferenceTime(diffInMs));
        eventAdapter.replaceNewData(eventsTodayList);

        if (SystemUtils.isNetworkConnected(getActivity())) {
            SyncService.start(getActivity());
        }
    }


    private void getEventsFromDb() {
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D).format(curTime);
        List<RealmEvent> realmEventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDate(curStringDate);
//        for (RealmEvent rl : realmEventList) {
//            Log.d("getEventsFromDb", "rl.getIsSent()=" + rl.getIsSent() + "     getCreationTime=" + rl.getCreationTime() + "   getObjectId=" + rl.getObjectId());
//        }
        loadTodayEventsList(realmEventList);

    }

    private void loadEventsFromBackendless() {
        long lastSyncDate = mActivityBridge.getUApplication().getSharedHelper().getLastSyncDate();
        String myId = mActivityBridge.getUApplication().getDbBridge().getMe().getObjectId();
        long curTime = System.currentTimeMillis();
        if (lastSyncDate == 0) {
            mActivityBridge.getUApplication().getNetBridge().getAllEventsByUserId(myId, new NetCallback());
        } else {
            mActivityBridge.getUApplication().getNetBridge().getTodayEventsByUserId(myId, lastSyncDate, new NetCallback());
        }
        mActivityBridge.getUApplication().getSharedHelper().setLastSyncDate(curTime);
    }

    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    private void doubleCheckInFilter(String cardId) {
        if (CardFilterUtil.isDoubleCheckIn(mActivityBridge.getUApplication().getDbBridge(), cardId)) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showShortToast("It is a double checkin!");
                }
            });
        } else {
            createNewEvent(cardId);
        }
    }

    private void createNewEvent(String cardId) {
        RealmEvent realmEvent = new RealmEvent();
        realmEvent.setIdCard(cardId);
        realmEvent.setCreationTime((int) (System.currentTimeMillis() / 1000));
        realmEvent.setIsSent(false);
        mActivityBridge.getUApplication().getDbBridge().saveEvent(realmEvent);
    }

    private void checkUpdateDataFromBackendless(List<RealmEvent> realmEventList) {
        mActivityBridge.getUApplication().getDbBridge().saveEvent(realmEventList);
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
        public void onSuccessGetEvents(List<RealmEvent> realmEventList) {
            checkUpdateDataFromBackendless(realmEventList);
        }
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.transferToStatisticsButton:
                    mActivityBridge.getFragmentLauncher().launchStatisticsFragment(null);
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
