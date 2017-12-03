package rezkyaulia.android.dont_do.controller.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import rezkyaulia.android.dont_do.PreferencesManager;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.UserTbl;


/**
 * Created by Shiburagi on 16/06/2016.
 */
public class BaseFragment extends Fragment {


    private static final String TAG = BaseFragment.class.getSimpleName();

    protected PreferencesManager pref;

    protected UserTbl userTbl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferencesManager.getInstance();
        userTbl = Facade.getInstance().getManagerUserTbl().get();

    }




    @Override
    public void onResume() {
        super.onResume();

    }
    protected int getColorPrimary() {
        return ContextCompat.getColor(getContext(), R.color.colorPrimary);
    }

    public void update() {

    }
}
