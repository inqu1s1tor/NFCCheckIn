package com.erminesoft.nfcpp.db;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.erminesoft.nfcpp.model.Card;

import java.util.List;

final class CardHelper {

    void saveCard(List<Card> cards) {
        ActiveAndroid.beginTransaction();
        try {

            for (Card card : cards) {
                card.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    void saveCard(Card card) {
        card.save();
    }

    List<Card> getAllCards() {
        return new Select().from(Card.class).execute();
    }

    Card getCardById(String cardId) {
        Card card = null;
        try {
            card = new Select().from(Card.class).where("card_id = ?", cardId).executeSingle();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return card;
    }

    void clearAllCards() {
        new Delete().from(Card.class).execute();
    }
}
