package com.babacit.alarm.ui.activity;

import com.babacit.alarm.R;
import com.babacit.alarm.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ChangeRoleActivity extends BaseActivity implements OnClickListener {
	private RadioGroup mRg1, mRg2;
	private RadioButton mRbPa, mRbMa, mRbGrandPa, mRbGrandMa, mRbOther;
	private int role;
	private EditText mEtRole;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_role);
		bindField();
		initData();
	}

	private void initData() {
		String roleName = getIntent().getStringExtra("roleName");
		if (roleName != null) {
			if (roleName.equals("爸爸")) {
				role = 1;
				setCheckedStatus(true, false, false, false, false);
			} else if (roleName.equals("妈妈")) {
				role = 2;
				setCheckedStatus(false, true, false, false, false);
			} else if (roleName.equals("爷爷")) {
				role = 3;
				setCheckedStatus(false, false, true, false, false);
			} else if (roleName.equals("奶奶")) {
				role = 4;
				setCheckedStatus(false, false, false, true, false);
			} else {
				role = 5;
				setCheckedStatus(false, false, false, false, true);
				mEtRole.setVisibility(View.VISIBLE);
				mEtRole.setText(roleName);
			}
		}
	}

	private void setCheckedStatus(boolean c1, boolean c2, boolean c3,
			boolean c4, boolean c5) {
		mRbPa.setChecked(c1);
		mRbMa.setChecked(c2);
		mRbGrandPa.setChecked(c3);
		mRbGrandMa.setChecked(c4);
		mRbOther.setChecked(c5);
	}

	private void bindField() {
		findViewById(R.id.btn_confirm).setOnClickListener(this);
		findViewById(R.id.btn_change_role_back).setOnClickListener(this);
		mEtRole = (EditText) findViewById(R.id.et_role);

		mRg1 = (RadioGroup) findViewById(R.id.rg_1);
		mRg2 = (RadioGroup) findViewById(R.id.rg_2);
		mRbPa = (RadioButton) findViewById(R.id.rb_role_father);
		mRbMa = (RadioButton) findViewById(R.id.rb_role_mother);
		mRbGrandPa = (RadioButton) findViewById(R.id.rb_role_grandpa);
		mRbGrandMa = (RadioButton) findViewById(R.id.rb_role_grandma);
		mRbOther = (RadioButton) findViewById(R.id.rb_role_other);
		mRbPa.setOnClickListener(new RadioSelected("1"));
		mRbMa.setOnClickListener(new RadioSelected("2"));
		mRbGrandPa.setOnClickListener(new RadioSelected("3"));
		mRbGrandMa.setOnClickListener(new RadioSelected("4"));
		mRbOther.setOnClickListener(new RadioSelected("5"));
	}

	class RadioSelected implements OnClickListener {
		final public String btnId;

		public RadioSelected(String str) {
			btnId = str;
		}

		@Override
		public void onClick(View v) {
			if (btnId.equals("1") || btnId.equals("2") || btnId.equals("3")) {
				mRg2.clearCheck();
				if (btnId.equals("1")) {
					mRbPa.setChecked(true);
					role = 1;
				} else if (btnId.equals("2")) {
					mRbMa.setChecked(true);
					role = 2;
				} else {
					mRbGrandPa.setChecked(true);
					role = 3;
				}
				mEtRole.setVisibility(View.GONE);
			} else if (btnId.equals("4") || btnId.equals("5")) {
				mRg1.clearCheck();
				if (btnId.equals("4")) {
					mRbGrandMa.setChecked(true);
					role = 4;
					mEtRole.setVisibility(View.GONE);
				} else {
					mRbOther.setChecked(true);
					mEtRole.setVisibility(View.VISIBLE);
					role = 5;
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_role_back:
			finish();
			break;
		case R.id.btn_confirm:
			if (role == 5 && TextUtils.isEmpty(mEtRole.getText().toString())) {
				ToastUtil.showToast(ChangeRoleActivity.this, "请输入角色名称");
				return;
			}
			Intent data = new Intent();
			data.putExtra("roleType", role);
			switch (role) {
			case 1:
				data.putExtra("roleName", mRbPa.getText().toString());
				break;
			case 2:
				data.putExtra("roleName", mRbMa.getText().toString());
				break;
			case 3:
				data.putExtra("roleName", mRbGrandPa.getText().toString());
				break;
			case 4:
				data.putExtra("roleName", mRbGrandMa.getText().toString());
				break;
			case 5:
				data.putExtra("roleName", mEtRole.getText().toString());
				break;
			default:
				break;
			}
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