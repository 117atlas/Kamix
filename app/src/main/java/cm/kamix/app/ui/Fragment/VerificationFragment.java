package cm.kamix.app.ui.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import cm.kamix.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerificationFragment extends DialogFragment {

    public static final int WHAT_MOBILE = 4;
    public static final int WHAT_EMAIL = 5;

    private int what = WHAT_MOBILE;
    private String target;

    private EditText etConfirmCode;
    private AppCompatButton skip, back, confirm;
    private TextView tvConfirmLabel;

    public VerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    public static VerificationFragment newInstance(int what, String target){
        VerificationFragment fragment = new VerificationFragment();
        Bundle args = new Bundle();
        args.putInt("What", what);
        args.putString("Target", target);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args!=null){
            what = args.getInt("What");
            target = args.getString("Target");
        }
        etConfirmCode = view.findViewById(R.id.confirm_code);
        skip = view.findViewById(R.id.skip);
        back = view.findViewById(R.id.back_modify);
        confirm = view.findViewById(R.id.confirm);
        if (what==WHAT_MOBILE){
            etConfirmCode.setInputType(InputType.TYPE_CLASS_NUMBER);
            tvConfirmLabel.setText(getString(R.string.verification_message_mobile));
        }
        else{
            etConfirmCode.setInputType(InputType.TYPE_CLASS_TEXT);
            tvConfirmLabel.setText(getString(R.string.verification_message_email));
        }

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                getActivity().finish();
                dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
