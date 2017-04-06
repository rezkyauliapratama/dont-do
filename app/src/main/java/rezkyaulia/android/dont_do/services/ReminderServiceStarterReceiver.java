package rezkyaulia.android.dont_do.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 09/03/2017.
 */

public final class ReminderServiceStarterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.e("ReminderServiceStarterReceiver !!!!!");

        new ReminderEventReceiver().setupAlarm(context);
    }
}