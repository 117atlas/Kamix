package cm.kamix.app.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WCUResponseContent implements Serializable{
    @SerializedName("providers_list") @Expose private List<Provider> providers;
    @SerializedName("transaction_token") @Expose private String transactionToken;
    @SerializedName("transaction_uid") @Expose private String transactionUid;
    @SerializedName("merchant_callback_url") @Expose private String callbackUrl;

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

    public String getTransactionUid() {
        return transactionUid;
    }

    public void setTransactionUid(String transactionUid) {
        this.transactionUid = transactionUid;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
