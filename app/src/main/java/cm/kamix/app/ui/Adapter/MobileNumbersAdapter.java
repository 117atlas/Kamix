package cm.kamix.app.ui.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.User;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.UserInterface;
import cm.kamix.app.ui.Activity.EditMobileActivity;
import cm.kamix.app.ui.Activity.SignUpConfirmationActivity;
import cm.kamix.app.ui.Activity.UserInfoActivity;
import cm.kamix.app.ui.Fragment.DelMobPINFragment;
import cm.kamix.app.ui.Fragment.VerifyEmailFragment;
import cm.kamix.app.ui.Fragment.VerifyMobileFragment;
import cm.kamix.app.ui.Model.Country;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileNumbersAdapter extends RecyclerView.Adapter<MobileNumbersAdapter.MobileNumberViewHolder> {
    private Context context;
    private List<String> mobiles;
    private List<Boolean> mobilesVerified;

    public MobileNumbersAdapter(Context context){
        this.context = context;
    }

    public void setData(List<String> mobiles, List<Boolean> mobilesVerified){
        this.mobiles = mobiles;
        this.mobilesVerified = mobilesVerified;
        notifyDataSetChanged();
    }

    @Override
    public MobileNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.mobile_number_itemview, parent, false);
        return new MobileNumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MobileNumberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mobiles==null?0:mobiles.size();
    }

    class MobileNumberViewHolder extends RecyclerView.ViewHolder{
        private Spinner spCountry;
        private TextView tvCode, tvUnVerify, etMob;
        private ImageButton popup;
        private int currentPosition;
        public MobileNumberViewHolder(View itemView) {
            super(itemView);
            spCountry = (Spinner) itemView.findViewById(R.id.spCountry);
            tvCode = (TextView) itemView.findViewById(R.id.tvCode);
            tvUnVerify = (TextView) itemView.findViewById(R.id.tvUnVerify);
            popup = itemView.findViewById(R.id.popup);
            etMob = itemView.findViewById(R.id.etMob);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final User user = ((UserInfoActivity)context).getUser();
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.mobiles_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.edit_mobile:{
                                    Intent intent = new Intent(context, EditMobileActivity.class);
                                    intent.putExtra(User.class.getSimpleName(), user);
                                    intent.putExtra("OLDMOBILE", etMob.getText().toString());
                                    ((UserInfoActivity) context).startActivityForResult(intent, ((UserInfoActivity) context).REQ_MODIFY);
                                } break;
                                case R.id.delete_mobile:{
                                    if (mobiles.size()>1){
                                        DelMobPINFragment delMobPINFragment = DelMobPINFragment.newInstance(user, etMob.getText().toString());
                                        delMobPINFragment.show(((UserInfoActivity) context).getSupportFragmentManager(), DelMobPINFragment.class.getSimpleName());
                                    }
                                    else{
                                        UiUtils.infoDialog(context, context.getString(R.string.you_cannot_have_less_than_one_mobile));
                                    }
                                } break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            tvUnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mobilesVerified.get(currentPosition)){
                        android.support.v7.app.AlertDialog alertDialog = UiUtils.questionDialog(context,
                                context.getString(R.string.verify_question).replace("?????", context.getString(R.string.mobile_verif_label)).replace("????", etMob.getText().toString()),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        redoVerificationMobile(mobiles.get(currentPosition));
                                    }
                                });
                        alertDialog.show();
                    }
                }
            });

            final ArrayList<Country> cArr = new ArrayList<Country>();
            cArr.add(new Country(R.drawable.cm, "+237"));
            spCountry.setAdapter(new CountrySpinnerAdapter(context, cArr));
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

        public void bind(int position){
            currentPosition = position;
            etMob.setText(mobiles.get(position).replace("+237", "").replace("00237", ""));
            if (mobilesVerified.get(position)) {
                tvUnVerify.setText(context.getString(R.string.verified));
                tvUnVerify.setTextColor(Color.GREEN);
            }
            else{
                tvUnVerify.setText(context.getString(R.string.unverified));
                tvUnVerify.setTextColor(Color.RED);
            }
        }
    }

    private String getMessageByErrorCode(int code){
        switch (code){
            case 1311: return context.getString(R.string.problem_occurs);
            case 1315: return context.getString(R.string.invalid_phone_or_email);
            case 171: return context.getString(R.string.invalid_pin);
            case 172: return context.getString(R.string.invalid_passwd_);
            default: return context.getString(R.string.connection_error);
        }
    }

    private void redoVerificationMobile(final String mobile){
        User user = ((UserInfoActivity)context).getUser();
        UserInterface userInterface = NetworkAPI.getClient().create(UserInterface.class);
        Call<UserInterface.UserResponse> redoVerifMobile = userInterface.redoVerificationMobile(mobile, user.getId());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(context, context.getString(R.string.please_wait));
        progressDialog.show();
        redoVerifMobile.enqueue(new Callback<UserInterface.UserResponse>() {
            @Override
            public void onResponse(Call<UserInterface.UserResponse> call, Response<UserInterface.UserResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(context, context.getString(R.string.connection_error));
                    Toast.makeText(context, "Null Body", Toast.LENGTH_LONG).show();
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(context, getMessageByErrorCode(response.body().getErrorCode()));
                    Log.e("REDO VERIF MOBILE", response.body().getErrorCode() + " error");
                }
                else{
                    User user = response.body().getUser();
                    DAO dao = new DAO(context);
                    dao.storeUser(user);
                    Toast.makeText(context, context.getString(R.string.mobile_modify_success), Toast.LENGTH_LONG).show();

                    VerifyMobileFragment verifyMobileFragment = VerifyMobileFragment.newInstance(user, mobile);
                    verifyMobileFragment.show(((UserInfoActivity) context).getSupportFragmentManager(), VerifyEmailFragment.class.getSimpleName());
                }
            }
            @Override
            public void onFailure(Call<UserInterface.UserResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(context, context.getString(R.string.connection_error));
                Log.e("REDO VERIF MOBILE", t.getMessage() + " error");
                t.printStackTrace();
            }
        });
    }
}
