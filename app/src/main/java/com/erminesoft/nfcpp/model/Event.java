package com.erminesoft.nfcpp.model;

import java.util.Date;

/**
 * Created by Evgen on 23.03.2016.
 */
public class Event {
    private String idCard;
    private boolean isSent;
    private Double creationTime;
    private String objectId;
    private Date created;
    private String ownerId;

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public Double getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Double creationTime) {
        this.creationTime = creationTime;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
