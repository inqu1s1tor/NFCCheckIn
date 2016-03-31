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
import com.erminesoft.nfcpp.model.RealmCard;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.ui.adapters.EventAdapter;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.SortUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


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

        dateTv.setText(date);

        initAdapter();
        getEventsFromDb(date);
    }

    private void initAdapter(){
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity(), eventsList);
        eventsListView.setAdapter(eventAdapter);
    }

    private void getEventsFromDb(String dateString) {
        List<RealmEvent> realmEventList = mActivityBridge.getUApplication().getDbBridge().getEventsByDate(dateString);
        List<RealmCard> realmCardList = mActivityBridge.getUApplication().getDbBridge().getAllCards();
        loadTodayEventsList(realmEventList, realmCardList);
    }

    private void loadTodayEventsList(List<RealmEvent> realmEventList, List<RealmCard> realmCardList) {
        if (realmEventList.size() <= 0) {
            return;
        }

        eventsList.clear();
        long diffInMs = SortUtil.sortEventsOnTodayAndReturnTotalWorkingTime(realmEventList, realmCardList,  eventsList, false);
        totalTime.setText(DateUtil.getDifferenceTime(diffInMs));
        eventAdapter.replaceNewData(eventsList);
    }
}
