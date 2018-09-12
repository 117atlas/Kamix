package cm.kamix.app.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cm.kamix.app.models.Funding;
import cm.kamix.app.network.models.WCUResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FundingInterface {

    int FUNDING_PENDING = 411,
    FUNDING_INVALID_USER = 401,
    FUNDING_UNACTIVE_USER = 413,
    FUNDING_UNACTIVE_RECEIVER = 414,
    FUNDING_SERVER_ERROR = 431,
    FUNDING_INCORRECT_PINCODE = 451,
    FUNDING_WECASHUP_ERROR = 461,
    FUNDING_WECASHUP_WAITING_TID = 471,
    FUNDING_WECASHUP_WAITING_CONFIRMATION = 481,
    FUNDING_SUCCESS = 499,
    FUNDING_FAILED = 498,
    FUNDING_CANCELED = 490;

    @GET("fundings/{userid}")
    Call<FundingsResponse> getFundings(@Path("userid") String userid);

    @FormUrlEncoded
    @POST("funding/init")
    Call<FundingResponse> init(@Field("userid") String userid, @Field("receivermobile") String receiverMobile, @Field("mobile") String mobile,
                               @Field("amount") String amount, @Field("message") String message, @Field("isforme") boolean isforme);

    @FormUrlEncoded
    @POST("funding/begin")
    Call<BeginFundingResponse> begin(@Field("userid") String userid, @Field("fundingid") String fundingid, @Field("pincode") String pincode,
                                     @Field("lang") String lang);


    @POST("funding/do")
    Call<FundingResponse> doFunding(@Body Funding funding);

    @FormUrlEncoded
    @POST("funding/cancel")
    Call<BaseResponse> cancel(@Field("fundingid") String fundingid);

    class FundingsResponse extends BaseResponse{
        @SerializedName("fundings") @Expose protected List<Funding> fundings;
        public List<Funding> getFundings() {
            return fundings;
        }
        public void setFundings(List<Funding> fundings) {
            this.fundings = fundings;
        }
    }

    class FundingResponse extends BaseResponse{
        @SerializedName("funding") @Expose protected Funding funding;
        public Funding getFunding() {
            return funding;
        }
        public void setFunding(Funding funding) {
            this.funding = funding;
        }
    }

    class BeginFundingResponse extends FundingResponse{
        @SerializedName("funding_datas") protected WCUResponse wcuResponse;
        public WCUResponse getWcuResponse() {
            return wcuResponse;
        }
        public void setWcuResponse(WCUResponse wcuResponse) {
            this.wcuResponse = wcuResponse;
        }
    }
}
