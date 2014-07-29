package com.linin.view.xlistview;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linin.tools.R;

/**
 * ��ˢ�µ�listview
 * 
 * @author user
 * 
 */
public class XListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemLongClickListener {

	private static final String TAG = "listview";

	private final static int RELEASE_To_REFRESH = 0;// ����ˢ��
	private final static int PULL_To_REFRESH = 1;// ��������
	private final static int REFRESHING = 2;// ˢ��
	private final static int DONE = 3;// ���
	private final static int LOADING = 4;// ������
	
	private final static int ENDINT_DONE = 0;// ���/�ȴ�ˢ��
	private final static int ENDINT_LOADING = 1;// ������
	
	// ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;

	private TextView head_tipsTextview;
	private TextView head_lastUpdatedTextView;
	private ImageView head_arrowImageView;
	private ProgressBar head_progressBar;
	
	
	private TextView ending_tipsTextview;
	private ProgressBar ending_progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;

	private int headstate;
	private int endingstate;

	private boolean isBack;

	private OnRefreshListener refreshListener;
	private OnLoadMoreListener loadMoreListener;

	private boolean isRefreshable;
	private boolean canloadmore = true;// ���Լ��ظ��ࣿ
	private boolean canrefresh = true;// ����ˢ�£�
	private boolean canloadmore_more = true;// ������ʾ���ࣿ
	private Context parentcontext;
	
	private LinearLayout endingView;
	private int lastItemIndex;
	private int count;
	private boolean enougcount;// �㹻����������Ļ��
	

	// ����״̬ͼ��
	private LinearLayout statView;
	private ImageView stat_Image;
	private TextView stat_Text;
	
	public XListView(Context context) {
		super(context);
		parentcontext= context;
		init();
	}

	/**
	 * ��ʾ��������״̬��
	 */
	public void setRefreshingstat() {
		setAllstatclose();// ��������״̬
		
		headView.setPadding(0, 0, 0, 0);
		head_progressBar.setVisibility(View.VISIBLE);
		head_arrowImageView.clearAnimation();
		head_arrowImageView.setVisibility(View.GONE);
		head_tipsTextview.setText("����ˢ��...");
		head_lastUpdatedTextView.setText("ˢ��ʱ�䣺" + gettime());
		head_lastUpdatedTextView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * �ر�����״̬
	 */
	public void setAllstatclose() {
		if (headView.getTop() >= 0) {// ˢ���е�״̬��
			onRefreshComplete();
		}
		if (!ending_tipsTextview.getText().toString().equals("����")) {// ���ظ����״̬
			ending_tipsTextview.setText("����ʧ�ܣ�");
			ending_tipsTextview.setVisibility(View.VISIBLE);
			ending_progressBar.setVisibility(View.GONE);
		}
		if (getFooterViewsCount()>1) {
			removeFooterView(statView);
		}
//		getAdapter().getCount()
//		super.setAdapter(adapter)
	}
	
	
	public void setNoDataStat() {
		setNoDataStat("�������ݣ�");
	}
	
	/**
	 * ����û������ʱ��ʾ����ͼ
	 * 
	 * @param tipText
	 *            ��ʾ��Ϣ
	 */
	public void setNoDataStat(String tipText) {
//		removeFooterView(endingView);
		if (getFooterViewsCount() == 1 ){
			addFooterView(statView);
//			statView.setVisibility(View.VISIBLE);
			stat_Image.setImageResource(android.R.drawable.ic_dialog_info);
			stat_Text.setText(tipText);
		}
	}
	
	public void setNetErrorStat() {
		setNetErrorStat("�������");
	}
	
	public void setNetErrorStat(String tipText)  {
//		removeFooterView(endingView);
		if (getFooterViewsCount() == 1 ){
			addFooterView(statView);
			stat_Image.setImageResource(android.R.drawable.ic_dialog_alert);
			stat_Text.setText(tipText);
		}
		// ����@android:drawable/ic_dialog_alert
	}
	
	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parentcontext= context;
		init();
	}
	
	public XListView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		parentcontext= context;
		init();
	}
	
	private void init() {
		addheadView ();
		addendingView();
		addstatView();

		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
		
		isRefreshable = false;
		
	}
	
	private void addheadView(){
		inflater = LayoutInflater.from(parentcontext);

		headView = (LinearLayout) inflater.inflate(R.layout.list_head, null);

		head_arrowImageView = (ImageView) headView
				.findViewById(R.id.head_arrowImageView);

		head_arrowImageView.setMinimumWidth(70);
		head_arrowImageView.setMinimumHeight(50);
		head_progressBar = (ProgressBar) headView
				.findViewById(R.id.head_progressBar);
		head_tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		head_lastUpdatedTextView = (TextView) headView
				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView, null, false);
		
		head_lastUpdatedTextView.setText("");
		
		headstate = DONE;
	}
	
	private void addstatView(){
		inflater = LayoutInflater.from(parentcontext);
		// ����״̬view
		statView = (LinearLayout) inflater.inflate(R.layout.list_stat, null);
		
		stat_Image = (ImageView) statView.findViewById(R.id.statimage);
		stat_Text = (TextView) statView.findViewById(R.id.stattext);
		
//		statView.invalidate();
//		addFooterView(statView, null, false);
	}
	
	private void addendingView(){
		inflater = LayoutInflater.from(parentcontext);

		endingView = (LinearLayout) inflater.inflate(R.layout.list_ending, null);

		ending_progressBar = (ProgressBar)endingView.findViewById(R.id.ending_progressBar);
		ending_tipsTextview  = (TextView)endingView.findViewById(R.id.ending_tipsTextView);
		
		endingView.invalidate();
		
		// ��ʼ�����صģ���Ϊ���ݿ��ܲ����Գ�����Ļ
		endingView.setVisibility(View.GONE);
		endingstate = ENDINT_DONE;
		
		addFooterView(endingView, null, false);
	}
	
	/**
	 * �����Ƿ����㹻��item������Ļ
	 * 
	 * @return
	 */
	public boolean IsEnougCount() {
		return enougcount;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		
		firstItemIndex = firstVisibleItem;
		lastItemIndex = firstVisibleItem + visibleItemCount - 2;
		count = totalItemCount - 2;
		if (totalItemCount>visibleItemCount ) {
			enougcount = true;
//			endingView.setVisibility(View.VISIBLE);
		} else {
			enougcount = false;
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (lastItemIndex ==  count && scrollState == SCROLL_STATE_IDLE  && enougcount) {
			// SCROLL_STATE_IDLE=0������ֹͣ
			if (endingstate !=ENDINT_LOADING) {
				endingstate = ENDINT_LOADING;
				onLoadmore();
			}
		} 
		changeEndingViewByState();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (canrefresh) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
						// Log.v(TAG, "��downʱ���¼��ǰλ�á�");
				}
				break;

			case MotionEvent.ACTION_UP:

				if (headstate != REFRESHING && headstate != LOADING) {
					if (headstate == DONE) {
							// ʲô������
					}
					if (headstate == PULL_To_REFRESH) {
						headstate = DONE;
						changeHeaderViewByState();

							// Log.v(TAG, "������ˢ��״̬����done״̬");
					}
					if (headstate == RELEASE_To_REFRESH) {
						headstate = REFRESHING;
						changeHeaderViewByState();
						onRefresh();

							// Log.v(TAG, "���ɿ�ˢ��״̬����done״̬");
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
						// Log.v(TAG, "��moveʱ���¼��λ��");
					isRecored = true;
					startY = tempY;
				}

				if (headstate != REFRESHING && isRecored && headstate != LOADING) {

						// ��֤������padding�Ĺ����У���ǰ��λ��һֱ����head������������б�����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���

						// ��������ȥˢ����
					if (headstate == RELEASE_To_REFRESH) {

						setSelection(0);

							// �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							headstate = PULL_To_REFRESH;
							changeHeaderViewByState();

								// Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");
						}
							// һ�����Ƶ�����
						else if (tempY - startY <= 0) {
							headstate = DONE;
							changeHeaderViewByState();

								// Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");
						}
							// �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�
						else {
								// ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������
						}
					}
						// ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬
					if (headstate == PULL_To_REFRESH) {

						setSelection(0);

							// ���������Խ���RELEASE_TO_REFRESH��״̬
						if ((tempY - startY) / RATIO >= headContentHeight) {
							headstate = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

								// Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");
						}
							// ���Ƶ�����
						else if (tempY - startY <= 0) {
							headstate = DONE;
							changeHeaderViewByState();

								// Log.v(TAG, "��DOne��������ˢ��״̬ת�䵽done״̬");
						}
					}

						// done״̬��
					if (headstate == DONE) {
						if (tempY - startY > 0) {
							headstate = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

						// ����headView��size
					if (headstate == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

						// ����headView��paddingTop
					if (headstate == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}

				}

				break;
			}
		}
		}
		return super.onTouchEvent(event);
	}

	/*
	 * ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
	 */
	private void changeHeaderViewByState() {
		switch (headstate) {
		case RELEASE_To_REFRESH:
			head_arrowImageView.setVisibility(View.VISIBLE);
			head_progressBar.setVisibility(View.GONE);
			head_tipsTextview.setVisibility(View.VISIBLE);
			head_lastUpdatedTextView.setVisibility(View.VISIBLE);

			head_arrowImageView.clearAnimation();
			head_arrowImageView.startAnimation(animation);

			head_tipsTextview.setText("�ɿ�����ˢ��");

			// Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");
			break;
		case PULL_To_REFRESH:
			head_progressBar.setVisibility(View.GONE);
			head_tipsTextview.setVisibility(View.VISIBLE);
			head_lastUpdatedTextView.setVisibility(View.VISIBLE);
			head_arrowImageView.clearAnimation();
			head_arrowImageView.setVisibility(View.VISIBLE);// �ɲ�����
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) {
				isBack = false;
				head_arrowImageView.clearAnimation();
				head_arrowImageView.startAnimation(reverseAnimation);

				head_tipsTextview.setText("�����϶�����ˢ��");
			} else {
				head_tipsTextview.setText("�����϶�����ˢ��");
			}
			// Log.v(TAG, "��ǰ״̬������ˢ��");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			head_progressBar.setVisibility(View.VISIBLE);
			head_arrowImageView.clearAnimation();
			head_arrowImageView.setVisibility(View.GONE);
			head_tipsTextview.setText("����ˢ��...");
			head_lastUpdatedTextView.setVisibility(View.VISIBLE);
			// Log.v(TAG, "��ǰ״̬,����ˢ��...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			head_progressBar.setVisibility(View.GONE);
			head_arrowImageView.clearAnimation();
			head_arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			head_tipsTextview.setText("�ɿ�����ˢ��");
			head_lastUpdatedTextView.setVisibility(View.VISIBLE);

			// Log.v(TAG, "��ǰ״̬��done");
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
		this.loadMoreListener = loadMoreListener;
		
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
	
	/**
	 * ���ظ����ѽ������������ݿɼ���
	 */
	public void setCanLoadMore(boolean arg) {
		canloadmore = arg;
		endingstate = ENDINT_DONE;
		changeEndingViewByState();
	}
	
	public void setCanLoadMore_More(boolean arg){
		canloadmore_more=arg;
	}
	public void setCanRefresh(boolean arg) {
		canrefresh = arg;
	}

	/**
	 * ˢ�����
	 */
	public void onRefreshComplete() {
		headstate = DONE;
		head_lastUpdatedTextView.setText("�ϴ�ˢ�� ��" + gettime());
		changeHeaderViewByState();
		
		// ˢ�����,Ĭ�ϲ����Լ��ظ��࣡
		enougcount = false;
		changeEndingViewByState();
	}
	
	/**
	 * ���ظ������
	 */
	public void onLoadMoreComplete() {
		// ��ʾ״̬����
		if (getFooterViewsCount() == 2) {
			removeFooterView(statView);
		}
		// ����ˢ������
		endingstate = ENDINT_DONE;
		changeEndingViewByState();
	}

	/**
	 * �ı���ظ���״̬
	 */
	private void  changeEndingViewByState() {
		if (canloadmore) {
			// ������ظ���
			switch (endingstate) {
			case ENDINT_LOADING:// ˢ����
				ending_tipsTextview.setText("������...");
				ending_tipsTextview.setVisibility(View.VISIBLE);
				ending_progressBar.setVisibility(View.VISIBLE);
				head_arrowImageView.startAnimation(animation);
				break;
			case ENDINT_DONE:// ��ɡ��ȴ�ˢ��
			default:
				if (enougcount) {					
					endingView.setVisibility(View.VISIBLE);
				} else {
					endingView.setVisibility(View.GONE);
				}
				if(canloadmore_more){
					ending_tipsTextview.setText("����");
				ending_tipsTextview.setVisibility(View.VISIBLE);
				ending_progressBar.setVisibility(View.GONE);
				}
				break;
			}
		} else {
			endingView.setVisibility(View.GONE);
			
			ending_tipsTextview.setVisibility(GONE);
			ending_progressBar.setVisibility(GONE);
		}
		
	}
	
	private void onRefresh() {
		if (null != refreshListener) {
			if (head_lastUpdatedTextView.getText().equals("")) {// ��ʼ������ʱ����Ϣ��ʾ
				head_lastUpdatedTextView.setText("ˢ��ʱ��:" + gettime());
			}
			// ���״̬
			this.onLoadMoreComplete();// �������
			refreshListener.onRefresh();
		}
	}
	
	private void onLoadmore() {
		if (canloadmore) {
			if (null != loadMoreListener) {
				loadMoreListener.onLoadMore();
			}
		}
	}
	
	// �˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		if (adapter.getCount()!=0) {
			head_lastUpdatedTextView.setText("�ϴ�ˢ�� ��" + gettime());
		}
		
		if (endingstate!=ENDINT_LOADING) {
			// ��ʼ�����صģ���Ϊ���ݿ��ܲ����Գ�����Ļ
			endingView.setVisibility(View.GONE);
		}
		
		super.setAdapter(adapter);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		return false;
	}
	
	private String gettime () {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd  HH:mm:ss");
		String date = format.format(new Date());
		return date;
	}

}