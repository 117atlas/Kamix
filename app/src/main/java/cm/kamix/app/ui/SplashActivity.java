package cm.kamix.app.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.models.User;
import cm.kamix.app.ui.Activity.DashboardActivity;
import cm.kamix.app.ui.Activity.MainActivity;

public class SplashActivity extends AppCompatActivity {
    public FrameLayout frm;
    public Runnable run;
    public int i = 0;
    public Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        frm = (FrameLayout) findViewById(R.id.frm);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        frm.startAnimation(animationFadeIn);

        User user = KamixApp.getUser(this);
        System.out.println();
        if (user!=null){
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra(User.class.getSimpleName(), user);
            startActivity(intent);
            finish();
        }
        else{
            run = new Runnable() {
                @Override
                public void run() {
                    if (i == 80) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        i = i + 10;
                        mHandler.postDelayed(run, 200);
                    }
                }
            };
            mHandler = new Handler();
            mHandler.postDelayed(run, 200);
        }
    }


}
