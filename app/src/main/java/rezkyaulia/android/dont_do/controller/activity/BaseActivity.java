package rezkyaulia.android.dont_do.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.Model.FirebaseToken;
import rezkyaulia.android.dont_do.PreferencesManager;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.RxBus;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.database.entity.DetailActivityTbl;
import rezkyaulia.android.dont_do.database.entity.UserTbl;
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
        constant = Constant.getInstance();

        getTokenObservable();

        initFirebase(userKey);
    }

    private void getTokenObservable(){
       RxBus.getInstance().observable(FirebaseToken.class).subscribe(s -> {
           Timber.e("subscribe token : "+new Gson().toJson(s));

           boolean isSave = pref.saveToken(s.getToken());

           if (isSave){
               UserTbl usr = new UserTbl();
               usr.setToken(s.getToken());
               DatabaseReference newRef = mDatabase.child(Constant.getInstance().USERS).push();
               newRef.setValue(usr);

               token = s.getToken();
               userKey = newRef.getKey();

               boolean b = pref.saveUserKey(userKey);

               if (!userKey.isEmpty()){
                   usr.setUserId(userKey);
                   Facade.getInstance().getManagerUserTbl().add(usr);
                   RxBus.getInstance().post(usr);

               }
               mListener.onRefreshToken(s.getToken());

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


    public void initFirebase(String child){
        Timber.e("initFirebase userKey : "+userKey);
        Constant.getInstance().PrimaryRef.child(child).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Timber.e("onChildAdded PrimaryRef: "+dataSnapshot.getKey()+" | "+new Gson().toJson(dataSnapshot.getValue())+" | "+child);
                ActivityTbl habit= dataSnapshot.getValue(ActivityTbl.class);
                Timber.e("onChildAdded habit : "+new Gson().toJson(habit));
                if (habit.getName() != null){
                    addHabitDB(dataSnapshot.getKey(),child,habit);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Timber.e("onChildChanged PrimaryRef: "+dataSnapshot.getKey()+" | "+new Gson().toJson(dataSnapshot.getValue())+" | "+child);
                ActivityTbl habit= dataSnapshot.getValue(ActivityTbl.class);
                Timber.e("onChildChanged habit : "+new Gson().toJson(habit));
                if (habit.getName() != null){
                    addHabitDB(dataSnapshot.getKey(),child,habit);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Timber.e("onChildRemoved PrimaryRef: "+dataSnapshot.getKey()+" | "+new Gson().toJson(dataSnapshot.getValue())+" | "+child);
                Habit habit= dataSnapshot.getValue(Habit.class);
                Timber.e("onChildRemoved habit : "+new Gson().toJson(habit));
                if (habit.getName() != null){
                    removeHabitDB(dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Timber.e("onChildMoved PrimaryRef: "+dataSnapshot.getKey()+" | "+new Gson().toJson(dataSnapshot.getValue())+" | "+child);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void addHabitDB(String key,String userKey, ActivityTbl habit){
        habit.setUserId(userKey);
        habit.setActivityId(key);
        Facade.getInstance().getManageActivityTbl().add(habit);
        initDetailActivity(habit);
    }

    void removeHabitDB(String key){
        ActivityTbl activityTbl = Facade.getInstance().getManageActivityTbl().get(key);
        Facade.getInstance().getManageActivityTbl().remove(activityTbl);
        initDetailActivity(activityTbl);

    }


    void initDetailActivity(ActivityTbl activityTbl){
        Timber.e("inidetailActivity : "+new Gson().toJson(activityTbl));
        Constant.getInstance().SecondaryPref.child(activityTbl.getActivityId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnap : dataSnapshot.getChildren()){
                    Timber.e("SecondaryPref OnChange : "+new Gson().toJson(datasnap.getValue())+" | "+datasnap.getKey());
                    DetailActivityTbl detailActivityTbl = datasnap.getValue(DetailActivityTbl.class);
                    detailActivityTbl.setDetailActivityId(datasnap.getKey());
                    detailActivityTbl.setActivityId(activityTbl.getActivityId());
                    Timber.e("detailActivityTbl : "+new Gson().toJson(detailActivityTbl));
                    Facade.getInstance().getManageDetailActivityTbl().add(detailActivityTbl);
                }
                RxBus.getInstance().post(activityTbl);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

    public interface onListener {
        void onRefreshToken(String token);
    }

}
