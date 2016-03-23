package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.core.callback.SimpleMainCallBack;
import com.erminesoft.nfcpp.model.Day;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.ui.adapters.DayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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

    private void handleList(List<RealmEvent> realmEventList) {
        String dayNumber = "";
        Day day = null;

            for (int i = 0; i < realmEventList.size(); i++) {
                String curDay = new SimpleDateFormat("dd").format(realmEventList.get(i).getCreated());
                Log.d("Element list", "curDay = " + curDay);
                Log.d("Element list", "IdCard = " + realmEventList.get(i).getIdCard() + "    Created = " + realmEventList.get(i).getCreated());

                if (dayNumber.equals(curDay)) {
                    day.addNewChecking(realmEventList.get(i).getCreated());
                } else {
                    if (!dayNumber.equals("")) {
                        dayList.add(day);
                    }
                    day = new Day();
                    day.setCurrentDate(new SimpleDateFormat("dd.MM.yyyy").format(realmEventList.get(i).getCreated()));
                    day.addNewChecking(realmEventList.get(i).getCreated());
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
        public void onSuccessGetEvents(List<RealmEvent> realmEventList) {
            Log.d("NetCallBack", "On");
            Collections.sort(realmEventList, new Comparator<RealmEvent>() {
                @Override
                public int compare(RealmEvent lhs, RealmEvent rhs) { // rhs  lhs
                    return lhs.getCreated().compareTo(rhs.getCreated());
                }
            });
            handleList(realmEventList);
        }
    }

}
