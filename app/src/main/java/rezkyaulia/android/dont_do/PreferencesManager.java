package rezkyaulia.android.dont_do;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mutya Nayavashti on 10/11/2016.
 */

public class PreferencesManager {

    private static PreferencesManager mInstance;
    private final Context mContext;

    public static PreferencesManager init(Context context){
        mInstance = new PreferencesManager(context);
        return mInstance;
    }

    public PreferencesManager(Context context) {
        this.mContext = context;
    }

    public static PreferencesManager getInstance() {
        return mInstance;
    }


    private boolean saveToSharedPref(HashMap<String,String> map){

        if (!map.isEmpty() && map != null){
            SharedPreferences prefs= mContext.getSharedPreferences(Constant.getInstance().NAME_PREF, mContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            for(Map.Entry<String, String> item: map.entrySet()) {
                String key = item.getKey();
                String value = item.getValue();
                if (!key.isEmpty())
                    editor.putString(key, value);

            }
            return editor.commit();
        }
        return false;
    }


    private String getSharedPref(String name){
        SharedPreferences prefs= mContext.getSharedPreferences(Constant.getInstance().NAME_PREF, mContext.MODE_PRIVATE);
        String pref=prefs.getString(name, "");
        return pref;
    }

    public String getToken(){
        return (getSharedPref(Constant.getInstance().TOKEN_PREF) != null) ? getSharedPref(Constant.getInstance().TOKEN_PREF) : "";
    }

    public boolean saveToken(String token){
        HashMap<String, String> map = new HashMap<>();
        map.put(Constant.getInstance().TOKEN_PREF, token);
        boolean b = saveToSharedPref(map);

        if (!getSharedPref(Constant.getInstance().EMAIL).isEmpty()){

        }else{

        }

        return b;
    }


    public String getUserKey(){
        return (getSharedPref(Constant.getInstance().USER_KEY_PREF) != null) ? getSharedPref(Constant.getInstance().USER_KEY_PREF) : "";
    }

    public boolean saveUserKey(String userKey){
        HashMap<String, String> map = new HashMap<>();
        map.put(Constant.getInstance().USER_KEY_PREF, userKey);
        boolean b = saveToSharedPref(map);


        return b;
    }

}
