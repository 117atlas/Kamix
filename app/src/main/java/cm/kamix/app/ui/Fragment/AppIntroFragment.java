package cm.kamix.app.ui.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppIntroFragment extends Fragment {

    public static AppIntroFragment newInstance(int layoutId){
        AppIntroFragment appIntroFragment = new AppIntroFragment();
        Bundle args = new Bundle();
        args.putInt("LAYOUT_ID", layoutId);
        appIntroFragment.setArguments(args);
        return appIntroFragment;
    }

    public AppIntroFragment() {
        // Required empty public constructor
    }

    private int layoutId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(layoutId, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey("LAYOUT_ID"))
            layoutId = getArguments().getInt("LAYOUT_ID");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
