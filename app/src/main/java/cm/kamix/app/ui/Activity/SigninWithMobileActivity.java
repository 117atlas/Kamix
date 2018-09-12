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

public class SigninWithMobileActivity extends AppCompatActivity {
    public EditText etMobile;
    public TextView tvSignin, tvError,tvJoinNow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinwith_mobile);
        etMobile = (EditText) findViewById(R.id.etMobile);
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        tvError = (TextView) findViewById(R.id.tvError);
        tvJoinNow= (TextView) findViewById(R.id.tvJoinNow);
        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMobile.getText().toString().equalsIgnoreCase("")) {
                    etMobile.requestFocus();
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Please enter register mobile number !");
                } else {
                    startActivity(new Intent(SigninWithMobileActivity.this, OTPVerficationActivity.class));
                }
            }
        });
        tvJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninWithMobileActivity.this,SignUpActivity.class));
            }
        });
    }
}
