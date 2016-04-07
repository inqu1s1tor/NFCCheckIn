package com.erminesoft.nfcpp.net;


import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.erminesoft.nfcpp.core.bridge.DbBridge;
import com.erminesoft.nfcpp.core.callback.MainCallBack;
import com.erminesoft.nfcpp.model.Card;

final class CardManager {

    private DbBridge dbBridge;

    CardManager(DbBridge dbBridge) {
        this.dbBridge = dbBridge;
    }

    void addNewCard(Card card, final MainCallBack callback) {
        Backendless.Persistence.save(card, new AsyncCallback<Card>() {
            @Override
            public void handleResponse(Card response) {
                callback.onSuccess();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("addNewCard", "fault = " + fault.toString());
                callback.onError(fault.toString());
            }
        });
    }


    void getAllCard(final MainCallBack callBack) {
        Backendless.Data.of(Card.class).find(new AsyncCallback<BackendlessCollection<Card>>() {
            @Override
            public void handleResponse(BackendlessCollection<Card> response) {
                dbBridge.saveCards(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("getAllCard", "fault = " + fault.toString());
                callBack.onError(fault.toString());
            }
        });
    }
}
