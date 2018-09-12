package cm.kamix.app.ui.Fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cm.kamix.app.app.KamixApp;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.Activity.AddMobileActivity;
import cm.kamix.app.ui.Activity.ChangePasswdActivity;
import cm.kamix.app.ui.Activity.ChangePinActivity;
import cm.kamix.app.ui.Activity.EditEmailActivity;
import cm.kamix.app.ui.Activity.EditMobileActivity;
import cm.kamix.app.ui.Adapter.CountrySpinnerAdapter;
import cm.kamix.app.ui.Adapter.MobileNumbersAdapter;
import cm.kamix.app.ui.Model.Country;
import cm.kamix.app.ui.Activity.SignUpConfirmationActivity;
import cm.kamix.app.ui.Activity.UserInfoActivity;
import cm.kamix.app.R;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yash Ajabiya on 7/27/2018.
 */

public class ProfileScreen1 extends Fragment {
    public ImageView imgNext;
    private RecyclerView mobiles;

    private EditText firstName, lastName, etPin, etPass, etEmail;
    private TextView emailVerified;
    private AppCompatButton addMobile;

    public static final int REQ_MODIFY = 8888;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgNext = view.findViewById(R.id.imgNext);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoActivity.mPager.setCurrentItem(1, true);
            }
        });
        mobiles = view.findViewById(R.id.mobiles);
        firstName = view.findViewById(R.id.firstname);
        lastName = view.findViewById(R.id.lastname);
        etPin = view.findViewById(R.id.etPin);
        etPass = view.findViewById(R.id.etPass);
        etEmail = view.findViewById(R.id.etEmail);
        emailVerified = view.findViewById(R.id.tvVerifyEmail);
        addMobile = view.findViewById(R.id.addmobile);

        emailVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = ((UserInfoActivity)getActivity()).getUser();
                if (user.getEmail()!=null && !user.getEmail().equalsIgnoreCase("")){
                    AlertDialog alertDialog = UiUtils.questionDialog(getContext(),
                            getString(R.string.verify_question).replace("?????", getString(R.string.email_verif_label)).replace("????", etEmail.getText().toString()),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    redoVerificationEmail();
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = ((UserInfoActivity)getActivity()).getUser();
                switch (view.getId()){
                    case R.id.firstname:{
                        EditUserInfoFragment editUserInfoFragment = EditUserInfoFragment.newInstance(EditUserInfoFragment.WHAT_FIRSTNAME, user.getFirstname(), user);
                        editUserInfoFragment.show(getFragmentManager(), EditUserInfoFragment.class.getSimpleName());
                        editUserInfoFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                bind();
                            }
                        });
                    } break;
                    case R.id.lastname:{
                        EditUserInfoFragment editUserInfoFragment = EditUserInfoFragment.newInstance(EditUserInfoFragment.WHAT_LASTNAME, user.getLastname(), user);
                        editUserInfoFragment.show(getFragmentManager(), EditUserInfoFragment.class.getSimpleName());
                        editUserInfoFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                bind();
                            }
                        });
                    } break;
                    case R.id.etPin:{
                        Intent intent = new Intent(getContext(), ChangePinActivity.class);
                        intent.putExtra(User.class.getSimpleName(), user);
                        startActivity(intent);
                    } break;
                    case R.id.etPass:{
                        Intent intent = new Intent(getContext(), ChangePasswdActivity.class);
                        intent.putExtra(User.class.getSimpleName(), user);
                        startActivity(intent);
                    } break;
                    case R.id.etEmail:{
                        Intent intent = new Intent(getContext(), EditEmailActivity.class);
                        intent.putExtra(User.class.getSimpleName(), user);
                        startActivityForResult(intent, REQ_MODIFY);
                    } break;
                    case R.id.addmobile:{
                        Intent intent = new Intent(getContext(), AddMobileActivity.class);
                        intent.putExtra(User.class.getSimpleName(), user);
                        startActivityForResult(intent, REQ_MODIFY);
                    } break;
                }
            }
        };
        firstName.setOnClickListener(listener);
        lastName.setOnClickListener(listener);
        etPin.setOnClickListener(listener);
        etPass.setOnClickListener(listener);
        etEmail.setOnClickListener(listener);
        addMobile.setOnClickListener(listener);

        //bind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_MODIFY && resultCode==getActivity().RESULT_OK){
            bind();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    public void bind(){
        User user = ((UserInfoActivity)getActivity()).getUser();
        firstName.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        etEmail.setText(user.getEmail());
        emailVerified.setText(user.isEmailVerified()?getString(R.string.verified):getString(R.string.unverified));
        emailVerified.setTextColor(user.isEmailVerified()? Color.GREEN:Color.RED);

        MobileNumbersAdapter mobileNumbersAdapter = new MobileNumbersAdapter(getContext());
        mobiles.setLayoutManager(new LinearLayoutManager(getContext()));
        mobiles.setAdapter(mobileNumbersAdapter);
        mobileNumbersAdapter.setData(user.getMobiles(), user.getMobilesVerified());
    }

    private String getMessageByErrorCode(int code){
        switch (code){
            case 1311: return getString(R.string.problem_occurs);
            case 1315: return getString(R.string.invalid_phone_or_email);
            case 171: return getString(R.string.invalid_pin);
            case 172: return getString(R.string.invalid_passwd_);
            default: return getString(R.string.connection_error);
        }
    }

    private void redoVerificationEmail(){
        User user = ((UserInfoActivity)getActivity()).getUser();
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> redoVerifEmail = userInterface.redoVerificationEmail(etEmail.getText().toString(), user.getId());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.please_wait));
        progressDialog.show();
        redoVerifEmail.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    Toast.makeText(getContext(), "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(getContext(), getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("REDO VERIF MOBILE", response.body().getErrorCode() + " error");
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(getContext());
                    dao.storeUser(user);
                    Toast.makeText(getContext(), getString(R.string.mobile_modify_success), Toast.LENGTH_LONG).show();

                    VerifyEmailFragment verifyEmailFragment = VerifyEmailFragment.newInstance(user, etEmail.getText().toString());
                    verifyEmailFragment.show(getFragmentManager(), VerifyEmailFragment.class.getSimpleName());
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                Log.e("REDO VERIF MOBILE", t.getMessage() + " error");
                t.printStackTrace();
            }
        });
    }

}
