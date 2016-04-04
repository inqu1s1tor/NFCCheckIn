package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.erminesoft.nfcpp.model.RealmCard;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.ui.adapters.AdminCardsAdapter;
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
    private AdminUsersAdapter adminUsersAdapter;
    private AdminCardsAdapter adminCardsAdapter;
    private Menu menu;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin_setting_menu, menu);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changeStateOfBackButton();

        adminList = (ListView) view.findViewById(R.id.adminList);
        adminList.setEmptyView(view.findViewById(R.id.empty_list_item_admin));
        userNameColumn = (TextView)view.findViewById(R.id.user_name_column_textView);
        totalTimeColumn = (TextView)view.findViewById(R.id.total_time_column_textView);

        mActivityBridge.getUApplication().getNetBridge().getAllUsers(new NetCallBack(), "");
        mActivityBridge.getUApplication().getNetBridge().getAllCard(new NetCallBack());

        View.OnClickListener listener = new Clicker();
        addCardBtn = (FloatingActionButton) view.findViewById(R.id.add_card_float_button);
        addCardBtn.setOnClickListener(listener);

        mActivityBridge.getUApplication().getNetBridge().getAllEvents(new NetCallBack());
        setHasOptionsMenu(true);

        radioGroup = (RadioGroup) view.findViewById(R.id.admin_list_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioListener());

        radioGroup.check(R.id.users_name_radio_button);

        AdapterView.OnItemClickListener itemClicker = new ItemClicker();
        adminList.setOnItemClickListener(itemClicker);
    }

    private void showVisibility() {
        if (state == TabState.USERS) {
            addCardBtn.setVisibility(View.GONE);
            userNameColumn.setText(getActivity().getResources().getString(R.string.user_name_list_column_tv));
            totalTimeColumn.setText(getActivity().getResources().getString(R.string.user_total_time_list_column));
        }else {
            userNameColumn.setText(R.string.card_column_id);
            totalTimeColumn.setText(R.string.card_name_column);
            addCardBtn.setVisibility(View.VISIBLE);
            }
    }

    private void initUsersAdapter() {
        adminUsersAdapter = new AdminUsersAdapter(getActivity(), mActivityBridge.getUApplication().getDbBridge().getAllUsers());
        adminList.setAdapter(adminUsersAdapter);
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

    @Override
    public void onStart() {
        super.onStart();

        if (observer == null) {
            observer = new DbObserver();
        }
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
        if (users.size() <= 0) {
            adminUsersAdapter.swapDataList(users);
        }
    }

    private void selectedItemUser(User user) {
        mActivityBridge.getFragmentLauncher().launchStatisticsFragment(user.getObjectId());
    }

    private void selectedItemCard(RealmCard realmCard) {
        Bundle bundle = CreateAndEditCardFragment.buildArgs(realmCard.getIdCard());
        mActivityBridge.getFragmentLauncher().launchCreateAndEditCardFragment(bundle);
    }

    @Override
    protected void changeStateOfBackButton() {
        mActivityBridge.switchBackButtonVisibility(false);
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
            if(state == TabState.CARDS) {
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
                RealmCard realmCard = (RealmCard) parent.getItemAtPosition(position);
                selectedItemCard(realmCard);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    private enum TabState {
        USERS, CARDS
    }
}
