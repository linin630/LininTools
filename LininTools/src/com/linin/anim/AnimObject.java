package com.linin.anim;

import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimObject {
	
	public static TranslateAnimation getTranslateAnim(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, int durationMillis){
		return getTranslateAnim(fromXDelta, toXDelta, fromYDelta, toYDelta, durationMillis, false);
	}
	public static TranslateAnimation getTranslateAnim(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, int durationMillis, boolean isLoop){
		TranslateAnimation anim = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
		anim.setDuration(durationMillis);
		anim.setFillAfter(true);
		if (isLoop) {
			anim.setRepeatCount(Integer.MAX_VALUE);
		}
		return anim;
	}
	
	public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, float pivotX, float pivotY, int durationMillis){
		return getRotateAnimation(fromDegrees, toDegrees, pivotX, pivotY, durationMillis, false);
	}
	public static RotateAnimation getRotateAnimation(float fromDegrees, float toDegrees, float pivotX, float pivotY, int durationMillis, boolean isLoop){
		RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		anim.setDuration(durationMillis);
		anim.setFillAfter(true);
		if (isLoop) {
			anim.setRepeatCount(Integer.MAX_VALUE);
		}
		return anim;
	}
	
	public static ScaleAnimation getScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotX, float pivotY, int durationMillis){
		return getScaleAnimation(fromX, toX, fromY, toY, pivotX, pivotY, durationMillis, false);
	}
	public static ScaleAnimation getScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotX, float pivotY, int durationMillis, boolean isLoop){
		ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY, pivotX, pivotY);
		anim.setDuration(durationMillis);
		anim.setFillAfter(true);
		if (isLoop) {
			anim.setRepeatCount(Integer.MAX_VALUE);
		}
		return anim;
	}
	
	public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, int durationMillis){
		return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, false);
	}
	public static AlphaAnimation getAlphaAnimation(float fromAlpha, float toAlpha, int durationMillis, boolean isLoop){
		AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
		anim.setDuration(durationMillis);
		anim.setFillAfter(true);
		if (isLoop) {
			anim.setRepeatCount(Integer.MAX_VALUE);
		}
		return anim;
	}
	
}
