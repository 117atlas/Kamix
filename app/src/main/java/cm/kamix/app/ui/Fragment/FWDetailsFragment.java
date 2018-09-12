package cm.kamix.app.ui.Fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Locale;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.Funding;
import cm.kamix.app.models.User;
import cm.kamix.app.models.Withdrawal;
import cm.kamix.app.network.BaseResponse;
import cm.kamix.app.network.FundingInterface;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.models.WithdrawalInterface;
import cm.kamix.app.ui.Activity.TransactionSuccessfullyActivity;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FWDetailsFragment extends DialogFragment implements TransactionPINFragment.DoTransaction{
    private TextView title;
    private TextView details;
    private TextView proceed;
    private AppCompatButton cancel;

    private Serializable transaction;
    private Funding funding;
    private Withdrawal withdrawal;
    private int what;
    public static final int DETAILS_FUNDING = 6;
    public static final int DETAILS_WITHDRAWAL = 7;

    private User user;

    public FWDetailsFragment() {
        // Required empty public constructor
    }

    public static FWDetailsFragment newInstance(Serializable transaction, int what){
        FWDetailsFragment fwDetailsFragment = new FWDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("TRANSACTION", transaction);
        args.putInt("WHAT", what);
        fwDetailsFragment.setArguments(args);
        return fwDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_fwpin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = KamixApp.getUser(getContext());

        transaction = getArguments().getSerializable("TRANSACTION");
        what = getArguments().getInt("WHAT");
        if (what==DETAILS_FUNDING) funding = (Funding) transaction;
        else withdrawal = (Withdrawal) transaction;

        title = view.findViewById(R.id.title);
        details = view.findViewById(R.id.text);
        cancel = view.findViewById(R.id.cancel);
        proceed = view.findViewById(R.id.proceed);

        if (what==DETAILS_FUNDING){
            title.setText(getString(R.string.funding_details_label));
            details.setText(getString(R.string.funding_details_text).replace("??????", funding.isForMe()?getString(R.string.your):funding.getReceiverName())
                    .replace("?????", funding.getAmount()+" XAF")
                .replace("????", funding.getMobileNumber()));
        }
        else{
            title.setText(getString(R.string.withdrawal_details_label));
            details.setText(getString(R.string.withdrawal_details_text).replace("??????", withdrawal.getAmount()+" XAF")
                    .replace("?????", withdrawal.getMobileNumber()));
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (what==DETAILS_FUNDING) cancelFunding();
                else cancelWithdrawal();
                dismiss();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPIN();
            }
        });
    }

    private void requestPIN(){
        TransactionPINFragment transactionPINFragment = TransactionPINFragment.newInstance(this);
        transactionPINFragment.show(getFragmentManager(), TransactionPINFragment.class.getSimpleName());
    }

    private void incorrectPINDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.transaction_incorrect_pin_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPIN();
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void transactionSuccess(Serializable transaction, User user, String data){
        if (user!=null) {
            DAO dao = new DAO(getContext());
            dao.storeUser(user);
        }
        Intent intent = new Intent(getContext(), TransactionSuccessfullyActivity.class);
        intent.putExtra("TRANSACTION", transaction);
        intent.putExtra("data", data);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void doTransaction(String pinCode) {
        if (what==DETAILS_FUNDING) beginFunding(pinCode);
        else beginWithdrawal(pinCode);
    }

    @Override
    public void cancel() {

    }

    private void beginFunding(final String pincode){
        FundingInterface fundingInterface = NetworkAPI.getClient().create(FundingInterface.class);
        Call<FundingInterface.BeginFundingResponse> begin = fundingInterface.begin(user.getId(), funding.getId(), pincode, Locale.getDefault().getLanguage());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.processing_transaction));
        progressDialog.show();
        begin.enqueue(new Callback<FundingInterface.BeginFundingResponse>() {
            @Override
            public void onResponse(Call<FundingInterface.BeginFundingResponse> call, Response<FundingInterface.BeginFundingResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    Log.e("BEGIN FUNDING", "Body Null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_INCORRECT_PINCODE){
                    incorrectPINDialog();
                }
                else if (response.body().isError() && response.body().getErrorCode()==FundingInterface.FUNDING_WECASHUP_ERROR) {
                    UiUtils.questionDialog(getContext(), getString(R.string.we_cash_up_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            beginFunding(pincode);
                        }
                    });
                    Log.e("BEGIN FUNDING", "We cash up server error");
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(getContext(), getString(R.string.transaction_server_error));
                    Log.e("BEGIN FUNDING", response.message() +" - "+response.body().getErrorCode());
                }
                else{
                    //transactionSuccess(response.body().getTransfert(), response.body().getSender(),"Money Transfer");
                    //save transfert
                    dismiss();
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<FundingInterface.BeginFundingResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }

    private void beginWithdrawal(final String pincode){
        WithdrawalInterface withdrawalInterface = NetworkAPI.getClient().create(WithdrawalInterface.class);
        Call<WithdrawalInterface.BeginWithdrawalResponse> begin = withdrawalInterface.begin(user.getId(), withdrawal.getId(), pincode, Locale.getDefault().getLanguage());
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.processing_transaction));
        progressDialog.show();
        begin.enqueue(new Callback<WithdrawalInterface.BeginWithdrawalResponse>() {
            @Override
            public void onResponse(Call<WithdrawalInterface.BeginWithdrawalResponse> call, Response<WithdrawalInterface.BeginWithdrawalResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    Log.e("BEGIN WITHDRAWAL", "Body Null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==WithdrawalInterface.WITHDRAWAL_INCORRECT_PINCODE){
                    incorrectPINDialog();
                }
                else if (response.body().isError() && response.body().getErrorCode()==WithdrawalInterface.WITHDRAWAL_WECASHUP_ERROR) {
                    UiUtils.questionDialog(getContext(), getString(R.string.we_cash_up_error), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            beginWithdrawal(pincode);
                        }
                    });
                    Log.e("BEGIN WITHDRAWAL", "We cash up server error");
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(getContext(), getString(R.string.transaction_server_error));
                    Log.e("BEGIN WITHDRAWAL", response.message() +" - "+response.body().getErrorCode());
                }
                else{
                    //transactionSuccess(response.body().getTransfert(), response.body().getSender(),"Money Transfer");
                    //save transfert
                    dismiss();
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<WithdrawalInterface.BeginWithdrawalResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }

    private void cancelFunding(){
        FundingInterface fundingInterface = NetworkAPI.getClient().create(FundingInterface.class);
        Call<BaseResponse> cancel = fundingInterface.cancel(funding.getId());
        cancel.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body()!=null && !response.body().isError()){
                    //Toast.makeText(getContext(), getString(R.string.transaction_cancelled), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void cancelWithdrawal(){
        WithdrawalInterface withdrawalInterface = NetworkAPI.getClient().create(WithdrawalInterface.class);
        Call<BaseResponse> cancel = withdrawalInterface.cancel(withdrawal.getId());
        cancel.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body()!=null && !response.body().isError()){
                    //Toast.makeText(getContext(), getString(R.string.transaction_cancelled), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
