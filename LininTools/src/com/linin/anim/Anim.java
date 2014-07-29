package com.linin.anim;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

public class Anim {

	private View view;
	private boolean isMoving = false;
	private Handler mHandler = new Handler();

	public Anim(View view){
		this.view = view;
	}

	/**
	 * 弹出效果
	 */
	public void pop(int duration){
		pop(duration,0);
	}

	public void pop(final int duration, int delay) {
		view.setVisibility(View.INVISIBLE);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				AnimationSet as = new AnimationSet(true);
				as.addAnimation(AnimObject.getAlphaAnimation(0, 1, duration));
				as.addAnimation(AnimObject.getScaleAnimation(0, 1, 0, 1,
						view.getWidth() / 2, view.getHeight() / 2, duration));
				as.setInterpolator(new OvershootInterpolator());
				as.setDuration(duration);
				as.setFillAfter(true);
				view.startAnimation(as);
			}
		}, delay);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				view.setVisibility(View.VISIBLE);
			}
		}, duration + delay);
	}
	/**
	 * 收缩消失效果
	 */
	public void popClose(int duration){
		popClose(duration,0);
	}

	public void popClose(final int duration, int delay) {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				AnimationSet as = new AnimationSet(true);
				as.addAnimation(AnimObject.getAlphaAnimation(1, 0, duration));
				as.addAnimation(AnimObject.getScaleAnimation(1, 0, 1, 0,
						view.getWidth() / 2, view.getHeight() / 2, duration));
				as.setDuration(duration);
				as.setFillAfter(true);
				view.startAnimation(as);
			}
		}, delay);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				view.setVisibility(View.INVISIBLE);
			}
		}, duration + delay);
	}

	/**
	 * 抖动效果
	 */
	public void shake(){
		shake(0);
	}
	public void shake(int delay){
		AnimationSet as = new AnimationSet(true);
		as.addAnimation(AnimObject.getRotateAnimation(0, 10, view.getWidth()/2, view.getHeight()/2, 50,true));
		RotateAnimation ra = AnimObject.getRotateAnimation(10, -10, view.getWidth()/2, view.getHeight()/2, 100,true);
		ra.setStartOffset(50);
		as.addAnimation(ra);
		RotateAnimation ra2 = AnimObject.getRotateAnimation(-10, 0, view.getWidth()/2, view.getHeight()/2, 50,true);
		ra2.setStartOffset(150);
		as.addAnimation(ra2);
		as.setStartOffset(delay);
		view.startAnimation(as);
	}

	/**
	 * 移动到某个位置
	 */
	public void Move(final float fromXDelta, final float toXDelta, float fromYDelta, float toYDelta, int durationMillis){
		if (isMoving) {
			Log.i("linin", "ismoving!can not move now!");
			return;
		}
		TranslateAnimation anim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		anim.setDuration(durationMillis);
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation arg0) {
				isMoving = true;
			}
			public void onAnimationRepeat(Animation arg0) {
			}
			public void onAnimationEnd(Animation arg0) {
				int left = view.getLeft()+(int)(toXDelta-fromXDelta);
				int top = view.getTop();
				int width = view.getWidth();
				int height = view.getHeight();
				view.clearAnimation();
				view.layout(left, top, left+width, top+height);
				isMoving = false;
			}
		});
		view.startAnimation(anim);
		Log.i("linin", "start moving!");
	}

}
