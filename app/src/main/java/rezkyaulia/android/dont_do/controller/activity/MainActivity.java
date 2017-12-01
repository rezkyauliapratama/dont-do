package rezkyaulia.android.dont_do.controller.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ActionMenuView;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rezkyaulia.android.dont_do.controller.adapter.ActivityRvAdapter;
import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.Model.Firebase.DateModel;
import rezkyaulia.android.dont_do.Model.Firebase.DetailHabit;
import rezkyaulia.android.dont_do.PreferencesManager;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.controller.adapter.TaskRecyclerViewAdapter;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.database.entity.DetailActivityTbl;
import rezkyaulia.android.dont_do.databinding.ActivityMainBinding;
import rezkyaulia.android.dont_do.databinding.DialogAddActivityBinding;
import rezkyaulia.android.dont_do.databinding.DialogUpdateActivityBinding;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements BaseActivity.onListener{


    ActivityMainBinding binding;

//    private FirebaseRecyclerAdapter mFirebaseAdapter;

    Context mContext;

    TaskRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void init(){
        if (token.isEmpty()){


        }
    }

    @Override
    public void onRefreshToken(String token) {
        runOnUiThread(new Runnable() {
            public void run()
            {

            }
        });
    }

    private void initAdapterRV(){
        List<ActivityTbl> activityTbls = Facade.getInstance().getManageActivityTbl().getAll();
        List<Habit> habits = new ArrayList<>();

        if (activityTbls != null){
            for(ActivityTbl item : activityTbls){
                Habit habit = new Habit();

                DetailActivityTbl detailActivityTbl = Facade.getInstance().getManageDetailActivityTbl().get(item.getActivityId());

                /*Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(detailActivityTbl.getTimestamp());*/
                DateModel dateModel = Util.getInstance().dateUtil().getDate(detailActivityTbl.getTimestamp());

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

        mAdapter = new TaskRecyclerViewAdapter(this,habits);
        binding.rvLayout.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLayout.recyclerView.setAdapter(mAdapter);
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

                        if (!keyDetail.isEmpty()){
                            detailActivityTbl.setActivityId(key);
                            detailActivityTbl.setDetailActivityId(keyDetail);
                            Facade.getInstance().getManageDetailActivityTbl().add(detailActivityTbl);

                            Habit habit = new Habit();
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



}


  /* mDatabase.child("users").orderByChild("token").equalTo(token).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Timber.e("datasnapshot : " + dataSnapshot.getValue()+"");
                }else{
                    Timber.e("! exist");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Timber.e(databaseError.getMessage()+"");
            }
        });
*/
      /*  mDatabase.child(Constant.instanceOf().ACTIVITIES).child(userKey).orderByChild("active").equalTo(true).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    mDatabase.child(Constant.instanceOf().DETAILS).child(dataSnapshot.getKey()).orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        if (dataSnapshot.exists()){
                                            Timber.e("datasnapshot : " + dataSnapshot.getValue()+"");
                                        }else{
                                            Timber.e("! exist");
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                );
                }else{
                    Timber.e("! exist");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
*/