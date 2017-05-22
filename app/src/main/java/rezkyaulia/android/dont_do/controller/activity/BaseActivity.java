package rezkyaulia.android.dont_do.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.Model.Firebase.User;
import rezkyaulia.android.dont_do.PreferencesManager;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.eventBus;
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

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.zoom_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.do_nothing, R.anim.slid_right);
    }

    /*fragment control*/
    public void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(id, fragment).commit();

    }

    public void displayFragment(int id, Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
        } catch (Exception e) {

        }

    }

    public void removeFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }



    public interface onListener {
        void onRefreshToken(String token);
    }

}
