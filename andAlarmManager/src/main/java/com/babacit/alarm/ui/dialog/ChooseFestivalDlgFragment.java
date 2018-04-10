package com.babacit.alarm.ui.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.babacit.alarm.R;
import com.babacit.alarm.entity.Holiday;

import android.app.Activity;
import android.app.DialogFragment;
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

public class ChooseFestivalDlgFragment extends DialogFragment implements
		OnClickListener {

	private String[] griditem3;
	private List<Holiday> gridItem1, gridItem2;

	private GridView mGvOne, mGvTwo, mGvThree;
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

		mGvThree = (GridView) view.findViewById(R.id.gv_three);
		mGvThree.setSelector(new ColorDrawable(Color.TRANSPARENT));

		gridItem1 = getArguments().getParcelableArrayList("grid_1");
		gridItem2 = getArguments().getParcelableArrayList("grid_2");
		griditem3 = getArguments().getStringArray("grid_3");
		List<Map<String, Object>> items1 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> items2 = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> items3 = new ArrayList<Map<String, Object>>();

		for (Holiday h : gridItem1) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", h.getHolidayName());
			items1.add(map);
		}

		SimpleAdapter adapter1 = new SimpleAdapter(getActivity(), items1,
				R.layout.grid_item, new String[] { "title" },
				new int[] { R.id.btn_grid_item });
		mGvOne.setAdapter(adapter1);

		if (gridItem2 != null && gridItem2.size() > 0) {
			mGvTwo.setVisibility(View.VISIBLE);
			mSep1.setVisibility(View.VISIBLE);
			mSep2.setVisibility(View.INVISIBLE);
			for (Holiday h : gridItem2) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", h.getHolidayName());
				items2.add(map);
			}
			SimpleAdapter adapter2 = new SimpleAdapter(getActivity(), items2,
					R.layout.grid_item, new String[] { "title" },
					new int[] { R.id.btn_grid_item });
			mGvTwo.setAdapter(adapter2);
		}

		if (griditem3 != null && griditem3.length > 0) {
			mGvThree.setVisibility(View.VISIBLE);
			mSep1.setVisibility(View.VISIBLE);
			mSep2.setVisibility(View.VISIBLE);
			for (String item : griditem3) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", item);
				items3.add(map);
			}
			SimpleAdapter adapter3 = new SimpleAdapter(getActivity(), items3,
					R.layout.grid_item, new String[] { "title" },
					new int[] { R.id.btn_grid_item });
			mGvThree.setAdapter(adapter3);
		}

		mGvOne.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
				if (null == getTag()) {
					return;
				}
				act.onDialogConfirm(getTag(), false, String.valueOf(position));
				dismiss();
			}
		});
		mGvTwo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
				if (null == getTag()) {
					return;
				}
				act.onDialogConfirm(getTag(), false, String.valueOf(arg2 + 100));
				dismiss();
			}
		});
		mGvThree.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
				if (null != getTag()) {
					act.onDialogConfirm(getTag(), false, "自定义");
					dismiss();
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