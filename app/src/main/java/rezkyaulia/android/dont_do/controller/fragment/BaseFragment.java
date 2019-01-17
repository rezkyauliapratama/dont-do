package rezkyaulia.android.dont_do.controller.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.PreferencesManager;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.RxBus;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.database.Facade;
import rezkyaulia.android.dont_do.database.entity.UserTbl;
import timber.log.Timber;


/**
 * Created by Shiburagi on 16/06/2016.
 */
public class BaseFragment extends Fragment {


    private static final String TAG = BaseFragment.class.getSimpleName();

    protected PreferencesManager pref;
    protected Util util;
    protected String token;
    protected String userKey;
    protected DatabaseReference mDatabase;
    protected Constant constant;
    protected UserTbl userTbl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferencesManager.getInstance();
        userTbl = Facade.getInstance().getManagerUserTbl().get();
        util = Util.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        token = pref.getToken();
        userKey = pref.getUserKey();

        Timber.e("token : "+token);
        Timber.e("userKey : "+userKey);
        constant = Constant.getInstance();

        RxBus.getInstance().observable(UserTbl.class).subscribe(s -> {
            Timber.e("subscribe token fragment : " + s);
            userKey = pref.getUserKey();
        });

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
