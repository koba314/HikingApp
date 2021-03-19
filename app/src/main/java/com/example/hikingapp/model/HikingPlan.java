package com.example.hikingapp.model;

import java.time.OffsetDateTime;

public class HikingPlan {
    private String id;

    private String name;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;

//    private OffsetDateTime startDateTime;
//    private OffsetDateTime endDateTime;

    private String startTime;
    private String endTime;

    public HikingPlan(){
        // default no-arg constructor
    }

    public HikingPlan(String id, String name, double startLatitude, double startLongitude,
                      double endLatitude, double endLongitude, String startTime, String endTime){
        this.id = id;
        this.name= name;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId(){ return id; }

    public String getName(){ return name; }
    public double getStartLatitude(){ return startLatitude; }
    public double getStartLongitude(){ return startLongitude; }
    public double getEndLatitude(){ return endLatitude; }
    public double getEndLongitude(){ return endLongitude; }
//    public OffsetDateTime getStartDateTime(){ return startDateTime; }
//    public OffsetDateTime getEndDateTime(){ return endDateTime; }
    public String getStartTime(){ return startTime; }
    public String getEndTime(){ return endTime; }

    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){ this.name = name; }
    public void setStartLatitude(double startLatitude){ this.startLatitude = startLatitude; }
    public void setStartLongitude(double startLongitude){ this.startLongitude = startLongitude; }
    public void setEndLatitude(double endLatitude){ this.endLatitude = endLatitude; }
    public void setEndLongitude(double endLongitude){ this.endLongitude = endLongitude; }
//    public void setStartDateTime(OffsetDateTime startDateTime){ this.startDateTime = startDateTime; }
//    public void setEndDateTime(OffsetDateTime endDateTime){ this.endDateTime = endDateTime; }
    public void setStartTime(String startTime){ this.startTime = startTime; }
    public void setEndTime(String endTime){ this.endTime = endTime; }
}
