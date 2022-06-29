package digi.coders.thecapsico.model;

public class DeliveryBoy {

    private String id;
    private String name;
    private String mobile;
    private String icon;
    private String description;
    private String vacStatus;

    public String getId() {
        return id;
    }
    public String getVacStatus() {
        return vacStatus;
    }
    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
