package cm.kamix.app.ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/19/2018.
 */

public class TrasactionAdapter extends BaseAdapter {

    public Context ct;

    public TrasactionAdapter(Context ct) {
        this.ct = ct;
    }

    @Override
    public int getCount() {
        return 15;
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
        if(view==null){
            view= LayoutInflater.from(ct).inflate(R.layout.custom_transaction_row,null);
            holder=new Holder();
            holder.tvAmount=(TextView)view.findViewById(R.id.tvAmount);
            view.setTag(holder);
        }else{
            holder=(Holder)view.getTag();
        }
        if(i==0||i==5||i==11||i==14){
            holder.tvAmount.setText("-$125");
            holder.tvAmount.setTextColor(ct.getResources().getColor(android.R.color.holo_red_dark));
        }else{
            holder.tvAmount.setText("$528");
            holder.tvAmount.setTextColor(ct.getResources().getColor(R.color.transaction_income_color));
        }
        return view;
    }
    class Holder{
        TextView tvAmount;
    }
}
