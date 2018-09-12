package cm.kamix.app.ui.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.Contact;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.NetworkUtils;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/18/2018.
 */

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int READ_CONTACT_PERM_CODE = 444;
    private User user;
    public TextView tvPaymentQR, tvMoneyTransfer, tvFunding,tvWithdraw,tvUserInfo,tvTransactionHistory,tvSendInvitation, balance;
    private LinearLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        root = findViewById(R.id.root);
        tvPaymentQR = (TextView) findViewById(R.id.tvPaymentQR);
        tvMoneyTransfer = (TextView) findViewById(R.id.tvMoneyTransfer);
        tvFunding = (TextView) findViewById(R.id.tvFunding);
        tvWithdraw= (TextView) findViewById(R.id.tvWithdraw);
        tvUserInfo= (TextView) findViewById(R.id.tvUserInfo);
        tvTransactionHistory= (TextView) findViewById(R.id.tvTransactionHistory);
        tvSendInvitation= (TextView) findViewById(R.id.tvSendInvitation);
        balance = findViewById(R.id.balance);

        tvPaymentQR.setOnClickListener(this);
        tvMoneyTransfer.setOnClickListener(this);
        tvFunding.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
        tvUserInfo.setOnClickListener(this);
        tvTransactionHistory.setOnClickListener(this);
        tvSendInvitation.setOnClickListener(this);

        //updateContacts();

        if (!NetworkUtils.checkInternetConnectivity(this)){
            UiUtils.snackBar(root, getString(R.string.not_online));
        }
        else {
            user = KamixApp.getUser(this);
            login();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        //user = (User) getIntent().getSerializableExtra(User.class.getSimpleName());
        /*if (user==null)*/ user = KamixApp.getUser(this);
        balance.setText(String.valueOf(user.getBalance()));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvPaymentQR:
                startActivity(new Intent(DashboardActivity.this, PaymentChoiceActivity.class)); // call payment transaction screen
                break;
            case R.id.tvMoneyTransfer:
                startActivity(new Intent(DashboardActivity.this, MoneyTransferActivity.class)); // call money transfer screen
                break;
            case R.id.tvFunding:
                startActivity(new Intent(DashboardActivity.this, FundingActivity.class)); // call funding screen
                break;
            case R.id.tvWithdraw:
                startActivity(new Intent(DashboardActivity.this,WithDrawActivity.class)); // call withdraw screen
                break;
            case R.id.tvUserInfo:
                startActivity(new Intent(DashboardActivity.this,UserInfoActivity.class)); // call user profile screen
                break;
            case R.id.tvTransactionHistory:
                startActivity(new Intent(DashboardActivity.this,TransactionActivity.class)); // call transaction screen
                break;
            case R.id.tvSendInvitation:
                startActivity(new Intent(DashboardActivity.this,SendInvitationActivity.class)); // send invitation screen
                break;
        }
    }

    private void updateContacts(){
        if (readContactsPermission()){
            new AsyncTask<Void, Void, List<Contact>>(){
                @Override
                protected List<Contact> doInBackground(Void... voids) {
                    return readContacts();
                }
                @Override
                protected void onPostExecute(List<Contact> contacts) {
                    super.onPostExecute(contacts);
                    UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
                    UserInterface.UpdateContactBody updateContactBody = new UserInterface.UpdateContactBody();
                    updateContactBody.setContacts(contacts);
                    retrofit2.Call<UserInterface.UserResponse> updateContacts = userInterface.updateContacts(updateContactBody, user.getId());
                    updateContacts.enqueue(new Callback<UserInterface.UserResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                            if (response.body()!=null && !response.body().isError()){
                                DAO dao = new DAO(DashboardActivity.this);
                                if (response.body().getUser()!=null) dao.storeUser(response.body().getUser());
                            }
                        }
                        @Override
                        public void onFailure(retrofit2.Call<UserInterface.UserResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }.execute();
        }

    }

    private List<Contact> readContacts(){
        List<Contact> phoneContacts= new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            int i = 1;
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("Phone Contacts", "Name: " + name);
                        Log.i("Phone Contacts", "Phone Number: " + phoneNo);
                        if (phoneNo!=null && !phoneNo.isEmpty()) {
                            phoneNo = phoneNo.replace(" ", "").replace("-", "").replace("+237", "")
                                    .replace("00237", "");
                            if (phoneNo.length()==8) phoneNo = "6"+phoneNo;
                            Contact contact = new Contact(name, "+237"+phoneNo);
                            if (!phoneContacts.contains(contact)) phoneContacts.add(contact);
                        }
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
        return phoneContacts;
    }

    private boolean readContactsPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    // Show an alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Read external storage permission is required.");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    DashboardActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    READ_CONTACT_PERM_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_CONTACT_PERM_CODE
                    );
                }
                return false;
            }else {
                // Permission already granted
                return true;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 117: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, getString(R.string.read_contacts_permissions_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void login(){
        //final ProgressDialog progressDialog = UiUtils.loadingDialog(this, getString(R.string.please_wait));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        retrofit2.Call<UserInterface.UserResponse> login = userInterface.login(user.getId());
        //progressDialog.show();
        login.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(retrofit2.Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                //UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    //UiUtils.snackBar(root, getString(R.string.connection_error));
                    //Toast.makeText(DashboardActivity.this, "Null Body", Toast.LENGTH_LONG).show();
                    //errorDialog();
                }
                else if (response.body().isError()){
                    //errorDialog();
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(DashboardActivity.this);
                    dao.storeUser(user);
                    //Next activity
                }

                updateContacts();
            }
            @Override
            public void onFailure(retrofit2.Call<UserInterface.UserResponse> call, Throwable t) {
                //UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                //UiUtils.snackBar(root, getString(R.string.connection_error));
                //errorDialog();
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
