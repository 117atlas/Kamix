package cm.kamix.app.ui.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cm.kamix.app.R;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/19/2018.
 */

public class AccountActivationActivity extends AppCompatActivity {
    public TextView tvComplete, tvSkip;
    private LinearLayout root;
    private String userId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_account_activation);
        root = findViewById(R.id.root);
        userId = getIntent().getStringExtra(User.class.getSimpleName());

        tvSkip = (TextView) findViewById(R.id.tvSkip);
        tvComplete = (TextView) findViewById(R.id.tvComplete);

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivationActivity.this, DashboardActivity.class); // go to direct main page
// set the new task and clear flags
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //login();
    }

    private void login(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.please_wait));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> login = userInterface.login(userId);
        progressDialog.show();
        login.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.snackBar(root, getString(R.string.connection_error));
                    Toast.makeText(AccountActivationActivity.this, "Null Body", Toast.LENGTH_LONG).show();
                    errorDialog();
                }
                else if (response.body().isError()){
                    errorDialog();
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(AccountActivationActivity.this);
                    dao.storeUser(user);
                    dao.storeTransactions(user);
                    //Next activity
                    Intent i = new Intent(AccountActivationActivity.this, UserInfoActivity.class);
                    startActivity(i);
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                UiUtils.snackBar(root, getString(R.string.connection_error));
                errorDialog();
            }
        });
    }

    private void errorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.register_finalisation_error))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        login();
                    }
                })
                .setNegativeButton(getString(R.string.no), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
