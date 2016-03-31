package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.User;

import java.util.List;

public class AdminUsersAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private List<User> users;

    public AdminUsersAdapter(Context context, List<User> users) {
        this.users = users;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getObjectId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_admin_list_adapter_users, parent, false);
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

    public void swapDataList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
}
