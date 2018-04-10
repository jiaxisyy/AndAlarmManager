package com.babacit.alarm.ui.activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.entity.CompletionInfo;
import com.babacit.alarm.entity.DayCompletionInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.QueryCompletionBarInfoServer;
import com.babacit.alarm.server.QueryCompletionPieInfoServer;
import com.babacit.alarm.ui.view.BarChartView;
import com.babacit.alarm.ui.view.SingleBarChartView;
import com.babacit.alarm.utils.NumberUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.babacit.alarm.utils.UnitUtils;
import com.umeng.analytics.MobclickAgent;

public class FlowerStatisticActivity extends BaseActivity implements
		OnClickListener {
	private SharedConfig config;
	private static final int MSG_QUERY_PIE_INFO_SUCCESS = 0;
	private static final int MSG_QUERY_PIE_INFO_FAIL = 1;
	private static final int MSG_QUERY_BAR_INFO_SUCCESS = 2;
	private static final int MSG_QUERY_BAR_INFO_FAIL = 3;
	private static int[] COLORS1 = new int[] { 0xFFFF8F3D, 0xFFFFE9A0 };
	private static int[] COLORS2 = new int[] { 0xFF91CF3C, 0xFFC3F089 };
	private static int[] COLORS3 = new int[] { 0xFF01B3F9, 0xFF91E0FF };
	private CategorySeries mSeries1 = new CategorySeries("");
	private CategorySeries mSeries2 = new CategorySeries("");
	private CategorySeries mSeries3 = new CategorySeries("");
	private DefaultRenderer mRenderer1 = new DefaultRenderer();
	private DefaultRenderer mRenderer2 = new DefaultRenderer();
	private DefaultRenderer mRenderer3 = new DefaultRenderer();
	private GraphicalView mChartView1, mChartView2, mChartView3;
	private LinearLayout mLlPieChart, mLlBarChart;

	private double pieValues1[] = new double[2];
	private double pieValues2[] = new double[2];
	private double pieValues3[] = new double[2];
	private String pieCategories[] = new String[] { "已完成", "未完成" };
	// BarChartView
	private List<DayCompletionInfo> _dayBarInfos = new ArrayList<DayCompletionInfo>();
	private List<CompletionInfo> _weekBarInfos = new ArrayList<CompletionInfo>();
	private List<CompletionInfo> _monthBarInfos = new ArrayList<CompletionInfo>();

	public static String WEEK_BARCHART_TEXT[] = { "", "周一", "周二", "周三", "周四",
			"周五", "周六", "周日" };
	public static String MONTH_BARCHART_TEXT[] = { "", "第一周", "第二周", "第三周",
			"第四周", "第五周"/*,"第六周"*/ };
	public static String YEAR_BARCHART_TEXT[] = { "", "1月", "2月", "3月", "4月",
			"5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
	private RelativeLayout mRlWeekBarChart, mRlMonthBarChart, mRlYearBarChart;
	private double week_first[] = new double[7];
	private double week_second[] = new double[7];
	private double week_third[] = new double[7];

	private double month_first[] = new double[5];
	private double month_second[] = new double[5];
	private double month_third[] = new double[5];

	private double year_first[] = new double[12];
	private double year_second[] = new double[12];
	private double year_third[] = new double[12];
	private List<String> weekOptions = new ArrayList<String>();
	private List<String> monthOptions = new ArrayList<String>();
	private List<String> yearOptions = new ArrayList<String>();
	private SingleBarChartView weekBarChart;
	private BarChartView monthBarChart, yearBarChart;
	private List<CompletionInfo> _pieChartInfos = new ArrayList<CompletionInfo>();

	private RequestCallBack queryPieInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(MSG_QUERY_PIE_INFO_SUCCESS);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_QUERY_PIE_INFO_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack queryBarInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(MSG_QUERY_BAR_INFO_SUCCESS);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_QUERY_BAR_INFO_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_QUERY_PIE_INFO_SUCCESS:
				_pieChartInfos = (List<CompletionInfo>) msg.obj;
				initPieChart();
				new QueryCompletionBarInfoServer().start(config.getUserId(),
						config.getAlarmCode(), queryBarInfoCallBack);
				break;
			case MSG_QUERY_PIE_INFO_FAIL:
				ToastUtil.showToast(getApplicationContext(),
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case MSG_QUERY_BAR_INFO_SUCCESS:
				HashMap map = (HashMap<String, List>) msg.obj;
				if (map.containsKey("day")) {
					_dayBarInfos = (List<DayCompletionInfo>) map.get("day");
					int size = _dayBarInfos.size();
					String str = "";
					for (int i = 0; i < size; i++) {
						str += String.valueOf(_dayBarInfos.get(i).getDay());
					}
					for (int i = 0; i < 7; i++) {
						DayCompletionInfo day = new DayCompletionInfo();
						if (!str.contains(String.valueOf(i))) {
							day.setDay(i);
							day.setCompleteCount(0);
							_dayBarInfos.add(day);
						}
					}
					// 本周不倒序，与iOS显示统一
					Collections.sort(_dayBarInfos,
							new Comparator<DayCompletionInfo>() {
								public int compare(DayCompletionInfo lhs,
										DayCompletionInfo rhs) {
									int a0 = lhs.getDay();
									int a1 = rhs.getDay();
									if (a1 < a0) {
										return 1;
									} else
										return -1;
								};
							});
					if (_dayBarInfos != null && _dayBarInfos.size() > 0) {
						for (int i = 0; i < _dayBarInfos.size(); i++) {
							week_first[_dayBarInfos.get(i).getDay()] = _dayBarInfos
									.get(_dayBarInfos.get(i).getDay())
									.getCompleteCount();
							week_second[_dayBarInfos.get(i).getDay()] = _dayBarInfos
									.get(i).getCompleteCount();
							week_third[_dayBarInfos.get(i).getDay()] = 0.11;
						}
					}
				}
				if (map.containsKey("week")) {
					_weekBarInfos = (List<CompletionInfo>) map.get("week");
					int size = _weekBarInfos.size();
					String str = "";
					for (int i = 0; i < size; i++) {
						str += String.valueOf(_weekBarInfos.get(i).getIndex());
					}
					for (int i = 0; i < 4; i++) {
						CompletionInfo week = new CompletionInfo();
						if (!str.contains(String.valueOf(i))) {
							week.setCompleteCount(0);
							week.setIncompleteCount(0);
							_weekBarInfos.add(week);
						}
					}
					// 本月数据不倒序，与iOS显示统一
					Collections.sort(_weekBarInfos,
							new Comparator<CompletionInfo>() {
								public int compare(CompletionInfo lhs,
										CompletionInfo rhs) {
									int a0 = lhs.getIndex();
									int a1 = rhs.getIndex();
									if (a1 < a0) {
										return 1;
									} else
										return -1;
								};
							});
					if (_weekBarInfos != null && _weekBarInfos.size() > 0) {
						for (int i = 0; i < _weekBarInfos.size(); i++) {
							month_first[i] = _weekBarInfos.get(i)
									.getCompleteCount();
							month_second[i] = _weekBarInfos.get(i)
									.getIncompleteCount();
							if (month_first[i] == 0) {
								month_third[i] = 0;
							} else
								month_third[i] = ((double) _weekBarInfos.get(i)
										.getCompleteCount())
										/ (_weekBarInfos.get(i)
												.getCompleteCount() + _weekBarInfos
												.get(i).getIncompleteCount());
						}
					}
				}
				if (map.containsKey("month")) {
					_monthBarInfos = (List<CompletionInfo>) map.get("month");
					int size = _monthBarInfos.size();
					String str = "";
					for (int i = 0; i < size; i++) {
						str += NumberUtils.number2Character(_monthBarInfos.get(
								i).getIndex());
					}
					for (int i = 1; i <= 12; i++) {
						CompletionInfo month = new CompletionInfo();
						if (!str.contains(NumberUtils.number2Character(i))) {
							month.setCompleteCount(0);
							month.setIncompleteCount(0);
							_monthBarInfos.add(month);
						} else
							continue;
					}
					// 本年不倒序，与iOS显示统一
					Collections.sort(_monthBarInfos,
							new Comparator<CompletionInfo>() {
								public int compare(CompletionInfo lhs,
										CompletionInfo rhs) {
									int a0 = lhs.getIndex();
									int a1 = rhs.getIndex();
									if (a1 < a0) {
										return 1;
									} else
										return -1;
								};
							});
					if (_monthBarInfos != null && _monthBarInfos.size() > 0) {
						for (int i = 0; i < _monthBarInfos.size(); i++) {
							CompletionInfo completionInfo = _monthBarInfos
									.get(i);
							year_first[i] = completionInfo.getCompleteCount();
							year_second[i] = completionInfo
									.getIncompleteCount();
							if (year_first[i] == 0) {
								year_third[i] = 0;
							} else
								year_third[i] = ((double) completionInfo
										.getCompleteCount())
										/ (completionInfo.getCompleteCount() + completionInfo
												.getIncompleteCount());
						}
					}
				}
				initBarChart();
				break;
			case MSG_QUERY_BAR_INFO_FAIL:
				ToastUtil.showToast(getApplicationContext(),
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flower_statistic);
		config = new SharedConfig(this);
		bindField();
		new QueryCompletionPieInfoServer().start(config.getUserId(),
				config.getAlarmCode(), queryPieInfoCallBack);
	}

	private void bindField() {
		findViewById(R.id.btn_statistic_back).setOnClickListener(this);
		mLlPieChart = (LinearLayout) findViewById(R.id.ll_pie_chart);
		mLlBarChart = (LinearLayout) findViewById(R.id.ll_bar_chart);
		mRlWeekBarChart = (RelativeLayout) findViewById(R.id.rl_week_bar);
		mRlMonthBarChart = (RelativeLayout) findViewById(R.id.rl_month_bar);
		mRlYearBarChart = (RelativeLayout) findViewById(R.id.rl_year_bar);

		findViewById(R.id.btn_finish_status).setOnClickListener(this);
		findViewById(R.id.btn_data_trend).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_statistic_back:
			finish();
			break;
		case R.id.btn_finish_status:
			mLlPieChart.setVisibility(View.VISIBLE);
			mLlBarChart.setVisibility(View.GONE);
			break;
		case R.id.btn_data_trend:
			mLlPieChart.setVisibility(View.GONE);
			mLlBarChart.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void initPieChart() {
		mRenderer1.setStartAngle(270);
		mRenderer1.setDisplayValues(true);
		mRenderer1.setPanEnabled(false);

		mRenderer2.setStartAngle(270);
		mRenderer2.setDisplayValues(true);
		mRenderer2.setPanEnabled(false);

		mRenderer3.setStartAngle(270);
		mRenderer3.setDisplayValues(true);
		mRenderer3.setPanEnabled(false);

		if (_pieChartInfos != null && _pieChartInfos.size() > 0) {
			int size = _pieChartInfos.size();
			for (int i = 0; i < size; i++) {
				CompletionInfo pieInfo = _pieChartInfos.get(i);
				switch (pieInfo.getType()) {
				case 1:
					pieValues1[0] = pieInfo.getCompleteCount();
					pieValues1[1] = pieInfo.getIncompleteCount();
					break;
				case 2:
					pieValues2[0] = pieInfo.getCompleteCount();
					pieValues2[1] = pieInfo.getIncompleteCount();
					break;
				case 3:
					pieValues3[0] = pieInfo.getCompleteCount();
					pieValues3[1] = pieInfo.getIncompleteCount();
					break;
				default:
					break;
				}
			}
		}

		for (int i = 0; i < pieCategories.length; i++) {
			mSeries1.add(pieCategories[i], pieValues1[i]
					/ (pieValues1[0] + pieValues1[1]));
			SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer();
			renderer1.setChartValuesFormat(NumberFormat.getPercentInstance());
			renderer1.setColor(COLORS1[i]);
			renderer1.setShowLegendItem(false);
			mRenderer1.addSeriesRenderer(renderer1);
			mRenderer1.setShowLabels(false);

			mSeries2.add(pieCategories[i], pieValues2[i]
					/ (pieValues2[0] + pieValues2[1]));
			SimpleSeriesRenderer renderer2 = new SimpleSeriesRenderer();
			renderer2.setChartValuesFormat(NumberFormat.getPercentInstance());
			renderer2.setColor(COLORS2[i]);
			renderer2.setShowLegendItem(false);
			mRenderer2.addSeriesRenderer(renderer2);
			mRenderer2.setShowLabels(false);

			mSeries3.add(pieCategories[i], pieValues3[i]
					/ (pieValues3[0] + pieValues3[1]));
			SimpleSeriesRenderer renderer3 = new SimpleSeriesRenderer();
			renderer3.setChartValuesFormat(NumberFormat.getPercentInstance());
			renderer3.setColor(COLORS3[i]);
			renderer3.setShowLegendItem(false);
			mRenderer3.addSeriesRenderer(renderer3);
			mRenderer3.setShowLabels(false);
		}

		if (mChartView1 == null) {
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.chart1);
			mChartView1 = ChartFactory.getPieChartView(this, mSeries1,
					mRenderer1);
			mRenderer1.setZoomEnabled(false);
			mRenderer1.setLegendHeight(UnitUtils.dip2px(this, 14));
			mRenderer1.setLabelsTextSize(UnitUtils.dip2px(this, 12));
			mRenderer1.setLegendTextSize(UnitUtils.dip2px(this, 12));
			mRenderer1.setChartTitleTextSize(UnitUtils.dip2px(this, 20));
			layout.addView(mChartView1, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView1.repaint();
		}
		if (mChartView2 == null) {
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.chart2);
			mChartView2 = ChartFactory.getPieChartView(this, mSeries2,
					mRenderer2);
			mRenderer2.setZoomEnabled(false);
			mRenderer2.setLegendHeight(UnitUtils.dip2px(this, 14));
			mRenderer2.setLabelsTextSize(UnitUtils.dip2px(this, 12));
			mRenderer2.setLegendTextSize(UnitUtils.dip2px(this, 12));
			mRenderer2.setChartTitleTextSize(UnitUtils.dip2px(this, 20));
			layout.addView(mChartView2, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView2.repaint();
		}
		if (mChartView3 == null) {
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.chart3);
			mChartView3 = ChartFactory.getPieChartView(this, mSeries3,
					mRenderer3);
			mRenderer3.setZoomEnabled(false);
			mRenderer3.setLegendHeight(UnitUtils.dip2px(this, 14));
			mRenderer3.setLabelsTextSize(UnitUtils.dip2px(this, 12));
			mRenderer3.setLegendTextSize(UnitUtils.dip2px(this, 12));
			mRenderer3.setChartTitleTextSize(UnitUtils.dip2px(this, 20));
			layout.addView(mChartView3, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			mChartView3.repaint();
		}
	}

	private int getMax(double[] first, double[] second) {
		int maxFirst = 0;
		int maxSecond = 0;
		for (int i = 0; i < first.length; i++) {
			maxFirst = (int) (first[i] > maxFirst ? first[i] : maxFirst);
			maxSecond = (int) (second[i] > maxSecond ? second[i] : maxSecond);
		}
		return maxFirst > maxSecond ? maxFirst : maxSecond;
	}

	private int getMax(double[] first) {
		int max = 0;
		for (int i = 0; i < first.length; i++) {
			max = (int) (first[i] > max ? first[i] : max);
		}
		return max;
	}

	private void initBarChart() {
		for (String tem : WEEK_BARCHART_TEXT) {
			weekOptions.add(tem);
		}

		weekBarChart = new SingleBarChartView(FlowerStatisticActivity.this);
		weekBarChart.initData(week_first, weekOptions, "本周完成情况",
				getMax(week_first) + 5);

		// weekBarChart = new BarChartView(FlowerStatisticActivity.this);
		// weekBarChart.initData(week_first, week_second, week_third,
		// weekOptions,
		// "本周完成情况", getMax(week_first, week_second) + 10);
		mRlWeekBarChart.setBackgroundColor(0xFFF4FAEC);
		mRlWeekBarChart.addView(weekBarChart.getBarChartView());

		for (String tem : MONTH_BARCHART_TEXT) {
			monthOptions.add(tem);
		}
		monthBarChart = new BarChartView(FlowerStatisticActivity.this);
		monthBarChart.initData(month_first, month_second, month_third,
				monthOptions, "本月完成情况", getMax(month_first, month_second) + 10);
		mRlMonthBarChart.setBackgroundColor(0xffffffff);
		mRlMonthBarChart.addView(monthBarChart.getBarChartView());

		for (String tem : YEAR_BARCHART_TEXT) {
			yearOptions.add(tem);
		}
		yearBarChart = new BarChartView(FlowerStatisticActivity.this);
		yearBarChart.initData(year_first, year_second, year_third, yearOptions,
				"今年完成情况", getMax(year_first, year_second) + 10);
		mRlYearBarChart.setBackgroundColor(0xffffffff);
		mRlYearBarChart.addView(yearBarChart.getBarChartView());
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
