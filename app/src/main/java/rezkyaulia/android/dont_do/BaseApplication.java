package rezkyaulia.android.dont_do;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.ViewConfiguration;

import com.app.infideap.stylishwidget.view.Stylish;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Field;

import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.controller.service.ReminderEventReceiver;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.DaoMaster;
import rezkyaulia.android.dont_do.database.entity.DaoSession;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 07/11/2016.
 */

public class BaseApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();



        /*initialize*/
        init();

    /*    new ReminderEventReceiver().setupAlarm(getApplicationContext());


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

*/
    }

    private void init(){
        FirebaseApp.initializeApp(this);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        PreferencesManager.init(this);
        Util.init(this);

//        initTimber();

        String fontFolder = "fonts/Exo_2/Exo2-";
        Stylish.getInstance().set(
                fontFolder.concat("Regular.ttf"),
                fontFolder.concat("Medium.ttf"),
                fontFolder.concat("RegularItalic.ttf")
        );

        PreferencesManager.getInstance().saveFontSize(1);
        Stylish.getInstance().setFontScale(
                PreferencesManager.getInstance().getFontSize()
        );

        String databaseName = Constant.getInstance().DB_NAME;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, databaseName);
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        Facade.init(daoSession);


        if (BuildConfig.DEBUG){
            Log.e("BaseApplication","is debug : "+BuildConfig.DEBUG);
            Timber.plant(new Timber.DebugTree());
            QueryBuilder.LOG_SQL = BuildConfig.DEBUG;
            QueryBuilder.LOG_VALUES = BuildConfig.DEBUG;
//            refWatcher = LeakCanary.install(this);

        }
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
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;



}
