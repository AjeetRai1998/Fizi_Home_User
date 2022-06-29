package digi.coders.thecapsico.helper;

import java.util.ArrayList;

public class Constraint {


    private String Loc = "";
    private String Latitude = "";
    private String Longitude = "";
    private String feature="";
    public static String addonPrice="";
    public static String addonId="";
    public static ArrayList<String> addOnIDList=new ArrayList<>();
    public static ArrayList<String> addOnPriceList=new ArrayList<>();

    public static final String LOCATION = "location";
    public static final String FETAURE="feature";
    public static final String LATITUDE="latitude";
    public static final  String LONGITUDE="longitude";

    public Constraint(String loc, String latitude, String longitude,String feature) {
        Loc = loc;
        Latitude = latitude;
        Longitude = longitude;
        this.feature=feature;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
