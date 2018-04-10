package com.babacit.alarm.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.babacit.alarm.R;
import com.babacit.alarm.ui.activity.AnniversaryAlarmActivity;
import com.babacit.alarm.ui.activity.BirthdayManagerAlarmActivity;
import com.babacit.alarm.ui.activity.CustomAlarmActivity;
import com.babacit.alarm.ui.activity.DailyRemindAlarmActivity;
import com.babacit.alarm.ui.activity.EyesProtectActivity;
import com.babacit.alarm.ui.activity.FestivalRemindAlarmActivity;
import com.babacit.alarm.ui.activity.GetupAlarmActivity;
import com.babacit.alarm.ui.activity.HomeworkRemindActivity;
import com.babacit.alarm.ui.activity.MainActivity;
import com.babacit.alarm.ui.activity.WaterAndMedicineActivity;
import com.babacit.alarm.utils.ToastUtil;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class ChooseAlarmTypeDlgFragment extends DialogFragment implements
		OnClickListener {

	private String[] griditem1, griditem2;

	private GridView mGvOne, mGvTwo;
	private View mSep1, mSep2;

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
		mGvOne = (GridView) view.findViewById(R.id.gv_one);
		mGvOne.setSelector(new ColorDrawable(Color.TRANSPARENT));

		mGvTwo = (GridView) view.findViewById(R.id.gv_two);
		mGvTwo.setSelector(new ColorDrawable(Color.TRANSPARENT));

		griditem1 = getArguments().getStringArray("grid_1");
		griditem2 = getArguments().getStringArray("grid_2");
		List<Map<String, Object>> items1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> items2 = new ArrayList<Map<String, Object>>();

		for (String item : griditem1) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", item);
			items1.add(map);
		}
		SimpleAdapter adapter1 = new SimpleAdapter(getActivity(), items1,
				R.layout.grid_item, new String[] { "title" },
				new int[] { R.id.btn_grid_item });
		mGvOne.setAdapter(adapter1);

		if (griditem2 != null && griditem2.length > 0) {
			mGvTwo.setVisibility(View.VISIBLE);
			mSep1.setVisibility(View.VISIBLE);
			mSep2.setVisibility(View.INVISIBLE);
			for (String item : griditem2) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", item);
				items2.add(map);
			}
			SimpleAdapter adapter2 = new SimpleAdapter(getActivity(), items2,
					R.layout.grid_item, new String[] { "title" },
					new int[] { R.id.btn_grid_item });
			mGvTwo.setAdapter(adapter2);
		}

		mGvOne.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (position) {
				case 0:
					//SYY test 2017/12/15
					Intent getup = new Intent(getActivity(),
							GetupAlarmActivity.class);
					startActivityForResult(getup, 3);
					dismiss();
					break;
				case 1:
					Intent homework = new Intent(getActivity(),
							HomeworkRemindActivity.class);
					startActivityForResult(homework, 5);
					dismiss();
					break;
				case 2:
					Intent birthday = new Intent(getActivity(),
							BirthdayManagerAlarmActivity.class);
					startActivityForResult(birthday, 6);
					dismiss();
					break;
				case 3:
					Intent daily = new Intent(getActivity(),
							DailyRemindAlarmActivity.class);
					startActivityForResult(daily, 7);
					dismiss();
					break;
				case 4:
					Intent eye = new Intent(getActivity(),
							EyesProtectActivity.class);
					startActivityForResult(eye, 8);
					dismiss();
					break;
				case 5:
					Intent anni = new Intent(getActivity(),
							AnniversaryAlarmActivity.class);
					startActivityForResult(anni, 9);
					dismiss();
					break;
				case 6:
					Intent custom = new Intent(getActivity(),
							CustomAlarmActivity.class);
					startActivityForResult(custom, 12);
					dismiss();
					break;
				case 7:
					Intent water = new Intent(getActivity(),
							WaterAndMedicineActivity.class);
					startActivityForResult(water, 10);
					dismiss();
					break;
				case 8:
					Intent festival = new Intent(getActivity(),
							FestivalRemindAlarmActivity.class);
					startActivityForResult(festival, 11);
					dismiss();
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
		default:
			break;
		}
	}

}