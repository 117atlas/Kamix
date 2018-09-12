package cm.kamix.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transfert implements Serializable {
    @SerializedName("_id") @Expose private String id;
    @SerializedName("sender") @Expose private String senderId;
    @SerializedName("receiver") @Expose private String receiverId;
    @SerializedName("amount") @Expose private double amount;
    @SerializedName("fees") @Expose private double fees;
    @SerializedName("transfert_date") @Expose private long date;
    @SerializedName("status") @Expose private boolean status;
    @SerializedName("status_code") @Expose private int status_code;
    @SerializedName("message") @Expose private String message;
    @SerializedName("receiver_mobile") @Expose private String receiverMobile;
    @SerializedName("receiver_name") @Expose private String receiverName;

    public Transfert() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
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

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
}
