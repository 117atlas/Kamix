package cm.kamix.app.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cm.kamix.app.models.Payment;
import cm.kamix.app.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentInterface {
    int PAYMENT_PENDING = 311,
            PAYMENT_INVALID_RECEIVER = 301,
            PAYMENT_UNACTIVE_RECEIVER =311,
            PAYMENT_UNACTIVE_SENDER = 312,
            PAYMENT_SERVER_ERROR = 331,
            PAYMENT_UNSUFFICIENT_BALANCE = 341,
            PAYMENT_INCORRECT_PINCODE = 351,
            PAYMENT_SUCCESS = 399,
            PAYMENT_CANCELED = 390;

    @GET("payments/{userid}")
    Call<PaymentsResponse> getPayments(@Path("userid") String userid);

    @FormUrlEncoded
    @POST("payment/init")
    Call<PaymentResponse> init(@Field("userid") String userid, @Field("merchant") String merchantid, @Field("amount") String amount, @Field("message") String message, @Field("qrcode") String qrcode);

    @POST("payment/do")
    Call<PaymentResponse> doPayment(@Body DoPaymentBody doPaymentBody);

    @FormUrlEncoded
    @POST("payment/cancel")
    Call<BaseResponse> cancelPayment(@Field("payment_id") String paymentId);


    class PaymentsResponse extends BaseResponse{
        @SerializedName("payments") @Expose
        protected List<Payment> payments;
        public List<Payment> getPayments() {
            return payments;
        }
        public void setPayments(List<Payment> payments) {
            this.payments = payments;
        }
    }

    class PaymentResponse extends BaseResponse{
        @SerializedName("payment") @Expose protected Payment payment;
        @SerializedName("customer") @Expose protected User customer;
        public User getCustomer() {
            return customer;
        }
        public void setCustomer(User customer) {
            this.customer = customer;
        }
        public Payment getPayment() {
            return payment;
        }
        public void setPayment(Payment payment) {
            this.payment = payment;
        }
    }

    class DoPaymentBody {
        @SerializedName("pin_code") @Expose private String pinCode;
        @SerializedName("payment") @Expose private Payment payment;

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }

        public Payment getPayment() {
            return payment;
        }

        public void setPayment(Payment payment) {
            this.payment = payment;
        }
    }
}
