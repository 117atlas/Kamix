package cm.kamix.app.ui.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cm.kamix.app.R;
import cm.kamix.app.models.Contact;
import cm.kamix.app.ui.Fragment.FirstIntroFragment;
import cm.kamix.app.ui.UiUtils;

/**
 * Created by Yash Ajabiya on 7/17/2018.
 */

public class MainActivity extends AppCompatActivity {

    public TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        ViewPager pager = (ViewPager) findViewById(R.id.photos_viewpager); //cast viewpager
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout); // cast tabindicator
        tabLayout.setupWithViewPager(pager, true);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class)); // call signupscreen
                finish();
            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            /*  fragment call with text which is show in pannel*/
            switch (pos) {
                case 0:
                    return FirstIntroFragment.newInstance("Transfer Money for free to family & friends", R.string.tagline);
                case 1:
                    return FirstIntroFragment.newInstance("Settle payments in 1000+ physical shops", R.string.tagline1);
                case 2:
                    return FirstIntroFragment.newInstance("Get bitcoin rewards for each transaction", R.string.tagline2);
                case 3:
                    return FirstIntroFragment.newInstance("Fund your account using mobile money", R.string.tagline3);
                case 4:
                    return FirstIntroFragment.newInstance("Withdraw your funds from anywhere", R.string.tagline4);
                default:
                    return FirstIntroFragment.newInstance("Transfer Money for free to family & friends", R.string.tagline);
            }
        }

        @Override
        public int getCount() {
            return 5; // number of fragments
        }
    }
}
