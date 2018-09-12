package cm.kamix.app.ui.Fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

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
 * A simple {@link Fragment} subclass.
 */
public class EditUserInfoFragment extends DialogFragment {

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private TextView euiTitle, euiLabel;
    private EditText euiEdit;
    private AppCompatButton modify;

    private int what;
    private String data;
    private User user;

    public static final int WHAT_FIRSTNAME = 789;
    public static final int WHAT_LASTNAME = 791;
    public static final int WHAT_ADDRESS = 795;
    public static final int WHAT_CITY = 799;

    public EditUserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getDialog().getWindow().setLayout(getResources().getDimensionPixelSize(R.dimen.po), ViewGroup.LayoutParams.WRAP_CONTENT);
        return inflater.inflate(R.layout.fragment_edit_user_info, container, false);
    }

    public static EditUserInfoFragment newInstance(int what, String data, User user){
        EditUserInfoFragment editUserInfoFragment = new EditUserInfoFragment();
        Bundle args = new Bundle();
        args.putInt("WHAT", what);
        args.putString("DATA", data);
        args.putSerializable(User.class.getSimpleName(), user);
        editUserInfoFragment.setArguments(args);
        return editUserInfoFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        euiTitle = view.findViewById(R.id.edit_dialog_title);
        euiLabel = view.findViewById(R.id.edit_info_label);
        euiEdit = view.findViewById(R.id.edit);
        modify = view.findViewById(R.id.modify);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (euiEdit.getText().toString().equalsIgnoreCase("") || euiEdit.getText().toString().equals(data)) dismiss();
                else updateUser();
            }
        });

        bind();
    }

    private void bind(){
        what = getArguments().getInt("WHAT");
        data = getArguments().getString("DATA");
        user = (User) getArguments().getSerializable(User.class.getSimpleName());
        switch (what){
            case WHAT_FIRSTNAME: euiLabel.setText(getString(R.string.edit_firstname_label)); break;
            case WHAT_LASTNAME: euiLabel.setText(getString(R.string.edit_lastname_label)); break;
            case WHAT_ADDRESS: euiLabel.setText(getString(R.string.edit_address_label)); break;
            case WHAT_CITY: euiLabel.setText(getString(R.string.edit_city_label)); break;
            default: euiLabel.setText(getString(R.string.edit_lastname_label)); break;
        }
        euiEdit.setText(data==null?"":data);
    }

    private void updateUser(){
        switch (what){
            case WHAT_FIRSTNAME: user.setFirstname(euiEdit.getText().toString()); break;
            case WHAT_LASTNAME: user.setLastname(euiEdit.getText().toString()); break;
            case WHAT_ADDRESS: user.setAddress(euiEdit.getText().toString()); break;
            case WHAT_CITY: user.setCity(euiEdit.getText().toString()); break;
            default: user.setFirstname(euiEdit.getText().toString()); break;
        }
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.please_wait));
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> updateUser = userInterface.updateUser(user, user.getId());
        progressDialog.show();
        updateUser.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    Log.e("EDIT USER INFOS", "Body null");
                    errorDialog(getString(R.string.connection_error));
                }
                else if (response.body().isError()){
                    Log.e("EDIT USER INFOS", "Is Error " + response.body().getErrorCode());
                    errorDialog(getMessageByErrorCode(response.body().getErrorCode()));
                }
                else{
                    User _user = response.body().getUser();
                    DAO dao = new DAO(getContext());
                    dao.storeUser(_user);
                    dismiss();
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                t.printStackTrace();
                errorDialog(getString(R.string.connection_error));
            }
        });
    }

    private String getMessageByErrorCode(int code){
        switch (code){
            case 1311: return getString(R.string.problem_occurs);
            case 1315: return getString(R.string.invalid_phone_or_email);
            default: return getString(R.string.connection_error);
        }
    }

    private void errorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setMessage(message + getString(R.string.would_like_retry))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateUser();
                    }
                })
                .setNegativeButton(getString(R.string.no), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener!=null) onDismissListener.onDismiss(dialog);
    }
}
