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


    private boolean saveToStringPref(HashMap<String,String> map){

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

    private boolean saveToFloatPref(HashMap<String,Float> map){

        if (!map.isEmpty() && map != null){
            SharedPreferences prefs= mContext.getSharedPreferences(Constant.getInstance().NAME_PREF, mContext.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            for(Map.Entry<String, Float> item: map.entrySet()) {
                String key = item.getKey();
                float value = item.getValue();
                if (!key.isEmpty())
                    editor.putFloat(key, value);

            }
            return editor.commit();
        }
        return false;
    }


    private String getStringPref(String name){
        SharedPreferences prefs= mContext.getSharedPreferences(Constant.getInstance().NAME_PREF, mContext.MODE_PRIVATE);
        String pref=prefs.getString(name, "");
        return pref;
    }

    private float getFloatPref(String name){
        SharedPreferences prefs= mContext.getSharedPreferences(Constant.getInstance().NAME_PREF, mContext.MODE_PRIVATE);
        float pref=prefs.getFloat(name, 0);
        return pref;
    }

    public String getToken(){
        return (getStringPref(Constant.getInstance().TOKEN_PREF) != null) ? getStringPref(Constant.getInstance().TOKEN_PREF) : "";
    }

    public boolean saveToken(String token){
        HashMap<String, String> map = new HashMap<>();
        map.put(Constant.getInstance().TOKEN_PREF, token);
        boolean b = saveToStringPref(map);

        if (!getStringPref(Constant.getInstance().EMAIL).isEmpty()){

        }else{

        }

        return b;
    }


    public String getUserKey(){
        return (getStringPref(Constant.getInstance().USER_KEY_PREF) != null) ? getStringPref(Constant.getInstance().USER_KEY_PREF) : "";
    }

    public boolean saveUserKey(String userKey){
        HashMap<String, String> map = new HashMap<>();
        map.put(Constant.getInstance().USER_KEY_PREF, userKey);
        boolean b = saveToStringPref(map);


        return b;
    }

    public float getFontSize(){
        return getFloatPref(Constant.getInstance().FONT_SIZE_PREF);
    }

    public boolean saveFontSize(float fontSize){
        HashMap<String, Float> map = new HashMap<>();
        map.put(Constant.getInstance().FONT_SIZE_PREF, fontSize);
        boolean b = saveToFloatPref(map);


        return b;
    }


}
