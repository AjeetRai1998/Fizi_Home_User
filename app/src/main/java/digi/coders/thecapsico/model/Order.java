
package digi.coders.thecapsico.model;

import com.google.gson.annotations.SerializedName;

public class Order {

    private String id;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("order_id")
    private String orderId;
    private String coupon;
    @SerializedName("coupon_discount")
    private String couponDiscount;
    @SerializedName("shipping_charge")
    private String shippingCharge;
    private String subtotal;
    private String amount;
    private String method;
    private String txn;
    private String wallet;
    @SerializedName("order_status")
    private String orderStatus;
    @SerializedName("delivery_boy_id")
    private String deliveryBoyId;
    private String message;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    @SerializedName("other_charge")
    private String otherCharge;
    @SerializedName("delivery_tip")
    private String deliveryTip;
    private UserAddress[] address;
    private String RazorpayOrderID;
    private String token;
    private String env;
    private String app_id;
    private String secret_key;
    private String token_url;



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getToken_url() {
        return token_url;
    }

    public void setToken_url(String token_url) {
        this.token_url = token_url;
    }

    public String getRazorpayOrderID() {
        return RazorpayOrderID;
    }

    public void setRazorpayOrderID(String razorpayOrderID) {
        RazorpayOrderID = razorpayOrderID;
    }

    public UserAddress[] getAddress() {
        return address;
    }

    public void setAddress(UserAddress[] address) {
        this.address = address;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(String deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getOtherCharge() {
        return otherCharge;
    }

    public void setOtherCharge(String otherCharge) {
        this.otherCharge = otherCharge;
    }

    public String getDeliveryTip() {
        return deliveryTip;
    }

    public void setDeliveryTip(String deliveryTip) {
        this.deliveryTip = deliveryTip;
    }

}
