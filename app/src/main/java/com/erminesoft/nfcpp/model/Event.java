package com.erminesoft.nfcpp.model;

import java.util.Date;

import io.realm.RealmObject;

public class Event extends RealmObject {

    private String objectId;
    private Date created;
    private String idCard;
    private boolean isSent;
    private Date creationTime;

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getObjectId() {
        return objectId;
    }

    public Date getCreated() {
        return created;
    }

    public String getIdCard() {
        return idCard;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public void setIsSent(boolean sent) {
        this.isSent = sent;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}


