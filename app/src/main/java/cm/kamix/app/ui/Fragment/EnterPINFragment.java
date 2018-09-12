package cm.kamix.app.ui.Fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import cm.kamix.app.ui.Activity.SignUpActivity;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EnterPINFragment extends DialogFragment {

    private EditText etPin;
    private AppCompatButton login, forgotten;
    private TextView euMessage;

    private User user;
    private String mobile;

    public EnterPINFragment() {
        // Required empty public constructor
    }

    public static EnterPINFragment newInstance(User user, String mobile){
        EnterPINFragment enterPINFragment = new EnterPINFragment();
        Bundle args = new Bundle();
        args.putSerializable(User.class.getSimpleName(), user);
        args.putString("MOBILE", mobile);
        enterPINFragment.setArguments(args);
        return enterPINFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment]
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_enter_pin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (User) getArguments().getSerializable(User.class.getSimpleName());
        mobile = getArguments().getString("MOBILE");
        login = view.findViewById(R.id.login);
        euMessage = view.findViewById(R.id.existing_user_message);
        forgotten = view.findViewById(R.id.forgotten);
        etPin = view.findViewById(R.id.etPin);
        euMessage.setText(getString(R.string.existing_user).replace("????", mobile));
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPin.getText().toString().equalsIgnoreCase("")){
                    etPin.setError(getString(R.string.empty_pin));
                    return;
                }
                pinCode();
            }
        });
        forgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setMessage(getString(R.string.reset_pin_message))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                resetPin();
                            }
                        })
                        .setNeutralButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    private void pinCode(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.please_wait));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> pincode = userInterface.pincode(etPin.getText().toString(), user.getId());
        progressDialog.show();
        pincode.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    AlertDialog dialog = UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    dialog.show();
                    Toast.makeText(getContext(), "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().getErrorCode()==KamixApp.PIN_CODE_INCORRECT){
                    AlertDialog dialog = UiUtils.infoDialog(getContext(), getString(R.string.incorrect_pin));
                    dialog.show();
                    Log.e("REGISTER EXIS USER", response.body().getErrorCode() + " error");
                }
                else if (response.body().isError()){
                    AlertDialog dialog = UiUtils.infoDialog(getContext(), getMessageByErrorCode(response.body().getErrorCode()));
                    dialog.show();
                    Log.e("REGISTER EXIS USER", response.body().getErrorCode() + " error");
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(getContext());
                    dao.storeUser(user);
                    ((SignUpActivity)getActivity()).dashBoard(user);
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                AlertDialog dialog = UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                dialog.show();
                Log.e("REGISTER EXIS USER", t.getMessage() + " error");
                t.printStackTrace();
            }
        });
    }

    private void resetPin(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.please_wait));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> resetPin = userInterface.resetPin(etPin.getText().toString(), user.getId());
        progressDialog.show();
        resetPin.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    AlertDialog dialog = UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    dialog.show();
                    Toast.makeText(getContext(), "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    AlertDialog dialog = UiUtils.infoDialog(getContext(), getMessageByErrorCode(response.body().getErrorCode()));
                    dialog.show();
                    Log.e("REGISTER EXIS-RESET PIN", response.body().getErrorCode() + " error");
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(getContext());
                    dao.storeUser(user);
                    AlertDialog dialog = UiUtils.infoDialog(getContext(), getString(R.string.pin_reset_success));
                    dialog.show();
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                AlertDialog dialog = UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                dialog.show();
                Log.e("REGISTER EXIS USER", t.getMessage() + " error");
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
