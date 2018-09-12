package cm.kamix.app.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkAPI {

    //public static String SERVER_URL = "http://192.168.43.22:3003/";
    public static String SERVER_URL = "https://dry-forest-44382.herokuapp.com/";
    public static String BASE_URL = SERVER_URL+"kamix/";
    public static String TEST_CONNECTION = BASE_URL+"ping/";

    private static Retrofit retrofit = null;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static void setServerUrl(String server){
        SERVER_URL = "http://"+server+"/";
    }

    public static Retrofit getClient() {
        if (retrofit==null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat(DATE_FORMAT)
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    //.addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
