package digi.coders.thecapsico.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryProduct {
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("category_id")
    private String categoryId;
    private Product[] product;
    public Product[] getProduct() {
        return product;
    }
    public void setProduct(Product[] product) {
        this.product = product;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


}
