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
import com.erminesoft.nfcpp.model.EventsToday;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.ui.adapters.EventAdapter;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.SortUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgen on 24.03.2016.
 */
public class DetailStatisticsFragment extends GenericFragment {

    private final static String DATE_ID = "date_id";
    private TextView dateTv;
    private TextView totalTime;
    private ListView eventsListView;
    private List<EventsToday> eventsList;
    private EventAdapter eventAdapter;
    private String date;


    public static Bundle buildArguments(String date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATE_ID, date);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_statistcs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTv = (TextView) view.findViewById(R.id.afdsDate);
        totalTime = (TextView) view.findViewById(R.id.afdsTotalTime);
        eventsListView = (ListView) view.findViewById(R.id.afdsList);

        Bundle bundle = getArguments();
        if (bundle != null) {
            date = (String) bundle.getSerializable(DATE_ID);
        }

        Log.d("", "date = " + date);
        dateTv.setText(date);

        initAdapter();
        getEventsFromDb();
    }

    private void initAdapter(){
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity(), eventsList);
        eventsListView.setAdapter(eventAdapter);
    }

    private void getEventsFromDb() {
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D).format(curTime);
        List<RealmEvent> realmEventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDate(curStringDate);
        loadTodayEventsList(realmEventList);
    }

    private void loadTodayEventsList(List<RealmEvent> realmEventList) {
        if (realmEventList.size() <= 0) {
            return;
        }

        eventsList.clear();
        long diffInMs = SortUtil.sortEventsOnTodayAndReturnTotalWorkingTime(realmEventList, eventsList);
        totalTime.setText(DateUtil.getDifferenceTime(diffInMs));
        eventAdapter.replaceNewData(eventsList);
    }
}
