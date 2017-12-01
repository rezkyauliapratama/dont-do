package rezkyaulia.android.dont_do.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;

import java.util.List;

import rezkyaulia.android.dont_do.Model.Firebase.Habit;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.database.entity.ActivityTbl;
import rezkyaulia.android.dont_do.databinding.ItemTaskBinding;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/1/2017.
 */

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
    Context mContext;
    List<Habit> mItems;


    private int lastPosition = -1;
    private int animationCount = 0;

    public TaskRecyclerViewAdapter(Context mContext, List<Habit> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @Override
    public TaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskRecyclerViewAdapter.ViewHolder holder, int position) {
        Habit item = mItems.get(position);

        holder.binding.text01.setText(item.getName());
        holder.binding.text02.setText(Util.getInstance().dateUtil().getHowLongItHasBeen(item.getDateModel()));


        holder.binding.lottieView.postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.binding.lottieView.playAnimation();

            }
        },  animationCount*150);

        setAnimation(holder.binding.getRoot(),position);
    }

    private void setAnimation(final View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
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
//        }
    }

    public List<Habit> getItems(){
        return this.mItems;
    }
    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.binding.getRoot().setVisibility(View.VISIBLE);
        holder.binding.getRoot().clearAnimation();
    }

    public void animateTo(List<Habit> mTempItems) {
        applyAndAnimateRemovals(mTempItems);
        applyAndAnimateAdditions(mTempItems);
        applyAndAnimateMovedItems(mTempItems);
    }

    private void applyAndAnimateRemovals(List<Habit> mItemTemps) {
        for (int i = this.mItems.size() - 1; i >= 0; i--) {
            final Habit nItems = this.mItems.get(i);
            if (!mItemTemps.contains(nItems)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Habit> mItemTemps) {
        Timber.e("applyAndAnimateAdditions");
        Timber.e("LIST HABITS : "+new Gson().toJson(mItemTemps));
        for (int i = 0, count = mItemTemps.size(); i < count; i++) {
            final Habit nItems = mItemTemps.get(i);
            Timber.e("Habit : "+new Gson().toJson(nItems));

            if (!this.mItems.contains(nItems)) {
                Timber.e("====================================");
                Timber.e("NEW Habit : "+new Gson().toJson(nItems));
                Timber.e("====================================");

                addItem(i, nItems);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Habit> mItemTemps) {
        for (int toPosition = mItemTemps.size() - 1; toPosition >= 0; toPosition--) {
            final Habit nItems = mItemTemps.get(toPosition);
            final int fromPosition = this.mItems.indexOf(nItems);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Habit removeItem(int mPosition) {
        final Habit mItems = this.mItems.remove(mPosition);
        notifyItemRemoved(mPosition);
        return mItems;
    }

    public void addItem(int mPosition, Habit mItems) {
        this.mItems.add(mPosition, mItems);
        notifyItemInserted(mPosition);
    }

    public void moveItem(int mFromPosition, int mToPosition) {
        final Habit mItems = this.mItems.remove(mFromPosition);
        this.mItems.add(mToPosition, mItems);
        notifyItemMoved(mFromPosition, mToPosition);
    }



    @Override
    public int getItemCount() {
        int count = 0;
        if (mItems !=null )
            count = mItems.size();

        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTaskBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemTaskBinding.bind(itemView);

        }
    }
}
