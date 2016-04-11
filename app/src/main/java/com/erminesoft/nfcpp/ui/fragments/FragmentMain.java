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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.backendless.Backendless;
import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.NfcApplication;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.EventsToday;
import com.erminesoft.nfcpp.net.SyncService;
import com.erminesoft.nfcpp.ui.adapters.EventAdapter;
import com.erminesoft.nfcpp.ui.dialogs.GenericDialog;
import com.erminesoft.nfcpp.ui.dialogs.UnsavedDataDialog;
import com.erminesoft.nfcpp.ui.launcher.DialogLauncher;
import com.erminesoft.nfcpp.util.CardFilterUtil;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.NfcUtil;
import com.erminesoft.nfcpp.util.SortUtil;
import com.erminesoft.nfcpp.util.SystemUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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
    private Button statButton;
    private ListView listViewEvents;

    private NfcAdapter nfcAdapter;
    private EventAdapter eventAdapter;
    private List<EventsToday> eventsTodayList;
    private Observer observer;
    private Tracker mTracker;

    private String myObjectId;
    private boolean isTestLogin = false;
    private boolean isAdminLogin = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String myRoles = mActivityBridge.getUApplication().getDbBridge().getMe().getUserRoles();
        isAdminLogin = myRoles.contains("Admin");

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NfcApplication application = mActivityBridge.getUApplication();
        mTracker = application.getDefaultTracker();

        currentTimeTv = (TextView) view.findViewById(R.id.currentTime);
        todayTotalTv = (TextView) view.findViewById(R.id.todayTotal);
        listViewEvents = (ListView) view.findViewById(R.id.list_events);

        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D_H_M).format(curTime);
        currentTimeTv.setText(curStringDate);

        if (!initNFC()) {
//            return;
        }

        setHasOptionsMenu(true);

        myObjectId = mActivityBridge.getUApplication().getDbBridge().getMe().getObjectId();
        Log.d("FM", "myObjectId = " + myObjectId);
        if (myObjectId.equals(getActivity().getString(R.string.objectid_test_user))) {
            isTestLogin = true;
        }

        initAdapter();
        getEventsFromDb();

        View.OnClickListener listener = new Clicker();
        statButton = (Button) view.findViewById(R.id.transferToStatisticsButton);
        statButton.setOnClickListener(listener);

        if (isAdminLogin){
            statButton.setVisibility(View.INVISIBLE);
        }


        if (!isTestLogin) {
            if (!Backendless.UserService.isValidLogin()){
                mActivityBridge.getUApplication().getNetBridge().autoLoginUser(new NetCallback());
            } else {
                mActivityBridge.getUApplication().getNetBridge().isUserAuthenticated(new NetCallback());
                loadDataFromBackendless();
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        observer = new DbObserver();
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
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

    private void loadTodayEventsList(List<Event> eventList, List<Card> cardList) {
        if (eventList.size() <= 0) {
            return;
        }

        eventsTodayList.clear();
        long diffInMs = SortUtil.sortEventsOnTodayAndReturnTotalWorkingTime(eventList, cardList, eventsTodayList, true);
        todayTotalTv.setText(DateUtil.getDifferenceTime(diffInMs));
        eventAdapter.replaceNewData(eventsTodayList);

        if (!isTestLogin && SystemUtils.isNetworkConnected(getActivity())) {
            Log.d("!", "!SyncService.start");
            SyncService.start(getActivity());
        }
    }

    private void getEventsFromDb() {
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D).format(curTime);
        List<Event> eventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDateAndUserId(curStringDate, myObjectId);     // getEventsByDate
//        Log.d("getEventsFromDb", "eventList.size() = " + eventList.size());
//        for (Event ev : eventList) {
//            Log.d("getEventsFromDb", "getOwnerId() = " + ev.getOwnerId() + "    getCreationTime() = " + ev.getCreationTime());
//        }
        List<Card> cardList = mActivityBridge.getUApplication().getDbBridge().getAllCards();

        loadTodayEventsList(eventList, cardList);
    }

    private void loadDataFromBackendless() {
        mActivityBridge.getUApplication().getNetBridge().getAllCard(new NetCallback());

        long lastSyncDate = mActivityBridge.getUApplication().getSharedHelper().getLastSyncDate();
        String myId = mActivityBridge.getUApplication().getDbBridge().getMe().getObjectId();
        long curTime = System.currentTimeMillis();
        if (lastSyncDate == (long) 0) {
            mActivityBridge.getUApplication().getNetBridge().getAllEventsByUserId(myId, new NetCallback());
        } else {
            mActivityBridge.getUApplication().getNetBridge().getTodayEventsByUserId(myId, lastSyncDate, new NetCallback());
        }
        mActivityBridge.getUApplication().getSharedHelper().setLastSyncDate(curTime);
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
        if (!isTestLogin) {
            mActivityBridge.getUApplication().getNetBridge().isUserAuthenticated(new NetCallback());
        }
        if (checkValidityEntryCard(cardId)) {
            Event event = new Event();
            event.setIdCard(cardId);
            event.setCreationTime((int) (System.currentTimeMillis() / 1000));
            event.setIsSent(false);
            event.setOwnerId(myObjectId);
            mActivityBridge.getUApplication().getDbBridge().saveEvent(event);

            sendLog("createNewEvent");
        } else {
            Log.e("MF", "wrong card id = " + cardId);
        }
    }


    private boolean checkValidityEntryCard(String cardId) {
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D).format(curTime);
        List<Event> eventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDate(curStringDate);

        if (!isTestLogin && !mActivityBridge.getUApplication().getDbBridge().containCardById(cardId)) {
            String message = getActivity().getString(R.string.message_unknown_card);
            showShortToastInsideThread(message);
            return false;
        }

        if (eventList.size() <= 0) {
            return true;
        }

        if (eventList.size() % 2 == 0) {
            return true;
        }

        int lastEvent = eventList.size() - 1;
        if (eventList.get(lastEvent).getIdCard().equals(cardId)) {
            return true;
        }

        String message = getActivity().getString(R.string.message_invalid_card_to_exit);
        showShortToastInsideThread(message);
        return false;
    }

    private void showShortToastInsideThread(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showShortToast(message);
            }
        });

    }

    private void checkUpdateDataFromBackendless(List<Event> eventList) {
        mActivityBridge.getUApplication().getDbBridge().saveEvent(eventList);
    }

    private void sendLog(String Category) {
        String myName = mActivityBridge.getUApplication().getDbBridge().getMe().getName();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(Category)
                .setAction(myName)
                .build());
    }

    private void logout() {
        mActivityBridge.getUApplication().getDbBridge().clearAllData();
        mActivityBridge.getUApplication().getSharedHelper().sharedHelperClear();
        mActivityBridge.getUApplication().getNetBridge().userLogout();
        mActivityBridge.getFragmentLauncher().launchWelcomeFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isAdminLogin) {
            inflater.inflate(R.menu.user_setting_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.user_setting_menu_action_tutorial:
//                mActivityBridge.getFragmentLauncher().launchTutorialFragment();
//                break;

            case R.id.user_setting_menu_action_sync:
                if (!isTestLogin && SystemUtils.isNetworkConnected(getActivity())) {
                    SyncService.start(getActivity());
                }
                break;

            case R.id.user_setting_menu_action_log_out:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isBackButtonVisible() {
        return isAdminLogin;
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
        public void onSuccessGetEvents(List<Event> eventList) {
            checkUpdateDataFromBackendless(eventList);
        }

        @Override
        public void isUserAuthenticated(boolean isAuth) {
            if (!isAuth){
                Bundle bundle = UnsavedDataDialog.buildArguments(getActivity().getResources().getString(R.string.loggen_another_device));
                DialogLauncher.launchConfirmationDialog(getActivity(), new DialogListener(), bundle);
            }
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

    private final class DialogListener implements GenericDialog.DialogListener{
        @Override
        public void onOkPressed() {
            logout();
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getEventsFromDb();
                }
            });
        }

    }

}
