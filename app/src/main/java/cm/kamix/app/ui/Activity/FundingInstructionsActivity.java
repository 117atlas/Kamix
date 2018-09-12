package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cm.kamix.app.R;
import cm.kamix.app.models.Funding;
import cm.kamix.app.network.BaseResponse;
import cm.kamix.app.network.FundingInterface;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FundingInstructionsActivity extends AppCompatActivity {
    private TextView instructions, proceed;
    private AppCompatButton cancel;
    private EditText operatorTid;

    private FundingInterface.BeginFundingResponse beginFundingResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funding_instructions);
        beginFundingResponse = (FundingInterface.BeginFundingResponse) getIntent()
                .getSerializableExtra(FundingInterface.BeginFundingResponse.class.getSimpleName());
        instructions = findViewById(R.id.funding_instructions);
        proceed = findViewById(R.id.proceed);
        cancel = findViewById(R.id.cancel);
        operatorTid = findViewById(R.id.operator_tid);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.questionDialog(FundingInstructionsActivity.this, getString(R.string.cancel_transaction_question), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelFunding();
                    }
                });
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operatorTid.getText().toString().equalsIgnoreCase("")){
                    operatorTid.setError(getString(R.string.empty_operator_tid));
                    return;
                }
                doFunding();
            }
        });
    }

    private void cancelFunding(){
        FundingInterface fundingInterface = NetworkAPI.getClient().create(FundingInterface.class);
        Call<BaseResponse> cancel = fundingInterface.cancel(beginFundingResponse.getFunding().getId());
        cancel.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body()!=null && !response.body().isError()){
                    Toast.makeText(FundingInstructionsActivity.this, getString(R.string.transaction_cancelled), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void doFunding(){
        FundingInterface fundingInterface = NetworkAPI.getClient().create(FundingInterface.class);
        Call<FundingInterface.FundingResponse> begin = fundingInterface.doFunding(beginFundingResponse.getFunding());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.processing_transaction));
        progressDialog.show();
        begin.enqueue(new Callback<FundingInterface.FundingResponse>() {
            @Override
            public void onResponse(Call<FundingInterface.FundingResponse> call, Response<FundingInterface.FundingResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(FundingInstructionsActivity.this, getString(R.string.connection_error));
                    Log.e("DO FUNDING", "Body Null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_SERVER_ERROR){
                    UiUtils.questionDialog(FundingInstructionsActivity.this, getString(R.string.funding_server_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            doFunding();
                        }
                    });
                    Log.e("DO FUNDING", "Funding server error");
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_FAILED) {
                    UiUtils.questionDialog(FundingInstructionsActivity.this, getString(R.string.funding_failed), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            doFunding();
                        }
                    });
                    Log.e("DO FUNDING", "Funding Failed");
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(FundingInstructionsActivity.this, getString(R.string.transaction_server_error));
                    Log.e("DO FUNDING", response.message() +" - "+response.body().getErrorCode());
                }
                else{
                    //transactionSuccess(response.body().getTransfert(), response.body().getSender(),"Money Transfer");
                    //save transfert
                    //int i =1;
                }
            }
            @Override
            public void onFailure(Call<FundingInterface.FundingResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(FundingInstructionsActivity.this, getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }
}
