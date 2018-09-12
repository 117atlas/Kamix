package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import cm.kamix.app.app.KamixApp;
import cm.kamix.app.models.User;
import cm.kamix.app.models.Withdrawal;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.models.WithdrawalInterface;
import cm.kamix.app.ui.Adapter.CountrySpinnerAdapter;
import cm.kamix.app.ui.Fragment.FWDetailsFragment;
import cm.kamix.app.ui.Model.Country;
import cm.kamix.app.R;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/19/2018.
 */

public class WithDrawActivity extends AppCompatActivity {
    public Spinner spCountry;
    public TextView tvExecute, tvError, tvCode;
    public EditText etAmount, etMob, message;
    private Spinner myMob;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        user = KamixApp.getUser(this);
        if (user==null) onBackPressed();

        //spCountry = (Spinner) findViewById(R.id.spCountry);
        tvError = (TextView) findViewById(R.id.tvError);
        tvExecute = (TextView) findViewById(R.id.tvExecute);
        //tvCode = (TextView) findViewById(R.id.tvCode);
        etAmount = (EditText) findViewById(R.id.etAmount);
        //etMob = (EditText) findViewById(R.id.etMob);
        message = findViewById(R.id.etMessage);
        myMob = findViewById(R.id.mymob);

        ArrayAdapter<String> myMobAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, user.getMobiles());
        myMobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myMob.setAdapter(myMobAdapter);
        myMob.setSelection(0);


        /*final ArrayList<Country> cArr = new ArrayList<Country>();
        cArr.add(new Country(R.drawable.africanflag, "+27"));
        cArr.add(new Country(R.drawable.france_flg, "+33"));
        cArr.add(new Country(R.drawable.us_flag, "+1"));
        cArr.add(new Country(R.drawable.uk, "+44"));
        cArr.add(new Country(R.drawable.india_flag, "+91"));

        spCountry.setAdapter(new CountrySpinnerAdapter(WithDrawActivity.this, cArr));

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvCode.setText("" + cArr.get(i).getCode());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });*/

        tvExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etAmount.getText().toString().equalsIgnoreCase("")) {
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText("Error : Invalid amount");
                } else {
                    /*Intent in = new Intent(WithDrawActivity.this, TransactionErrorActivity.class); // call trnsaction error screen
                    in.putExtra("data", "Withdrawal");
                    startActivity(in);
                    finish();*/
                    initWithdrawal();
                }
            }
        });
    }

    private void initWithdrawal(){
        WithdrawalInterface withdrawalInterface = NetworkAPI.getClient().create(WithdrawalInterface.class);
        Call<WithdrawalInterface.WithdrawalResponse> init = withdrawalInterface.init(user.getId(), myMob.getSelectedItem().toString(), etAmount.getText().toString(), message.getText().toString());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.init_transaction));
        progressDialog.show();
        init.enqueue(new Callback<WithdrawalInterface.WithdrawalResponse>() {
            @Override
            public void onResponse(Call<WithdrawalInterface.WithdrawalResponse> call, Response<WithdrawalInterface.WithdrawalResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(WithDrawActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", "Body null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==WithdrawalInterface.WITHDRAWAL_INVALID_USER){
                    UiUtils.infoDialog(WithDrawActivity.this, getString(R.string.transfert_invalid_receiver));
                }
                else if (response.body().isError() && response.body().getErrorCode()==WithdrawalInterface.WITHDRAWAL_UNACTIVE_USER){
                    UiUtils.infoDialog(WithDrawActivity.this, getString(R.string.transfert_unactive_sender));
                }
                else if (response.body().isError() && response.body().getErrorCode()==WithdrawalInterface.WITHDRAWAL_UNSUFFICIENT_BALANCE){
                    UiUtils.infoDialog(WithDrawActivity.this, getString(R.string.transfert_unsufficient_balance));
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(WithDrawActivity.this, getString(R.string.connection_error));
                    Log.e("TRANSFERT", response.message() + " - " + response.body().getErrorCode());
                }
                else{
                    Withdrawal withdrawal = response.body().getWithdrawal();
                    System.out.println();
                    FWDetailsFragment fwDetailsFragment = FWDetailsFragment.newInstance(response.body().getWithdrawal(), FWDetailsFragment.DETAILS_WITHDRAWAL);
                    fwDetailsFragment.show(getSupportFragmentManager(), FWDetailsFragment.class.getSimpleName());
                }
            }
            @Override
            public void onFailure(Call<WithdrawalInterface.WithdrawalResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(WithDrawActivity.this, getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }
}
