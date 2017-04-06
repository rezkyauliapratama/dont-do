package rezkyaulia.android.dont_do.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.Utility.Util;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 09/03/2017.
 */

public class ReminderEventReceiver extends WakefulBroadcastReceiver {

    private static final String ACTION_START_SERVICE = "ACTION_START_SERVICE";
    private static final String ACTION_DELETE_SERVICE = "ACTION_DELETE_NOTIFICATION";


    public void setupAlarm(Context context) {

        Timber.e("ReminderEventReceiver !!!!!");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context, Constant.instanceOf().EVERYHOUR_RUNNING_SERVICE);

        int interval  = 1000 * 60 ;

        if(Build.VERSION.SDK_INT < 23){
            if(Build.VERSION.SDK_INT >= 19){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        Util.getInstance().dateUtil().getTriggerEveryHours(),
                        alarmIntent);


            }
            else{
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        Util.getInstance().dateUtil().getTriggerEveryHours(),
                        alarmIntent);

            }
        }
        else{
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    Util.getInstance().dateUtil().getTriggerEveryHours(),
                    alarmIntent);


        }


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String category = intent.getType();
        Intent serviceIntent = null;
        Timber.e("onReceive !!!!");
        if (ACTION_START_SERVICE.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
            serviceIntent = ReminderIntentService.createIntentStartNotificationService(context,category);
        } else if (ACTION_DELETE_SERVICE.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive delete notification action, starting notification service to handle delete");
            serviceIntent = ReminderIntentService.createIntentDeleteNotification(context);
        }

        if (serviceIntent != null) {
            startWakefulService(context, serviceIntent);
        }
    }


    private static PendingIntent getStartPendingIntent(Context context,String category) {
        Intent intent = new Intent(context, ReminderEventReceiver.class);
        intent.setAction(ACTION_START_SERVICE);
        intent.setType(category);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, ReminderEventReceiver.class);
        intent.setAction(ACTION_DELETE_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}