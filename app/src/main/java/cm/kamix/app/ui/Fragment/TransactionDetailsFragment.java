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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import cm.kamix.app.R;
import cm.kamix.app.localstorage.DAO;
import cm.kamix.app.models.Payment;
import cm.kamix.app.models.Transfert;
import cm.kamix.app.models.User;
import cm.kamix.app.network.BaseResponse;
import cm.kamix.app.network.NetworkAPI;
import cm.kamix.app.network.PaymentInterface;
import cm.kamix.app.network.TransfertInterface;
import cm.kamix.app.ui.Activity.TransactionSuccessfullyActivity;
import cm.kamix.app.ui.UiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionDetailsFragment extends DialogFragment implements TransactionPINFragment.DoTransaction {

    private TextView transactionDetailsHeader, message, receiverLabel;
    private EditText receiver, amountFees, total;
    private TextView proceed;
    private AppCompatButton cancel;

    private Transfert transfert;
    private Payment payment;

    public static final int DETAILS_TRANSFERT = 266;
    public static final int DETAILS_PAYMENT =269;

    private int what;

    public TransactionDetailsFragment() {
        // Required empty public constructor
    }

    public static TransactionDetailsFragment newInstance(Serializable transaction, int what){
        TransactionDetailsFragment transactionDetailsFragment = new TransactionDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("TRANSACTION", transaction);
        args.putInt("WHAT", what);
        transactionDetailsFragment.setArguments(args);
        return transactionDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_transaction_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transactionDetailsHeader = view.findViewById(R.id.transaction_details_header);
        message = view.findViewById(R.id.transaction_message);
        receiver = view.findViewById(R.id.receiver);
        receiverLabel = view.findViewById(R.id.receiver_label);
        amountFees = view.findViewById(R.id.amount_fees);
        total = view.findViewById(R.id.total);
        proceed = view.findViewById(R.id.proceed);
        cancel = view.findViewById(R.id.cancel);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPIN();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (what==DETAILS_TRANSFERT) cancelTransfert();
                else cancelPayment();
                dismiss();
            }
        });

        what = getArguments().getInt("WHAT");
        if (what==DETAILS_TRANSFERT) transfert = (Transfert) getArguments().getSerializable("TRANSACTION");
        else payment = (Payment) getArguments().getSerializable("TRANSACTION");

        if (what==DETAILS_TRANSFERT){
            transactionDetailsHeader.setText(getString(R.string.transfert_details_label));
            receiverLabel.setText(getString(R.string.receiver_label));
            receiver.setText((transfert.getReceiverName()==null||transfert.getReceiverName().equalsIgnoreCase(""))?transfert.getReceiverMobile():
                    transfert.getReceiverName()+" ("+transfert.getReceiverMobile()+")");
            amountFees.setText(transfert.getAmount()+" + "+transfert.getFees()+" = ");
            total.setText((transfert.getFees()+transfert.getAmount())+" XAF");
            message.setText(transfert.getMessage());
        }
        else{
            transactionDetailsHeader.setText(getString(R.string.payment_details_label));
            receiverLabel.setText(getString(R.string.customer_label));
            //receiver.setText((payment.getMerchantName()==null||payment.getMerchantName().equalsIgnoreCase(""))?payment.getCustomerMobile():
                    //payment.getCustomerName()+" ("+payment.getCustomerMobile()+")");
            receiver.setText(payment.getMerchantName());
            amountFees.setText(payment.getAmount()+" + "+payment.getFees()+" = ");
            total.setText((payment.getFees()+payment.getAmount())+" XAF");
            message.setText(payment.getMessage());
        }
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

    private void doTransfert(String pinCode){
        TransfertInterface.DoTransfertBody doTransfertBody = new TransfertInterface.DoTransfertBody();
        doTransfertBody.setPinCode(pinCode);
        doTransfertBody.setTransfert(transfert);
        TransfertInterface transfertInterface = NetworkAPI.getClient().create(TransfertInterface.class);
        Call<TransfertInterface.TransfertResponse> doTransfert = transfertInterface.doTransfert(doTransfertBody);
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.processing_transaction));
        progressDialog.show();
        doTransfert.enqueue(new Callback<TransfertInterface.TransfertResponse>() {
            @Override
            public void onResponse(Call<TransfertInterface.TransfertResponse> call, Response<TransfertInterface.TransfertResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    Log.e("DO TRANDFERT", "Body Null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==TransfertInterface.TRANSFERT_INCORRECT_PINCODE){
                    incorrectPINDialog();
                }
                else if (response.body().isError() && response.body().getErrorCode()==TransfertInterface.TRANSFERT_SUCCESS) {
                    transactionSuccess(null, null, "Money Transfer");
                    Log.e("DO TRANSFERT", "TRANSACTION MADE");
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(getContext(), getString(R.string.transaction_server_error));
                    Log.e("DO TRANSFERT", response.message() +" - "+response.body().getErrorCode());
                }
                else{
                    transactionSuccess(response.body().getTransfert(), response.body().getSender(),"Money Transfer");
                    //save transfert
                    dismiss();
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<TransfertInterface.TransfertResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }

    private void doPayment(String pinCode){
        PaymentInterface.DoPaymentBody doPaymentBody = new PaymentInterface.DoPaymentBody();
        doPaymentBody.setPinCode(pinCode);
        doPaymentBody.setPayment(payment);
        PaymentInterface paymentInterface = NetworkAPI.getClient().create(PaymentInterface.class);
        Call<PaymentInterface.PaymentResponse> doPayment = paymentInterface.doPayment(doPaymentBody);
        final ProgressDialog progressDialog = UiUtils.loadingDialog(getContext(), getString(R.string.processing_transaction));
        progressDialog.show();
        doPayment.enqueue(new Callback<PaymentInterface.PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentInterface.PaymentResponse> call, Response<PaymentInterface.PaymentResponse> response) {
                UiUtils.dismissLoadingDialog(progressDialog);
                if (response.body()==null){
                    UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                    Log.e("DO PAYMENT", "Body Null");
                }
                else if (response.body().isError() && response.body().getErrorCode()==PaymentInterface.PAYMENT_INCORRECT_PINCODE){
                    incorrectPINDialog();
                }
                else if (response.body().isError() && response.body().getErrorCode()==PaymentInterface.PAYMENT_SUCCESS) {
                    transactionSuccess(null, null, "Payment");
                    Log.e("DO PAYMENT", "TRANSACTION MADE");
                }
                else if (response.body().isError()){
                    UiUtils.infoDialog(getContext(), getString(R.string.transaction_server_error));
                    Log.e("DO PAYMENT", response.message() +" - "+response.body().getErrorCode());
                }
                else{
                    //TODO: save payment
                    transactionSuccess(response.body().getPayment(), response.body().getCustomer(), "Payment");
                    dismiss();
                    getActivity().finish();
                }
            }
            @Override
            public void onFailure(Call<PaymentInterface.PaymentResponse> call, Throwable t) {
                UiUtils.dismissLoadingDialog(progressDialog);
                UiUtils.infoDialog(getContext(), getString(R.string.connection_error));
                t.printStackTrace();
            }
        });
    }

    @Override
    public void doTransaction(String pinCode) {
        if (what==DETAILS_TRANSFERT) doTransfert(pinCode);
        else doPayment(pinCode);
    }

    private void cancelTransfert(){
        TransfertInterface transfertInterface = NetworkAPI.getClient().create(TransfertInterface.class);
        Call<BaseResponse> cancelTransfert = transfertInterface.cancelTransfert(transfert.getId());
        cancelTransfert.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body()!=null && !response.body().isError()){
                    Toast.makeText(getContext(), getString(R.string.transaction_cancelled), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void cancelPayment(){
        PaymentInterface paymentInterface = NetworkAPI.getClient().create(PaymentInterface.class);
        Call<BaseResponse> cancelPayment = paymentInterface.cancelPayment(payment.getId());
        cancelPayment.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body()!=null && !response.body().isError()){
                    Toast.makeText(getContext(), getString(R.string.transaction_cancelled), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void cancel() {

    }
}
