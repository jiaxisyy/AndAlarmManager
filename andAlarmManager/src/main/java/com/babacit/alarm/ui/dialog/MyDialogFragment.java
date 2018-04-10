package com.babacit.alarm.ui.dialog;

import com.babacit.alarm.R;
import com.babacit.alarm.constant.Constant;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyDialogFragment extends DialogFragment implements OnClickListener {
	private TextView mTvDlgTitle, mTvDlgContent;
	private Button mBtDlgCancel, mBtDlgConfirm;
	private String _dlgTitle, _dlgContent, _btnCancelTitle, _btnConfirmTitle;
	private boolean _oneButton, _noButton;

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
		View view = inflater.inflate(R.layout.my_dialog_fragment, container,
				false);
		_dlgTitle = getArguments().getString(Constant.DLG_TITLE);
		_dlgContent = getArguments().getString(Constant.DLG_CONTENT);
		_oneButton = getArguments().getBoolean(Constant.DLG_ONE_BUTTON);
		_noButton = getArguments().getBoolean(Constant.DLG_NO_BUTTON);
		_btnConfirmTitle = getArguments().getString(Constant.DLG_CONFIRM_TEXT);
		_btnCancelTitle = getArguments().getString(Constant.DLG_CANCEL_TEXT);
		mTvDlgTitle = (TextView) view.findViewById(R.id.tv_dlg_title);
		mTvDlgContent = (TextView) view.findViewById(R.id.tv_dlg_content);
		mTvDlgContent.setOnClickListener(this);
		mBtDlgConfirm = (Button) view.findViewById(R.id.btn_dlg_confirm);
		mBtDlgConfirm.setText(_btnConfirmTitle);
		mBtDlgCancel = (Button) view.findViewById(R.id.btn_dlg_cancel);
		mBtDlgConfirm.setOnClickListener(this);
		if (_oneButton) {
			mBtDlgCancel.setVisibility(View.GONE);
		} else {
			mBtDlgCancel.setText(_btnCancelTitle);
			mBtDlgCancel.setOnClickListener(this);
		}
		if (_noButton) {
			mBtDlgCancel.setVisibility(View.GONE);
			mBtDlgConfirm.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(_dlgTitle)) {
			mTvDlgTitle.setVisibility(View.GONE);
		} else
			mTvDlgTitle.setText(_dlgTitle);
		if (TextUtils.isEmpty(_dlgContent)) {
			mTvDlgContent.setVisibility(View.GONE);
		} else
			mTvDlgContent.setText(_dlgContent);
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
		OnMyDialogClickListener act = (OnMyDialogClickListener) getActivity();
		switch (v.getId()) {
		case R.id.btn_dlg_confirm:
			if (getTag().equals("bind")) {
				act.onDialogConfirm(getTag(), false, "bind");
			} else if (getTag().equals("record_finish")) {
				act.onDialogConfirm(getTag(), false, "重录");
			} else if (getTag().equals("delete_alarm")) {
				act.onDialogConfirm(getTag(), false,
						getArguments().getString("clock_id"));
			} else {
				act.onDialogConfirm(getTag(), false, "dismiss");
			}
			dismiss();
			break;
		case R.id.btn_dlg_cancel:
			if (getTag().equals("record_finish")) {
				act.onDialogConfirm(getTag(), false, "试听");
			}
			dismiss();
			break;
		case R.id.tv_dlg_content:
			Uri uri = Uri.parse(getArguments().getString("url"));
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			dismiss();
			break;
		default:
			break;
		}
	}
}