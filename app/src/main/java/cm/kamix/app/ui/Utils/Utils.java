package cm.kamix.app.ui.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yash Ajabiya on 7/17/2018.
 */

public class Utils {

    public static void showToast(String msg, Context ct) {
        Toast.makeText(ct, "" + msg, Toast.LENGTH_LONG).show();
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isValidMobile(CharSequence target) {
        Pattern pattern = Pattern.compile("^[+]2376[98765432][0-9]{7}$");
        Matcher matcher = pattern.matcher(target);
        return matcher.matches();
    }
}
