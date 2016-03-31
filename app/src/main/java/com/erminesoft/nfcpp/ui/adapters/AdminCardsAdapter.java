package com.erminesoft.nfcpp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.erminesoft.nfcpp.R;
import com.erminesoft.nfcpp.model.RealmCard;
import com.erminesoft.nfcpp.model.User;

import java.util.List;

/**
 * Created by Aleks on 31.03.2016.
 */
public class AdminCardsAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private List<RealmCard> realmCards;

    public AdminCardsAdapter(Context context, List<RealmCard> realmCards) {
        this.realmCards = realmCards;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return realmCards.size();
    }

    @Override
    public RealmCard getItem(int position) {
        return realmCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RealmCard realmCard = getItem(position);
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.item_admin_list_adapter_cards, parent, false);
            holder.idCard = (TextView) convertView.findViewById(R.id.card_id);
            holder.cardName = (TextView) convertView.findViewById(R.id.card_name);
            holder.cardDescription = (TextView) convertView.findViewById(R.id.description_card);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.idCard.setText(realmCard.getIdCard());
        holder.cardName.setText(realmCard.getNameCard());
        holder.cardDescription.setText(realmCard.getDescriptionCard());

        return convertView;
    }

    private static final class Holder {
        TextView idCard;
        TextView cardName;
        TextView cardDescription;
    }

    public void swapDataList(List<RealmCard> realmCards) {
        this.realmCards = realmCards;
        notifyDataSetChanged();
    }
}

