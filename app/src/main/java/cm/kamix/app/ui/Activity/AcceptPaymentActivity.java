package cm.kamix.app.ui.Activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import net.glxn.qrgen.android.QRCode;

import cm.kamix.app.R;
import cm.kamix.app.app.KamixApp;
import cm.kamix.app.models.User;
import cm.kamix.app.ui.UiUtils;

public class AcceptPaymentActivity extends AppCompatActivity {
    private ImageView qrCodeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_payment);
        qrCodeContainer = findViewById(R.id.qrcode_container);

        User me = KamixApp.getUser(this);
        if (me.noName()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.complete_profile_name))
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    })
                    .setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            String qrCodeText = me.getId()+"|"+me.name();
            Bitmap bitmap = QRCode.from(qrCodeText).withSize(1000, 1000).bitmap();
            qrCodeContainer.setImageBitmap(bitmap);
        }

    }
}
