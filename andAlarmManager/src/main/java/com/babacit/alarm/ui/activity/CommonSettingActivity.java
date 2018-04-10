package com.babacit.alarm.ui.activity;

import com.babacit.alarm.R;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class CommonSettingActivity extends BaseActivity implements
		OnClickListener {
	private TextView mTvCommonTitle;
	private EditText mEtCommon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_setting);
		bindField();
		initData();
	}

	private void bindField() {
		mTvCommonTitle = (TextView) findViewById(R.id.tv_common_title);
		mEtCommon = (EditText) findViewById(R.id.et_common);
		findViewById(R.id.btn_common_setting_back).setOnClickListener(this);
		findViewById(R.id.tv_common_setting_finish).setOnClickListener(this);
	}

	private void initData() {
		String title = getIntent().getStringExtra("title");
		String content = getIntent().getStringExtra("content");
		if (title.equals("设置备注")) {
			InputFilter[] filters = new InputFilter[] { new InputFilter.LengthFilter(
					15) };
			mEtCommon.setFilters(filters);
		}
		mTvCommonTitle.setText(title);
		if (content != null && !content.equals("无")) {
			mEtCommon.setText(content);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_common_setting_back:
			finish();
			break;
		case R.id.tv_common_setting_finish:
			if (TextUtils.isEmpty(mEtCommon.getText())) {
				ToastUtil.showToast(getApplicationContext(), "内容不能为空！");
				return;
			}
			Intent data = new Intent();
			data.putExtra("common", mEtCommon.getText().toString().trim());
			setResult(0, data);
			finish();
			break;
		default:
			break;
		}
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
