package rezkyaulia.android.dont_do.controller.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rezkyaulia.android.dont_do.Utility.DimensionConverter;
import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.Model.Firebase.DateModel;
import rezkyaulia.android.dont_do.Model.Firebase.DetailHabit;
import rezkyaulia.android.dont_do.PreferencesManager;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.RxBus;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.controller.adapter.TaskRecyclerViewAdapter;
import rezkyaulia.android.dont_do.controller.fragment.HomeFragment;
import rezkyaulia.android.dont_do.controller.fragment.NavigationMenuFragment;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.database.entity.DetailActivityTbl;
import rezkyaulia.android.dont_do.database.entity.UserTbl;
import rezkyaulia.android.dont_do.databinding.ActivityMainBinding;
import rezkyaulia.android.dont_do.databinding.DialogAddActivityBinding;
import rezkyaulia.android.dont_do.databinding.DialogUpdateActivityBinding;
import rezkyaulia.android.dont_do.view.LayoutEyeInflate;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements
        BaseActivity.onListener,HomeFragment.OnFragmentViewInteraction,
        NavigationView.OnNavigationItemSelectedListener,
        NavigationMenuFragment.OnFragmentInteractionListener{


    ActivityMainBinding binding;
    LayoutEyeInflate layoutEyeInflate;

    Context mContext;

    private ActionBarDrawerToggle toggle;

    HomeFragment fragment;
    DrawerArrowDrawable arrowDrawable = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //activity
        setSupportActionBar(binding.toolbar);

        mContext = this;

        init();

        fragment = HomeFragment.newInstance();
        displayFragment(binding.layoutContainer.getId(),fragment);

        binding.drawerLayout.setScrimColor(Color.TRANSPARENT);

        initDrawableMenu();
        initNavigationView();

        Timber.e("UserKey : "+userKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateOffset(binding.drawerLayout.isDrawerOpen(binding.navView) ? 1 : 0);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    public void onRefreshToken(String token) {
        runOnUiThread(new Runnable() {
            public void run()
            {
                binding.layoutProgress.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onListItemRVInteraction(Habit habit) {
        Intent intent = new Intent(this,DetailTaskActivity.class);
        intent.putExtra(DetailTaskActivity.ARGS1,habit);
        startActivity(intent);
    }


    private void init(){
        if (token.isEmpty()){
            binding.layoutProgress.setVisibility(View.VISIBLE);
        }else{
            binding.layoutProgress.setVisibility(View.GONE);
        }

        layoutEyeInflate = new LayoutEyeInflate(this,binding.activityMain);
        layoutEyeInflate.setVisibility(View.GONE);
    }


    private void initDrawableMenu(){


        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public float defaultElevation = binding.drawerLayout.getDrawerElevation();
            public float defaultWidth = binding.appBar.getWidth();
            public float defaultHeight = binding.appBar.getHeight();


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN, drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (drawerView == binding.navView) {
                    updateOffset(slideOffset);
                    arrowDrawable.setProgress(slideOffset);
                    binding.drawerLayout.setDrawerElevation(0);
                }  else
                    binding.drawerLayout.setDrawerElevation(defaultElevation);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.navView.setElevation(0);
        }

       arrowDrawable = toggle.getDrawerArrowDrawable();
//        arrowDrawable.setBarLength(10);
        arrowDrawable.setBarLength(DimensionConverter.getInstance().stringToDimension("24dp", getResources().getDisplayMetrics()));
        arrowDrawable.setBarThickness(DimensionConverter.getInstance().stringToDimension("2dp", getResources().getDisplayMetrics()));
        arrowDrawable.setGapSize(DimensionConverter.getInstance().stringToDimension("8dp", getResources().getDisplayMetrics()));
        arrowDrawable.setColor(ContextCompat.getColor(this, (R.color.colorBlack_1000)));

        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        binding.navView.setNavigationItemSelectedListener(this);


    }


    private void initNavigationView(){
        NavigationMenuFragment navigationMenuFragment = NavigationMenuFragment.newInstance();
        displayFragment(R.id.nav_view, navigationMenuFragment);
    }

    private void updateOffset(float slideOffset) {
//        Timber.e("updateOffset :"+slideOffset);
//        Util.getInstance().hideKeyBoard(drawer);


        binding.navView.setAlpha(slideOffset);
        binding.activityMain.setX((binding.navView.getWidth() + 10f) * slideOffset);
        int defaultHeight = ((ViewGroup)binding.navView.getParent()).getHeight();
        ViewGroup.LayoutParams params
                = binding.activityMain.getLayoutParams();
//        Timber.e("coord height:"+params.height);
        float diffHeight = defaultHeight * (0.1f * slideOffset);
        if (slideOffset == 0)
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        else
            params.height = (int) (defaultHeight - diffHeight);

//        Timber.e("coord height:"+params.height+"|ELE :"+(20 * slideOffset));

        binding.activityMain.setLayoutParams(params);
        ViewCompat.setElevation(binding.activityMain, 20 * slideOffset);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onSignInInteraction(FirebaseUser user) {
        Timber.e("onSignInInteraction");
        mDatabase.child(Constant.getInstance().USERS).orderByChild(UserTbl.colEmail).equalTo(user.getEmail()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       Timber.e("onDataChange : "+new Gson().toJson(dataSnapshot.getValue()));

                       if (dataSnapshot.getValue() == null){
                           UserTbl userTbl = new UserTbl();
                           userTbl.setToken(token);
                           userTbl.setEmail(user.getEmail());
                           userTbl.setUserId(userKey);

                           Map<String, Object> childUpdates = new HashMap<>();
                           childUpdates.put("/" + userKey, userTbl);
                            Timber.e(Constant.getInstance().USERS+"/"+userKey);
                           mDatabase.child(Constant.getInstance().USERS).updateChildren(childUpdates);
                           Facade.getInstance().getManagerUserTbl().removeAll();
                           Facade.getInstance().getManagerUserTbl().add(userTbl);
                       }else{
                           for (DataSnapshot dataSnap : dataSnapshot.getChildren()){
                               UserTbl userTbl= dataSnap.getValue(UserTbl.class);
                               updateExisitingEmail(dataSnap.getKey(),userTbl);
                           }

                       }


                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {
                       Timber.e("onCancelled"+new Gson().toJson(databaseError));
                   }
               });
    }

    private void updateExisitingEmail(String key ,UserTbl userTbl){
        userTbl.setToken(token);
        userTbl.setUserId(key);

        Timber.e("updateExisitingEmail : "+new Gson().toJson(userTbl));
        mDatabase.child(Constant.getInstance().USERS).child(userKey).removeValue();


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key, userTbl);

        mDatabase.child(Constant.getInstance().USERS).updateChildren(childUpdates);

        Facade.getInstance().getManagerUserTbl().removeAll();
        Facade.getInstance().getManagerUserTbl().add(userTbl);
        PreferencesManager.getInstance().saveUserKey(key);
        RxBus.getInstance().post(userTbl);

        updateExistingActivity(key);
    }

    private void updateExistingActivity(String key){
        Timber.e("key Update: "+key);
        constant.PrimaryRef.child(userKey).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Timber.e("key : "+key);
                        Timber.e("Userkey : "+userKey);
                        if (!key.equals(userKey)){
                            Map<String, String> childUpdates = new HashMap<>();

                            for(DataSnapshot dataSnap : dataSnapshot.getChildren()){
                                ActivityTbl activityTbl= dataSnap.getValue(ActivityTbl.class);
                                Timber.e("update exist data : "+new Gson().toJson(activityTbl)+" | "+dataSnap.getKey());

                                DatabaseReference activityRef = constant.PrimaryRef.child(key).push();
                                activityRef.setValue(activityTbl);
                                String newkey = activityRef.getKey();
                                Timber.e("key Activity : "+key);
                                if (!newkey.isEmpty()){

                                    childUpdates.put(dataSnap.getKey(),newkey);
                                }

                            }

                            updateExistingDetailActivity(childUpdates);
                            constant.PrimaryRef.child(userKey).removeValue();
                            userKey = key;
                            PreferencesManager.getInstance().saveUserKey(key);
                            Timber.e("ONUPDATE DETAILS");
                            initFirebase(userKey);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




    }

    private void updateExistingDetailActivity(Map<String,String> hashMap){
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry me2 = (Map.Entry) iterator.next();
//            System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());

            constant.SecondaryPref.child(String.valueOf(me2.getKey())).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                                DetailActivityTbl detailActivityTbl = dataSnap.getValue(DetailActivityTbl.class);
                                Timber.e("detailActivityTbl : "+new Gson().toJson(detailActivityTbl));

                                DatabaseReference activityRef = constant.SecondaryPref.child(String.valueOf(me2.getValue())).push();
                                activityRef.setValue(detailActivityTbl);
                                String newkey = activityRef.getKey();
                                Timber.e("New key Activity : "+newkey);
                                if (!newkey.isEmpty()){

                                }
                            }

                            constant.SecondaryPref.child(String.valueOf(me2.getKey())).removeValue();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }

}



