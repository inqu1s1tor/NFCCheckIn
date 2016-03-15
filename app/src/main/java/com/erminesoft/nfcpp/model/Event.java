package com.erminesoft.nfcpp.model;

import android.renderscript.Element;

import java.util.Date;

/**
 * Created by Aleks on 11.03.2016.
 */
public class Event {

    private String objectId;
    private Date created;
    private String idCard;

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
}


