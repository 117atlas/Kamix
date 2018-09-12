package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cm.kamix.app.app.KamixApp;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.NetworkUtils;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.Adapter.CountrySpinnerAdapter;
import cm.kamix.app.ui.Fragment.EnterPINFragment;
import cm.kamix.app.ui.Model.Country;
import cm.kamix.app.R;
import cm.kamix.app.ui.UiUtils;
import cm.kamix.app.ui.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/18/2018.
 */

public class SignUpActivity extends AppCompatActivity {
    public TextView  tvRegister, tvError, tvCode;
    public EditText etEmail, etMob, etSecretCode, etSecretCodeConfirm;
    public CheckBox cbTermsAndConditions;
    public Spinner spCountry;
    public CoordinatorLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        root = findViewById(R.id.root);
        tvError = (TextView) findViewById(R.id.tvError);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvCode = (TextView) findViewById(R.id.tvCode);
        etSecretCode = findViewById(R.id.secret_code);
        etSecretCodeConfirm = findViewById(R.id.confirm_secret_code);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etMob = (EditText) findViewById(R.id.etMob);
        spCountry = (Spinner) findViewById(R.id.spCountry);
        cbTermsAndConditions = (CheckBox) findViewById(R.id.cbTermsAndConditions);


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMob.getText().toString().equalsIgnoreCase("")) {
                    tvError.setText(getString(R.string.empty_mobile));
                    tvError.setVisibility(View.VISIBLE);
                } else if (!Utils.isValidMobile("+237"+etMob.getText().toString())){
                    tvError.setText(getString(R.string.invalid_mobile));
                    tvError.setVisibility(View.VISIBLE);
                } /*else if (etEmail.getText().toString().equalsIgnoreCase("")) {
                    tvError.setText(getString(R.string.empty_email));
                    tvError.setVisibility(View.VISIBLE);
                }*/ else if (!etEmail.getText().toString().isEmpty() && !Utils.isValidEmail(etEmail.getText().toString())) {
                    tvError.setText(getString(R.string.invalid_email));
                    tvError.setVisibility(View.VISIBLE);
                } /*else if (etSecretCode.getText().toString().equalsIgnoreCase("")){
                    tvError.setText(getString(R.string.empty_secret_code));
                    tvError.setVisibility(View.VISIBLE);
                } else if (!etSecretCode.getText().toString().equals(etSecretCodeConfirm.getText().toString())){
                    tvError.setText(getString(R.string.secret_code_doesnot_match));
                    tvError.setVisibility(View.VISIBLE);
                } */else {
                    if (/*!NetworkUtils.checkInternetConnectivity(SignUpActivity.this)*/false){
                        UiUtils.snackBar(root, getString(R.string.not_online));
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setMessage(getString(R.string.are_you_sure_register).replace("?????", "+237"+etMob.getText().toString()).replace("????", etEmail.getText().toString()));
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                register();
                            }
                        });
                        builder.setNegativeButton(getString(R.string.no), null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }
                }

            }
        });
        cbTermsAndConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startActivity(new Intent(SignUpActivity.this, TermsAndConditionsActivity.class)); // start terms and condition once user checked the checkbox
                }
            }
        });
        final ArrayList<Country> cArr = new ArrayList<Country>();
        cArr.add(new Country(R.drawable.cm, "+237"));
        //cArr.add(new Country(R.drawable.africanflag, "+27"));
        //cArr.add(new Country(R.drawable.france_flg, "+33"));
        //cArr.add(new Country(R.drawable.us_flag, "+1"));
        //cArr.add(new Country(R.drawable.uk, "+44"));
        //cArr.add(new Country(R.drawable.india_flag, "+91"));

        spCountry.setAdapter(new CountrySpinnerAdapter(SignUpActivity.this, cArr)); // set country flag dropdown

        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvCode.setText("" + cArr.get(i).getCode());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void register(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.please_wait));
        progressDialog.show();
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> register = userInterface.register("+237"+etMob.getText().toString(), etEmail.getText().toString());
        register.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.snackBar(root, getString(R.string.connection_error));
                    Toast.makeText(SignUpActivity.this, "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("REGISTER", response.body().getErrorCode() + " error");
                }
                else{
                    if (response.body().getErrorCode()== KamixApp.EXISTING_USER){
                        EnterPINFragment enterPINFragment = EnterPINFragment.newInstance(response.body().getUser(), "+237"+etMob.getText().toString());
                        enterPINFragment.show(getSupportFragmentManager(), EnterPINFragment.class.getSimpleName());
                    }
                    else{
                        User user = response.body().getUser();
                        DAO dao = new DAO(SignUpActivity.this);
                        dao.storeUser(user);
                        nextActivity(user);
                    }
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                UiUtils.snackBar(root, getString(R.string.connection_error));
            }
        });
    }

    private String getMessageByErrorCode(int code){
        switch (code){
            case 1311: return getString(R.string.problem_occurs);
            case 1315: return getString(R.string.invalid_phone_or_email);
            case 1900: return getString(R.string.existing_user);
            default: return getString(R.string.connection_error);
        }
    }

    public void nextActivity(User user){
        Intent i = new Intent(SignUpActivity.this, SignUpConfirmationActivity.class); // call signup conrimation screen with pass selected country and mobile & email
        //i.putExtra("email", etEmail.getText().toString());
        //i.putExtra("mob", etMob.getText().toString());
        //i.putExtra("flag",spCountry.getSelectedItemPosition());
        i.putExtra(User.class.getSimpleName(), user);
        i.putExtra("OLDMOBILE", etMob.getText().toString());
        i.putExtra("OLDEMAIL", etEmail.getText().toString());
        startActivity(i);
    }

    public void dashBoard(User user){
        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
        i.putExtra(User.class.getSimpleName(), user);
        startActivity(i);
    }

}
