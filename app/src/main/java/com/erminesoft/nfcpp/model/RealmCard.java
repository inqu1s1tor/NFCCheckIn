package com.erminesoft.nfcpp.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmCard extends RealmObject {

    @PrimaryKey
    private String idCard;
    private String nameCard;
    private String descriptionCard;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getDescriptionCard() {
        return descriptionCard;
    }

    public void setDescriptionCard(String descriptionCard) {
        this.descriptionCard = descriptionCard;
    }
}
