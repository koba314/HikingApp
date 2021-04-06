package com.example.hikingapp.model;

public class MapPin {
    public static final int DEFAULT = 0;

    /*
     * FOR DATABASE USE: DON'T UPDATE THESE FIELDS OUTSIDE OF DATA SOURCE CLASSES
     */
    // the id of this MapPin in db/mappin/{id}
    private String id;
    // the id of this MapPin in db/user/{userid}/mappin/{uid}
    private String uid;
    // the id of the user in db/user/{userid}/mappin/{uid}
    private String createdByUid;
    // the name of the user who created this
    private String username;
    //////

    private String name;
    private String info;
    private int symbol = DEFAULT;

    private double latitude;
    private double longitude;

    private boolean isPublic;

    public MapPin(){
        // default no-arg constructor
    }

    public MapPin(String id, String uid, String createdByUid, String name, String info, int symbol, double latitude, double longitude, boolean isPublic){
        this.id = id;
        this.uid = uid;
        this.createdByUid = createdByUid;
        this.name = name;
        this.info = info;
        this.symbol = symbol;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isPublic = isPublic;
    }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getUid(){ return uid; }
    public void setUid(String uid){ this.uid = uid; }
    public String getCreatedByUid(){ return createdByUid; }
    public void setCreatedByUid(String createdByUid){ this.createdByUid = createdByUid; }
    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }
    public String getInfo(){ return info; }
    public void setInfo(String info){ this.info = info; }
    public int getSymbol(){ return symbol; }
    public void setSymbol(int symbol){ this.symbol = symbol; }
    public double getLatitude(){ return latitude; }
    public void setLatitude(double latitude){ this.latitude = latitude; }
    public double getLongitude(){ return longitude; }
    public void setLongitude(double longitude){ this.longitude = longitude; }
    public boolean getIsPublic(){ return isPublic; }
    public void setIsPublic(boolean isPublic){ this.isPublic = isPublic; }
}
