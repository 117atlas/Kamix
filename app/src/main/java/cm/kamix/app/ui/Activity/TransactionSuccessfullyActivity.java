package cm.kamix.app.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cm.kamix.app.R;
import cm.kamix.app.models.Payment;
import cm.kamix.app.models.Transfert;

/**
 * Created by Yash Ajabiya on 7/26/2018.
 */

public class TransactionSuccessfullyActivity extends AppCompatActivity {
    public String textContain = "";
    public TextView tvText, tvGotoMainPage, tvHistory, transactionConfirmMessage, hbs;

    private Transfert transfert;
    private Payment payment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasaction_successfully);
        textContain = getIntent().getStringExtra("data");
        tvText = (TextView) findViewById(R.id.tvText);
        tvGotoMainPage = (TextView) findViewById(R.id.tvGotoMainPage);
        tvHistory = (TextView) findViewById(R.id.tvHistory);
        transactionConfirmMessage = findViewById(R.id.transaction_confirm_message);
        hbs = findViewById(R.id.hbs);
        tvText.setText("Your " + textContain + " has");

        switch (textContain){
            case "Transfert": {
                transactionConfirmMessage.setVisibility(View.GONE);
                hbs.setText(getString(R.string.hbs_executed));
            } break;
            case "Payment": {
                transactionConfirmMessage.setVisibility(View.GONE);
                hbs.setText(getString(R.string.hbs_executed));
            } break;
            default:{
                transactionConfirmMessage.setVisibility(View.VISIBLE);
                hbs.setText(getString(R.string.hbs_submitted));
            } break;
        }

        tvGotoMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(TransactionSuccessfullyActivity.this, DashboardActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);*/
                onBackPressed();
            }
        });

        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransactionSuccessfullyActivity.this, TransactionActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void getTransactions(){

    }
}
