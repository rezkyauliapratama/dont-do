package rezkyaulia.android.dont_do.controller.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;


import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 09/03/2017.
 */
public class ReminderIntentService extends IntentService {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public ReminderIntentService() {
        super(ReminderIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context,String category) {
        Intent intent = new Intent(context, ReminderIntentService.class);
        intent.setAction(ACTION_START);
        intent.setType(category);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, ReminderIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.e( "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                Timber.e( "onHandleIntent, started handling a notification event");
                String category = intent.getType();
                Timber.e("Category : "+category);
                Toast.makeText(this,category,Toast.LENGTH_LONG);
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {

    }

    private void processStartNotification(int notification_type) {

    }

    @Override
    public void onDestroy() {
        // I want to restart this service again in one hour
        Timber.e("onDestroy service , reminder event receiver restart");
        new ReminderEventReceiver().setupAlarm(getApplicationContext());

    }
}