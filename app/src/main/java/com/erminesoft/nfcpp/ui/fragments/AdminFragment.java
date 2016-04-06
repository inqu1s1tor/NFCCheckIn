package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.model.EventsToday;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.ui.adapters.AdminCardsAdapter;
import com.erminesoft.nfcpp.ui.adapters.AdminUsersAdapter;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.SortUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aleks on 18.03.2016.
 */
public class AdminFragment extends GenericFragment {

    private ListView adminList;
    private Observer observer;
    private AdminUsersAdapter adminUsersAdapter;
    private AdminCardsAdapter adminCardsAdapter;
    private RadioGroup radioGroup;
    private TabState state;
    private FloatingActionButton addCardBtn;
    private TextView userNameColumn;
    private TextView totalTimeColumn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminList = (ListView) view.findViewById(R.id.adminList);
        adminList.setEmptyView(view.findViewById(R.id.empty_list_item_admin));
        userNameColumn = (TextView) view.findViewById(R.id.user_name_column_textView);
        totalTimeColumn = (TextView) view.findViewById(R.id.total_time_column_textView);

//        mActivityBridge.getUApplication().getNetBridge().getAllUsers(new NetCallBack(), "");
//        mActivityBridge.getUApplication().getNetBridge().getAllCard(new NetCallBack());

        View.OnClickListener listener = new Clicker();
        addCardBtn = (FloatingActionButton) view.findViewById(R.id.add_card_float_button);
        addCardBtn.setOnClickListener(listener);

//        mActivityBridge.getUApplication().getNetBridge().getAllEvents(new NetCallBack());
        startSync();
        setHasOptionsMenu(true);

        radioGroup = (RadioGroup) view.findViewById(R.id.admin_list_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioListener());

        radioGroup.check(R.id.users_name_radio_button);

        AdapterView.OnItemClickListener itemClicker = new ItemClicker();
        adminList.setOnItemClickListener(itemClicker);
    }

    void startSync(){
        mActivityBridge.getUApplication().getNetBridge().getAllUsers(new NetCallBack(), "");
        mActivityBridge.getUApplication().getNetBridge().getAllCard(new NetCallBack());
        mActivityBridge.getUApplication().getNetBridge().getAllEvents(new NetCallBack());
    }

    @Override
    public void onStart() {
        super.onStart();

        if (observer == null) {
            observer = new DbObserver();
        }
        mActivityBridge.getUApplication().getDbBridge().addNewObserver(observer);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin_setting_menu, menu);
    }

    private void showVisibility() {
        if (state == TabState.USERS) {
            addCardBtn.setVisibility(View.GONE);
            userNameColumn.setText(getActivity().getResources().getString(R.string.user_name_list_column_tv));
            totalTimeColumn.setText(getActivity().getResources().getString(R.string.user_total_time_list_column));
        } else {
            userNameColumn.setText(R.string.card_column_id);
            totalTimeColumn.setText(R.string.card_name_column);
            addCardBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initUsersAdapter() {
        List<User> userList = mActivityBridge.getUApplication().getDbBridge().getAllUsers();
        for (User user : userList) {
            user.setUserTotalTimeToday(getUserTodayTime(user.getObjectId()));
        }
        adminUsersAdapter = new AdminUsersAdapter(getActivity(), userList);
        adminList.setAdapter(adminUsersAdapter);
    }

    private String getUserTodayTime(String userId) {
        long curTime = System.currentTimeMillis();
        String dateString = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D_H_M).format(curTime);
        List<Event> realmEventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDateAndUserId(dateString, userId);
        List<EventsToday> eventsList = new ArrayList<>();
        long todayTime = SortUtil.sortEventsOnTodayAndReturnTotalWorkingTime(realmEventList, null, eventsList, true);
        String usertotalTime = DateUtil.getDifferenceTime(todayTime);
        return usertotalTime;
    }

    private void initCardsAdapter() {
        adminCardsAdapter = new AdminCardsAdapter(getActivity(), mActivityBridge.getUApplication().getDbBridge().getAllCards());
        adminList.setAdapter(adminCardsAdapter);
    }

    private void logout() {
        mActivityBridge.getUApplication().getDbBridge().clearAllData();
        mActivityBridge.getUApplication().getSharedHelper().sharedHelperClear();
        mActivityBridge.getUApplication().getNetBridge().userLogout();
        mActivityBridge.getFragmentLauncher().launchWelcomeFragment();
    }

    private void getUsersFromDb() {
        List<User> users = mActivityBridge.getUApplication().getDbBridge().getAllUsers();
        if (users.size() <= 0) {
            adminUsersAdapter.swapDataList(users);
        }
    }

    private void selectedItemUser(User user) {
        mActivityBridge.getFragmentLauncher().launchStatisticsFragment(user.getObjectId());
    }

    private void selectedItemCard(Card card) {
        Bundle bundle = CreateAndEditCardFragment.buildArgs(card.getIdCard());
        mActivityBridge.getFragmentLauncher().launchCreateAndEditCardFragment(bundle);
    }

    @Override
    protected boolean isBackButtonVisible() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_tutorial:
                mActivityBridge.getFragmentLauncher().launchTutorialFragment();
                break;

            case R.id.action_sync:
                startSync();
                break;

            case R.id.action_log_out:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (observer != null) {
            mActivityBridge.getUApplication().getDbBridge().removeObserver(observer);
            observer = null;
        }
    }

    private enum TabState {
        USERS, CARDS
    }

    private final class NetCallBack extends SimpleMainCallBack {
        @Override
        public void onError(String error) {
            hideProgressDialog();
            showShortToast(error);
        }

    }

    private final class DbObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            if (state == TabState.CARDS) {
                initCardsAdapter();
            } else {
                initUsersAdapter();
            }
        }
    }

    private final class ItemClicker implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (state == TabState.USERS) {
                User user = (User) parent.getItemAtPosition(position);
                selectedItemUser(user);
            } else {
                Card card = (Card) parent.getItemAtPosition(position);
                selectedItemCard(card);
            }

        }
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_card_float_button:
                    mActivityBridge.getFragmentLauncher().launchCreateAndEditCardFragment(Bundle.EMPTY);
                    break;
            }

        }
    }

    private final class RadioListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.cards_radio_button:
                    state = TabState.CARDS;
                    showVisibility();
                    initCardsAdapter();
                    break;

                case R.id.users_name_radio_button:
                    state = TabState.USERS;
                    showVisibility();
                    initUsersAdapter();
                    break;
            }
        }
    }
}
