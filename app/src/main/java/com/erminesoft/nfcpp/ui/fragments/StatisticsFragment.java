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
import com.erminesoft.nfcpp.model.DayStatistics;
import com.erminesoft.nfcpp.model.EventsToday;
import com.erminesoft.nfcpp.model.RealmEvent;
import com.erminesoft.nfcpp.ui.adapters.DayAdapter;
import com.erminesoft.nfcpp.ui.adapters.StatisticsAdapter;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.SortUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Created by Aleks on 15.03.2016.
 */
public class StatisticsFragment extends GenericFragment {

    private ListView statisticsListView;
    private List<DayStatistics> dayList;
    private StatisticsAdapter statisticsAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statisticsListView = (ListView) view.findViewById(R.id.statisticsList);

        dayList = new ArrayList<>();
        statisticsAdapter = new StatisticsAdapter(getActivity(), dayList);
        statisticsListView.setAdapter(statisticsAdapter);

        getMyEvents();
    }

    private void getMyEvents() {
        Log.d("getMyEvents", "getMyEvents");
        long curTime = System.currentTimeMillis();
        String curStringDate = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M_D).format(curTime);
        List<RealmEvent> realmEventList = mActivityBridge.getUApplication().getDbBridge().getEventsByMonth(curStringDate);
//        for (RealmEvent rl : realmEventList) {
//            Log.d("getMyEvents", "rl.getIsSent()=" + rl.getIsSent() + "     getCreationTime=" + rl.getCreationTime() + "   getObjectId=" + rl.getObjectId());
//        }
        handleList(realmEventList);
    }


    private void handleList(List<RealmEvent> realmEventList) {
        String dayNumber = "";
        DayStatistics dayStatistics = null;
        List<Long> creationTimeList = new ArrayList<>();

        for (int i = 0; i < realmEventList.size(); i++) {
            long creationTime = realmEventList.get(i).getCreationTime();
            String curDay = DateUtil.dateToFormatString(creationTime * (long) 1000, DateUtil.DATE_FORMAT_D);

            if (dayNumber.equals(curDay)) {
                creationTimeList.add(creationTime);
                if (i == realmEventList.size() - 1) {
                    long diffInMs = SortUtil.sortEventsAndReturnTotalWorkingTime(creationTimeList);
                    dayStatistics.setTotalTime(DateUtil.getDifferenceTime(diffInMs));
                    dayList.add(dayStatistics);
                }

            } else {
                if (!dayNumber.equals("")) {
                    long diffInMs = SortUtil.sortEventsAndReturnTotalWorkingTime(creationTimeList);
                    dayStatistics.setTotalTime(DateUtil.getDifferenceTime(diffInMs));
                    dayList.add(dayStatistics);
                    creationTimeList = new ArrayList<>();
                }
                dayStatistics = new DayStatistics();
                dayStatistics.setDate(DateUtil.dateToFormatString(creationTime * (long) 1000, DateUtil.DATE_FORMAT_Y_M_D));

                creationTimeList.add(creationTime);
                dayNumber = curDay;
            }
        }

        statisticsAdapter.replaceNewData(dayList);
    }



    private final class NetCallback extends SimpleMainCallBack {

    }

}
