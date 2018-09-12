package cm.kamix.app.network.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cm.kamix.app.models.Withdrawal;
import cm.kamix.app.network.BaseResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WithdrawalInterface {

    int WITHDRAWAL_PENDING = 511,
    WITHDRAWAL_INVALID_USER = 501,
    WITHDRAWAL_UNACTIVE_USER = 513,
    WITHDRAWAL_SERVER_ERROR =531,
    WITHDRAWAL_UNSUFFICIENT_BALANCE = 541,
    WITHDRAWAL_INCORRECT_PINCODE =551,
    WITHDRAWAL_WECASHUP_ERROR = 561,
    WITHDRAWAL_WECASHUP_WAITING_CONFIRMATION =581,
    WITHDRAWAL_SUCCESS = 599,
    WITHDRAWAL_FAILED =598,
    WITHDRAWAL_CANCELED = 590;

    @GET("withdrawals/{userid}")
    Call<WithdrawalsResponse> getWithdrawals(@Path("userid") String userid);

    @FormUrlEncoded
    @POST("withdrawal/init")
    Call<WithdrawalResponse> init(@Field("userid") String userid, @Field("mobile") String mobile, @Field("amount") String amount, @Field("message") String message);

    @FormUrlEncoded
    @POST("withdrawal/begin")
    Call<BeginWithdrawalResponse> begin(@Field("userid") String userid, @Field("withdrawalid") String withdrawalid, @Field("pincode") String pincode,
                                     @Field("lang") String lang);

    @FormUrlEncoded
    @POST("withdrawal/cancel")
    Call<BaseResponse> cancel(@Field("withdrawalid") String withdrawalid);

    class WithdrawalsResponse extends BaseResponse {
        @SerializedName("withdrawals") @Expose protected List<Withdrawal> withdrawals;
        public List<Withdrawal> getWithdrawals() {
            return withdrawals;
        }
        public void setWithdrawals(List<Withdrawal> withdrawals) {
            this.withdrawals = withdrawals;
        }
    }

    class WithdrawalResponse extends BaseResponse{
        @SerializedName("withdrawal") @Expose protected Withdrawal withdrawal;
        public Withdrawal getWithdrawal() {
            return withdrawal;
        }
        public void setWithdrawal(Withdrawal withdrawal) {
            this.withdrawal = withdrawal;
        }
    }

    class BeginWithdrawalResponse extends WithdrawalResponse{
        @SerializedName("withdrawal_datas") protected WCUResponse wcuResponse;
        public WCUResponse getWcuResponse() {
            return wcuResponse;
        }
        public void setWcuResponse(WCUResponse wcuResponse) {
            this.wcuResponse = wcuResponse;
        }
    }
}
