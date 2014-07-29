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
 * ����Ϳѻ���Զ���view
 * 
 * @author linin
 */
public class DrawView extends View {

	// ������ɫ��Ĭ�Ϻ�ɫ
	public int color = Color.RED;
	// ���ʿ�ȣ�Ĭ��5
	public int paintWidth = 5;
	// // �����С
	// public int textSize = 30;

	private Paint paint;

	private List<Point> allpoint = new ArrayList<Point>();// �������еĲ�������

	private List<List<Point>> historyPoints = new ArrayList<List<Point>>();// ���ڼ�¼Ϳѻ��ʷ��¼

	public float dx = 0, dy = 0;
	
	public int index = -1;// �༭���۵�index

	public DrawView(Context context, AttributeSet attrs) {// ����context��ͬʱ�������Լ��ϡ�
		super(context, attrs);// ���ø���Ĺ���
		super.setOnTouchListener(new OnTouchListenerimpl());
		paint = new Paint();
		// paint.setTextSize(textSize);
		paint.setAntiAlias(true);
	}

	private class OnTouchListenerimpl implements OnTouchListener {

		private long currentTime = 0;
		private float dd = 300;

		public boolean onTouch(View v, MotionEvent event) {

			Point p = new Point((int) event.getX(), (int) event.getY());// �����걣����Point��
			if (event.getAction() == MotionEvent.ACTION_DOWN) { // �û�����

				DrawView.this.allpoint = new ArrayList<Point>();// �ػ�
				DrawView.this.allpoint.add(p);// ���������

				dx = event.getX();
				dy = event.getY();
				dd = (event.getX() - dx) * (event.getY() - dy)
						* (event.getX() - dx) * (event.getY() - dy);
				currentTime = System.currentTimeMillis();

			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {// �û��ƶ�
				DrawView.this.allpoint.add(p);// ��¼�����
				DrawView.this.postInvalidate();// �ػ�ͼ��

				dd = (event.getX() - dx) * (event.getY() - dy)
						* (event.getX() - dx) * (event.getY() - dy);

			} else if (event.getAction() == MotionEvent.ACTION_UP) {// �û��ɿ�
				DrawView.this.allpoint.add(p);// ��¼�����
				DrawView.this.postInvalidate();// �ػ�ͼ��
				// ��¼����ʷ��¼
				historyPoints.add(allpoint);

				long dt = System.currentTimeMillis() - currentTime;
				dd = (event.getX() - dx) * (event.getY() - dy)
						* (event.getX() - dx) * (event.getY() - dy);
				if (dd < 200 && dt >= 800) {
					// ����ƶ������ڸ���200px�ڣ����ҳ���ʱ�䳬��0.8�룬����¼�ñʻ�
					historyPoints.remove(historyPoints.size() - 1);
				}
				dd = 300;

			}

			return true; // ��ʾ����Ĳ�������ִ���ˡ�
		}

	}

	@Override
	protected void onDraw(Canvas canvas) { // ���л�ͼ
		paint.setColor(color);
		paint.setStrokeWidth(paintWidth);
		// ������ʷ��¼
		for (List<Point> points : historyPoints) {
			if (points.size() > 0)// ������㱣���ʱ��ʼ���л�ͼ
			{
				Iterator<Point> iterator = points.iterator();
				Point first = null;
				Point last = null;
				while (iterator.hasNext()) {
					if (first == null) {
						first = (Point) iterator.next();// ȡ������
					} else {
						if (last != null) {// ǰһ�׶��Ѿ����
							first = last;// ���¿�ʼ��һ�׶�
						}
						last = (Point) iterator.next();// ���������
						canvas.drawLine(first.x, first.y, last.x, last.y, paint);
					}
				}
			}
		}
		// ������ǰ����
		if (DrawView.this.allpoint.size() > 0)// ������㱣���ʱ��ʼ���л�ͼ
		{
			Iterator<Point> iterator = DrawView.this.allpoint.iterator();
			Point first = null;
			Point last = null;
			while (iterator.hasNext()) {
				if (first == null) {
					first = (Point) iterator.next();// ȡ������
				} else {
					if (last != null) {// ǰһ�׶��Ѿ����
						first = last;// ���¿�ʼ��һ�׶�
					}
					last = (Point) iterator.next();// ���������
					canvas.drawLine(first.x, first.y, last.x, last.y, paint);
				}
			}
		}

	}

	/**
	 * ���������������Ƴ�������ʷ��¼�����һ��
	 */
	public void cancel() {
		if (historyPoints != null) {
			if (historyPoints.size() > 0) {
				historyPoints.remove(historyPoints.size() - 1);
				invalidate();
			}
		}
	}

	/** ���û�����ɫ */
	public void setColor(int color) {
		this.color = color;
	}
	/** ���û�����ɫ */
	public void setColor(String color) {
		this.color = Color.parseColor(color);
	}
	/** ���û��ʴ�ϸ */
	public void setPaintWidth(int paintWidth) {
		this.paintWidth = paintWidth;
	}

}

