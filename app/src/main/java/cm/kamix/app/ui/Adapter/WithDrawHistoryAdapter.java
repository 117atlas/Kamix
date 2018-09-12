package cm.kamix.app.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/19/2018.
 */

public class WithDrawHistoryAdapter extends BaseAdapter {
    public Context ct;

    public WithDrawHistoryAdapter(Context ct) {
        this.ct = ct;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(ct).inflate(R.layout.custom_transaction_row, null);
        return view;
    }
}
