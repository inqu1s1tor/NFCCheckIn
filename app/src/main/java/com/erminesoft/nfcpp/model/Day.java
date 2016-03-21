package com.erminesoft.nfcpp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Day {

    private String currentDate;
    private List<Date> checkingList;

    public Day() {
        checkingList = new ArrayList<>();
    }


    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public List<Date> getCheckingList() {
        return checkingList;
    }

    public void setCheckingList(List<Date> checkingList) {
        this.checkingList = checkingList;
    }

    public void addNewChecking(Date newChecking) {
        this.checkingList.add(newChecking);
    }

}
