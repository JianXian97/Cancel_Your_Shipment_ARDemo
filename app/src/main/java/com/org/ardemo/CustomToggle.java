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
    private int backgroundColor = Color.parseColor("#EFEFEF"); //default
    private int selectedColor = Color.parseColor("#FFFFFF"); //default
    private int selectedStrokeColor = Color.parseColor("#DC593B"); //default

    private int unselectedStrokeColor = Color.parseColor("#00000000"); //default
    private int selectedStrokeThickness = 1; //default
    private int unselectedStrokeThickness = 1; //default
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
    @Override
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        initBackground();
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor =  selectedColor;
        initBackground();
    }

    public void setSelectedStrokeColor(int selectedStrokeColor) {
        this.selectedStrokeColor = selectedStrokeColor;
        initBackground();
    }

    public void setSelectedStrokeThickness(int selectedStrokeThickness) {
        this.selectedStrokeThickness = selectedStrokeThickness;
        initBackground();
    }

    public void setUnselectedStrokeColor(int unselectedStrokeColor) {
        this.unselectedStrokeColor = unselectedStrokeColor;
        initBackground();
    }

    public void setUnselectedStrokeThickness(int unselectedStrokeThickness) {
        this.unselectedStrokeThickness = unselectedStrokeThickness;
        initBackground();
    }

    private void init() {
        initBackground();
        initText();
        initAnimation();
    }

    private void initBackground() {
        int shopeeOrange = ContextCompat.getColor(getContext(), R.color.shopeeOrange);
        GradientDrawable checked = new GradientDrawable();
        checked.setColor(selectedColor);
        checked.setCornerRadius(dpToPx(5));
        checked.setStroke((int) dpToPx(selectedStrokeThickness), selectedStrokeColor);
        mIvBg.setImageDrawable(checked);


        // Change background
        GradientDrawable background = new GradientDrawable();
        background.setColor(backgroundColor);
        background.setCornerRadius(dpToPx(5));
        // Set background for LabelToggle
        setBackgroundDrawable(background);

        GradientDrawable unchecked = new GradientDrawable();
        unchecked.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        unchecked.setStroke((int) dpToPx(unselectedStrokeThickness), unselectedStrokeColor);
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