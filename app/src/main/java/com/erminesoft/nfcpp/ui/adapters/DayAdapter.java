package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.Day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aleks on 15.03.2016.
 */
public class DayAdapter extends BaseAdapter {

    LayoutInflater mLayoutInflater;
    Context ctx;
    List<Day> objects;

    public DayAdapter(Context context, List<Day> days) {
        ctx = context;
        objects = days;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Day getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Day day = getItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_day_list, parent, false);
            holder.dateTv = (TextView) convertView.findViewById(R.id.dateId);
            holder.entryTimeTv = (TextView) convertView.findViewById(R.id.entryTimeId);
            holder.exitTimeTv = (TextView) convertView.findViewById(R.id.exitTimeId);
            holder.totalTimeTv = (TextView) convertView.findViewById(R.id.totalTimeId);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.dateTv.setText(day.getCurrentDate().toString());

        holder.entryTimeTv.setText(getEntryTime(day));
        holder.exitTimeTv.setText(getExitTime(day));
        holder.totalTimeTv.setText(getTotal(day));

        return convertView;
    }

    public void replaceNewData(List<Day> newObjects) {
        objects = newObjects;
        notifyDataSetChanged();
    }

    private String getEntryTime(Day day) {
        String entryTime = "no data";
        int countEvent = day.getCheckingList().size();
        Date entry = day.getCheckingList().get(countEvent - 1);
        entryTime = new SimpleDateFormat("HH:mm").format(entry);
        return entryTime;
    }

    private String getExitTime(Day day) {
        String exitTime = "no data";
        Date exit = day.getCheckingList().get(0);
        exitTime = new SimpleDateFormat("HH:mm").format(exit);

        return exitTime;
    }

    private String getTotal(Day day) {
        String totalTime = "no data";

        if (day.getCheckingList().size() > 1){
            Date entry = day.getCheckingList().get(1);
            Date exit = day.getCheckingList().get(0);
            long diffInMs = exit.getTime() - entry.getTime();
            int hh = (int) (TimeUnit.MILLISECONDS.toHours(diffInMs) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(diffInMs)));
            int mm = (int) (TimeUnit.MILLISECONDS.toMinutes(diffInMs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diffInMs)));
            Log.d("handleList", "hh=" + hh + "   mm=" + mm);
            totalTime = hh + ":" + mm;
        }

        return totalTime;
    }

    private static final class Holder {
        TextView dateTv;
        TextView entryTimeTv;
        TextView exitTimeTv;
        TextView totalTimeTv;
    }

}

