package com.erminesoft.nfcpp.model;

/**
 * Created by Evgen on 22.03.2016.
 */
public class EventsToday {

    private String entry;
    private String exit;
    private String total_hours;
    private boolean selector;

    public EventsToday(){

    }

    public EventsToday(String entry, String exit, String total_hours, boolean selector){
        this.entry = entry;
        this.exit = exit;
        this.total_hours = total_hours;
        this.selector = selector;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getExit() {
        return exit;
    }

    public void setExit(String exit) {
        this.exit = exit;
    }

    public String getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(String total_hours) {
        this.total_hours = total_hours;
    }

    public boolean isSelector() {
        return selector;
    }

    public void setSelector(boolean selector) {
        this.selector = selector;
    }
}
