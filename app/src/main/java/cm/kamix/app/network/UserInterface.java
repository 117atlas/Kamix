package cm.kamix.app.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cm.kamix.app.models.Contact;
import cm.kamix.app.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserInterface {
    @GET("register/{mobile}/{email}")
    Call<UserResponse> register(@Path("mobile") String mobile, @Path("email") String email);

    @GET("redoverification/{userid}/{oldmobile}/{newmobile}/{oldemail}/{newemail}")
    Call<UserResponse> redoverification(@Path("userid") String userid, @Path("oldmobile") String oldmobile, @Path("newmobile") String newmobile,
                          @Path("oldemail") String oldemail, @Path("newemail") String newemail);

    @GET("redoverification/{userid}/{oldmobile}/{newmobile}/{newemail}")
    Call<UserResponse> redoverification(@Path("userid") String userid, @Path("oldmobile") String oldmobile, @Path("newmobile") String newmobile,
                                        @Path("newemail") String newemail);

    @GET("redoverification/{userid}/{oldmobile}/{newmobile}")
    Call<UserResponse> redoverification(@Path("userid") String userid, @Path("oldmobile") String oldmobile, @Path("newmobile") String newmobile);

    @POST("verifymobile/{userid}")
    Call<UserResponse> verifyMobile(@Body VerifyMobileBody verifyMobileBody, @Path("userid") String userid);

    @POST("verifyemail/{userid}")
    Call<UserResponse> verifyEmail(@Body VerifyEmailBody verifyEmailBody, @Path("userid") String userid);

    @POST("updatecontacts/{userid}")
    Call<UserResponse> updateContacts(@Body UpdateContactBody updateContactBody, @Path("userid") String userId);

    @POST("updateuser/{userid}")
    Call<UserResponse> updateUser(@Body User user, @Path("userid") String userId);

    @GET("login/{userid}")
    Call<UserResponse> login(@Path("userid") String userId);

    @POST("changepin/{userid}")
    Call<UserResponse> changePin(@Body ChangePINBody changePINBody, @Path("userid") String userId);

    @POST("changepasswd/{userid}")
    Call<UserResponse> changePasswd(@Body ChangePasswdBody changePasswdBody, @Path("userid") String userId);

    @POST("modifymobile/{userid}")
    Call<UserResponse> modifyMobile(@Body ModifyMobileBody modifyMobileBody, @Path("userid") String userId);

    @POST("modifyemail/{userid}")
    Call<UserResponse> modifyEmail(@Body ModifyEmailBody modifyEmailBody, @Path("userid") String userId);

    @FormUrlEncoded
    @POST("resetpin/{userid}")
    Call<UserResponse> resetPin(@Field("pin_code") String pin_code, @Path("userid") String userId);

    @FormUrlEncoded
    @POST("pincode/{userid}")
    Call<UserResponse> pincode(@Field("pin_code") String pin_code, @Path("userid") String userId);

    @FormUrlEncoded
    @POST("resetpass/{userid}")
    Call<UserResponse> resetPass(@Field("passwd") String passwd, @Path("userid") String userId);

    @FormUrlEncoded
    @POST("deletemobile/{userid}")
    Call<UserResponse> deleteMobile(@Field("pin_code") String pinCode, @Field("mobile") String mobile, @Path("userid") String userid);

    @FormUrlEncoded
    @POST("redoverificationemail/{userid}")
    Call<UserResponse> redoVerificationEmail(@Field("email") String email, @Path("userid") String userid);

    @FormUrlEncoded
    @POST("redoverificationmobile/{userid}")
    Call<UserResponse> redoVerificationMobile(@Field("mobile") String mobile, @Path("userid") String userid);

    class VerifyMobileBody{
        @SerializedName("userid") @Expose String userId;
        @SerializedName("mobile") @Expose String mobile;
        @SerializedName("pin_code") @Expose String pinCode;
        @SerializedName("is_user_pin") @Expose boolean isUserPin;

        public VerifyMobileBody() {
        }

        public boolean isUserPin() {
            return isUserPin;
        }

        public void setUserPin(boolean userPin) {
            isUserPin = userPin;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }
    }

    class VerifyEmailBody{
        @SerializedName("userid") @Expose String userId;
        @SerializedName("email") @Expose String email;
        @SerializedName("passwd") @Expose String passwd;
        @SerializedName("is_user_pass") @Expose boolean isUserPass;

        public boolean isUserPass() {
            return isUserPass;
        }

        public void setUserPass(boolean userPass) {
            isUserPass = userPass;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
    }

    class UpdateContactBody{
        @SerializedName("contacts") @Expose private List<Contact> contacts;

        public UpdateContactBody() {
        }

        public List<Contact> getContacts() {
            return contacts;
        }

        public void setContacts(List<Contact> contacts) {
            this.contacts = contacts;
        }
    }

    class UserResponse extends BaseResponse{
        @SerializedName("user") @Expose private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    class ChangePINBody{
        @SerializedName("old_pin") @Expose private String oldPin;
        @SerializedName("new_pin") @Expose private String newPin;

        public ChangePINBody() {
        }

        public String getOldPin() {
            return oldPin;
        }

        public void setOldPin(String oldPin) {
            this.oldPin = oldPin;
        }

        public String getNewPin() {
            return newPin;
        }

        public void setNewPin(String newPin) {
            this.newPin = newPin;
        }
    }

    class ChangePasswdBody{
        @SerializedName("old_passwd") @Expose private String oldPasswd;
        @SerializedName("new_passwd") @Expose private String newPasswd;

        public String getOldPasswd() {
            return oldPasswd;
        }

        public void setOldPasswd(String oldPasswd) {
            this.oldPasswd = oldPasswd;
        }

        public String getNewPasswd() {
            return newPasswd;
        }

        public void setNewPasswd(String newPasswd) {
            this.newPasswd = newPasswd;
        }
    }

    class ModifyMobileBody{
        @SerializedName("oldmobile") @Expose String oldMobile;
        @SerializedName("newmobile") @Expose String newMobile;
        @SerializedName("pin_code") @Expose String pinCode;

        public String getOldMobile() {
            return oldMobile;
        }

        public void setOldMobile(String oldMobile) {
            this.oldMobile = oldMobile;
        }

        public String getNewMobile() {
            return newMobile;
        }

        public void setNewMobile(String newMobile) {
            this.newMobile = newMobile;
        }

        public String getPinCode() {
            return pinCode;
        }

        public void setPinCode(String pinCode) {
            this.pinCode = pinCode;
        }
    }

    class ModifyEmailBody{
        @SerializedName("oldemail") @Expose String oldEmail;
        @SerializedName("newemail") @Expose String newEmail;
        @SerializedName("passwd") @Expose String passwd;

        public String getOldEmail() {
            return oldEmail;
        }

        public void setOldEmail(String oldEmail) {
            this.oldEmail = oldEmail;
        }

        public String getNewEmail() {
            return newEmail;
        }

        public void setNewEmail(String newEmail) {
            this.newEmail = newEmail;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }
    }
}
