package cm.kamix.app.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/24/2018.
 */

public class TermsAndConditionAdapter extends BaseAdapter {
    public Context ct;

    public TermsAndConditionAdapter(Context ct) {
        this.ct = ct;
    }

    @Override
    public int getCount() {
        return 22;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    Holder holder;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(ct).inflate(R.layout.custom_termscondi_row, null);
            holder = new Holder();
            holder.tvText = (TextView) view.findViewById(R.id.tvText);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        int p = i + 1;
        holder.tvText.setText(p + ". " + ct.getResources().getString(R.string.terms_and_condition) + " #" + p);
        return view;
    }

    class Holder {
        TextView tvText;
    }
}
