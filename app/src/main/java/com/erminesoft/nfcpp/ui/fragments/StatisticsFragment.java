package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

import java.text.ParseException;
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
    private TextView statDate;
    private List<DayStatistics> dayList;
    private List<RealmEvent> realmEventList;
    private StatisticsAdapter statisticsAdapter;
    private String objectUserId = null;
    private String curDateMonth = null;

    private final static String OBJECT_ID = "object_id";


    public static Bundle buildArguments(String objectId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(OBJECT_ID, objectId);
        return bundle;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changeStateOfBackButton();

        statisticsListView = (ListView) view.findViewById(R.id.statisticsList);
        statDate = (TextView) view.findViewById(R.id.stat_textview_date);

        dayList = new ArrayList<>();
        statisticsAdapter = new StatisticsAdapter(getActivity(), dayList);
        statisticsListView.setAdapter(statisticsAdapter);
        statisticsListView.setOnItemClickListener(new ItemClicker());

        View.OnClickListener listener = new Clicker();
        view.findViewById(R.id.stat_button_month_down).setOnClickListener(listener);
        view.findViewById(R.id.stat_button_month_up).setOnClickListener(listener);

        Bundle bundle = getArguments();
        if (bundle != null) {
            objectUserId = (String) bundle.getSerializable(OBJECT_ID);
        }

        getEvents();
    }


    private void getEvents() {
        if (curDateMonth == null) {
            long curTime = System.currentTimeMillis();
            curDateMonth = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M).format(curTime);
        }
        statDate.setText(curDateMonth);
        if (objectUserId != null) {
            realmEventList = mActivityBridge.getUApplication().getDbBridge().getEventsByIdPerMonth(objectUserId, curDateMonth);
            handleList(realmEventList);
        } else {
            realmEventList = mActivityBridge.getUApplication().getDbBridge().getEventsByMonth(curDateMonth);
            handleList(realmEventList);
        }


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


    private void selectedItem(DayStatistics dayStatistics) {
        Bundle bundle = DetailStatisticsFragment.buildArguments(dayStatistics.getDate(), objectUserId);
        mActivityBridge.getFragmentLauncher().launchDetailStatisticsFragment(bundle);
    }

    private void getDateMonthSelected(boolean isPrevious) {
        try {
            dayList.clear();
            curDateMonth = DateUtil.getDateMonthSelected(curDateMonth, isPrevious);
            statDate.setText(curDateMonth);
            getEvents();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void changeStateOfBackButton() {
        mActivityBridge.switchBackButtonVisibility(true);
    }

    private final class ItemClicker implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DayStatistics dayStatistics = (DayStatistics) parent.getItemAtPosition(position);
            selectedItem(dayStatistics);
        }
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.stat_button_month_down:
                    getDateMonthSelected(true);
                    break;

                case R.id.stat_button_month_up:
                    getDateMonthSelected(false);
                    break;
            }
        }
    }

}
