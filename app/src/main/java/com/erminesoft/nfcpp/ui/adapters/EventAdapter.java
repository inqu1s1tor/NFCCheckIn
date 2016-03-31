package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
        mLayoutInflater = LayoutInflater.from(context);
        redColor = ContextCompat.getColor(context, R.color.colorRed);
        greenColor = ContextCompat.getColor(context, R.color.colorGreen);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventsToday eventsToday = getItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_event_list, parent, false);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.ad_event_name);
            holder.entryTextView = (TextView) convertView.findViewById(R.id.ad_event_list_entry);
            holder.exitTextView = (TextView) convertView.findViewById(R.id.ad_event_list_exit);
            holder.totalHoursTextView = (TextView) convertView.findViewById(R.id.ad_event_list_total_hours);
            holder.selectorTextView = (TextView) convertView.findViewById(R.id.ad_event_list_selector);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (eventsToday != null) {
            holder.nameTextView.setText(eventsToday.getNameCard());
            holder.entryTextView.setText(eventsToday.getEntry());
            holder.exitTextView.setText(eventsToday.getExit());
            holder.totalHoursTextView.setText("(" + eventsToday.getTotal_hours() + ")");

            holder.selectorTextView.setTextColor(eventsToday.isSelector() ? greenColor : redColor);
        } else {
            holder.nameTextView.setText("No data");
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
    public EventsToday getItem(int position) {
        return eventsTodayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    private static final class Holder {
        TextView nameTextView;
        TextView entryTextView;
        TextView exitTextView;
        TextView totalHoursTextView;
        TextView selectorTextView;
    }
}
