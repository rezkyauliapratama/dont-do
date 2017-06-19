package com.rezkyaulia.materialview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 6/6/2017.
 */

public class StyleFont {

    private static StyleFont mInstance;
    // Step 1: private static variable of INSTANCE variable
    private static volatile StyleFont INSTANCE;

    // Step 2: private constructor
    private StyleFont() {

    }

    // Step 3: Provide public static getInstance() method returning INSTANCE after checking
    public static StyleFont getInstance() {

        // double-checking lock
        if(null == INSTANCE){

            // synchronized block
            synchronized (StyleFont.class) {
                if(null == INSTANCE){
                    INSTANCE = new StyleFont();
                }
            }
        }
        return INSTANCE;
    }

    public final Map<String, Typeface> TYPEFACE = new HashMap<>();
    public String FONT_REGULAR = "";
    public String FONT_BOLD = "";
    public String FONT_ITALIC = "";

    private float fontScale = 1.0f;

    public void set(String regular, String bold, String italic) {
        FONT_REGULAR = regular;
        FONT_BOLD = bold;
        FONT_ITALIC = italic;
    }

    public void setFontRegular(String fontRegular) {
        FONT_REGULAR = fontRegular;
    }

    public void setFontBold(String fontBold) {
        FONT_BOLD = fontBold;
    }

    public void setFontItalic(String fontItalic) {
        FONT_ITALIC = fontItalic;
    }

    Typeface getTypeface(Context context, String font, Typeface typeface) {

        if (!TYPEFACE.containsKey(font)) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(),
                        font);

                TYPEFACE.put(font, typeface);
            } catch (Exception e) {
                Timber.e(
                        String.format(
                                Locale.getDefault(),
                                "Unable to load '%s'. " +
                                        "Please make sure the path of the font is correct.",
                                font
                        )
                );
            }
        } else
            typeface = TYPEFACE.get(font);

        return typeface;
    }

    public Typeface reqular(Context context) {
        return getTypeface(context, FONT_REGULAR, null);
    }

    public Typeface bold(Context context) {
        return getTypeface(context, FONT_BOLD, null);
    }


    public Typeface italic(Context context) {
        return getTypeface(context, FONT_ITALIC, null);
    }

    boolean isExist(Context context, String font) {
        try {
            return Arrays.asList(context.getResources().getAssets().list("")).contains(font);
        } catch (IOException e) {
            return false;
        }
    }

    public float getFontScale() {
        return fontScale;
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
    }


    public Typeface getRegular() {
        return TYPEFACE.get(FONT_REGULAR);
    }
    public Typeface getBold() {
        return TYPEFACE.get(FONT_BOLD);
    }
    public Typeface getItalic() {
        return TYPEFACE.get(FONT_ITALIC);
    }
}
