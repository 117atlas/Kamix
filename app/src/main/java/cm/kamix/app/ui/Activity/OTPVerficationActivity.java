package cm.kamix.app.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/17/2018.
 */

public class OTPVerficationActivity extends AppCompatActivity {
    public TextView tvSignin, tvError, tvBack;
    public EditText etOTP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        tvSignin = (TextView) findViewById(R.id.tvSignin);
        tvError = (TextView) findViewById(R.id.tvError);
        tvBack = (TextView) findViewById(R.id.tvBack);
        etOTP = (EditText) findViewById(R.id.etOTP);

        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOTP.getText().toString().equalsIgnoreCase("")) {
                    etOTP.requestFocus();
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Enter PIN number");
                } else if (etOTP.getText().toString().length() < 4) {
                    etOTP.requestFocus();
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Enter 4 digit PIN number");
                } else {
                    Intent i = new Intent(OTPVerficationActivity.this, AccountActivationActivity.class);
// set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
