package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminAdapter extends ArrayAdapter<User> {

    private final LayoutInflater mLayoutInflater;
    private List<User> users;

    public AdminAdapter(Context context, List<User> users) {
        super(context, 0, users);
        mLayoutInflater = LayoutInflater.from(context);
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        holder.lastNameTv.setText(user.getLastName());
        holder.firstNameTv.setText(user.getFirstName());

        return convertView;
    }

    private static final class Holder {
        TextView firstNameTv;
        TextView lastNameTv;
    }
}
