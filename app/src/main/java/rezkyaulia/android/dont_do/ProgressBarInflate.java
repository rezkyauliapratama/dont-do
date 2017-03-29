package rezkyaulia.android.dont_do;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rezkyaulia.android.dont_do.databinding.ContentProgressbarBinding;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 31/01/2017.
 */

public class ProgressBarInflate {

    ContentProgressbarBinding binding;
    ViewGroup mView;
    Context mContext;

    public ProgressBarInflate (Context context, ViewGroup view){
        this.mView = view;
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding =  DataBindingUtil.inflate(inflater,R.layout.content_progressbar,mView,false); // LayoutInflater.from(context).inflate(R.layout.content_progressbar,view,false);
        mView.addView(binding.containerProgressBar);

        setVisibility(View.GONE);
    }

    public void setVisibility(int visible){
        binding.containerProgressBar.setVisibility(visible);
    }

    public int getVisibility(){
        return binding.containerProgressBar.getVisibility();
    }

    public void setTitle(String text){
        Timber.e("setTitle : "+text);
        if (binding != null && !text.isEmpty()){
            binding.text01.setText(text);
        }
    }

    public void removeTitle(){
        if(binding.text01 != null){
            binding.text01 .setText("");
        }
    }











}
