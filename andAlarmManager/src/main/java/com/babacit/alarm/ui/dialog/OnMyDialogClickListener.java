package com.babacit.alarm.ui.dialog;

public interface OnMyDialogClickListener {
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message);
}