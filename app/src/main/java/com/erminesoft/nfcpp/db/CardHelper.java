package com.erminesoft.nfcpp.db;

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

}
