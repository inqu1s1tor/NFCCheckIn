package com.erminesoft.nfcpp.model;

import java.util.Date;

/**
 * Created by Aleks on 15.03.2016.
 */
public class Day {


    private String currentDate;
    private Date entry;
    private Date exit;
//    private String totalTime;

//    public Day(String currentDate, String entry, String exit, String totalTime) {
//        this.currentDate = currentDate;
//        this.entry = entry;
//        this.exit = exit;
//        this.totalTime = totalTime;
//    }

    public Day(){}


    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Date getEntry() {
        return entry;
    }

    public void setEntry(Date entry) {
        this.entry = entry;
    }

    public Date getExit() {
        return exit;
    }

    public void setExit(Date exit) {
        this.exit = exit;
    }
}
