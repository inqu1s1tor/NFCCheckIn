package com.erminesoft.nfcpp.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmEvent extends RealmObject {

    private String objectId;
    private Date created;
    //    private Date updated;
    private String ownerId;
    private String idCard;
    private boolean isSent;

    @PrimaryKey
    private int creationTime;

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getCreated() {
        return created;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(boolean sent) {
        this.isSent = sent;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(int creationTime) {
        this.creationTime = creationTime;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

}


