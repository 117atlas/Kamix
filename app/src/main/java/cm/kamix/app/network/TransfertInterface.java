package cm.kamix.app.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cm.kamix.app.models.Transfert;
import cm.kamix.app.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TransfertInterface {
    int TRANSFERT_PENDING = 211,
    TRANSFERT_INVALID_RECEIVER = 201,
    TRANSFERT_UNACTIVE_RECEIVER =211,
    TRANSFERT_UNACTIVE_SENDER = 212,
    TRANSFERT_SERVER_ERROR = 231,
    TRANSFERT_UNSUFFICIENT_BALANCE = 241,
    TRANSFERT_INCORRECT_PINCODE = 251,
    TRANSFERT_SUCCESS = 299,
    TRANSFERT_CANCELED = 290;

    @GET("transferts/{userid}")
    Call<TransfertsResponse> getTransferts(@Path("userid") String userid);

    @FormUrlEncoded
    @POST("transfert/init")
    Call<TransfertResponse> init(@Field("userid") String userid, @Field("receiver_mobile") String receiverMobile, @Field("amount") String amount, @Field("message") String message);

    @POST("transfert/do")
    Call<TransfertResponse> doTransfert(@Body DoTransfertBody doTransfertBody);

    @FormUrlEncoded
    @POST("transfert/cancel")
    Call<BaseResponse> cancelTransfert(@Field("transfert_id") String transfertId);


    class TransfertsResponse extends BaseResponse{
        @SerializedName("transferts") @Expose protected List<Transfert> transferts;
        public List<Transfert> getTransferts() {
            return transferts;
        }
        public void setTransferts(List<Transfert> transferts) {
            this.transferts = transferts;
        }
    }

    class TransfertResponse extends BaseResponse{
        @SerializedName("transfert") @Expose protected Transfert transfert;
        @SerializedName("sender") @Expose protected User sender;
        public User getSender() {
            return sender;
        }
        public void setSender(User sender) {
            this.sender = sender;
        }
        public Transfert getTransfert() {
            return transfert;
        }
        public void setTransfert(Transfert transfert) {
            this.transfert = transfert;
        }
    }

    class DoTransfertBody {
        @SerializedName("pin_code") @Expose private String pinCode;
        @SerializedName("transfert") @Expose private Transfert transfert;

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public Transfert getTransfert() {
            return transfert;
        }

        public void setTransfert(Transfert transfert) {
            this.transfert = transfert;
        }
    }
}
