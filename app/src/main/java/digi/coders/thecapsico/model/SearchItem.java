package digi.coders.thecapsico.model;

public class SearchItem {
    private String name;
    private String id;
    private String icon;
    private String search_key;
    private String type;
    private String data;
    private String mobile;
    private String email;
    private String shop_name;
    private String menu_type;

    public String getName() {
        return name;
    }
    public String getMenu_type() {
        return menu_type;
    }
    public String getShop_name() {
        return shop_name;
    }
    public String getEmail() {
        return email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSearch_key() {
        return search_key;
    }

    public void setSearch_key(String search_key) {
        this.search_key = search_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
