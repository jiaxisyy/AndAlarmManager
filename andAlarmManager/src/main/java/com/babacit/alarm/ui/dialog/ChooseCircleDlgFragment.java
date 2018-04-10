package com.babacit.alarm.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.babacit.alarm.R;
import com.babacit.alarm.entity.AlarmInfo;
import com.babacit.alarm.logger.Logger;
import com.babacit.alarm.ui.adapter.CircleAdapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class ChooseCircleDlgFragment extends DialogFragment implements
		OnClickListener {
	private String[] griditem1, griditem2;

	private GridView mGvOne, mGvTwo;
	private View mSep1, mSep2, mSep3;
	private Button mBtConfirm;
	private CircleAdapter adapter1, adapter2;
	private List<Map<String, Object>> items1 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> items2 = new ArrayList<Map<String, Object>>();
	private JSONObject json = new JSONObject();
	private String week = "";
	private int holidayFilter = 0;
	private int timeType = 0;
	private AlarmInfo _alarmInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = 0;
		setStyle(style, theme);
	}

	@Override
	public void onStart() {
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.grid_dialog_fragment, container,
				false);

		mSep1 = view.findViewById(R.id.v_grid_sep_1);
		mSep2 = view.findViewById(R.id.v_grid_sep_2);
		mSep3 = view.findViewById(R.id.v_grid_sep_3);
		mSep3.setVisibility(View.VISIBLE);
		mBtConfirm = (Button) view.findViewById(R.id.btn_confirm_circle);
		mBtConfirm.setVisibility(View.VISIBLE);
		mBtConfirm.setOnClickListener(this);
		mGvOne = (GridView) view.findViewById(R.id.gv_one);
		mGvOne.setSelector(new ColorDrawable(Color.TRANSPARENT));

		mGvTwo = (GridView) view.findViewById(R.id.gv_two);
		mGvTwo.setSelector(new ColorDrawable(Color.TRANSPARENT));

		griditem1 = getArguments().getStringArray("grid_1");
		griditem2 = getArguments().getStringArray("grid_2");
		_alarmInfo = getArguments().getParcelable("alarm");
		holidayFilter = _alarmInfo.getHolidayFilter();
		timeType = _alarmInfo.getTimeType();
		week = TextUtils.isEmpty(_alarmInfo.getDay()) ? "" : _alarmInfo.getDay() + ",";

		for (int j = 0; j < griditem1.length; j++) {
			String item = griditem1[j];
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", item);
			if (timeType == 5) {
				if (week.contains(String.valueOf(j))) {
					map.put("status", true);
				} else
					map.put("status", false);
			} else if ((timeType == 1 && j == 9) || (timeType == 2 && j == 7)
					|| (timeType == 3 && j == 8)) {
				map.put("status", true);
			} else
				map.put("status", false);
			items1.add(map);
		}
		adapter1 = new CircleAdapter(getActivity(), items1);
		mGvOne.setAdapter(adapter1);

		if (griditem2 != null && griditem2.length > 0) {
			mGvTwo.setVisibility(View.VISIBLE);
			mSep1.setVisibility(View.VISIBLE);
			mSep2.setVisibility(View.INVISIBLE);
			// for (String item : griditem2) {
			for (int i = 0; i < griditem2.length; i++) {
				String item = griditem2[i];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", item);
				if (holidayFilter == i + 1) {
					map.put("status", true);
				} else
					map.put("status", false);
				items2.add(map);
			}
			adapter2 = new CircleAdapter(getActivity(), items2);
			mGvTwo.setAdapter(adapter2);
		}

		mGvOne.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (null == getTag()) {
					return;
				}
				boolean status = (Boolean) items1.get(position).get("status");
				if (week.contains(String.valueOf(position))) {
					week = week.replace((position) + ",", "");
				} else if (position <= 6
						&& !week.contains(String.valueOf(position))) {
					week += ((position) + ",");
				} else {
					week = "";
				}
				// 设置周一 周二 周三 周四 周五 周六 周日应该自动跳转为每天
				if (week.length() == 14 && items1.size() > 7) {
					position = 7;
					week = "";
				}
				for (int i = 0; i < items1.size(); i++) {
					if (position > 6) {
						items1.get(i).put("status", false);
					} else if (position <= 6 && i > 6) {
						items1.get(i).put("status", false);
					}
					if (i <= 6) {
						timeType = 5;
					}
				}
				items1.get(position).put("status", !status);
				adapter1.notifyDataSetChanged();
				switch (position) {
				case 7:
					timeType = 2;
					break;
				case 8:
					timeType = 3;
					break;
				case 9:
					timeType = 1;
					break;
				default:
					break;
				}
			}
		});
		mGvTwo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (null == getTag()) {
					return;
				}
				boolean status = (Boolean) items2.get(position).get("status");
				items2.get(position).put("status", !status);
				for (int i = 0; i < items2.size(); i++) {
					if (i != position) {
						items2.get(i).put("status", false);
					}
				}
				adapter2.notifyDataSetChanged();
				switch (position) {
				case 0:
					if (status) {
						holidayFilter = 0;
					} else {
						holidayFilter = 1;
					}
					break;
				case 1:
					if (status) {
						holidayFilter = 0;
					} else
						holidayFilter = 2;
					break;
				case 2:
					if (status) {
						holidayFilter = 0;
					} else
						holidayFilter = 3;
					break;
				default:
					break;
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		try {
			OnMyDialogClickListener act = (OnMyDialogClickListener) activity;
		} catch (Exception e) {

		}
		super.onAttach(activity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm_circle:
			OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
			try {
				if (!week.equals("")) {
					week = week.substring(0, week.length() - 1);
				}
				json.put("week", week);
				json.put("timeType", timeType);
				json.put("holidayFilter", holidayFilter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			act.onDialogConfirm(getTag(), false, json.toString());
			dismiss();
			break;
		default:
			break;
		}
	}
}