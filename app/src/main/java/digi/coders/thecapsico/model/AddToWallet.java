package digi.coders.thecapsico.model;

import com.google.gson.annotations.SerializedName;

public class AddToWallet {

    private String id;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("payment_response")
    private String paymentResponse;
    @SerializedName("txt_id")
    private String txtId;
    @SerializedName("payment_id")
    private String paymentId;

    private String amount;
    @SerializedName("is_status")
    private String isStatus;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("modified_at")
    private String modifiedAt;
    private String msg;
    private String type;
    private String token;
    private String env;
    @SerializedName("app_id")
    private String appId;
    @SerializedName("secret_key")
    private String secretKey;
    @SerializedName("token_ur   l")
    private String tokenUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public String getTxtId() {
        return txtId;
    }

    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

}
