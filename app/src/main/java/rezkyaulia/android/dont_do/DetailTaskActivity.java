package rezkyaulia.android.dont_do;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import rezkyaulia.android.dont_do.BaseActivity;
import rezkyaulia.android.dont_do.databinding.ActivityDetailTaskBinding;

/**
 * Created by Mutya Nayavashti on 25/04/2017.
 */

public class DetailTaskActivity extends BaseActivity implements BaseActivity.onListener {

    ActivityDetailTaskBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_task);

    }

    @Override
    public void onRefreshToken(String token) {

    }
}
