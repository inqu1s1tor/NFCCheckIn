package com.erminesoft.nfcpp.model;


public class Card {

    private String idCard;
    private String objectId;
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

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
