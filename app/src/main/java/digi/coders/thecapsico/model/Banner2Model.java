package digi.coders.thecapsico.model;

public class Banner2Model {
    String id,image,percent;

    public Banner2Model(String id, String image, String percent) {
        this.id = id;
        this.image = image;
        this.percent = percent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
