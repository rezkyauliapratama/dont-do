package rezkyaulia.android.dont_do;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import rezkyaulia.android.dont_do.Models.Activity.Activity;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.databinding.ListItemTaskBinding;

/**
 * Created by Mutya Nayavashti on 24/02/2017.
 */

public class ActivityRvAdapter  extends RecyclerView.ViewHolder {

    private ListItemTaskBinding binding;
    private Context mContext;

    public ActivityRvAdapter(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        mContext = itemView.getContext();
    }

    public void bind(Activity item){
        binding.text01.setText(item.getName());
        String date = Util.getInstance().dateUtil().getDateFromFirebase(item.getDate());
        String strDate = date;
        binding.text02.setText(strDate);
    }
}
