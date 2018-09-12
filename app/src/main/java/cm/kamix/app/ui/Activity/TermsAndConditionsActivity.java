package cm.kamix.app.ui.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import cm.kamix.app.ui.Adapter.TermsAndConditionAdapter;
import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/24/2018.
 */

public class TermsAndConditionsActivity extends AppCompatActivity {
    public ListView lvTermsCondi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        lvTermsCondi = (ListView) findViewById(R.id.lvTermsCondi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* tvTitle = (TextView) findViewById(R.id.tvTitle);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setVisibility(View.VISIBLE);
        tvTitle.setText("Terms and conditions");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

        lvTermsCondi.setAdapter(new TermsAndConditionAdapter(TermsAndConditionsActivity.this));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
