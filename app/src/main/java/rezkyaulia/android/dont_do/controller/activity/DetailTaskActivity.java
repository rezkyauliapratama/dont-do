package rezkyaulia.android.dont_do.controller.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.controller.fragment.DetailTaskFragment;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.databinding.ActivityDetailTaskBinding;

/**
 * Created by Mutya Nayavashti on 25/04/2017.
 */

public class DetailTaskActivity extends BaseActivity implements BaseActivity.onListener {
    public static final String ARGS1 = "args1";

    ActivityDetailTaskBinding binding;

    private Habit mHabit;
    private DetailTaskFragment mDetailTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_task);

        if (getIntent() != null){
            mHabit = getIntent().getParcelableExtra(ARGS1);
        }


        if (mHabit != null){
            binding.actionbarTitle.setText(mHabit.getName());
            mDetailTaskFragment = DetailTaskFragment.newInstance(mHabit);
            displayFragment(binding.container.framelayout.getId(),mDetailTaskFragment);
        }



    }

    @Override
    public void onRefreshToken(String token) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Facade.getInstance().getDaoSession().clear();
    }
}
