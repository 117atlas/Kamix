package cm.kamix.app.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cm.kamix.app.ui.Utils.Utils;
import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 8/1/2018.
 */

public class SendInvitationActivity extends AppCompatActivity {
    public EditText etMobile, etEmail, etMessage;
    public TextView tvError, tvExecute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invitation);
        tvExecute = (TextView) findViewById(R.id.tvExecute);
        tvError = (TextView) findViewById(R.id.tvError);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMessage = (EditText) findViewById(R.id.etMessage);

        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMobile.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Error : Invalid mobile number");
                } else if (etEmail.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Error : Invalid email, please try again");
                } else if (!Utils.isValidEmail(etEmail.getText().toString())) {
                    tvError.setText("Error : Invalid email, please enter proper email address");
                    tvError.setVisibility(View.VISIBLE);
                } else {

                    /* show toast of successful send invite and move to main screen */
                    Toast.makeText(SendInvitationActivity.this, "Invitation send sucessfully.", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SendInvitationActivity.this, DashboardActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
    }
}
