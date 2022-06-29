package digi.coders.thecapsico.model;

import com.google.gson.annotations.SerializedName;

public class ShippingCharges {

    private String shippingcharges;
    private String kilometer;
    @SerializedName("address_id")
    private String addressId;
    private String lat1;
    private String lon1;
    private String lat2;
    private String lon2;

    public String getShippingcharges() {
        return shippingcharges;
    }

    public void setShippingcharges(String shippingcharges) {
        this.shippingcharges = shippingcharges;
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getLat1() {
        return lat1;
    }

    public void setLat1(String lat1) {
        this.lat1 = lat1;
    }

    public String getLon1() {
        return lon1;
    }

    public void setLon1(String lon1) {
        this.lon1 = lon1;
    }

    public String getLat2() {
        return lat2;
    }

    public void setLat2(String lat2) {
        this.lat2 = lat2;
    }

    public String getLon2() {
        return lon2;
    }

    public void setLon2(String lon2) {
        this.lon2 = lon2;
    }

}
