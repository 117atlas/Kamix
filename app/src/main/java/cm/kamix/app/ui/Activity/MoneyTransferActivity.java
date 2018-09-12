package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.models.Contact;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.TransfertInterface;
import cm.kamix.app.ui.Adapter.ReceiverMobileAdapter;
import cm.kamix.app.ui.Fragment.TransactionDetailsFragment;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/25/2018.
 */

public class MoneyTransferActivity extends AppCompatActivity {
    public EditText etAmount, etMessage;
    private AutoCompleteTextView etTransferTo;
    public TextView tvError, tvExecute;

    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer);

        user = KamixApp.getUser(this);
        if (user==null) onBackPressed();

        etTransferTo = findViewById(R.id.etTransferTo);
        etAmount = (EditText) findViewById(R.id.etAmount);
        tvError=(TextView)findViewById(R.id.tvError);
        tvExecute=(TextView)findViewById(R.id.tvExecute);
        etMessage = findViewById(R.id.etMessage);

        etTransferTo.setInputType(InputType.TYPE_CLASS_PHONE);
        ReceiverMobileAdapter receiverMobileAdapter = new ReceiverMobileAdapter(this, (ArrayList<Contact>) user.getContacts());
        etTransferTo.setAdapter(receiverMobileAdapter);
        etTransferTo.setThreshold(1);

        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAmount.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Error : Invalid amount");
                } else if (etTransferTo.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Error : Invalid transfer to name");
                } else {
                    /*Intent in = new Intent(MoneyTransferActivity.this, TransactionSuccessfullyActivity.class);// call trnsaction sucess screen
                    in.putExtra("data", "Money Transfer");
                    startActivity(in);
                    finish();*/
                    initTransfert();
                }
            }
        });
    }

    public void initTransfert(){
        String receiver = etTransferTo.getText().toString();
        if (!receiver.contains("+237")) receiver = "+237"+receiver;
        TransfertInterface transfertInterface = NetworkAPI.getClient().create(TransfertInterface.class);
        Call<TransfertInterface.TransfertResponse> init = transfertInterface.init(user.getId(), receiver, etAmount.getText().toString(), etMessage.getText().toString());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.init_transaction));
        progressDialog.show();
        init.enqueue(new Callback<TransfertInterface.TransfertResponse>() {
            @Override
            public void onResponse(Call<TransfertInterface.TransfertResponse> call, Response<TransfertInterface.TransfertResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(MoneyTransferActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", "Body null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==TransfertInterface.TRANSFERT_INVALID_RECEIVER){
                    UiUtils.infoDialog(MoneyTransferActivity.this, getString(R.string.transfert_invalid_receiver));
                }
                else if (response.body().isError() && response.body().getErrorCode()==TransfertInterface.TRANSFERT_UNACTIVE_SENDER){
                    UiUtils.infoDialog(MoneyTransferActivity.this, getString(R.string.transfert_unactive_sender));
                }
                else if (response.body().isError() && response.body().getErrorCode()==TransfertInterface.TRANSFERT_UNACTIVE_RECEIVER){
                    UiUtils.infoDialog(MoneyTransferActivity.this, getString(R.string.transfert_unactive_receiver));
                }
                else if (response.body().isError() && response.body().getErrorCode()==TransfertInterface.TRANSFERT_UNSUFFICIENT_BALANCE){
                    UiUtils.infoDialog(MoneyTransferActivity.this, getString(R.string.transfert_unsufficient_balance));
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(MoneyTransferActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", response.message() + " - " + response.body().getErrorCode());
                }
                else{
                    TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(response.body().getTransfert(), TransactionDetailsFragment.DETAILS_TRANSFERT);
                    transactionDetailsFragment.show(getSupportFragmentManager(), TransactionDetailsFragment.class.getSimpleName());
                }
            }
            @Override
            public void onFailure(Call<TransfertInterface.TransfertResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(MoneyTransferActivity.this, getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }
}
