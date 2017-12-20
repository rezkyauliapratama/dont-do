package rezkyaulia.android.dont_do.controller.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.Model.Firebase.DateModel;
import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.controller.adapter.TaskRecyclerViewAdapter;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.database.entity.DetailActivityTbl;
import rezkyaulia.android.dont_do.databinding.DialogAddActivityBinding;
import rezkyaulia.android.dont_do.databinding.FragmentHomeBinding;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/4/2017.
 */

public class HomeFragment extends BaseFragment {

    FragmentHomeBinding binding;
    private OnFragmentViewInteraction mListener;

    TaskRecyclerViewAdapter mAdapter;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        /*Bundle args = new Bundle();
        args.putParcelable(ARGS1, habit);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false);

        if(savedInstanceState != null){
            Timber.e("SAVEDINSTACESTATE");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.layoutContent.swipeRefreshLayout.setEnabled(false);

        initAdapterRV();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });

    }





    private void customDialog(){
        final DialogAddActivityBinding dialogBinding;
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_add_activity, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        final Calendar c = Calendar.getInstance();

        final DateModel[] dateModel = {new DateModel()};

        dialogBinding.edit02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            layoutEyeInflate.setVisibility(View.GONE);

                        }
                    });
                } catch (InterruptedException e) {
                    Timber.e("ERROR THREAD : "+e.getMessage());
                }


            }
        }).start();

    }


    private void initAdapterRV(){
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

        mAdapter = new TaskRecyclerViewAdapter(getContext(),mListener,habits);
        binding.layoutContent.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.layoutContent.recyclerView.setAdapter(mAdapter);

      /*  new Thread(new Runnable() {
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

*/

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentViewInteraction) {
            mListener = (OnFragmentViewInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentViewInteraction{
        // TODO: Update argument type and name
        void onListItemRVInteraction(Habit habit);

    }
}
