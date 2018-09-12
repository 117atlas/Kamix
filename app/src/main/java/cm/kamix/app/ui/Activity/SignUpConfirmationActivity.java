package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cm.kamix.app.models.User;
import cm.kamix.app.network.BaseResponse;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.Model.Country;
import cm.kamix.app.R;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/24/2018.
 */

public class SignUpConfirmationActivity extends AppCompatActivity {

    public EditText etEmail, etMob, etPin, etPass;
    public TextView tvRegister, tvError, tvResend, tvResendSuccess, tvCode;
    public ImageView spCountry;
    private LinearLayout root;

    private User user;
    private String oldMobile;
    private String oldEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_confirmation);

        user = (User) getIntent().getSerializableExtra(User.class.getSimpleName());
        oldMobile = getIntent().getStringExtra("OLDMOBILE");
        oldEmail = getIntent().getStringExtra("OLDEMAIL");

        root = findViewById(R.id.root);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMob = (EditText) findViewById(R.id.etMob);
        etPin = (EditText) findViewById(R.id.etPin);
        etPass = (EditText) findViewById(R.id.etPass);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvError = (TextView) findViewById(R.id.tvError);
        tvCode = (TextView) findViewById(R.id.tvCode);
        tvResend = (TextView) findViewById(R.id.tvResend);
        spCountry = (ImageView) findViewById(R.id.spCountry);
        tvResendSuccess = (TextView) findViewById(R.id.tvResendSuccess);
        etEmail.setText("" + getIntent().getStringExtra("email"));
        etMob.setText("" + getIntent().getStringExtra("mob"));

        final ArrayList<Country> cArr = new ArrayList<Country>();
        cArr.add(new Country(R.drawable.cm, "+237"));
        //cArr.add(new Country(R.drawable.africanflag, "+27"));
        //cArr.add(new Country(R.drawable.france_flg, "+33"));
        //cArr.add(new Country(R.drawable.us_flag, "+1"));
        //cArr.add(new Country(R.drawable.uk, "+44"));
        //cArr.add(new Country(R.drawable.india_flag, "+91"));


        spCountry.setImageResource(cArr.get(getIntent().getIntExtra("flag", 0)).getFlag());
        tvCode.setText(cArr.get(getIntent().getIntExtra("flag", 0)).getCode() + "");

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvResendSuccess.setVisibility(View.GONE);
                if (etPin.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.empty_pin));
                } else if (etPin.getText().toString().length() < 4) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.invalid_pin_length));
                } else if (!( oldEmail==null || oldEmail.isEmpty()) && etPass.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.invalid_passwd));
                } else {
                    tvError.setVisibility(View.GONE);
                    verifyMobile();
                }
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tvResendSuccess.setVisibility(View.VISIBLE); // show resend pin text
                redoVerification();
            }
        });

        bind();
    }

    private void bind(){
        etMob.setText(oldMobile);
        etEmail.setText(oldEmail);
    }

    public void redoVerification(){
        String _oldmobile = oldMobile.contains("+237")?oldMobile:"+237"+oldMobile;
        String _newmobile = "+237"+etMob.getText().toString();

        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.please_wait));

        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> redoVerification = null;
        if (oldEmail!=null && !oldEmail.equalsIgnoreCase("") && !etEmail.getText().toString().equalsIgnoreCase(""))
            redoVerification = userInterface.redoverification(user.getId(), _oldmobile, _newmobile, oldEmail, etEmail.getText().toString());
        else if ((oldEmail==null || oldEmail.equalsIgnoreCase("")) &&  !etEmail.getText().toString().equalsIgnoreCase(""))
            redoVerification = userInterface.redoverification(user.getId(), _oldmobile, _newmobile, etEmail.getText().toString());
        else
            redoVerification = userInterface.redoverification(user.getId(), _oldmobile, _newmobile);

        progressDialog.show();
        redoVerification.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.snackBar(root, getString(R.string.connection_error));
                    Toast.makeText(SignUpConfirmationActivity.this, "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("REGISTER", response.body().getErrorCode() + " error");
                }
                else{
                    //Alert
                    tvResendSuccess.setVisibility(View.VISIBLE); // show resend pin text
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                tvError.setText(getString(R.string.connection_error));
            }
        });
    }

    private String getMessageByErrorCode(int code){
        switch (code){
            case 1311: return getString(R.string.problem_occurs);
            case 1315: return getString(R.string.invalid_phone_or_email);
            case 171: return getString(R.string.invalid_pin);
            case 172: return getString(R.string.invalid_passwd_);
            default: return getString(R.string.connection_error);
        }
    }

    private void verifyMobile(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.verifying_mobile));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);

        UserInterface.VerifyMobileBody verifyMobileBody = new UserInterface.VerifyMobileBody();
        verifyMobileBody.setUserId(user.getId());
        verifyMobileBody.setMobile("+237"+etMob.getText().toString());
        verifyMobileBody.setPinCode(etPin.getText().toString());
        verifyMobileBody.setUserPin(true);

        Call<UserInterface.UserResponse> verifyMobile = userInterface.verifyMobile(verifyMobileBody, user.getId());
        progressDialog.show();
        verifyMobile.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body() == null) {
                    UiUtils.snackBar(root, getString(R.string.connection_error));
                    Toast.makeText(SignUpConfirmationActivity.this, "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    tvError.setText(getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("REGISTER", response.body().getErrorCode() + " error");
                }
                else{
                    //Success verification Mobile
                    if (!etEmail.getText().toString().equalsIgnoreCase("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpConfirmationActivity.this)
                                .setMessage(getString(R.string.mobile_verification_success))
                                .setPositiveButton(getString(R.string.verify_label), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        verifyEmail();
                                    }
                                })
                                .setNegativeButton(getString(R.string.skip_email_verif), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        nextActivity();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                    else{
                        nextActivity();
                    }

                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                tvError.setText(getString(R.string.connection_error));
            }
        });
    }

    private void verifyEmail(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.verifying_mobile));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);

        UserInterface.VerifyEmailBody verifyEmailBody = new UserInterface.VerifyEmailBody();
        verifyEmailBody.setUserId(user.getId());
        verifyEmailBody.setEmail(etEmail.getText().toString());
        verifyEmailBody.setPasswd(etPass.getText().toString());
        verifyEmailBody.setUserPass(true);

        Call<UserInterface.UserResponse> verifyEmail = userInterface.verifyEmail(verifyEmailBody, user.getId());
        progressDialog.show();
        verifyEmail.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body() == null) {
                    UiUtils.snackBar(root, getString(R.string.connection_error));
                    Toast.makeText(SignUpConfirmationActivity.this, "Null Body", Toast.LENGTH_LONG).show();
                    alertErrorVerifyEmail();
                }
                else if (response.body().isError()){
                    Toast.makeText(SignUpConfirmationActivity.this, getMessageByErrorCode(response.body().getErrorCode()), Toast.LENGTH_LONG).show();
                    Log.e("REGISTER", response.body().getErrorCode() + " error");
                    alertErrorVerifyEmail();
                }
                else{
                    //Success email verification
                    Toast.makeText(SignUpConfirmationActivity.this, getString(R.string.email_verification_success), Toast.LENGTH_LONG).show();
                    nextActivity();
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                tvError.setText(getString(R.string.connection_error));

                alertErrorVerifyEmail();
            }
        });
    }

    private void nextActivity(){
        Intent i = new Intent(SignUpConfirmationActivity.this, AccountActivationActivity.class); // account activation screen call
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(User.class.getSimpleName(), user.getId());
        startActivity(i);
    }

    private void alertErrorVerifyEmail(){
        //Skip dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpConfirmationActivity.this)
                .setMessage(getString(R.string.restart_verify_email))
                .setPositiveButton(getString(R.string.skip_email_verif), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nextActivity();
                    }
                })
                .setNegativeButton(getString(R.string.restart_email_verif), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        verifyEmail();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void alertSuccessVerifyMobile(){

    }
}
