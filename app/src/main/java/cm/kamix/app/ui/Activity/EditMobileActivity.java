package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.Adapter.CountrySpinnerAdapter;
import cm.kamix.app.ui.Fragment.VerifyEmailFragment;
import cm.kamix.app.ui.Fragment.VerifyMobileFragment;
import cm.kamix.app.ui.Model.Country;
import cm.kamix.app.ui.UiUtils;
import cm.kamix.app.ui.Utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMobileActivity extends AppCompatActivity {
    private Spinner spCountryOldN, spCountryNewN;
    private TextView tvCodeOldN, tvCodeNewN, tvError;
    private EditText etOldN, etNewN, pinCode;
    private AppCompatButton modify;
    private LinearLayout root;

    private User user;
    private String oldMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mobile);

        user = (User) getIntent().getSerializableExtra(User.class.getSimpleName());
        oldMobile = getIntent().getStringExtra("OLDMOBILE");

        root = findViewById(R.id.root);
        spCountryOldN = findViewById(R.id.spCountryOldM);
        spCountryNewN = findViewById(R.id.spCountryNewM);
        tvCodeOldN = findViewById(R.id.tvCodeOldM);
        tvCodeNewN = findViewById(R.id.tvCodeNewM);
        etOldN = findViewById(R.id.etMobOldM);
        etNewN = findViewById(R.id.etMobNewM);
        pinCode = findViewById(R.id.etPin);
        modify = findViewById(R.id.modify);
        tvError = findViewById(R.id.tvError);

        final ArrayList<Country> cArr = new ArrayList<Country>();
        cArr.add(new Country(R.drawable.cm, "+237"));
        spCountryOldN.setAdapter(new CountrySpinnerAdapter(this, cArr));
        spCountryOldN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvCodeOldN.setText("" + cArr.get(i).getCode());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spCountryNewN.setAdapter(new CountrySpinnerAdapter(this, cArr));
        spCountryNewN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvCodeNewN.setText("" + cArr.get(i).getCode());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNewN.getText().toString().equalsIgnoreCase("")){
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.empty_mobile));
                }
                else if (!Utils.isValidMobile("+237"+etNewN.getText().toString())){
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.invalid_mobile));
                }
                else if (etOldN.getText().toString().equals(etNewN.getText().toString())){
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.match_old_new_mobile));
                }
                else if (pinCode.getText().toString().equalsIgnoreCase("")){
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(getString(R.string.empty_pin));
                }
                else{
                    tvError.setVisibility(View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditMobileActivity.this)
                            .setMessage(getString(R.string.edit_mobile_confirm).replace("?????", etOldN.getText().toString()).replace("????", etNewN.getText().toString()))
                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    modifyMobile();
                                }
                            })
                            .setNegativeButton(getString(R.string.no), null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        });

        etOldN.setText(oldMobile);
    }

    private void modifyMobile(){
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        UserInterface.ModifyMobileBody modifyMobileBody = new UserInterface.ModifyMobileBody();
        modifyMobileBody.setOldMobile("+237"+etOldN.getText().toString());
        modifyMobileBody.setNewMobile("+237"+etNewN.getText().toString());
        modifyMobileBody.setPinCode(pinCode.getText().toString());
        Call<UserInterface.UserResponse> modifyMobile = userInterface.modifyMobile(modifyMobileBody, user.getId());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.please_wait));
        progressDialog.show();
        modifyMobile.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.snackBar(root, getString(R.string.connection_error));
                    Toast.makeText(EditMobileActivity.this, "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    UiUtils.snackBar(root, getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("EDIT MOBILE ACT", response.body().getErrorCode() + " error");
                }
                else if (response.body().getErrorCode()== KamixApp.PIN_CODE_INCORRECT){
                    AlertDialog dialog = UiUtils.infoDialog(EditMobileActivity.this, getString(R.string.incorrect_pin));
                    dialog.show();
                    Log.e("EDIT MOBILE ACT", response.body().getErrorCode() + " error");
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(EditMobileActivity.this);
                    dao.storeUser(user);
                    Toast.makeText(EditMobileActivity.this, getString(R.string.mobile_modify_success), Toast.LENGTH_LONG).show();

                    VerifyMobileFragment verifyMobileFragment = VerifyMobileFragment.newInstance(user, etNewN.getText().toString());
                    verifyMobileFragment.show(getSupportFragmentManager(), VerifyEmailFragment.class.getSimpleName());
                    verifyMobileFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            setResult(RESULT_OK);
                            onBackPressed();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.snackBar(root, getString(R.string.connection_error));
                Log.e("EDIT MOBILE ACT", t.getMessage() + " error");
                t.printStackTrace();
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
}
