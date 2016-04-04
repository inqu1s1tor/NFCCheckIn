package com.erminesoft.nfcpp.db;

import com.erminesoft.nfcpp.model.Card;
import com.erminesoft.nfcpp.model.RealmCard;
import java.util.List;
import io.realm.Realm;


final class CardHelper {

    void saveCard(Realm realm, List<RealmCard> realmCards) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmCards);
        realm.commitTransaction();
        realm.close();
    }

    void saveCard(Realm realm, RealmCard realmCard) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(realmCard);
        realm.commitTransaction();
        realm.close();
    }

    List<RealmCard> getAllCards(Realm realm) {
        return realm.where(RealmCard.class).findAll();
    }

    RealmCard getCardById(Realm realm, String cardId){
        return realm.where(RealmCard.class).equalTo("idCard", cardId).findFirst();
    }


    void clearAllCards(Realm realm) {
        realm.beginTransaction();
        realm.where(RealmCard.class).findAll().clear();
        realm.commitTransaction();
    }

}
