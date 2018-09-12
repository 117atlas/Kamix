package cm.kamix.app.ui.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cm.kamix.app.R;


/**
 * Created by Yash Ajabiya on 3/16/2018.
 */

public class FirstIntroFragment extends Fragment {
    public TextView tvText, tvThought;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first_intro, null);

        tvText = (TextView) v.findViewById(R.id.tvText);
        tvThought = (TextView) v.findViewById(R.id.tvThought);
        tvText.setText(getArguments().getString("msg") + "");
        tvThought.setText(getResources().getString(getArguments().getInt("thought")) + ""); // set text as per getting from adapter
        return v;
    }

    public static FirstIntroFragment newInstance(String text, int thought) {

        FirstIntroFragment f = new FirstIntroFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        b.putInt("thought", thought);
        f.setArguments(b);

        return f;
    }
}
