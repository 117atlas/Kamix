package cm.kamix.app.ui.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import cm.kamix.app.ui.Adapter.WithDrawHistoryAdapter;
import cm.kamix.app.R;

/**
 * Created by Yash Ajabiya on 7/19/2018.
 */

public class WithDrawHistoryActivity extends AppCompatActivity {
    public ListView lvWithdraw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvWithdraw = (ListView) findViewById(R.id.lvWithdraw);
        lvWithdraw.setAdapter(new WithDrawHistoryAdapter(WithDrawHistoryActivity.this));
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
