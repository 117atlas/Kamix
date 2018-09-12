package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.app.Utils;
import cm.kamix.app.models.Payment;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.PaymentInterface;
import cm.kamix.app.network.TransfertInterface;
import cm.kamix.app.ui.Fragment.TransactionDetailsFragment;
import cm.kamix.app.ui.UiUtils;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/19/2018.
 */

public class PaymentTransactionActivity extends AppCompatActivity {
    public static final int REQ_QR_SCAN = 808;
    //private ZBarScannerView mScannerView;
    public TextView tvError, tvExecute;
    public EditText etAmount, etMerchant, message;

    private String qrCodeResult = "";

    private String merchantId = "";

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_transaction);
        //mScannerView = (ZBarScannerView) findViewById(R.id.zbQr);

        user = KamixApp.getUser(this);
        if (user==null) onBackPressed();

        tvError = (TextView) findViewById(R.id.tvError);
        tvExecute = (TextView) findViewById(R.id.tvExecute);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etMerchant = findViewById(R.id.etMerchant);
        message = findViewById(R.id.etMessage);

        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAmount.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Error : Invalid amount");
                } else {
                    /*Intent in = new Intent(PaymentTransactionActivity.this, TransactionSuccessfullyActivity.class); // call trnsaction sucess screen
                    in.putExtra("data", "Payment");
                    startActivity(in);
                    finish();*/
                    initPayment();
                }
            }
        });

        etMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PaymentTransactionActivity.this, QRCodeScanActivity.class), REQ_QR_SCAN);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        //mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        //mScannerView.stopCamera();           // Stop camera on pause
    }

    /*@Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        // Do something with the result here
        Log.v("kkkk", result.getContents()); // Prints scan results
        Log.v("uuuu", result.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)


        onBackPressed();

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_QR_SCAN && resultCode==RESULT_OK){
            qrCodeResult = data.getStringExtra("QRCODERESULT");
            Log.d("QRCODERESULT", qrCodeResult);
            try{
                ArrayList<String> qrcodeDatas = Utils.split(qrCodeResult, "|"); //TextUtils.split(qrCodeResult, "|");

                if (qrcodeDatas==null || qrcodeDatas.size()!=2){
                    UiUtils.infoDialog(this, getString(R.string.invalid_qrcode));
                }
                else{
                    etMerchant.setText(qrcodeDatas.get(1));
                    merchantId = qrcodeDatas.get(0);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initPayment(){
        PaymentInterface paymentInterface = NetworkAPI.getClient().create(PaymentInterface.class);
        Call<PaymentInterface.PaymentResponse> init = paymentInterface.init(user.getId(), merchantId, etAmount.getText().toString(), message.getText().toString(), "0");
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.init_transaction));
        progressDialog.show();
        init.enqueue(new Callback<PaymentInterface.PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentInterface.PaymentResponse> call, Response<PaymentInterface.PaymentResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(PaymentTransactionActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", "Body null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==PaymentInterface.PAYMENT_INVALID_RECEIVER){
                    UiUtils.infoDialog(PaymentTransactionActivity.this, getString(R.string.transfert_invalid_receiver));
                }
                else if (response.body().isError() && response.body().getErrorCode()==PaymentInterface.PAYMENT_UNACTIVE_SENDER){
                    UiUtils.infoDialog(PaymentTransactionActivity.this, getString(R.string.transfert_unactive_sender));
                }
                else if (response.body().isError() && response.body().getErrorCode()==PaymentInterface.PAYMENT_UNACTIVE_RECEIVER){
                    UiUtils.infoDialog(PaymentTransactionActivity.this, getString(R.string.transfert_unactive_receiver));
                }
                else if (response.body().isError() && response.body().getErrorCode()==PaymentInterface.PAYMENT_UNSUFFICIENT_BALANCE){
                    UiUtils.infoDialog(PaymentTransactionActivity.this, getString(R.string.transfert_unsufficient_balance));
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(PaymentTransactionActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", response.message() + " - " + response.body().getErrorCode());
                }
                else{
                    TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(response.body().getPayment(), TransactionDetailsFragment.DETAILS_PAYMENT);
                    transactionDetailsFragment.show(getSupportFragmentManager(), TransactionDetailsFragment.class.getSimpleName());
                }
            }
            @Override
            public void onFailure(Call<PaymentInterface.PaymentResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(PaymentTransactionActivity.this, getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }
}
