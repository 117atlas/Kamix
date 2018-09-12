package cm.kamix.app.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cm.kamix.app.R;
import cm.kamix.app.ui.Utils.Utils;

/**
 * Created by Yash Ajabiya on 7/17/2018.
 */

public class SigninWithEmailActivity extends AppCompatActivity {
    public EditText etEmail, etPass;
    public TextView tvSignin, tvError, tvJoinNow, tvForgetPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinwith_email);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        tvError = (TextView) findViewById(R.id.tvError);
        tvJoinNow = (TextView) findViewById(R.id.tvJoinNow);
        tvForgetPass = (TextView) findViewById(R.id.tvForgetPass);
        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().toString().equalsIgnoreCase("")) {
                    etEmail.requestFocus();
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Please enter email address !");
                } else if (!Utils.isValidEmail(etEmail.getText().toString())) {
                    etEmail.requestFocus();
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Please enter proper email address !");
                } else if (etPass.getText().toString().equalsIgnoreCase("")) {
                    etPass.requestFocus();
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Please enter password !");
                } else {
                    Intent i = new Intent(SigninWithEmailActivity.this, DashboardActivity.class);
// set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
        tvJoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninWithEmailActivity.this, SignUpActivity.class));
            }
        });
        tvForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SigninWithEmailActivity.this, ForgetPassActivity.class));
            }
        });
    }
}
