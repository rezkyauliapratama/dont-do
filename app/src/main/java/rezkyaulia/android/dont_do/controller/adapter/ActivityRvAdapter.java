package rezkyaulia.android.dont_do.controller.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;

import rezkyaulia.android.dont_do.Constant;
import rezkyaulia.android.dont_do.FirebaseEvent;
import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.Model.Firebase.DateModel;
import rezkyaulia.android.dont_do.Model.Firebase.DetailHabit;
import rezkyaulia.android.dont_do.PreferencesManager;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.databinding.DialogUpdateActivityBinding;
import rezkyaulia.android.dont_do.databinding.ListItemTaskBinding;

/**
 * Created by Mutya Nayavashti on 24/02/2017.
 */

public class ActivityRvAdapter  extends RecyclerView.ViewHolder {

    public ListItemTaskBinding binding;
    private Context mContext;
    DateModel date;


    private int lastPosition = -1;
    private int animationCount = 0;


    public ActivityRvAdapter(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        mContext = itemView.getContext();
    }

    public void bind(final String key, Habit item,int position){
        binding.text01.setText(item.getName());

        /*Query query = FirebaseDatabase.getInstance().getReference().child(Constant.getInstance().DETAILS).child(key).orderByPriority().limitToFirst(1);
        FirebaseEvent event = new FirebaseEvent(query);
        event.addChildEventListener(new FirebaseEvent.onChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot item1 : dataSnapshot.getChildren()){

                    date = item1.getValue(DateModel.class);
//                    Timber.e(date.getTimestamp()+"");
                    binding.text02.setText(Util.getInstance().dateUtil().getHowLongItHasBeen(date));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
        });
*/
        setAnimation(binding.getRoot(), position);

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(final View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            final Animation animation = AnimationUtils.loadAnimation(
                    viewToAnimate.getContext(), android.R.anim.slide_in_left);
            animationCount++;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animationCount--;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(300);
            viewToAnimate.setVisibility(View.GONE);
            viewToAnimate.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewToAnimate.setVisibility(View.VISIBLE);
                    viewToAnimate.startAnimation(animation);

                }
            }, animationCount * 100);
            lastPosition = position;
        }
    }




}
