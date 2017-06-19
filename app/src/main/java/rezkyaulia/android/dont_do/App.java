package rezkyaulia.android.dont_do;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.view.ViewConfiguration;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.lang.reflect.Field;

import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.controller.service.ReminderEventReceiver;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 07/11/2016.
 */

public class App extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();



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
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        PreferencesManager.init(this);
        Util.init(this);
        eventBus.instanceOf();
        Foreground.get(this).addListener(myListener);
        initTimber();

      /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);*/

    }



    private void initTimber(){
            Timber.plant(new Timber.DebugTree());
    }

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;


    Foreground.Listener myListener = new Foreground.Listener()
    {
        public void onBecameForeground()
        {
        }

        public void onBecameBackground()
        {
        }
    };



}
