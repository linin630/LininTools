package com.linin.view.xlistview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

/**
 * ���������ص��������ص���ListView����û������ˢ�º��������ظ���Ĺ���
 * 
 * bug:���϶�������������������ף��ټ����϶��Ļ���ͻȻ����λ�ã����鲻��
 * 
 * @author linin
 * 
 */
public class XListViewSimple extends ListView implements OnScrollListener {

	private int paddingLeft = 0;
	private int paddingRight = 0;
	private int paddingTop = 0;
	private int paddingBottom = 0;

	private float lastY = 0;

	private boolean isPull = true;// �Ƿ�����/����//true������false����
	private boolean isTop = true;// �Ƿ��������һ��
	private boolean isBottom = false;// �Ƿ���������һ��
	private int scrollState = 0;

	private Handler mHandler = new Handler();

	public XListViewSimple(Context context) {
		super(context);
		init(context);
	}

	public XListViewSimple(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public XListViewSimple(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		// ��ʼ��padding��ֵ
		paddingLeft = getPaddingLeft();
		paddingRight = getPaddingRight();
		paddingTop = getPaddingTop();
		paddingBottom = getPaddingBottom();
		//
		setOnScrollListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		float y = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			float historicalY = ev.getY();
			int dy = (int) (historicalY - lastY) / 3;
			isPull = dy > 0;
			if (isPull) {// ����
				if (isTop && scrollState != SCROLL_STATE_FLING) {
					dy += paddingTop;
					setPadding(paddingLeft, dy, paddingRight, paddingBottom);
					setSelection(0);// ѡ�е�һ��item����Ȼû������Ч��
				}
			} else {// ����
				if (isBottom && scrollState != SCROLL_STATE_FLING) {
					dy -= paddingBottom;
					setPadding(paddingLeft, paddingTop, paddingRight, -dy);
					setSelection(getCount());// ѡ�����һ��item����Ȼû������Ч��
				}
			}
			break;
		case MotionEvent.ACTION_UP:// �ص�
			if (isPull) {
				int top = getPaddingTop();
				int duration = 0;
				while (top > paddingTop) {
					top -= 10;
					duration += 10;
					final int t = top;
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							setPadding(paddingLeft, t, paddingRight,
									paddingBottom);
						}
					}, duration);
				}
			} else {
				int bottom = getPaddingBottom();
				int duration = 0;
				while (bottom > paddingTop) {
					bottom -= 10;
					duration += 10;
					final int b = bottom;
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							setPadding(paddingLeft, paddingTop, paddingRight, b);
						}
					}, duration);
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScroll(AbsListView lv, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		isTop = firstVisibleItem == 0;
		isBottom = firstVisibleItem + visibleItemCount == totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView lv, int scrollState) {
		this.scrollState = scrollState;
	}

}
