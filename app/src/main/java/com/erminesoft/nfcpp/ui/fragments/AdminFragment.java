package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.ui.adapters.AdminAdapter;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aleks on 18.03.2016.
 */
public class AdminFragment extends GenericFragment {

    private ListView adminList;
    private Observer observer;
    private AdminAdapter adminAdapter;


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

        mActivityBridge.getUApplication().getNetBridge().getAllUsers(new NetCallBack(), "");

        adminAdapter = new AdminAdapter(getActivity(), mActivityBridge.getUApplication().getDbBridge().getAllUsers());
        adminList.setAdapter(adminAdapter);

        AdapterView.OnItemClickListener listener = new ItemClicker();
        adminList.setOnItemClickListener(listener);

        mActivityBridge.getUApplication().getNetBridge().getAllEvents(new NetCallBack());

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
}
