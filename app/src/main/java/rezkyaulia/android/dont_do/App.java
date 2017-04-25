package rezkyaulia.android.dont_do;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.view.ViewConfiguration;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;

import java.lang.reflect.Field;

import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.services.ReminderEventReceiver;
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

        new ReminderEventReceiver().setupAlarm(getApplicationContext());


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

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        enabledStrictMode();
        LeakCanary.install(this);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    private static void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                .detectAll() //
                .penaltyLog() //
                .penaltyDeath() //
                .build());
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
