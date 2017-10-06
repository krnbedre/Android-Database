package com.dhdigital.lms.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kiran Bedre on 25/1/16.
 * DarkHorse BOA
 */
public class AnimUtil {


    public static void expand(View summary) {
        //set Visible
        summary.setVisibility(View.VISIBLE);
        summary.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ValueAnimator mAnimator = slideAnimator(0, summary.getMeasuredHeight(), summary);
        mAnimator.start();
    }


    public static void expandList(View summary) {
        //set Visible
        summary.setVisibility(View.VISIBLE);
        summary.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ValueAnimator mAnimator = slideAnimator(0, summary.getLayoutParams().height, summary);
        mAnimator.start();
    }

    public static void collapse(final View summary) {
        int finalHeight = summary.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, summary);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    public static void collapseDrawerItem(final View summary) {
        final int finalHeight = summary.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, summary);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                summary.setVisibility(View.GONE);
                ViewGroup.LayoutParams layoutParams = summary.getLayoutParams();
                layoutParams.height = finalHeight;
                summary.setLayoutParams(layoutParams);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    public static void collapseItem(final View summary, final View parent) {
        final int finalHeight = summary.getHeight();
        Log.d(AppConstants.APP_TAG,"PArent height : "+parent.getHeight() + " Child Height : "+finalHeight);
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, summary);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                ViewGroup.LayoutParams layoutParams = summary.getLayoutParams();
                layoutParams.height = finalHeight;
                summary.setLayoutParams(layoutParams);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }
    private static ValueAnimator slideAnimator(int start, int end, final View summary) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = summary.getLayoutParams();
                layoutParams.height = value;
                summary.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
