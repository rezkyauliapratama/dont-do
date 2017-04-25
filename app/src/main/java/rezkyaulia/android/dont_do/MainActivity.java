package rezkyaulia.android.dont_do;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import rezkyaulia.android.dont_do.Models.Firebase.Activity;
import rezkyaulia.android.dont_do.Models.Firebase.DetailActivity;
import rezkyaulia.android.dont_do.databinding.ActivityMainBinding;
import rezkyaulia.android.dont_do.databinding.DialogAddActivityBinding;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements BaseActivity.onListener{

    private ProgressBarInflate mProgressBarInflate;

    ActivityMainBinding binding;

    private FirebaseRecyclerAdapter mFirebaseAdapter;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mProgressBarInflate = new ProgressBarInflate(this,binding.activityMain);

        mContext = this;

        init();

        setAdapterRv();
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
        mDatabase.child(Constant.instanceOf().ACTIVITIES).child(userKey).orderByChild("active").equalTo(true).addChildEventListener(new ChildEventListener() {

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

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                customDialog();

                startActivity(new Intent(getBaseContext(),DetailTaskActivity.class));
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
                        activityRef.orderByPriority()) {

            @Override
            protected void populateViewHolder(ActivityRvAdapter viewHolder, Activity model, int position) {
                String key = this.getRef(position).getKey();
                viewHolder.bind(key,model);
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
//                DateModel date = ;

                if (!text.isEmpty()) {
                    Activity activity = new Activity(text);

                    DatabaseReference activityRef = constant.PrimaryRef.child(userKey).push();
                    activityRef.setValue(activity);
                    String key = activityRef.getKey();

                    Constant.instanceOf().PrimaryRef.child(userKey).child(key).setPriority(-(activity.date.getTimestamp()));

                    if (!key.isEmpty()){
                        DetailActivity detailActivity = new DetailActivity(activity.date);
                        DatabaseReference detailPref = Constant.instanceOf().SecondaryPref.child(key).push();
                        detailPref.setValue(detailActivity);
                        String keyDetail = detailPref.getKey();
                        constant.SecondaryPref.child(key).child(keyDetail).setPriority(-(detailActivity.date.getTimestamp()));
                    }
                    dialog.hide();
                }
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    }

}
