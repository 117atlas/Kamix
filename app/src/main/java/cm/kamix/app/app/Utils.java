package cm.kamix.app.app;

import java.util.ArrayList;

public class Utils {
    public static ArrayList<String> split(String s, String c){
        if (s==null || s.equalsIgnoreCase("")) return null;
        String temp = s.toString();
        ArrayList<String> res = new ArrayList<>();
        while (temp.contains(c)){
            res.add(temp.substring(0, temp.indexOf(c)));
            temp = temp.substring(temp.indexOf(c)+1, temp.length());
        }
        res.add(temp);
        return res;
    }
}
