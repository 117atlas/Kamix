package cm.kamix.app.ui.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cm.kamix.app.R;

public class PaymentChoiceActivity extends AppCompatActivity {
    private TextView proceed, accept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_choice);
        proceed = findViewById(R.id.proceed_payment);
        accept = findViewById(R.id.accept_payment);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentChoiceActivity.this, PaymentTransactionActivity.class));
                finish();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentChoiceActivity.this, AcceptPaymentActivity.class));
                finish();
            }
        });
    }
}
