package rezkyaulia.android.dont_do;

import android.app.Application;
import android.content.Context;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import rezkyaulia.android.dont_do.Utility.Util;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 07/11/2016.
 */

public class App extends Application
{
    private  Context mContext;

    public static final String TAG = App.class
            .getSimpleName();




    @Override
    public void onCreate()
    {
        super.onCreate();


        mContext = this;

        /*initialize*/
        init();

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }


    }

    private void init(){
        PreferencesManager.init(this);
        Util.init(this);
        eventBus.instanceOf();
        Foreground.get(this).addListener(myListener);
        initTimber();
    }

    private void initTimber(){
//        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
//        }
    }


    Foreground.Listener myListener = new Foreground.Listener()
    {
        public void onBecameForeground()
        {
            /*String token = pref.getSharedPref(Constant.TOKEN_PREF);

            if (token.isEmpty()){
                token = FirebaseInstanceId.getInstance().getToken();
                Log.e(TAG,token);
                if (!token.isEmpty()){
                    HashMap<String,String> item = new HashMap<>();
                    item.put(Constant.TOKEN_PREF,token);
                    pref.saveToSharedPref(item);
                }
            }
*/
        }

        public void onBecameBackground()
        {
//            GcmService.setNotifPopUp(true);
        }
    };



}
