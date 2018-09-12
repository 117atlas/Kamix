package cm.kamix.app.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WCUResponse implements Serializable {
    @SerializedName("response_content") @Expose private WCUResponseContent content;
    @SerializedName("response_details") @Expose private String details;
    @SerializedName("response_code") @Expose private int code;
    @SerializedName("response_status") @Expose private String status;

    public WCUResponseContent getContent() {
        return content;
    }

    public void setContent(WCUResponseContent content) {
        this.content = content;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
