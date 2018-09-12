package cm.kamix.app.ui.Model;

import java.io.Serializable;

/**
 * Created by Yash Ajabiya on 7/24/2018.
 */

public class Country implements Serializable {

    public String code;
    public int flag;

    public Country (int f,String c){
        this.code=c;
        this.flag=f;
    }

    public int getFlag() {
        return flag;
    }

    public String getCode() {

        return code;
    }
}
