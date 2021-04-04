package com.example.hikingapp.model;

import java.time.OffsetDateTime;

public class HikingPlan {
    private String id;

    private String name;

    private boolean active = false;
    private boolean visible = false;

    private boolean checkedIn = false;

    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;

    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;

    public HikingPlan(){
        // default no-arg constructor
    }

    public HikingPlan(String id, String name, boolean active, boolean visible, double startLatitude, double startLongitude,
                      double endLatitude, double endLongitude, String startTime, String endTime, String startDate, String endDate){
        this.id = id;
        this.name= name;
        this.active = active;
        this.visible = visible;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId(){ return id; }

    public String getName(){ return name; }
    public boolean getActive(){ return active; }
    public boolean getVisible(){ return visible; }
    public boolean getCheckedIn(){ return checkedIn; }
    public double getStartLatitude(){ return startLatitude; }
    public double getStartLongitude(){ return startLongitude; }
    public double getEndLatitude(){ return endLatitude; }
    public double getEndLongitude(){ return endLongitude; }
//    public OffsetDateTime getStartDateTime(){ return startDateTime; }
//    public OffsetDateTime getEndDateTime(){ return endDateTime; }
    public String getStartTime(){ return startTime; }
    public String getEndTime(){ return endTime; }
    public String getStartDate(){ return startDate; }
    public String getEndDate(){ return endDate; }

    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){ this.name = name; }
    public void setActive(boolean active){ this.active = active; }
    public void setVisible(boolean visible){ this.visible = visible; }
    public void setCheckedIn(boolean checkedIn){ this.checkedIn = checkedIn; }
    public void setStartLatitude(double startLatitude){ this.startLatitude = startLatitude; }
    public void setStartLongitude(double startLongitude){ this.startLongitude = startLongitude; }
    public void setEndLatitude(double endLatitude){ this.endLatitude = endLatitude; }
    public void setEndLongitude(double endLongitude){ this.endLongitude = endLongitude; }
//    public void setStartDateTime(OffsetDateTime startDateTime){ this.startDateTime = startDateTime; }
//    public void setEndDateTime(OffsetDateTime endDateTime){ this.endDateTime = endDateTime; }
    public void setStartTime(String startTime){ this.startTime = startTime; }
    public void setEndTime(String endTime){ this.endTime = endTime; }
    public void setStartDate(String startDate){ this.startDate = startDate; }
    public void setEndDate(String endDate){ this.endDate = endDate; }
}
