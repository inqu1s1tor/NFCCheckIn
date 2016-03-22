package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.EventsToday;

import java.util.List;

/**
 * Created by Evgen on 22.03.2016.
 */
public class EventAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private List<EventsToday> eventsTodayList;
    private int redColor;
    private int greenColor;

    public EventAdapter(Context context, List<EventsToday> eventsTodayList) {
        this.eventsTodayList = eventsTodayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        redColor = context.getResources().getColor(R.color.colorRed);
        greenColor = context.getResources().getColor(R.color.colorGreen);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventsToday eventsToday = getEventItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_event_list, parent, false);
            holder.entryTextView = (TextView) convertView.findViewById(R.id.ad_event_list_entry);
            holder.exitTextView = (TextView) convertView.findViewById(R.id.ad_event_list_exit);
            holder.totalHoursTextView = (TextView) convertView.findViewById(R.id.ad_event_list_total_hours);
            holder.selectorTextView = (TextView) convertView.findViewById(R.id.ad_event_list_selector);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (eventsToday != null) {
            holder.entryTextView.setText(eventsToday.getEntry());
            holder.exitTextView.setText(eventsToday.getExit());
            holder.totalHoursTextView.setText("(" + eventsToday.getTotal_hours() + ")");

            holder.selectorTextView.setTextColor(eventsToday.isSelector() ? greenColor : redColor);
        } else {
            holder.entryTextView.setText("--:--");
            holder.exitTextView.setText("--:--");
        }

        return convertView;
    }

    public void replaceNewData(List<EventsToday> newEventsTodayList) {
        eventsTodayList = newEventsTodayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eventsTodayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public EventsToday getEventItem(int position) {
        return eventsTodayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static final class Holder {
        TextView entryTextView;
        TextView exitTextView;
        TextView totalHoursTextView;
        TextView selectorTextView;
    }
}
