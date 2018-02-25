package com.theah64.dots;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by theapache64 on 17/2/18.
 */

public class Dots extends LinearLayout {

    private static final int DEFAULT_COUNT = 2;
    private static final int DEFAULT_INACTIVE_COLOR = R.color.grey;
    private static final int DEFAULT_SPEED = 100;
    private static final int DEFAULT_RADIUS = 25;
    private static final int DEFAULT_MARGIN = 8;
    private int count = DEFAULT_COUNT;
    private int lastActiveIndex;
    private int inactiveColor;
    private int activeColor;
    private Context context;
    private int speed;
    private int radius;
    private int margin;

    public Dots(Context context) {
        super(context);
        init(context, null);
    }

    public Dots(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @ColorInt
    public static int getAttributeColor(Context context, @AttrRes int colorAttribute) {
        int[] attrs = {colorAttribute};
        TypedArray ta = context.obtainStyledAttributes(attrs);

        @ColorInt int color = ContextCompat.getColor(context,
                ta.getResourceId(0, -1));
        ta.recycle();
        return color;
    }

    private void init(Context context, AttributeSet attrs) {

        this.context = context;
        this.inactiveColor = ContextCompat.getColor(context, DEFAULT_INACTIVE_COLOR);
        this.activeColor = getAttributeColor(context, R.attr.colorPrimary);
        this.speed = DEFAULT_SPEED;
        this.radius = DEFAULT_RADIUS;
        this.margin = DEFAULT_MARGIN;

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Dots, 0, 0);
            count = ta.getInt(R.styleable.Dots_count, DEFAULT_COUNT);
            inactiveColor = ta.getColor(R.styleable.Dots_inactive_dot_color, ContextCompat.getColor(context, DEFAULT_INACTIVE_COLOR));
            activeColor = ta.getColor(R.styleable.Dots_active_dot_color, activeColor);
            speed = ta.getInt(R.styleable.Dots_speed, DEFAULT_SPEED);
            radius = (int) ta.getDimension(R.styleable.Dots_radius, DEFAULT_RADIUS);
            margin = (int) ta.getDimension(R.styleable.Dots_margin, DEFAULT_MARGIN);
            ta.recycle();
        }

        setCount(count);

    }

    private boolean isAnimating = false;

    public void setActive(int activeIndex) {

        if (activeIndex >= count) {
            throw new IllegalArgumentException("Invalid index" + activeIndex + " , size is " + count);
        }

        if (isAnimating) {
            Log.d("X", "Animation going on....");
            return;
        }

        isAnimating = true;

        //Setting current to inactive
        final View currentDot = getChildAt(lastActiveIndex);
        final View newDot = getChildAt(activeIndex);

        //Old go
        YoYo.with(Techniques.ZoomOut).duration(speed).onEnd(new YoYo.AnimatorCallback() {

            @Override
            public void call(Animator animator) {

                ((GradientDrawable) currentDot.getBackground()).setColor(inactiveColor);

                //Old come
                YoYo.with(Techniques.ZoomIn).duration(speed).onEnd(new YoYo.AnimatorCallback() {

                    @Override
                    public void call(Animator animator) {

                        //New go
                        YoYo.with(Techniques.ZoomOut).duration(speed).onEnd(new YoYo.AnimatorCallback() {

                            @Override
                            public void call(Animator animator) {

                                ((GradientDrawable) newDot.getBackground()).setColor(inactiveColor);

                                //Setting active index
                                ((GradientDrawable) newDot.getBackground()).setColor(activeColor);

                                //New come
                                YoYo.with(Techniques.ZoomIn).duration(speed).onEnd(new YoYo.AnimatorCallback() {

                                    @Override
                                    public void call(Animator animator) {
                                        isAnimating = false;
                                    }

                                }).playOn(newDot);
                            }
                        }).playOn(newDot);


                    }
                }).playOn(currentDot);
            }
        }).playOn(currentDot);


        lastActiveIndex = activeIndex;
    }

    public void setCount(int count) {

        removeAllViews();

        this.count = count;
        this.lastActiveIndex = 0;

        final LayoutInflater inflater = LayoutInflater.from(context);

        for (int i = 0; i < count; i++) {

            final View dot = inflater.inflate(R.layout.dot_row, (ViewGroup) getParent(), false);
            LayoutParams params = new LayoutParams(radius, radius);
            params.setMargins(margin, margin, margin, margin);
            dot.setLayoutParams(params);

            GradientDrawable bg = (GradientDrawable) dot.getBackground();
            if (i > 0) {
                //Not first item
                bg.setColor(inactiveColor);
            } else {

                bg.setColor(activeColor);
            }

            addView(dot);
        }

    }

    public void moveBack() {
        int temp = lastActiveIndex - 1;

        if (temp == -1) {
            temp = count - 1;
        }

        setActive(temp);
    }

    public void moveForward() {
        int temp = lastActiveIndex + 1;

        if (temp == count) {
            temp = 0;
        }

        setActive(temp);
    }
}
