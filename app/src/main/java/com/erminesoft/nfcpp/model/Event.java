package com.erminesoft.nfcpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

@Table(name = "EVENT")
public class Event extends Model {

    @Column(name = "object_id")
    private String objectId;

    @Column(name = "created")
    private Date created;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "card_id")
    private String idCard;

    @Column(name = "is_sent")
    private boolean isSent;

    @Column(name = "creation_time", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int creationTime;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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


