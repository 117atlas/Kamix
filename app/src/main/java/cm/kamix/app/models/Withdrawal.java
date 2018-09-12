package cm.kamix.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Withdrawal implements Serializable{
    @SerializedName("_id") @Expose private String id;
    @SerializedName("user") @Expose private String userId;
    @SerializedName("amount") @Expose private double amount;
    @SerializedName("fees") @Expose private double fees;
    @SerializedName("withdrawal_date") @Expose private long date;
    @SerializedName("mobile_number") @Expose private String mobileNumber;
    @SerializedName("status") @Expose private boolean status;
    @SerializedName("status_code") @Expose private int status_code;
    @SerializedName("massage") @Expose private String message;
    @SerializedName("transaction_wcu_uid") @Expose private String transactionWcuUid;
    @SerializedName("transaction_wcu_token") @Expose private String transactionWcuToken;
    @SerializedName("transaction_confirmation_code") @Expose private String transactionConfCode;
    @SerializedName("transaction_provider_name") @Expose private String transactionProvName;
    @SerializedName("wcu_response_code") @Expose private String wcuResponseCode;

    public Withdrawal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionWcuUid() {
        return transactionWcuUid;
    }

    public void setTransactionWcuUid(String transactionWcuUid) {
        this.transactionWcuUid = transactionWcuUid;
    }

    public String getTransactionWcuToken() {
        return transactionWcuToken;
    }

    public void setTransactionWcuToken(String transactionWcuToken) {
        this.transactionWcuToken = transactionWcuToken;
    }

    public String getTransactionConfCode() {
        return transactionConfCode;
    }

    public void setTransactionConfCode(String transactionConfCode) {
        this.transactionConfCode = transactionConfCode;
    }

    public String getTransactionProvName() {
        return transactionProvName;
    }

    public void setTransactionProvName(String transactionProvName) {
        this.transactionProvName = transactionProvName;
    }

    public String getWcuResponseCode() {
        return wcuResponseCode;
    }

    public void setWcuResponseCode(String wcuResponseCode) {
        this.wcuResponseCode = wcuResponseCode;
    }
}
