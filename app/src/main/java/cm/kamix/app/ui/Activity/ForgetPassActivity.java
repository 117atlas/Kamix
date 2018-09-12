package cm.kamix.app.ui.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/18/2018.
 */

public class ForgetPassActivity extends AppCompatActivity {
    public TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("Forgot Password");
    }
}
