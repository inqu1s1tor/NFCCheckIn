package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.backendless.BackendlessUser;
import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Day;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.ui.adapters.DayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Aleks on 15.03.2016.
 */
public class StatisticsFragment extends GenericFragment {

    private ListView statisticsListView;
    private List<Day> dayList;
    private DayAdapter dayAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statisticsListView = (ListView) view.findViewById(R.id.statisticsList);

        getMyEvents();
        dayList = new ArrayList<>();

        dayAdapter = new DayAdapter(getActivity(), dayList);
        statisticsListView.setAdapter(dayAdapter);
    }

    private void getMyEvents() {
        String myId = mActivityBridge.getUApplication().getDbBridge().getMyUser().getObjectId().toString();
        mActivityBridge.getUApplication().getNetBridge().getAllEvents(myId, new NetCallback());
    }

    private void handleList(List<Event> eventList) {
        String dayNumber = "";
        Day day = null;

        for (int i = 0; i < eventList.size(); i++) {
            String curDay = new SimpleDateFormat("dd").format(eventList.get(i).getCreated());
            Log.d("Element list", "curDay = " + curDay);
            Log.d("Element list", "IdCard = " + eventList.get(i).getIdCard() + "    Created = " + eventList.get(i).getCreated());

            if (dayNumber.equals(curDay)) {
                day.addNewChecking(eventList.get(i).getCreated());
            } else {
                if (!dayNumber.equals("")) {
                    dayList.add(day);
                }
                day = new Day();
                day.setCurrentDate(new SimpleDateFormat("dd.MM.yyyy").format(eventList.get(i).getCreated()));
                day.addNewChecking(eventList.get(i).getCreated());
                dayNumber = curDay;
            }
        }

        dayList.add(day);
        Log.d("handleList", "dayList.size() =  " + dayList.size());

//        for (Day d : dayList) {
//            Log.d("handleList", "Date=" + d.getCheckingList().toString());
//        }

        dayAdapter.replaceNewData(dayList);
    }

    private final class NetCallback extends SimpleMainCallBack {

        @Override
        public void onSuccessGetEvents(List<Event> eventList) {
            Log.d("NetCallBack", "On");
            handleList(eventList);
        }
    }

}
