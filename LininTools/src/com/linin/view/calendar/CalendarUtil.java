package com.linin.view.calendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.linin.tools.R;
import com.linin.utils.DialogUtil;
import com.linin.utils.L;
import com.linin.utils.ScreenUtil;
import com.linin.view.calendar.CalendarView.OnCellTouchListener;

/**
 * �������򹤾���
 * 
 * @author linin
 * 
 */
public class CalendarUtil implements OnCellTouchListener {

	public final static String[] MS = new String[] { "һ��", "����", "����", "����",
			"����", "����", "����", "����", "����", "ʮ��", "ʮһ��", "ʮ����" };

	public interface onCalendarSelectListener {
		public void onCalendarSelected(int year, int month, int day);
	}

	private static CalendarUtil me;

	public static CalendarUtil init(Context context) {
		if (me == null) {
			me = new CalendarUtil(context);
		} else if (me.context != context) {
			me = new CalendarUtil(context);
		}
		return me;
	}

	public CalendarUtil(Context context) {
		this.context = context;
	}

	public Context context;
	private onCalendarSelectListener listener;
	private int year = 1992, month = 6, day = 30;

	private Dialog dialogCalendar;// ��������
	private com.linin.view.calendar.CalendarView mView = null;
	private Button btCenter;
	private Rect ecBounds;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				dialogCalendar.cancel();
				// Ū������������
				if (listener != null) {
					listener.onCalendarSelected(year, month, day);
				}
				break;
			default:
				break;
			}
		}
	};

	/** ��ʾ���� */
	public void show(onCalendarSelectListener listener) {
		this.listener = listener;
		if (dialogCalendar == null) {
			View calendar = LayoutInflater.from(context).inflate(
					R.layout.view_calendar, null);
			mView = (CalendarView) calendar.findViewById(R.id.calendar);
			mView.setOnCellTouchListener(this);
			btCenter = (Button) calendar.findViewById(R.id.btCenter);
			btCenter.setText(MS[mView.getMonth()] + " " + mView.getYear());
			ImageView btLeft = (ImageView) calendar.findViewById(R.id.btnLeft);
			ImageView btRight = (ImageView) calendar.findViewById(R.id.btRight);
			btLeft.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mView.previousMonth();
					btCenter.setText(MS[mView.getMonth()] + " "
							+ mView.getYear());
				}
			});
			btRight.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mView.nextMonth();
					btCenter.setText(MS[mView.getMonth()] + " "
							+ mView.getYear());
				}
			});
			dialogCalendar = DialogUtil.getMenuDialog(
					(Activity) context,
					calendar,
					ScreenUtil.getScreenWidth((Activity) context),
					(int) (context.getResources().getDimension(
									R.dimen.cell_heigh) * 7 + 40));
		}
		dialogCalendar.show();
	}

	@Override
	public void onTouch(Cell cell) {
		if (cell.mPaint.getColor() == Color.GRAY) {
			// �������µ�
			mView.previousMonth();
			btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
		} else if (cell.mPaint.getColor() == Color.LTGRAY) {
			// ���µ�
			mView.nextMonth();
			btCenter.setText(mView.getYear() + "-" + (mView.getMonth() + 1));
		} else { // ���µ�
			Intent ret = new Intent();
			ret.putExtra("year", mView.getYear());
			ret.putExtra("month", mView.getMonth());
			ret.putExtra("day", cell.getDayOfMonth());
			// �ڴ��õ�ǰ��View �ػ�һ��
			ecBounds = cell.getBound();
			mView.getDate();
			mView.mDecoraClick.setBounds(ecBounds);
			mView.invalidate();

			year = mView.getYear();
			month = mView.getMonth() + 1;
			day = cell.getDayOfMonth();
			String currentTime = year + "-" + month + "-" + day;
			L.d("��ǰ���ڣ�" + currentTime);
			mView.setToday(year, month - 1, day);
			btCenter.setText(MS[month - 1] + " " + year);
			// ������������ѡ��ʱ��//�ӳ�100���룬���Կ���ѡ��Ч��
			mHandler.sendEmptyMessageDelayed(0, 100);
		}
	}

}
