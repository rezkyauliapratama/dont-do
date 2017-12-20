package rezkyaulia.android.dont_do.controller.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.philliphsu.bottomsheetpickers.date.BottomSheetDatePickerDialog;
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.Model.Firebase.DateModel;
import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.controller.activity.DetailTaskActivity;
import rezkyaulia.android.dont_do.controller.adapter.DetailTastRecyclerViewAdapter;
import rezkyaulia.android.dont_do.controller.adapter.TaskRecyclerViewAdapter;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.database.entity.DetailActivityTbl;
import rezkyaulia.android.dont_do.databinding.FragmentDetailTaskBinding;
import rezkyaulia.android.dont_do.view.LayoutEyeInflate;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/2/2017.
 */

public class DetailTaskFragment extends BaseFragment {
    public final static String ARGS1 = "Args1";

    FragmentDetailTaskBinding binding;

    private Habit mHabit;



    Context mContext;

    DetailTastRecyclerViewAdapter mAdapter;


    public static DetailTaskFragment newInstance(Habit habit) {
        DetailTaskFragment fragment = new DetailTaskFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS1, habit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            Timber.e("SAVEDINSTACESTATE");
            mHabit = getArguments().getParcelable(ARGS1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_detail_task,container,false);

        if(savedInstanceState != null){
            Timber.e("SAVEDINSTACESTATE");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAdapterRV();

        initView();

    }

    private void initView(){
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimePicker();
            }
        });
    }

    List<DateModel> initData(){

        List<DetailActivityTbl> dateDetailActivityTbls = new ArrayList<>();
        dateDetailActivityTbls = Facade.getInstance().getManageDetailActivityTbl().getAll(mHabit.getActivityId());
       /* for (int i = 0 ; i<20;i++){
            DetailActivityTbl itm = new DetailActivityTbl();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,-i);

            DateModel dateModel = Util.getInstance().dateUtil().getDate(cal);
            itm.setYear(dateModel.getYear());
            itm.setMonth(dateModel.getMonth());
            itm.setDay(dateModel.getDay());
            itm.setTimestamp(dateModel.getTimestamp());
            dateDetailActivityTbls.add(itm);


            if (i==1){
                dateDetailActivityTbls.add(itm);
                dateDetailActivityTbls.add(itm);

            }
        }*/

        long tempDiffMilis = 0;

        int i = 0;
        List <DateModel> temps = new ArrayList<>();
        for (DetailActivityTbl item : dateDetailActivityTbls){
            DateModel dateModel = new DateModel();
            dateModel.setDay(item.getDay());
            dateModel.setMonth(item.getMonth());
            dateModel.setYear(item.getYear());
            dateModel.setTimestamp(item.getTimestamp());

            if (dateDetailActivityTbls.size() > 1){
                if(i<dateDetailActivityTbls.size()-1){
                    int nextI = i+1;
                    DetailActivityTbl nextItem = dateDetailActivityTbls.get(nextI);

                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTimeInMillis(item.getTimestamp());
                    cal1.set(Calendar.HOUR,0);
                    cal1.set(Calendar.MINUTE,0);
                    cal1.set(Calendar.SECOND,0);

                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTimeInMillis(nextItem.getTimestamp());
                    cal2.set(Calendar.HOUR,0);
                    cal2.set(Calendar.MINUTE,0);
                    cal2.set(Calendar.SECOND,0);

                    long diffMilis = cal1.getTimeInMillis() - cal2.getTimeInMillis();
                    if (diffMilis > 0){
                        if (tempDiffMilis<diffMilis){
                            tempDiffMilis = diffMilis;
                        }
                        dateModel.setShowLine(true);
                    }else{
                        dateModel.setShowLine(false);

                    }
                    dateModel.setRunningDay(diffMilis);

                }else{
                    dateModel.setShowLine(false);
                    dateModel.setRunningDay(0L);
                }
            }else{
                dateModel.setShowLine(false);
                dateModel.setRunningDay(0L);
            }
            i++;
            Timber.e("ITEM : "+new Gson().toJson(dateModel));
            temps.add(dateModel);
        }

        long days = TimeUnit.DAYS.convert(tempDiffMilis, TimeUnit.MILLISECONDS);
        if (days > 0){
            binding.layoutTrophy.setVisibility(View.VISIBLE);
            binding.textViewHeader.setText("Your best : "+days+" days");
        }else{
            binding.layoutTrophy.setVisibility(View.GONE);
        }

        if (temps.size() > 0){
            String howLong = Util.getInstance().dateUtil().getHowLongItHasBeen(temps.get(0));
            binding.textViewDays.setText("Your running day : "+howLong);
        }


        return temps;
    }

    private void initAdapterRV(){
        //List<DetailActivityTbl> dateDetailActivityTbls = Facade.getInstance().getManageDetailActivityTbl().getAll(mHabit.getActivityId());
        List<DateModel> items = initData();
        if (items!= null){
            mAdapter = new DetailTastRecyclerViewAdapter(getContext(),items);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerView.setAdapter(mAdapter);
        }
    }


    void setDateTimePicker(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog =
                DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
                        now.set(year, monthOfYear, dayOfMonth);
                        Timber.e("DATE : "+Util.getInstance().dateUtil().getUserFriendlyDate(now.getTime()));
                        initSetDetailHabit(now);
                    }
                },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setThemeDark(true);
        datePickerDialog.show(getChildFragmentManager(), this.getClass().getSimpleName());

    }


    void initSetDetailHabit(Calendar cal){
        DetailActivityTbl detailActivityTbl = new DetailActivityTbl();
        detailActivityTbl.setDay(cal.get(Calendar.DAY_OF_MONTH));
        detailActivityTbl.setMonth(cal.get(Calendar.MONTH));
        detailActivityTbl.setTimestamp(cal.getTimeInMillis());
        detailActivityTbl.setYear(cal.get(Calendar.YEAR));

        DatabaseReference detailPref = Constant.getInstance().SecondaryPref.child(mHabit.getActivityId()).push();
        detailPref.setValue(detailActivityTbl);
        String keyDetail = detailPref.getKey();
        Timber.e("keyDetail Activity : "+keyDetail);

        if (!keyDetail.isEmpty()){
            detailActivityTbl.setActivityId(mHabit.getActivityId());
            detailActivityTbl.setDetailActivityId(keyDetail);
            Facade.getInstance().getManageDetailActivityTbl().add(detailActivityTbl);

            List<DateModel> temps = initData();
            mAdapter.animateTo(temps);

        }
    }
}
