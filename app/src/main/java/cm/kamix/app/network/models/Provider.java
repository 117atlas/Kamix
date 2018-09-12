package cm.kamix.app.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Provider implements Serializable{
    @SerializedName("provider_order") @Expose private int order;
    @SerializedName("provider_instructions") @Expose private String instructions;
    @SerializedName("provider_communication_mode_asynchronous") @Expose private boolean communicationModeAsynchronous;
    @SerializedName("provider_direct_ussd_capable") @Expose private boolean directUssdCapable;
    @SerializedName("provider_telecom_transaction_init_mode") @Expose private String telecomTransactionInitMode;
    @SerializedName("provider_communication_mode_synchronous") @Expose private boolean communicationModeSynchronous;
    @SerializedName("provider_name") @Expose private String name;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isCommunicationModeAsynchronous() {
        return communicationModeAsynchronous;
    }

    public void setCommunicationModeAsynchronous(boolean communicationModeAsynchronous) {
        this.communicationModeAsynchronous = communicationModeAsynchronous;
    }

    public boolean isDirectUssdCapable() {
        return directUssdCapable;
    }

    public void setDirectUssdCapable(boolean directUssdCapable) {
        this.directUssdCapable = directUssdCapable;
    }

    public String getTelecomTransactionInitMode() {
        return telecomTransactionInitMode;
    }

    public void setTelecomTransactionInitMode(String telecomTransactionInitMode) {
        this.telecomTransactionInitMode = telecomTransactionInitMode;
    }

    public boolean isCommunicationModeSynchronous() {
        return communicationModeSynchronous;
    }

    public void setCommunicationModeSynchronous(boolean communicationModeSynchronous) {
        this.communicationModeSynchronous = communicationModeSynchronous;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
