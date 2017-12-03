package rezkyaulia.android.dont_do.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.databinding.LayoutEmptyBinding;
import rezkyaulia.android.dont_do.databinding.LayoutEyeBinding;

/**
 * Created by Mutya Nayavashti on 31/01/2017.
 */

public class LayoutEyeInflate {

    LayoutEyeBinding binding;
    ViewGroup mView;
    Context mContext;

    public LayoutEyeInflate(Context context, ViewGroup view){
        this.mView = view;
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding =  DataBindingUtil.inflate(inflater, R.layout.layout_eye,mView,false); // LayoutInflater.from(context).inflate(R.layout.content_progressbar,view,false);
        mView.addView(binding.container);

        setVisibility(View.VISIBLE);
    }

    public void setAnimation(JSONObject object){
        binding.lottie.setAnimation(object);
    }
    public void setVisibility(int visible){
        binding.container.setVisibility(visible);
    }

    public int getVisibility(){
        return binding.container.getVisibility();
    }


}
