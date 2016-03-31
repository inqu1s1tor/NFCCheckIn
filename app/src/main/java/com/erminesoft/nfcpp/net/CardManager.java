package com.erminesoft.nfcpp.net;


import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.RealmCard;

import java.util.ArrayList;
import java.util.List;

public class CardManager {

    private DbBridge dbBridge;

    public CardManager(DbBridge dbBridge){
        this.dbBridge = dbBridge;
    }

    void addNewCard(RealmCard realmCard, final MainCallBack callback) {
        Card card = CardConverter.realmCardToClearCard(realmCard);
        Backendless.Persistence.save(card, new AsyncCallback<Card>() {
            @Override
            public void handleResponse(Card response) {
                Log.d("addNewCard", "response = " + response.getNameCard());
                 // TODO  permision
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("addNewCard", "fault = " + fault.toString());
                callback.onError(fault.toString());
            }
        });
    }


    void getAllCard(final MainCallBack callBack){
        Backendless.Data.of(Card.class).find(new AsyncCallback<BackendlessCollection<Card>>() {
            @Override
            public void handleResponse(BackendlessCollection<Card> response) {
                Log.d("getAllCard", "response");
                List<Card> cards = response.getData();
                List<RealmCard> realmCards = new ArrayList<RealmCard>(cards.size());
                for (Card card : cards) {
                    Log.d("getAllCard", "card.getNameCard() = "+card.getNameCard());
                    realmCards.add(CardConverter.clearCardToRealmCard(card));
                }
                dbBridge.saveCard(realmCards);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("getAllCard", "fault = " + fault.toString());
                callBack.onError(fault.toString());
            }
        });
    }




    static final class CardConverter {

        static Card realmCardToClearCard(RealmCard realmCard) {
            Log.d("realmCardToClear", "realmCard.getIdCard() = " + realmCard.getIdCard());
            Card clearCard = new Card();
            clearCard.setIdCard(realmCard.getIdCard());
            clearCard.setDescriptionCard(realmCard.getDescriptionCard());
            clearCard.setNameCard(realmCard.getNameCard());
            return clearCard;
        }

        static RealmCard clearCardToRealmCard(Card card) {
            Log.d("clearCardToRealm", "card.getIdCard() = " + card.getIdCard());
            RealmCard realmCard = new RealmCard();
            realmCard.setIdCard(card.getIdCard());
            realmCard.setDescriptionCard(card.getDescriptionCard());
            realmCard.setNameCard(card.getNameCard());
            return realmCard;
        }
    }
}
