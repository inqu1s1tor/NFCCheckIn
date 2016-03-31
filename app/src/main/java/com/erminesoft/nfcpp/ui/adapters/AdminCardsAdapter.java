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

/**
 * Created by Aleks on 31.03.2016.
 */
public class AdminCardsAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private List<RealmCard> cards;

    public AdminCardsAdapter(Context context, List<RealmCard> cards) {
        this.cards = cards;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public User getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getObjectId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Card card = getItem(position);
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

        holder.idCard.setText(card.getLastName());
        holder.cardName.setText(card.getFirstName());
        holder.cardDescription.setText(card.get);

        return convertView;
    }

    private static final class Holder {
        TextView idCard;
        TextView cardName;
        TextView cardDescription;
    }

    public void swapDataList(List<RealmCard> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }
}

