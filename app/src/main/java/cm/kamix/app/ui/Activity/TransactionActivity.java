package cm.kamix.app.ui.Activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import cm.kamix.app.ui.Fragment.PaymentTransactionHistoryFragment;
import cm.kamix.app.ui.Fragment.TransferTransactionHistoryFragment;
import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/19/2018.
 */

public class TransactionActivity extends AppCompatActivity {
    public TextView tvPayments, tvTransfer, tvWithdraw, tvFunding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        tvPayments = (TextView) findViewById(R.id.tvPayments);
        tvTransfer = (TextView) findViewById(R.id.tvTransfer);
        tvWithdraw = (TextView) findViewById(R.id.tvWithdraw);
        tvFunding = (TextView) findViewById(R.id.tvFunding);

        /* Default Payment Tab */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frmTransaction, new PaymentTransactionHistoryFragment());
        transaction.commit();
        tvPayments.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tvTransfer.setBackgroundResource(R.drawable.transaction_unselect_tab);
        tvWithdraw.setBackgroundResource(R.drawable.transaction_unselect_tab);
        tvFunding.setBackgroundResource(R.drawable.transaction_unselect_tab);

        /** set trnsaction tab */
        tvPayments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frmTransaction, new PaymentTransactionHistoryFragment());
                transaction.commit();
                tvPayments.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                tvTransfer.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvWithdraw.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvFunding.setBackgroundResource(R.drawable.transaction_unselect_tab);
            }
        });
        tvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frmTransaction, new TransferTransactionHistoryFragment());
                transaction.commit();
                tvTransfer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                tvPayments.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvWithdraw.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvFunding.setBackgroundResource(R.drawable.transaction_unselect_tab);
            }
        });
        tvWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvWithdraw.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                tvTransfer.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvPayments.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvFunding.setBackgroundResource(R.drawable.transaction_unselect_tab);
            }
        });
        tvFunding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFunding.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                tvTransfer.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvWithdraw.setBackgroundResource(R.drawable.transaction_unselect_tab);
                tvPayments.setBackgroundResource(R.drawable.transaction_unselect_tab);
            }
        });
    }


}
