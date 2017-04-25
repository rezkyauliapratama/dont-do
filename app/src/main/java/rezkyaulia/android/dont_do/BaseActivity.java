package rezkyaulia.android.dont_do;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import rezkyaulia.android.dont_do.Models.Firebase.User;
import rezkyaulia.android.dont_do.Utility.Util;
import rx.Observer;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Rezky Aulia on 10/11/2016.
 */

public abstract class BaseActivity extends AppCompatActivity  {
    protected final String TAG = this.getClass().getSimpleName();
    protected PreferencesManager pref;
    protected Util util;
    protected String token;
    protected String userKey;
    protected DatabaseReference mDatabase;
    protected Constant constant;

    Context mContext;
    private onListener mListener;
    private Subscription mSubs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (onListener) this;

        mContext = this;

        pref = PreferencesManager.getInstance();
        util = Util.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        token = pref.getToken();
        userKey = pref.getUserKey();

        Timber.e("token : "+token);
        Timber.e("userKey : "+userKey);
        constant = Constant.instanceOf();

        getTokenObservable();

    }

    private void getTokenObservable(){
        mSubs = eventBus.instanceOf().getTokenObservable().
                subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Timber.e("subscribe token : "+s);

                        boolean isSave = pref.saveToken(s);

                        if (isSave){
                            User usr = new User(s);
                            DatabaseReference newRef = mDatabase.child(Constant.instanceOf().USERS).push();
                            newRef.setValue(usr);

                            token = s;
                            userKey = newRef.getKey();

                            boolean b = pref.saveUserKey(userKey);

                            mListener.onRefreshToken(s);

                        }

                    }
                });
    }
    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubs.unsubscribe();
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume of base");
        this.registerReceiver(mMessageReceiver, new IntentFilter("token"));

        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mKey = intent.getStringExtra("mKey");
           // Log.e(TAG,"broadcast token : "+mKey);
           // mListener.onRefreshToken(mKey);


        }
    };

    public interface onListener {
        void onRefreshToken(String token);
    }

}
