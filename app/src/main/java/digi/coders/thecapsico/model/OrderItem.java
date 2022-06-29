
package digi.coders.thecapsico.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderItem {

    private String id;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("product_id")
    private String productId;
    private String qty;
    private Product product;
    @SerializedName("addonproduct_prize")
    private String addOnProductPrize;
    @SerializedName("addonproduct_id")
    private String addOnProductId;
    private Merchant MerchantResults;


    public Merchant getMerchantResults() {
        return MerchantResults;
    }

    public void setMerchantResults(Merchant merchantResults) {
        MerchantResults = merchantResults;
    }

    public String getAddOnProductPrize() {
        return addOnProductPrize;
    }

    public void setAddOnProductPrize(String addOnProductPrize) {
        this.addOnProductPrize = addOnProductPrize;
    }

    public String getAddOnProductId() {
        return addOnProductId;
    }

    public void setAddOnProductId(String addOnProductId) {
        this.addOnProductId = addOnProductId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
