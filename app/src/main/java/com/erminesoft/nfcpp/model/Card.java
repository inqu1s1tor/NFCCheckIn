package com.erminesoft.nfcpp.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "CARD")
public class Card extends Model {

    @Column(name = "card_id", unique = true, index = true)
    private String idCard;

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "card_name")
    private String nameCard;

    @Column(name = "card_description")
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
