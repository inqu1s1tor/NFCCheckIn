package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleks on 22.03.2016.
 */
public class AdminAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<User> object;
    Context ctx;

    public AdminAdapter(Context context, ArrayList<User> users) {
        ctx = context;
        object = users;
        mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return object.size();
    }

    @Override
    public User getItem(int position) {
        return object.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        User user = getItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_admin_list, parent, false);
            holder.firstNameTv = (TextView) convertView.findViewById(R.id.firstNameAdminModeTv);
            holder.lastNameTv = (TextView) convertView.findViewById(R.id.lastNameAdminModeTv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Log.d("adapter", "*day=" + user);
        if (user != null) {
            holder.lastNameTv.setText(user.getLastName().toString());
            holder.firstNameTv.setText(user.getFirstName().toString());
        } else {
            holder.lastNameTv.setText("no data");
        }

        return convertView;
    }

    private final class Holder {
        TextView firstNameTv;
        TextView lastNameTv;
    }
}
