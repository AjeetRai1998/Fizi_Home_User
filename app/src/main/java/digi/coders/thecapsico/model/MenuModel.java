package digi.coders.thecapsico.model;

import com.google.gson.annotations.SerializedName;

public class MenuModel {

    @SerializedName("img")
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String couponCode) {
        this.img = couponCode;
    }
}
