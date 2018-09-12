package cm.kamix.app.ui.Fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.User;
import cm.kamix.app.network.BaseResponse;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.Activity.EditEmailActivity;
import cm.kamix.app.ui.Activity.SignUpConfirmationActivity;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyEmailFragment extends DialogFragment {

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private TextView confirmEmailMessage;
    private EditText confirmPass;
    private AppCompatButton confirm, confirmLater;

    private User user;
    private String email;

    public VerifyEmailFragment() {
        // Required empty public constructor
    }

    public static VerifyEmailFragment newInstance(User user, String email){
        VerifyEmailFragment verifyEmailFragment = new VerifyEmailFragment();
        Bundle args = new Bundle();
        args.putSerializable(User.class.getSimpleName(), user);
        args.putString("EMAIL", email);
        verifyEmailFragment.setArguments(args);
        return verifyEmailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_verify_email, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmEmailMessage = view.findViewById(R.id.confirm_email_address_message);
        confirmPass = view.findViewById(R.id.etPass);
        confirm = view.findViewById(R.id.yes);
        confirmLater = view.findViewById(R.id.no);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { verifyEmail();
            }
        });
        confirmLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (getActivity()!=null && getActivity() instanceof EditEmailActivity){
                    getActivity().onBackPressed();
                }
            }
        });

        user = (User) getArguments().getSerializable(User.class.getSimpleName());
        email = getArguments().getString("EMAIL");

        confirmEmailMessage.setText(confirmEmailMessage.getText().toString().replace("????", email));
    }

    private void verifyEmail(){
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.verifying_mobile));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);

        UserInterface.VerifyEmailBody verifyEmailBody = new UserInterface.VerifyEmailBody();
        verifyEmailBody.setUserId(user.getId());
        verifyEmailBody.setEmail(email);
        verifyEmailBody.setPasswd(confirmPass.getText().toString());
        verifyEmailBody.setUserPass(false);

        Call<UserInterface.UserResponse> verifyEmail = userInterface.verifyEmail(verifyEmailBody, user.getId());
        progressDialog.show();
        verifyEmail.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body() == null) {
                    UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    Toast.makeText(getContext(), "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(getContext(), getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("REGISTER", response.body().getErrorCode() + " error");
                }
                else{
                    //Success email verification
                    User user = response.body().getUser();
                    DAO dao = new DAO(getContext());
                    dao.storeUser(user);

                    Toast.makeText(getContext(), getString(R.string.email_verification_success), Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
            }
        });
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener!=null) onDismissListener.onDismiss(dialog);
    }
}
