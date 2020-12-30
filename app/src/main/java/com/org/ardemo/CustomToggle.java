package com.org.ardemo;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.ScaleDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.nex3z.togglebuttongroup.button.LabelToggle;
import com.nex3z.togglebuttongroup.button.MarkerButton;
import com.nex3z.togglebuttongroup.button.ToggleButton;

public class CustomToggle extends MarkerButton implements ToggleButton {
    private static final String LOG_TAG = LabelToggle.class.getSimpleName();

    private static final int DEFAULT_ANIMATION_DURATION = 150;

    private long mAnimationDuration = DEFAULT_ANIMATION_DURATION;
    private Animation mCheckAnimation;
    private Animation mUncheckAnimation;
    private ValueAnimator mTextColorAnimator;
    private Context context;

    public CustomToggle(Context context) {
        this(context, null);
    }

    public CustomToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Override
    public void setMarkerColor(int markerColor) {
        super.setMarkerColor(markerColor);
        initBackground();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        initAnimation();
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(colors);
        initAnimation();
    }

    private void init() {
        initBackground();
        initText();
        initAnimation();
    }

    private void initBackground() {
        int shopeeOrange = ContextCompat.getColor(getContext(), R.color.shopeeOrange);
        GradientDrawable checked = new GradientDrawable();
        checked.setColor(0xFFFFFFFF);
        checked.setCornerRadius(dpToPx(5));
        checked.setStroke((int) dpToPx(1), shopeeOrange);
        mIvBg.setImageDrawable(checked);


        // Change background
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.parseColor("#EFEFEF"));
        background.setCornerRadius(dpToPx(5));
        // Set background for LabelToggle
        setBackgroundDrawable(background);

        GradientDrawable unchecked = new GradientDrawable();
        unchecked.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        unchecked.setCornerRadius(dpToPx(5));
        mTvText.setBackgroundDrawable(unchecked);
    }

    private void initText() {
        int padding = (int) dpToPx(0);
        mTvText.setPadding(padding, 0, padding, 0);
    }

    private void initAnimation() {
        final int defaultTextColor = getDefaultTextColor();
//        final int checkedTextColor = getCheckedTextColor();
        final int checkedTextColor = 0xFF000000;
        Log.v(LOG_TAG, "initAnimation(): defaultTextColor = " + defaultTextColor + ", checkedTextColor = " + checkedTextColor);

        mTextColorAnimator = ValueAnimator.ofObject(
                new ArgbEvaluator(), defaultTextColor, checkedTextColor);
        mTextColorAnimator.setDuration(mAnimationDuration);
        mTextColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mTvText.setTextColor((Integer)valueAnimator.getAnimatedValue());
            }
        });

        mCheckAnimation = new AlphaAnimation(0, 1);
        mCheckAnimation.setDuration(mAnimationDuration);
        mCheckAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mTvText.setTextColor(checkedTextColor);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        mUncheckAnimation = new AlphaAnimation(1, 0);
        mUncheckAnimation.setDuration(mAnimationDuration);
        mUncheckAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mIvBg.setVisibility(INVISIBLE);
                mTvText.setTextColor(defaultTextColor);}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (checked) {
            mIvBg.setVisibility(VISIBLE);
            mIvBg.startAnimation(mCheckAnimation);
            //mTextColorAnimator.start();
        } else {
            mIvBg.setVisibility(VISIBLE);
            mIvBg.startAnimation(mUncheckAnimation);
            //mTextColorAnimator.reverse();
        }
    }


}