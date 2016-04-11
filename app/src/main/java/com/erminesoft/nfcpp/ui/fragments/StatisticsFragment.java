package com.erminesoft.nfcpp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.DayStatistics;
import com.erminesoft.nfcpp.model.Event;
import com.erminesoft.nfcpp.ui.adapters.StatisticsAdapter;
import com.erminesoft.nfcpp.util.DateUtil;
import com.erminesoft.nfcpp.util.SortUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Aleks on 15.03.2016.
 */
public class StatisticsFragment extends GenericFragment {

    private final static String OBJECT_ID = "object_id";
    private ListView statisticsListView;
    private TextView statDate;
    private List<DayStatistics> dayList;
    private List<Event> eventList;
    private StatisticsAdapter statisticsAdapter;
    private String objectUserId = null;
    private String curDateMonth = null;

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
            curDateMonth = new SimpleDateFormat(DateUtil.DATE_FORMAT_Y_M, Locale.getDefault()).format(curTime);
        }
        statDate.setText(curDateMonth);
        if (objectUserId != null) {
            eventList = mActivityBridge.getUApplication().getDbBridge().getEventsByIdPerMonth(objectUserId, curDateMonth);
            handleList(eventList);
        } else {
            eventList = mActivityBridge.getUApplication().getDbBridge().getEventsByMonth(curDateMonth);
            handleList(eventList);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void handleList(List<Event> eventList) {
        String dayNumber = "";
        DayStatistics dayStatistics = null;
        List<Long> creationTimeList = new ArrayList<>();

        for (int i = 0; i < eventList.size(); i++) {
            long creationTime = eventList.get(i).getCreationTime();
            String curDay = DateUtil.dateToFormatString(creationTime * (long) 1000, DateUtil.DATE_FORMAT_D);

            if (TextUtils.isEmpty(dayNumber)) {
                dayStatistics = new DayStatistics();
                dayStatistics.setDate(DateUtil.dateToFormatString(creationTime * (long) 1000, DateUtil.DATE_FORMAT_Y_M_D));

                creationTimeList.add(creationTime);
                dayNumber = curDay;
            } else if (dayNumber.equals(curDay)) {
                creationTimeList.add(creationTime);
                if (i == eventList.size() - 1) {
                    long diffInMs = SortUtil.sortEventsAndReturnTotalWorkingTime(creationTimeList);
                    dayStatistics.setTotalTime(DateUtil.getDifferenceTime(diffInMs));
                    dayList.add(dayStatistics);
                }
            } else {
                long diffInMs = SortUtil.sortEventsAndReturnTotalWorkingTime(creationTimeList);
                dayStatistics.setTotalTime(DateUtil.getDifferenceTime(diffInMs));
                dayList.add(dayStatistics);
                creationTimeList.clear();
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
    protected boolean isBackButtonVisible() {
        return true;
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
