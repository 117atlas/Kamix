package cm.kamix.app.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/27/2018.
 */

public class TransactionErrorActivity extends AppCompatActivity {
    public String textContain = "";
    public TextView tvText, tvGotoMainPage, tvHistory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasaction_error);

        textContain = getIntent().getStringExtra("data");
        tvGotoMainPage = (TextView) findViewById(R.id.tvGotoMainPage);
        tvHistory = (TextView) findViewById(R.id.tvHistory);
        tvText = (TextView) findViewById(R.id.tvText);
        tvText.setText("Your " + textContain + " was");

        tvGotoMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransactionErrorActivity.this, DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransactionErrorActivity.this, TransactionActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
