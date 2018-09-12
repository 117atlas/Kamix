package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import cm.kamix.app.app.KamixApp;
import cm.kamix.app.models.Contact;
import cm.kamix.app.models.User;
import cm.kamix.app.network.FundingInterface;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.ui.Adapter.CountrySpinnerAdapter;
import cm.kamix.app.ui.Adapter.ReceiverMobileAdapter;
import cm.kamix.app.ui.Fragment.FWDetailsFragment;
import cm.kamix.app.ui.Model.Country;
import cm.kamix.app.R;
import cm.kamix.app.ui.UiUtils;
import cm.kamix.app.ui.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/26/2018.
 */

public class FundingActivity extends AppCompatActivity {
    //private Spinner spCountry;
    private SwitchCompat fundSwitch;
    private TextView tvExecute, tvError, tvCode;
    private EditText etAmount, message;
    private Spinner myMob;
    private AutoCompleteTextView receiverMob;
    private LinearLayout receiverContainer;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funding);

        user = KamixApp.getUser(this);
        if (user==null) onBackPressed();

        fundSwitch = findViewById(R.id.fund_switch);
        tvError = (TextView) findViewById(R.id.tvError);
        tvExecute = (TextView) findViewById(R.id.tvExecute);
        //tvCode = (TextView) findViewById(R.id.tvCode);
        etAmount = (EditText) findViewById(R.id.etAmount);
        message = findViewById(R.id.etMessage);
        myMob = findViewById(R.id.mymob);
        receiverMob = findViewById(R.id.receiver);
        receiverContainer = findViewById(R.id.receiver_container);

        fundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) receiverContainer.setVisibility(View.VISIBLE);
                else receiverContainer.setVisibility(View.GONE);
            }
        });
        receiverContainer.setVisibility(View.GONE);

        ArrayAdapter<String> myMobAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user.getMobiles());
        myMobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myMob.setAdapter(myMobAdapter);
        myMob.setSelection(0);

        receiverMob.setInputType(InputType.TYPE_CLASS_PHONE);
        ReceiverMobileAdapter receiverMobileAdapter = new ReceiverMobileAdapter(this, (ArrayList<Contact>) user.getContacts());
        receiverMob.setAdapter(receiverMobileAdapter);
        receiverMob.setThreshold(1);

        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAmount.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.invalid_amount));
                } else if (fundSwitch.isChecked() && (receiverMob.getText().toString().equalsIgnoreCase("") || !Utils.isValidMobile(receiverMob.getText().toString()))) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.invalid_receiver_mobile_num));
                } else {
                    /*Intent in = new Intent(FundingActivity.this, TransactionSuccessfullyActivity.class); // call trnsaction sucess screen
                    in.putExtra("data", "Funding");
                    startActivity(in);
                    finish();*/
                    initFunding();
                }
            }
        });

    }

    private void initFunding(){
        String receiver = receiverMob.getText().toString();
        if (!receiver.contains("+237")) receiver = "+237"+receiver;
        FundingInterface fundingInterface = NetworkAPI.getClient().create(FundingInterface.class);
        Call<FundingInterface.FundingResponse> init = fundingInterface.init(user.getId(), receiver, myMob.getSelectedItem().toString(), etAmount.getText().toString(), message.getText().toString(), !fundSwitch.isChecked());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.init_transaction));
        progressDialog.show();
        init.enqueue(new Callback<FundingInterface.FundingResponse>() {
            @Override
            public void onResponse(Call<FundingInterface.FundingResponse> call, Response<FundingInterface.FundingResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(FundingActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", "Body null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_INVALID_USER){
                    UiUtils.infoDialog(FundingActivity.this, getString(R.string.transfert_invalid_receiver));
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_UNACTIVE_USER){
                    UiUtils.infoDialog(FundingActivity.this, getString(R.string.transfert_unactive_sender));
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_INVALID_USER){
                    UiUtils.infoDialog(FundingActivity.this, getString(R.string.transfert_invalid_receiver));
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_UNACTIVE_RECEIVER){
                    UiUtils.infoDialog(FundingActivity.this, getString(R.string.transfert_unactive_receiver));
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(FundingActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", response.message() + " - " + response.body().getErrorCode());
                }
                else{
                    FWDetailsFragment fwDetailsFragment = FWDetailsFragment.newInstance(response.body().getFunding(), FWDetailsFragment.DETAILS_FUNDING);
                    fwDetailsFragment.show(getSupportFragmentManager(), FWDetailsFragment.class.getSimpleName());
                }
            }
            @Override
            public void onFailure(Call<FundingInterface.FundingResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(FundingActivity.this, getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }
}
