package digi.coders.thecapsico.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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


    public static String getDate() {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(new Date());
        // calendar.add(Calendar.DATE);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//hh:mm a "yyyy-MM-dd"
        String time = null;
        try {
            time = sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

}
