package rezkyaulia.android.dont_do;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import rezkyaulia.android.dont_do.Models.Activity.Activity;
import rezkyaulia.android.dont_do.databinding.ActivityMainBinding;
import rezkyaulia.android.dont_do.databinding.DialogAddActivityBinding;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements BaseActivity.onListener{

    private ProgressBarInflate mProgressBarInflate;

    ActivityMainBinding binding;

    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mProgressBarInflate = new ProgressBarInflate(this,binding.activityMain);

        init();

//        setAdapterRv();
        /*mDatabase.child("users").orderByChild("token").equalTo(token).addListenerForSingleValueEvent(new ValueEventListener() {
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
*//*
        *//*mDatabase.child(Constant.instanceOf().ACTIVITIES).orderByChild(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                   for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                       Activity activity= messageSnapshot.getValue(Activity.class);
                       Timber.e("Activity : "+activity.getName());
                   }
                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });*/

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
        mFirebaseAdapter.cleanup();

    }

    private void init(){
        if (token.isEmpty()){
            mProgressBarInflate.setVisibility(View.VISIBLE);
            mProgressBarInflate.setTitle("initialize");
        }
    }



    @Override
    public void onRefreshToken(String token) {
        runOnUiThread(new Runnable() {
            public void run()
            {
                if (mProgressBarInflate.getVisibility() == View.VISIBLE){
                    mProgressBarInflate.setVisibility(View.GONE);
                }
            }
        });


    }


   private void setAdapterRv (){
        DatabaseReference activityRef = mDatabase.child(Constant.instanceOf().ACTIVITIES).child(userKey);
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Activity, ActivityRvAdapter>
                (Activity.class, R.layout.list_item_task, ActivityRvAdapter.class,
                        activityRef) {

            @Override
            protected void populateViewHolder(ActivityRvAdapter viewHolder, Activity model, int position) {
                viewHolder.bind(model);
            }
        };

        binding.rvLayout.recyclerView.setHasFixedSize(true);
        binding.rvLayout.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLayout.recyclerView.setAdapter(mFirebaseAdapter);

    }
    private void customDialog(){
        final DialogAddActivityBinding dialogBinding;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_activity, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = dialogBinding.edit01.getText().toString();
//                DateActivity date = ;

                if (!text.isEmpty()) {
                    Activity activity = new Activity(text);
                    Timber.e("gson : "+ new Gson().toJson(activity));

                    mDatabase.child(Constant.instanceOf().ACTIVITIES).child(userKey).push().setValue(activity);
                    dialog.hide();
                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    }

}
