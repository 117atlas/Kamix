package cm.kamix.app.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;

import cm.kamix.app.app.KamixApp;
import cm.kamix.app.models.User;
import cm.kamix.app.ui.Fragment.ProfileScreen1;
import cm.kamix.app.ui.Fragment.ProfileScreen2;
import cm.kamix.app.ui.Utils.NonSwipeableViewPager;
import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/27/2018.
 */

public class UserInfoActivity extends AppCompatActivity {
    public static final int REQ_MODIFY = 8888;
    public static NonSwipeableViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private ProfileScreen1 profileScreen1 = new ProfileScreen1();
    private ProfileScreen2 profileScreen2 = new ProfileScreen2();

    private User user;

    public User getUser(){
        if (user==null) user = KamixApp.getUser(this);
        return user;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mPager = (NonSwipeableViewPager) findViewById(R.id.vViewPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager()); // set pager
        mPager.setAdapter(mPagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return profileScreen1; // call profile framgnet

                case 1:
                    return profileScreen2;// call profile framgnet

                default:
                    return profileScreen1;// call profile framgnet

            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ_MODIFY && resultCode==RESULT_OK){
            try{
                if (profileScreen1!=null) profileScreen1.bind();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
