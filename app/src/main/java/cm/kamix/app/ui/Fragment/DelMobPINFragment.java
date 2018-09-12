package cm.kamix.app.ui.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.Activity.EditMobileActivity;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DelMobPINFragment extends DialogFragment {

    private TextView deleteMobileMessage;
    private EditText pinCode;
    private AppCompatButton yes, no;

    private User user;
    private String mobile;

    public DelMobPINFragment() {
        // Required empty public constructor
    }

    public static DelMobPINFragment newInstance(User user, String mobile){
        DelMobPINFragment delMobPINFragment = new DelMobPINFragment();
        Bundle args = new Bundle();
        args.putSerializable(User.class.getSimpleName(), user);
        args.putString("MOBILE", mobile);
        delMobPINFragment.setArguments(args);
        return delMobPINFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_pin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleteMobileMessage = view.findViewById(R.id.delete_mobile_message);
        pinCode = view.findViewById(R.id.etPin);
        yes = view.findViewById(R.id.yes);
        no = view.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMobile();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        user = (User) getArguments().getSerializable(User.class.getSimpleName());
        mobile = getArguments().getString("MOBILE");

        deleteMobileMessage.setText(deleteMobileMessage.getText().toString().replace("????", "+237"+mobile));
    }

    private void deleteMobile(){
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> deleteMobile = userInterface.deleteMobile(pinCode.getText().toString(), "+237"+mobile, user.getId());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.please_wait));
        progressDialog.show();
        deleteMobile.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    Toast.makeText(getContext(), "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError() && response.body().getErrorCode()== KamixApp.PIN_CODE_INCORRECT){
                    UiUtils.infoDialog(getContext(), getString(R.string.incorrect_pin));
                    Log.e("DELETE MOBILE ACT", response.body().getErrorCode() + " error");
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(getContext(), getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("DELETE MOBILE", response.body().getErrorCode() + " error");
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(getContext());
                    dao.storeUser(user);
                    Toast.makeText(getContext(), getString(R.string.mobile_deleted_success), Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                Log.e("DELETE MOBILE", t.getMessage() + " error");
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
