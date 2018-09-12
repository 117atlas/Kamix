package cm.kamix.app.ui.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

import cm.kamix.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionPINFragment extends DialogFragment {

    public interface DoTransaction extends Serializable{
        void doTransaction(String pinCode);
        void cancel();
    }

    private DoTransaction doTransaction;

    private EditText etPin;
    private TextView proceed;
    private AppCompatButton cancel;

    public TransactionPINFragment() {
        // Required empty public constructor
    }

    public static TransactionPINFragment newInstance(DoTransaction doTransaction){
        TransactionPINFragment transactionPINFragment = new TransactionPINFragment();
        Bundle args = new Bundle();
        args.putSerializable("DOTRANSACTION", doTransaction);
        transactionPINFragment.setArguments(args);
        return transactionPINFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_transaction_pin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etPin = view.findViewById(R.id.etPin);
        proceed = view.findViewById(R.id.proceed);
        cancel = view.findViewById(R.id.cancel);
        doTransaction = (DoTransaction) getArguments().getSerializable("DOTRANSACTION");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doTransaction!=null) doTransaction.cancel();
                dismiss();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPin.getText().toString().equalsIgnoreCase("")){
                    etPin.setError(getString(R.string.empty_pin));
                    return;
                }
                if (etPin.getText().toString().length()<4){
                    etPin.setError(getString(R.string.invalid_pin));
                    return;
                }
                if (doTransaction!=null) doTransaction.doTransaction(etPin.getText().toString());
                dismiss();
            }
        });
    }
}
