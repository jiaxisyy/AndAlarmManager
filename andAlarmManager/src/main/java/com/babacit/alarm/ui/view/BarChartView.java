package com.babacit.alarm.ui.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.babacit.alarm.utils.UnitUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

public class BarChartView {
	private static int margins[] = new int[] { 70, 70, 70, 70 };
	private static String[] titles = new String[] { "已完成", "未完成"/*, "完成率"*/ };
	private List<double[]> values = new ArrayList<double[]>();
	private static int[] colors = new int[] { 0xFF00B3F9, 0xFF91D03C/*,
			0xFFAAAAAA*/ };
	private XYMultipleSeriesRenderer renderer;
	private Context mContext;
	private String mTitle;
	private List<String> option;
	private int maxValue;

	public BarChartView(Context context) {
		this.mContext = context;
		this.renderer = new XYMultipleSeriesRenderer();
	}

	public void initData(double[] first, double[] second, double[] third,
			List<String> option, String title, int maxValue) {
		this.values.add(first);
		this.values.add(second);
//		this.values.add(third);
		this.mTitle = title;
		this.option = option;
		this.maxValue = maxValue;
	}

	public View getBarChartView() {
		buildBarRenderer();
		setChartSettings(renderer, mTitle, "", "", 0, 6, 0, maxValue, Color.BLACK,
				Color.LTGRAY);
		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
//		renderer.getSeriesRendererAt(2).setChartValuesFormat(
//				NumberFormat.getPercentInstance());
		int size = option.size();
		for (int i = 0; i < size; i++) {
			renderer.addXTextLabel(i, option.get(i));
		}
		renderer.setMargins(margins);
		renderer.setMarginsColor(0xFFF4FAEC);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(false, false);// 设置x，y方向都不可以放大或缩小
		renderer.setInScroll(false);
		renderer.setBackgroundColor(0xFFF4FAEC);
		renderer.setApplyBackgroundColor(true);
		renderer.setShowGridX(true);
		renderer.setShowLegend(false);
		View view = ChartFactory.getBarChartView(mContext,
				buildBarDataset(titles, values), renderer, Type.DEFAULT); // Type.STACKED
		view.setBackgroundColor(0xFFF4FAEC);
		return view;
	}

	private XYMultipleSeriesDataset buildBarDataset(String[] titles,
			List<double[]> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			CategorySeries series = new CategorySeries(titles[i]);
			double[] v = values.get(i);
			int seriesLength = v.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(v[k]);
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setXLabels(0);
		renderer.setYLabels(10);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setXLabelsAlign(Align.CENTER);
		// renderer.setXLabelsColor(0xff000000);//设置X轴上的字体颜色
		// renderer.setYLabelsColor(0,0xff000000);//设置Y轴上的字体颜色

	}

	/*
	 * 初始化柱子
	 */
	protected void buildBarRenderer() {
		if (null == renderer) {
			return;
		}
		renderer.setBarWidth(UnitUtils.dip2px(mContext, 15));
		renderer.setBarSpacing(0.2);
		renderer.setAxisTitleTextSize(UnitUtils.dip2px(mContext, 10));
		renderer.setChartTitleTextSize(UnitUtils.dip2px(mContext, 16));
		renderer.setLabelsTextSize(UnitUtils.dip2px(mContext, 12));
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer ssr = new SimpleSeriesRenderer();
			ssr.setChartValuesTextAlign(Align.RIGHT);
			ssr.setChartValuesTextSize(15);
			ssr.setDisplayChartValues(true);
			ssr.setColor(colors[i]);
			renderer.addSeriesRenderer(ssr);
		}
	}

}
