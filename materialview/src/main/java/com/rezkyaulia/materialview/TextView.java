package com.rezkyaulia.materialview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Rezky Aulia Pratama on 6/6/2017.
 */

public class TextView extends android.widget.TextView {
    StyleFont styleFont;
    public TextView(Context context) {
        super(context);
        setCustomTypeface(context, null);

    }

    public TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface(context, attrs);

    }

    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomTypeface(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setCustomTypeface(context, attrs);

    }

    private void setCustomTypeface(Context context, AttributeSet attrs){
        styleFont = StyleFont.getInstance();
        TypedArray a = context.getTheme().obtainStyledAttributes(android.support.v7.appcompat.R.styleable.TextAppearance);
        int style = a.getInt(android.support.v7.appcompat.R.styleable.TextAppearance_android_textStyle, Typeface.NORMAL);

        try {
            setTextStyle(style);
        } finally {
            a.recycle();
        }


    }

    public void setTextStyle(int style) {
        if(isInEditMode())
            return;

        String font;
        switch (style) {
            case Typeface.BOLD:
                font = styleFont.FONT_BOLD;
                break;
            case Typeface.ITALIC:
                font = styleFont.FONT_ITALIC;
                break;
            case Typeface.NORMAL:
                font = styleFont.FONT_REGULAR;
                break;
            default:
                font = styleFont.FONT_REGULAR;
        }

        try {

            setTypeface(styleFont.getTypeface(getContext(), font, getTypeface()));
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    public void setSupportTextAppearance(int resId){
        if (Build.VERSION.SDK_INT >= 23)
            this.setTextAppearance(resId);
        else
            this.setTextAppearance(getContext(),resId);
    }


}
