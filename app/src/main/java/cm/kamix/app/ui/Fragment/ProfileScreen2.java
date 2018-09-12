package cm.kamix.app.ui.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cm.kamix.app.models.User;
import cm.kamix.app.ui.Activity.UserInfoActivity;
import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/27/2018.
 */

public class ProfileScreen2 extends Fragment {
    public ImageView imgPrev;

    private EditText address, city, country;
    private TextView uploadIdDoc;
    private ImageView cni;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile2, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgPrev = (ImageView) view.findViewById(R.id.imgPrev);
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfoActivity.mPager.setCurrentItem(0, true);
            }
        });

        address = view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        country = view.findViewById(R.id.country);
        uploadIdDoc = view.findViewById(R.id.uploadiddoc);
        cni = view.findViewById(R.id.document);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = ((UserInfoActivity)getActivity()).getUser();
                switch (view.getId()){
                    case R.id.city:{
                        EditUserInfoFragment editUserInfoFragment = EditUserInfoFragment.newInstance(EditUserInfoFragment.WHAT_CITY, user.getCity(), user);
                        editUserInfoFragment.show(getFragmentManager(), EditUserInfoFragment.class.getSimpleName());
                        editUserInfoFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                bind();
                            }
                        });
                    } break;
                    case R.id.address:{
                        EditUserInfoFragment editUserInfoFragment = EditUserInfoFragment.newInstance(EditUserInfoFragment.WHAT_ADDRESS, user.getAddress(), user);
                        editUserInfoFragment.show(getFragmentManager(), EditUserInfoFragment.class.getSimpleName());
                        editUserInfoFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                bind();
                            }
                        });
                    } break;
                    case R.id.uploadiddoc:{

                    } break;
                }
            }
        };
        address.setOnClickListener(listener);
        city.setOnClickListener(listener);
        uploadIdDoc.setOnClickListener(listener);

        bind();
    }

    @Override
    public void onResume() {
        super.onResume();
        bind();
    }

    private void bind(){
        User user = ((UserInfoActivity)getActivity()).getUser();
        address.setText(user.getAddress());
        city.setText(user.getCity());
        country.setText(user.getCountry());
        //Charge cni
    }
}
