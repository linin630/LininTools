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
 * 可刷新的listview
 * 
 * @author user
 * 
 */
public class XListView extends ListView implements OnScrollListener,android.widget.AdapterView.OnItemLongClickListener {

	private static final String TAG = "listview";

	private final static int RELEASE_To_REFRESH = 0;// 发布刷新
	private final static int PULL_To_REFRESH = 1;// 下拉更新
	private final static int REFRESHING = 2;// 刷新
	private final static int DONE = 3;// 完成
	private final static int LOADING = 4;// 加载中
	
	private final static int ENDINT_DONE = 0;// 完成/等待刷新
	private final static int ENDINT_LOADING = 1;// 加载中
	
	// 实际的padding的距离与界面上偏移距离的比例
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

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
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
	private boolean canloadmore = true;// 可以加载更多？
	private boolean canrefresh = true;// 可以刷新？
	private boolean canloadmore_more = true;// 可以显示更多？
	private Context parentcontext;
	
	private LinearLayout endingView;
	private int lastItemIndex;
	private int count;
	private boolean enougcount;// 足够数量充满屏幕？
	

	// 网络状态图标
	private LinearLayout statView;
	private ImageView stat_Image;
	private TextView stat_Text;
	
	public XListView(Context context) {
		super(context);
		parentcontext= context;
		init();
	}

	/**
	 * 显示加载数据状态！
	 */
	public void setRefreshingstat() {
		setAllstatclose();// 隐藏所有状态
		
		headView.setPadding(0, 0, 0, 0);
		head_progressBar.setVisibility(View.VISIBLE);
		head_arrowImageView.clearAnimation();
		head_arrowImageView.setVisibility(View.GONE);
		head_tipsTextview.setText("正在刷新...");
		head_lastUpdatedTextView.setText("刷新时间：" + gettime());
		head_lastUpdatedTextView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 关闭所有状态
	 */
	public void setAllstatclose() {
		if (headView.getTop() >= 0) {// 刷新中的状态！
			onRefreshComplete();
		}
		if (!ending_tipsTextview.getText().toString().equals("更多")) {// 加载更多的状态
			ending_tipsTextview.setText("加载失败！");
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
		setNoDataStat("暂无数据！");
	}
	
	/**
	 * 设置没有数据时显示的视图
	 * 
	 * @param tipText
	 *            提示信息
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
		setNetErrorStat("网络错误！");
	}
	
	public void setNetErrorStat(String tipText)  {
//		removeFooterView(endingView);
		if (getFooterViewsCount() == 1 ){
			addFooterView(statView);
			stat_Image.setImageResource(android.R.drawable.ic_dialog_alert);
			stat_Text.setText(tipText);
		}
		// 错误：@android:drawable/ic_dialog_alert
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
		// 网络状态view
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
		
		// 初始是隐藏的，因为数据可能不足以充满屏幕
		endingView.setVisibility(View.GONE);
		endingstate = ENDINT_DONE;
		
		addFooterView(endingView, null, false);
	}
	
	/**
	 * 返回是否有足够的item充满屏幕
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
			// SCROLL_STATE_IDLE=0，滑动停止
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
						// Log.v(TAG, "在down时候记录当前位置‘");
				}
				break;

			case MotionEvent.ACTION_UP:

				if (headstate != REFRESHING && headstate != LOADING) {
					if (headstate == DONE) {
							// 什么都不做
					}
					if (headstate == PULL_To_REFRESH) {
						headstate = DONE;
						changeHeaderViewByState();

							// Log.v(TAG, "由下拉刷新状态，到done状态");
					}
					if (headstate == RELEASE_To_REFRESH) {
						headstate = REFRESHING;
						changeHeaderViewByState();
						onRefresh();

							// Log.v(TAG, "由松开刷新状态，到done状态");
					}
				}

				isRecored = false;
				isBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
						// Log.v(TAG, "在move时候记录下位置");
					isRecored = true;
					startY = tempY;
				}

				if (headstate != REFRESHING && isRecored && headstate != LOADING) {

						// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

						// 可以松手去刷新了
					if (headstate == RELEASE_To_REFRESH) {

						setSelection(0);

							// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - startY) / RATIO < headContentHeight)
								&& (tempY - startY) > 0) {
							headstate = PULL_To_REFRESH;
							changeHeaderViewByState();

								// Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
						}
							// 一下子推到顶了
						else if (tempY - startY <= 0) {
							headstate = DONE;
							changeHeaderViewByState();

								// Log.v(TAG, "由松开刷新状态转变到done状态");
						}
							// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
						else {
								// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
						// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (headstate == PULL_To_REFRESH) {

						setSelection(0);

							// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							headstate = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();

								// Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
						}
							// 上推到顶了
						else if (tempY - startY <= 0) {
							headstate = DONE;
							changeHeaderViewByState();

								// Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
						}
					}

						// done状态下
					if (headstate == DONE) {
						if (tempY - startY > 0) {
							headstate = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}

						// 更新headView的size
					if (headstate == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}

						// 更新headView的paddingTop
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
	 * 当状态改变时候，调用该方法，以更新界面
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

			head_tipsTextview.setText("松开进行刷新");

			// Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			head_progressBar.setVisibility(View.GONE);
			head_tipsTextview.setVisibility(View.VISIBLE);
			head_lastUpdatedTextView.setVisibility(View.VISIBLE);
			head_arrowImageView.clearAnimation();
			head_arrowImageView.setVisibility(View.VISIBLE);// 可不可视
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				head_arrowImageView.clearAnimation();
				head_arrowImageView.startAnimation(reverseAnimation);

				head_tipsTextview.setText("向下拖动进行刷新");
			} else {
				head_tipsTextview.setText("向下拖动进行刷新");
			}
			// Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			head_progressBar.setVisibility(View.VISIBLE);
			head_arrowImageView.clearAnimation();
			head_arrowImageView.setVisibility(View.GONE);
			head_tipsTextview.setText("正在刷新...");
			head_lastUpdatedTextView.setVisibility(View.VISIBLE);
			// Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			head_progressBar.setVisibility(View.GONE);
			head_arrowImageView.clearAnimation();
			head_arrowImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			head_tipsTextview.setText("松开进行刷新");
			head_lastUpdatedTextView.setVisibility(View.VISIBLE);

			// Log.v(TAG, "当前状态，done");
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
	 * 加载更多已结束，已无数据可加载
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
	 * 刷新完成
	 */
	public void onRefreshComplete() {
		headstate = DONE;
		head_lastUpdatedTextView.setText("上次刷新 ：" + gettime());
		changeHeaderViewByState();
		
		// 刷新完成,默认不可以加载更多！
		enougcount = false;
		changeEndingViewByState();
	}
	
	/**
	 * 加载更多完成
	 */
	public void onLoadMoreComplete() {
		// 提示状态隐藏
		if (getFooterViewsCount() == 2) {
			removeFooterView(statView);
		}
		// 下拉刷新隐藏
		endingstate = ENDINT_DONE;
		changeEndingViewByState();
	}

	/**
	 * 改变加载更多状态
	 */
	private void  changeEndingViewByState() {
		if (canloadmore) {
			// 允许加载更多
			switch (endingstate) {
			case ENDINT_LOADING:// 刷新中
				ending_tipsTextview.setText("加载中...");
				ending_tipsTextview.setVisibility(View.VISIBLE);
				ending_progressBar.setVisibility(View.VISIBLE);
				head_arrowImageView.startAnimation(animation);
				break;
			case ENDINT_DONE:// 完成、等待刷新
			default:
				if (enougcount) {					
					endingView.setVisibility(View.VISIBLE);
				} else {
					endingView.setVisibility(View.GONE);
				}
				if(canloadmore_more){
					ending_tipsTextview.setText("更多");
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
			if (head_lastUpdatedTextView.getText().equals("")) {// 初始化后无时间信息显示
				head_lastUpdatedTextView.setText("刷新时间:" + gettime());
			}
			// 清空状态
			this.onLoadMoreComplete();// 加载完成
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
	
	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
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
			head_lastUpdatedTextView.setText("上次刷新 ：" + gettime());
		}
		
		if (endingstate!=ENDINT_LOADING) {
			// 初始是隐藏的，因为数据可能不足以充满屏幕
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