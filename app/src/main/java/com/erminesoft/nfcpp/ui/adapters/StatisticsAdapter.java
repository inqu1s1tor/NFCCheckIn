package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.DayStatistics;

import java.util.List;

/**
 * Created by Evgen on 24.03.2016.
 */
public class StatisticsAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<DayStatistics> objectsDays;


    public StatisticsAdapter(Context context, List<DayStatistics> objectsDays) {
        this.objectsDays = objectsDays;
        mLayoutInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayStatistics dayStatistics = getItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_statistics_list, parent, false);
            holder.dateTv = (TextView) convertView.findViewById(R.id.asDate);
            holder.totalTimeTv = (TextView) convertView.findViewById(R.id.asTotalTime);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (dayStatistics != null) {
            holder.dateTv.setText(dayStatistics.getDate());
            holder.totalTimeTv.setText("(" + dayStatistics.getTotalTime() + ")");
        }

        return convertView;
    }


    @Override
    public int getCount() {
        return objectsDays.size();
    }

    @Override
    public DayStatistics getItem(int position) {
        return objectsDays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    public void replaceNewData(List<DayStatistics> newObjects) {
        objectsDays = newObjects;
        notifyDataSetChanged();
    }

    private static final class Holder {
        TextView dateTv;
        TextView totalTimeTv;

    }
}
