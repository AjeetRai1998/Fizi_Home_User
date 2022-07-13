package digi.coders.thecapsico.model;

import com.google.gson.annotations.SerializedName;
public class MyOrder {
    private String id;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("return_order_id")
    private String return_order_id;
    private String coupon;
    @SerializedName("coupon_discount")
    private String couponDiscount;
    private String subtotal;
    @SerializedName("shipping_charge")
    private String shippinCharge;
    private String amount;
    private String method;
    private String txn;
    @SerializedName("payment_response")
    private String paymentResponse;
    private String wallet;
    @SerializedName("order_status")
    private String orderStatus;
    @SerializedName("delivery_boy_id")
    private String deliveryBoyId;
    private UserAddress[] address;
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
    @SerializedName("delivery_time")
    private String deliveryTime;
    @SerializedName("accepted_time")
    private String acceptedTIme;
    private String alt_number;
    private String extra_amount;
    private Orderproduct[] orderproduct;

    private Merchant[] merchant;
    private DeliveryBoy[] deliveryboy;
    private String merchantratingstatus;
    private String deliveryboyratingstatus;
    private Review[] deliveryboyrating;
    private String rejectmsg;
    private String delivery_boy_status_accepted;


    public String getReturn_order_id() {
        return return_order_id;
    }

    public void setReturn_order_id(String return_order_id) {
        this.return_order_id = return_order_id;
    }

    public DeliveryBoy[] getDeliveryboy() {
        return deliveryboy;
    }
    public Review[] getDeliveryboyrating() {
        return deliveryboyrating;
    }

    public String getExtra_amount() {
        return extra_amount;
    }

    public void setExtra_amount(String extra_amount) {
        this.extra_amount = extra_amount;
    }

    public void setDeliveryboy(DeliveryBoy[] deliveryboy) {
        this.deliveryboy = deliveryboy;
    }

    public String getRejectmsg() {
        return rejectmsg;
    }
    public String getDelivery_boy_status_accepted() {
        return delivery_boy_status_accepted;
    }
    public String getAcceptedTIme() {
        return acceptedTIme;
    }
    public String getDeliveryTime() {
        return deliveryTime;
    }
    public String getAlt_number() {
        return alt_number;
    }

    public void setRejectmsg(String rejectmsg) {
        this.rejectmsg = rejectmsg;
    }

    public String getMerchantratingstatus() {
        return merchantratingstatus;
    }

    public void setMerchantratingstatus(String merchantratingstatus) {
        this.merchantratingstatus = merchantratingstatus;
    }

    public String getDeliveryboyratingstatus() {
        return deliveryboyratingstatus;
    }

    public void setDeliveryboyratingstatus(String deliveryboyratingstatus) {
        this.deliveryboyratingstatus = deliveryboyratingstatus;
    }

    public Merchant[] getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant[] merchant) {
        this.merchant = merchant;
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

    public String getShippinCharge()    {
        return shippinCharge;
    }

    public void setShippinCharge(String shippinCharge) {
        this.shippinCharge = shippinCharge;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
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

    public UserAddress[] getAddress() {
        return address;
    }

    public void setAddress(UserAddress[] address) {
        this.address = address;
    }

    public Orderproduct[] getOrderproduct() {
        return orderproduct;
    }

    public void setOrderproduct(Orderproduct[] orderproduct) {
        this.orderproduct = orderproduct;
    }
}
