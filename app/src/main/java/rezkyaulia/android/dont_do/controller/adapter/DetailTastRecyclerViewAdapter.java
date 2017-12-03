package rezkyaulia.android.dont_do.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rezkyaulia.android.dont_do.Model.Firebase.DateModel;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.databinding.ItemDetailTaskBinding;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/2/2017.
 */

public class DetailTastRecyclerViewAdapter extends RecyclerView.Adapter<DetailTastRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<DateModel> mItems;


    private int lastPosition = -1;
    private int animationCount = 0;


    public DetailTastRecyclerViewAdapter(Context mContext, List<DateModel> mItems) {
        this.mContext = mContext;
        this.mItems = mItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_task, parent, false);
        return new DetailTastRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         DateModel item = mItems.get(position);
        Timber.e("pos: "+position+" | Adap : "+new Gson().toJson(item));
        Date date = new Date(item.getTimestamp());
        holder.binding.textViewDate.setText(Util.getInstance().dateUtil().getUserFriendlyDate(date));


        if (!item.isShowLine()){
            holder.binding.viewLine.setVisibility(View.GONE);
        }else{
            holder.binding.viewLine.setVisibility(View.VISIBLE);
        }

        if (item.getRunningDay() > 0){
            long days = TimeUnit.DAYS.convert(item.getRunningDay(), TimeUnit.MILLISECONDS);
            holder.binding.textVuewRunningDays.setText(days + " day");
        }else{
            holder.binding.textVuewRunningDays.setText("");
        }

        setAnimation(holder.binding.getRoot(),position);

    }


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

    public List<DateModel> getItems(){
        return this.mItems;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.binding.getRoot().setVisibility(View.VISIBLE);
        holder.binding.getRoot().clearAnimation();
    }

    public void animateTo(List<DateModel> mTempItems) {
        applyAndAnimateRemovals(mTempItems);
        applyAndAnimateAdditions(mTempItems);
        applyAndAnimateMovedItems(mTempItems);
    }

    private void applyAndAnimateRemovals(List<DateModel> mItemTemps) {
        for (int i = this.mItems.size() - 1; i >= 0; i--) {
            final DateModel nItems = this.mItems.get(i);
            if (!mItemTemps.contains(nItems)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<DateModel> mItemTemps) {
        Timber.e("applyAndAnimateAdditions");
        Timber.e("LIST DateModelS : "+new Gson().toJson(mItemTemps));
        for (int i = 0, count = mItemTemps.size(); i < count; i++) {
            final DateModel nItems = mItemTemps.get(i);
            Timber.e("DateModel : "+new Gson().toJson(nItems));

            if (!this.mItems.contains(nItems)) {
                Timber.e("====================================");
                Timber.e("NEW DateModel : "+new Gson().toJson(nItems));
                Timber.e("====================================");

                addItem(i, nItems);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<DateModel> mItemTemps) {
        for (int toPosition = mItemTemps.size() - 1; toPosition >= 0; toPosition--) {
            final DateModel nItems = mItemTemps.get(toPosition);
            final int fromPosition = this.mItems.indexOf(nItems);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public DateModel removeItem(int mPosition) {
        final DateModel mItems = this.mItems.remove(mPosition);
        notifyItemRemoved(mPosition);
        return mItems;
    }

    public void addItem(int mPosition, DateModel mItems) {
        this.mItems.add(mPosition, mItems);
        notifyItemInserted(mPosition);
    }

    public void moveItem(int mFromPosition, int mToPosition) {
        final DateModel mItems = this.mItems.remove(mFromPosition);
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
        final ItemDetailTaskBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = ItemDetailTaskBinding.bind(itemView);
        }


    }
}
