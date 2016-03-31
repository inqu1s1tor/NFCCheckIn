package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.ui.adapters.AdminUsersAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aleks on 18.03.2016.
 */
public class AdminFragment extends GenericFragment {

    private ListView adminList;
    private Observer observer;
    private AdminUsersAdapter adminAdapter;
    private Menu menu;
    private RadioGroup radioGroup;
    private TabState state;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin_setting_menu, menu);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminList = (ListView) view.findViewById(R.id.adminList);
        adminList.setEmptyView(view.findViewById(R.id.empty_list_item_admin));

        mActivityBridge.getUApplication().getNetBridge().getAllUsers(new NetCallBack(), "");



        AdapterView.OnItemClickListener itemClicker = new ItemClicker();
        adminList.setOnItemClickListener(itemClicker);
        View.OnClickListener listener = new Clicker();
        mActivityBridge.getUApplication().getNetBridge().getAllEvents(new NetCallBack());
        setHasOptionsMenu(true);

        radioGroup = (RadioGroup)view.findViewById(R.id.admin_list_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioListener());

        radioGroup.check(R.id.users_name_radio_button);

    }

    private void initUsersAdapter() {
        adminAdapter = new AdminUsersAdapter(getActivity(), mActivityBridge.getUApplication().getDbBridge().getAllUsers());
        adminList.setAdapter(adminAdapter);
    }


    private void logout() {
        mActivityBridge.getUApplication().getDbBridge().clearAllData();
        mActivityBridge.getUApplication().getSharedHelper().sharedHelperClear();
        mActivityBridge.getUApplication().getNetBridge().userLogout();
        mActivityBridge.getFragmentLauncher().launchWelcomeFragment();
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
        if (observer != null) {
            mActivityBridge.getUApplication().getDbBridge().removeObserver(observer);
            observer = null;
        }
    }

    private void getUsersFromDb() {
        List<User> users = mActivityBridge.getUApplication().getDbBridge().getAllUsers();
        adminAdapter.swapDataList(users);
    }

    private void selectedItem(User user) {
        mActivityBridge.getFragmentLauncher().launchStatisticsFragment(user.getObjectId());
    }

    private final class NetCallBack extends SimpleMainCallBack {
        @Override
        public void onError(String error) {
            showShortToast(error);
        }

    }

    private final class DbObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            getUsersFromDb();
        }
    }


    private final class ItemClicker implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            User user = (User) parent.getItemAtPosition(position);
            selectedItem(user);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_sync:
                break;

            case R.id.action_log_out:
                logout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            }

        }
    }

    private final class RadioListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.cards_radio_button:
                    state = TabState.USERS;
                    initUsersAdapter();
                    break;

                case R.id.users_name_radio_button:
                    state = TabState.CARDS;
                    break;
            }
        }
    }

    private enum TabState {
        USERS, CARDS
    }
}
