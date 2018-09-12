package cm.kamix.app.network;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PingInterface {
    @GET("ping")
    Call<BaseResponse> ping();
}
