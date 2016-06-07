package se.martenolsson.lah15.classes;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import se.martenolsson.lah15.ApplicationController;

/**
 * Created by martenolsson on 16-01-13.
 */
public class IconTextView extends TextView {
    public static Typeface FONT_NAME;

    public IconTextView(Context context) {
        super(context);
        if (!isInEditMode()) {
            this.setTypeface(((ApplicationController) context.getApplicationContext()).icons);
        }
    }
    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            this.setTypeface(((ApplicationController) context.getApplicationContext()).icons);
        }
    }
    public IconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            this.setTypeface(((ApplicationController) context.getApplicationContext()).icons);
        }
    }
}