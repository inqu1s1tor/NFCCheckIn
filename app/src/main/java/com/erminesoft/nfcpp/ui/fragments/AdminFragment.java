package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.User;
import com.erminesoft.nfcpp.ui.adapters.AdminAdapter;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminList = (ListView)view.findViewById(R.id.adminList);
        mActivityBridge.getUApplication().getNetBridge().getAllUsers(new NetCallBack(), "");

//        getUsersFromDb();
//        AdminAdapter adminAdapter = new AdminAdapter(getActivity(), (ArrayList<User>) mActivityBridge.getUApplication().getDbBridge().getAllUsers());

//        adminList.setAdapter(adminAdapter);

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

    private void getUsersFromDb() {
        List<User> users  = mActivityBridge.getUApplication().getDbBridge().getAllUsers();
        Log.d("getUsersFromDb", "users.size() = " + users.size());
    }

    private final class NetCallBack extends SimpleMainCallBack {
        @Override
        public void onSuccess() {
            Log.d("myLog", "save success");
        }
    }

    private final class DbObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            Log.e("FM", "update");
            getUsersFromDb();
//            hideProgressDialog();
        }


    }
}
