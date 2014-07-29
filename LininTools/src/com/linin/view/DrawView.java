package com.linin.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 用于涂鸦的自定义view
 * 
 * @author linin
 */
public class DrawView extends View {

	// 画笔颜色，默认红色
	public int color = Color.RED;
	// 画笔宽度，默认5
	public int paintWidth = 5;
	// // 字体大小
	// public int textSize = 30;

	private Paint paint;

	private List<Point> allpoint = new ArrayList<Point>();// 保存所有的操作坐标

	private List<List<Point>> historyPoints = new ArrayList<List<Point>>();// 用于记录涂鸦历史记录

	public float dx = 0, dy = 0;
	
	public int index = -1;// 编辑评论的index

	public DrawView(Context context, AttributeSet attrs) {// 接收context，同时接收属性集合。
		super(context, attrs);// 调用父类的构造
		super.setOnTouchListener(new OnTouchListenerimpl());
		paint = new Paint();
		// paint.setTextSize(textSize);
		paint.setAntiAlias(true);
	}

	private class OnTouchListenerimpl implements OnTouchListener {

		private long currentTime = 0;
		private float dd = 300;

		public boolean onTouch(View v, MotionEvent event) {

			Point p = new Point((int) event.getX(), (int) event.getY());// 将坐标保存在Point类
			if (event.getAction() == MotionEvent.ACTION_DOWN) { // 用户按下

				DrawView.this.allpoint = new ArrayList<Point>();// 重绘
				DrawView.this.allpoint.add(p);// 保存坐标点

				dx = event.getX();
				dy = event.getY();
				dd = (event.getX() - dx) * (event.getY() - dy)
						* (event.getX() - dx) * (event.getY() - dy);
				currentTime = System.currentTimeMillis();

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {// 用户移动
				DrawView.this.allpoint.add(p);// 记录坐标点
				DrawView.this.postInvalidate();// 重绘图形

				dd = (event.getX() - dx) * (event.getY() - dy)
						* (event.getX() - dx) * (event.getY() - dy);

			} else if (event.getAction() == MotionEvent.ACTION_UP) {// 用户松开
				DrawView.this.allpoint.add(p);// 记录坐标点
				DrawView.this.postInvalidate();// 重绘图形
				// 记录到历史记录
				historyPoints.add(allpoint);

				long dt = System.currentTimeMillis() - currentTime;
				dd = (event.getX() - dx) * (event.getY() - dy)
						* (event.getX() - dx) * (event.getY() - dy);
				if (dd < 200 && dt >= 800) {
					// 如果移动距离在根号200px内，并且长按时间超过0.8秒，不记录该笔画
					historyPoints.remove(historyPoints.size() - 1);
				}
				dd = 300;

			}

			return true; // 表示下面的操作不在执行了。
		}

	}

	@Override
	protected void onDraw(Canvas canvas) { // 进行绘图
		paint.setColor(color);
		paint.setStrokeWidth(paintWidth);
		// 画出历史记录
		for (List<Point> points : historyPoints) {
			if (points.size() > 0)// 有坐标点保存的时候开始进行绘图
			{
				Iterator<Point> iterator = points.iterator();
				Point first = null;
				Point last = null;
				while (iterator.hasNext()) {
					if (first == null) {
						first = (Point) iterator.next();// 取出坐标
					} else {
						if (last != null) {// 前一阶段已经完成
							first = last;// 重新开始下一阶段
						}
						last = (Point) iterator.next();// 结果点坐标
						canvas.drawLine(first.x, first.y, last.x, last.y, paint);
					}
				}
			}
		}
		// 画出当前的线
		if (DrawView.this.allpoint.size() > 0)// 有坐标点保存的时候开始进行绘图
		{
			Iterator<Point> iterator = DrawView.this.allpoint.iterator();
			Point first = null;
			Point last = null;
			while (iterator.hasNext()) {
				if (first == null) {
					first = (Point) iterator.next();// 取出坐标
				} else {
					if (last != null) {// 前一阶段已经完成
						first = last;// 重新开始下一阶段
					}
					last = (Point) iterator.next();// 结果点坐标
					canvas.drawLine(first.x, first.y, last.x, last.y, paint);
				}
			}
		}

	}

	/**
	 * 撤销操作，可以移除画笔历史记录的最后一步
	 */
	public void cancel() {
		if (historyPoints != null) {
			if (historyPoints.size() > 0) {
				historyPoints.remove(historyPoints.size() - 1);
				invalidate();
			}
		}
	}

	/** 设置画笔颜色 */
	public void setColor(int color) {
		this.color = color;
	}
	/** 设置画笔颜色 */
	public void setColor(String color) {
		this.color = Color.parseColor(color);
	}
	/** 设置画笔粗细 */
	public void setPaintWidth(int paintWidth) {
		this.paintWidth = paintWidth;
	}

}

