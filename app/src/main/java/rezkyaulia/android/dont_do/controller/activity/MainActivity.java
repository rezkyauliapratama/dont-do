package rezkyaulia.android.dont_do.controller.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
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
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.controller.adapter.TaskRecyclerViewAdapter;
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
        BaseActivity.onListener,TaskRecyclerViewAdapter.OnRecyclerViewInteraction,
        NavigationView.OnNavigationItemSelectedListener,
        NavigationMenuFragment.OnFragmentInteractionListener{


    ActivityMainBinding binding;
    LayoutEyeInflate layoutEyeInflate;

//    private FirebaseRecyclerAdapter mFirebaseAdapter;

    Context mContext;

    TaskRecyclerViewAdapter mAdapter;

    private ActionBarDrawerToggle toggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //activity
        setSupportActionBar(binding.toolbar);


        binding.rvLayout.swipeRefreshLayout.setEnabled(false);
        mContext = this;

        init();

        initAdapterRV();
//        setAdapterRv();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });

        initDrawableMenu();
        initNavigationView();

        Timber.e("UserKey : "+userKey);
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onListItemInteraction(Habit habit) {
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
                    updateOffset(slideOffset);
                    binding.drawerLayout.setDrawerElevation(0);

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.navView.setElevation(0);
        }

        DrawerArrowDrawable arrowDrawable = toggle.getDrawerArrowDrawable();
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
        Timber.e("updateOffset()");
//        Util.getInstance().hideKeyBoard(drawer);

        int defaultHeight = ((ViewGroup) binding.navView.getParent()).getHeight();

        binding.navView.setAlpha(slideOffset);
        binding.activityMain.setX((binding.navView.getWidth() + 10f) * slideOffset);
        ViewGroup.LayoutParams params
                = binding.activityMain.getLayoutParams();
        float diffHeight = defaultHeight * (0.1f * slideOffset);
        if (slideOffset == 0)
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        else
            params.height = (int) (defaultHeight - diffHeight);

        binding.activityMain.setLayoutParams(params);
        ViewCompat.setElevation(binding.activityMain, 20 * slideOffset);

    }

    private void initAdapterRV(){
        layoutEyeInflate.setVisibility(View.VISIBLE);
        List<ActivityTbl> activityTbls = Facade.getInstance().getManageActivityTbl().getAll();
        List<Habit> habits = new ArrayList<>();

        if (activityTbls != null){
            for(ActivityTbl item : activityTbls){
                Habit habit = new Habit();

                DetailActivityTbl detailActivityTbl = Facade.getInstance().getManageDetailActivityTbl().getUniqeNew(item.getActivityId());

                /*Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(detailActivityTbl.getTimestamp());*/
                DateModel dateModel = Util.getInstance().dateUtil().getDate(detailActivityTbl.getTimestamp());

                habit.setActivityId(item.getActivityId());
                habit.setName(item.getName());
                habit.setDateModel(dateModel);

                habits.add(habit);

            }

            Collections.sort(habits, new Comparator<Habit>() {
                @Override
                public int compare(Habit lhs, Habit rhs) {
                    return Long.compare(rhs.getDateModel().getTimestamp(),lhs.getDateModel().getTimestamp());
                }
            });
        }

        mAdapter = new TaskRecyclerViewAdapter(this,this,habits);
        binding.rvLayout.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLayout.recyclerView.setAdapter(mAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long milis = TimeUnit.SECONDS.toMillis(3);
                    Timber.e("milis : "+milis);
                    Thread.sleep(milis);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layoutEyeInflate.setVisibility(View.GONE);

                        }
                    });
                } catch (InterruptedException e) {
                    Timber.e("ERROR THREAD : "+e.getMessage());
                }


            }
        }).start();



    }


   private void setAdapterRv (){
       Timber.e("User Key : ".concat(userKey));
       /*DatabaseReference activityRef = mDatabase.child(Constant.getInstance().ACTIVITIES).child(userKey);
       mFirebaseAdapter = new FirebaseRecyclerAdapter<Habit, ActivityRvAdapter>
                (Habit.class, R.layout.list_item_task, ActivityRvAdapter.class,
                        activityRef.orderByPriority()) {


            @Override
            protected void populateViewHolder(ActivityRvAdapter viewHolder, Habit model, int position) {
                String key = this.getRef(position).getKey();
                Timber.e("key : "+key+" | model : "+new Gson().toJson(model));
                viewHolder.bind(key,model,position);
            }



           @Override
           public void onViewDetachedFromWindow(ActivityRvAdapter holder) {
               super.onViewDetachedFromWindow(holder);
               holder.binding.getRoot().setVisibility(View.VISIBLE);
               holder.binding.getRoot().clearAnimation();
           }


       };
*/
//        binding.rvLayout.recyclerView.setHasFixedSize(true);
       /*List<ActivityTbl> activityTbls = Facade.getInstance().getManageActivityTbl().getAll();
        binding.rvLayout.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLayout.recyclerView.setAdapter(new TaskRecyclerViewAdapter(this,activityTbls));*/

/*

        activityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Timber.e("onDataChanged : "+dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

    }
    private void customDialog(){
        final DialogAddActivityBinding dialogBinding;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_activity, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        final Calendar c = Calendar.getInstance();

        final DateModel[] dateModel = {new DateModel()};

        dialogBinding.edit02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year,monthOfYear,dayOfMonth/*,0,0*/);
                                dateModel[0] = Util.getInstance().dateUtil().getDate(c);
                                dialogBinding.edit02.setText(Util.getInstance().dateUtil().getDateFromFirebase(dateModel[0]));

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });


        dialogBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = dialogBinding.edit01.getText().toString();
                Timber.e("ON CLICK");
                if (!text.isEmpty()) {
                    ActivityTbl activityTbl = new ActivityTbl();
                    activityTbl.setName(text);
                    activityTbl.setActive(true);
                    activityTbl.setCreatedDate(dateModel[0].getTimestamp());

                    DatabaseReference activityRef = constant.PrimaryRef.child(userKey).push();
                    activityRef.setValue(activityTbl);
                    String key = activityRef.getKey();
                    Timber.e("key Activity : "+key);
                    if (!key.isEmpty()){
                        activityTbl.setUserId(userKey);
                        activityTbl.setActivityId(key);
                        long id = Facade.getInstance().getManageActivityTbl().add(activityTbl);

                        Timber.e("id add activity : "+id);
                        DetailActivityTbl detailActivityTbl = new DetailActivityTbl();
                        detailActivityTbl.setDay(dateModel[0].getDay());
                        detailActivityTbl.setMonth(dateModel[0].getMonth());
                        detailActivityTbl.setTimestamp(dateModel[0].getTimestamp());
                        detailActivityTbl.setYear(dateModel[0].getYear());

                        DatabaseReference detailPref = Constant.getInstance().SecondaryPref.child(key).push();
                        detailPref.setValue(detailActivityTbl);
                        String keyDetail = detailPref.getKey();
                        Timber.e("keyDetail Activity : "+keyDetail);

                        if (!keyDetail.isEmpty()){
                            detailActivityTbl.setActivityId(key);
                            detailActivityTbl.setDetailActivityId(keyDetail);
                            Facade.getInstance().getManageDetailActivityTbl().add(detailActivityTbl);

                            Habit habit = new Habit();
                            habit.setActivityId(activityTbl.getActivityId());
                            habit.setName(activityTbl.getName());
                            habit.setDateModel(Util.getInstance().dateUtil().getDate(detailActivityTbl.getTimestamp()));
                            addHabit(habit);

                        }
//                        constant.SecondaryPref.child(key).child(keyDetail).setPriority(-(detailHabit.date.getTimestamp()));
                    }

//                    Constant.getInstance().PrimaryRef.child(userKey).child(key).setPriority(-(dateModel[0].getTimestamp()));
/*
                    if (!key.isEmpty()){

                    }*/
                    dialog.hide();
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private void addHabit(Habit habit){
        layoutEyeInflate.setVisibility(View.VISIBLE);
        final List<Habit> tempHabits = new ArrayList<>();
        tempHabits.addAll(mAdapter.getItems());

        tempHabits.add(habit);

        Collections.sort(tempHabits, new Comparator<Habit>() {
            @Override
            public int compare(Habit lhs, Habit rhs) {
                return Long.compare(rhs.getDateModel().getTimestamp(),lhs.getDateModel().getTimestamp());
            }
        });

        Timber.e("animateTo");
        mAdapter.animateTo(tempHabits);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long milis = TimeUnit.SECONDS.toMillis(3);
                    Timber.e("milis : "+milis);
                    Thread.sleep(milis);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layoutEyeInflate.setVisibility(View.GONE);

                        }
                    });
                } catch (InterruptedException e) {
                    Timber.e("ERROR THREAD : "+e.getMessage());
                }


            }
        }).start();

    }

    private void customDialog(final String key){
        final DialogUpdateActivityBinding dialogBinding;
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final Calendar c = Calendar.getInstance();

        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_update_activity, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.edit01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year,monthOfYear,dayOfMonth,0,0,0);
                                DateModel date = Util.getInstance().dateUtil().getDate(c);
                                dialogBinding.edit01.setText(Util.getInstance().dateUtil().getDateFromFirebase(date));

                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        });

        dialogBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailHabit detailHabit = new DetailHabit(Util.getInstance().dateUtil().getDate(c));
                DatabaseReference save  = Constant.getInstance().SecondaryPref.child(key).push();
                save.setValue(detailHabit);
                String saveKey = save.getKey();

                Constant.getInstance().SecondaryPref.child(key).child(saveKey).setPriority(-(detailHabit.date.getTimestamp()));
                Constant.getInstance().PrimaryRef.child(PreferencesManager.getInstance().getUserKey()).child(key).setPriority(-(detailHabit.date.getTimestamp()));
                if (!saveKey.isEmpty()){
                    dialog.hide();
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

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
                           userTbl.setUserId(user.getUid());


                           Map<String, Object> childUpdates = new HashMap<>();
                           childUpdates.put("/" + userKey, userTbl);
                            Timber.e(Constant.getInstance().USERS+"/"+userKey);
                           mDatabase.child(Constant.getInstance().USERS).updateChildren(childUpdates);
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

        Timber.e("updateExisitingEmail : "+new Gson().toJson(userTbl));
        mDatabase.child(Constant.getInstance().USERS).child(userKey).removeValue();


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key, userTbl);

        mDatabase.child(Constant.getInstance().USERS).updateChildren(childUpdates);
        updateExistingActivity(key);
    }

    private void updateExistingActivity(String key){
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



