package cm.kamix.app.ui.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 8/1/2018.
 */

public class TransferTransactionHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_transfer_transactionhistory,null);

        return v;
    }
}
