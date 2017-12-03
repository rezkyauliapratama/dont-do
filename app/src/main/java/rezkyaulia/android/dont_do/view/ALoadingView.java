package rezkyaulia.android.dont_do.view;

import android.content.Context;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;

import rezkyaulia.android.dont_do.Utility.Util;

/**
 * Created by Shiburagi on 25/08/2017.
 */

public class ALoadingView extends LottieAnimationView {
    public ALoadingView(Context context) {
        super(context);

    }

    public ALoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ALoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initAnimation();

    }

    private void initAnimation() {

        int size = (int) Util.getInstance().convertDpToPixel(144);
        setMaxWidth(size);
        setMaxHeight(size);

        if (getLayoutParams() != null) {
            getLayoutParams().width = size;
            getLayoutParams().height = size;
        }
        setAnimation("animation/loading_animation.json");
        loop(true);
        playAnimation();
    }

}
