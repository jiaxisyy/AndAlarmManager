package com.babacit.alarm.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.db.dao.HolidayDao;
import com.babacit.alarm.db.dao.HoneyDao;
import com.babacit.alarm.db.dao.UserDao;
import com.babacit.alarm.entity.UserInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.QueryUserInfoServer;
import com.babacit.alarm.server.UpdateUserInfoServer;
import com.babacit.alarm.server.UploadFileServer;
import com.babacit.alarm.ui.dialog.DateWheelDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.utils.BitmapCompressUtil;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.babacit.alarm.utils.Utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSettingActivity extends BaseActivity implements
		OnClickListener, OnMyDialogClickListener {
	private TextView mTvNickName, mTvRealName, mTvBirthday, mTvRole, mTvPhone;
	private SharedConfig config;
	/** 修改头像布局 */
	private LinearLayout mLlChangePortrait;
	private LinearLayout mLlAccountInfos;
	private static final int MSG_QUERY_USER_INFO_SUCCESS = 0;
	private static final int MSG_QUERY_USER_INFO_FAIL = 1;
	private static final int MSG_UPDATE_USER_INFO_SUCCESS = 2;
	private static final int MSG_UPDATE_USER_INFO_FAIL = 3;
	private static final int MSG_UPLOAD_IMG_SUCCESS = 4;
	private static final int MSG_UPLOAD_IMG_FAIL = 5;
	private UserInfo userInfo = new UserInfo();
	private String portraitPath;
	private ImageView mIvPortrait;
	private static final int TAKE_PHOTO_WITH_DATA = 1001;
	private static final int PICK_PHOTO_FROM_ALBUM = 1002;
	private static final int CHANGE_PHONE_NUM = 1003;
	private static final int CHANGE_NICK_NAME = 1004;
	private static final int CHANGE_REAL_NAME = 1005;
	private static final int ACCOUNT_SETTING_CHANGE_ROLE = 1006;
	private int flag = 0;

	private RequestCallBack queryUserInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(MSG_QUERY_USER_INFO_SUCCESS);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_QUERY_USER_INFO_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack updateUserInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			config.setPortraitPath(portraitPath);
			mHandler.sendEmptyMessage(MSG_UPDATE_USER_INFO_SUCCESS);
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_UPDATE_USER_INFO_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack uploadImgCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(MSG_UPLOAD_IMG_SUCCESS);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(MSG_UPLOAD_IMG_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_QUERY_USER_INFO_SUCCESS:
				userInfo = (UserInfo) msg.obj;
				int id = UserDao.getInstance()
						.queryUserByUserId(config.getUserId()).getId();
				userInfo.setId(id);
				refreshUI();
				dismissProgress();
				break;
			case MSG_QUERY_USER_INFO_FAIL:
				dismissProgress();
				ToastUtil.showToast(AccountSettingActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case MSG_UPDATE_USER_INFO_SUCCESS:
				UserDao.getInstance().updateUser(userInfo);
				dismissProgress();
				ToastUtil.showToast(AccountSettingActivity.this, "修改已生效！");
				finish();
				break;
			case MSG_UPDATE_USER_INFO_FAIL:
				ToastUtil.showToast(AccountSettingActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			case MSG_UPLOAD_IMG_SUCCESS:
				userInfo.setImgUrl((String) msg.obj);
				new UpdateUserInfoServer().start(2, userInfo,
						updateUserInfoCallBack);
				break;
			case MSG_UPLOAD_IMG_FAIL:
				ToastUtil.showToast(AccountSettingActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				dismissProgress();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_settings);
		config = new SharedConfig(this);
		bindField();
		showProgressDialog("", true);
		new QueryUserInfoServer().start(config.getUserId(),
				queryUserInfoCallBack);
	}

	private void bindField() {
		findViewById(R.id.tv_save_account_info).setOnClickListener(this);
		mTvNickName = (TextView) findViewById(R.id.tv_account_setting_nickname);
		mTvRealName = (TextView) findViewById(R.id.tv_account_setting_name);
		mTvBirthday = (TextView) findViewById(R.id.tv_account_setting_birthday);
		mTvRole = (TextView) findViewById(R.id.tv_account_setting_role);
		mTvPhone = (TextView) findViewById(R.id.tv_baby_info_telephone);

		findViewById(R.id.btn_account_setting_back).setOnClickListener(this);
		findViewById(R.id.btn_log_out).setOnClickListener(this);
		mLlChangePortrait = (LinearLayout) findViewById(R.id.ll_change_portrait);
		mIvPortrait = (ImageView) findViewById(R.id.iv_account_setting_user_portrait);
		if (!config.getPortraitPath().equals("")) {
			mIvPortrait.setImageDrawable(new BitmapDrawable(getResources(),
					config.getPortraitPath()));
			// mIvPortrait.setImageBitmap(BitmapCompressUtil.getBitmapFromFile(
			// new File(config.getPortraitPath()), 200, 200));
		}
		mIvPortrait.setScaleType(ScaleType.FIT_XY);
		mLlAccountInfos = (LinearLayout) findViewById(R.id.ll_account_setting_list);
		findViewById(R.id.rl_account_setting_portrait).setOnClickListener(this);
		findViewById(R.id.rl_account_setting_nickname).setOnClickListener(this);
		findViewById(R.id.rl_account_setting_name).setOnClickListener(this);
		findViewById(R.id.rl_account_setting_birthday).setOnClickListener(this);
		findViewById(R.id.rl_account_setting_role).setOnClickListener(this);
		findViewById(R.id.rl_account_setting_phone).setOnClickListener(this);
		findViewById(R.id.rl_account_setting_modify_pwd).setOnClickListener(
				this);
		findViewById(R.id.rl_account_setting_gesture).setOnClickListener(this);
		findViewById(R.id.btn_camera).setOnClickListener(this);
		findViewById(R.id.btn_pick_from_album).setOnClickListener(this);
		findViewById(R.id.btn_cancel_change_pic).setOnClickListener(this);
	}

	private void showOrHidePortraitSettingLayout() {
		if (mLlChangePortrait.getVisibility() == View.VISIBLE) {
			mLlChangePortrait.setVisibility(View.GONE);
			mLlAccountInfos.setAlpha(1.0f);
		} else {
			mLlChangePortrait.setVisibility(View.VISIBLE);
			mLlAccountInfos.setAlpha(0.3f);
		}
	}

	private void hidePortraitSettingLayout() {
		mLlChangePortrait.setVisibility(View.GONE);
		mLlAccountInfos.setAlpha(1.0f);
	}

	private void refreshUI() {
		mTvNickName.setText(userInfo.getNickName());
		mTvRealName.setText(userInfo.getName());
		mTvBirthday.setText(userInfo.getBirthday());
		mTvRole.setText(userInfo.getRoleInfo());
		mTvPhone.setText(userInfo.getPhoneNo());
		if (userInfo.getImgUrl() != null) {
			ImageLoader.getInstance().displayImage(userInfo.getImgUrl(),
					mIvPortrait);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_save_account_info:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			showProgressDialog("", false);
			if (flag == 1) {
				new UploadFileServer().start(getSurffix(portraitPath),
						Utils.fileToByteArray(portraitPath), uploadImgCallBack);
			} else {
				new UpdateUserInfoServer().start(2, userInfo,
						updateUserInfoCallBack);
			}
			break;
		case R.id.btn_account_setting_back:
			finish();
			break;
		case R.id.btn_log_out:
			config.clear();
			UserDao.getInstance().deteleAll();
			HoneyDao.getInstance().deteleAll();
			HolidayDao.getInstance().deteleAll();
			Intent main = new Intent(AccountSettingActivity.this,
					MainActivity.class);
			main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(main);
			finish();
			break;
		case R.id.rl_account_setting_portrait:
			showOrHidePortraitSettingLayout();
			break;
		case R.id.rl_account_setting_nickname:
			hidePortraitSettingLayout();
			Intent nick = new Intent(AccountSettingActivity.this,
					CommonSettingActivity.class);
			nick.putExtra("title", "设置昵称");
			nick.putExtra("content", mTvNickName.getText().toString());
			startActivityForResult(nick, CHANGE_NICK_NAME);
			break;
		case R.id.rl_account_setting_name:
			hidePortraitSettingLayout();
			Intent name = new Intent(AccountSettingActivity.this,
					CommonSettingActivity.class);
			name.putExtra("title", "设置姓名");
			name.putExtra("content", mTvRealName.getText().toString());
			startActivityForResult(name, CHANGE_REAL_NAME);
			break;
		case R.id.rl_account_setting_birthday:
			hidePortraitSettingLayout();
			DateWheelDialogFragment date = new DateWheelDialogFragment();
			date.show(getFragmentManager(), "account_setting_birthday");
			break;
		case R.id.rl_account_setting_role:
			hidePortraitSettingLayout();
			Intent role = new Intent(AccountSettingActivity.this,
					ChangeRoleActivity.class);
			role.putExtra("roleName", userInfo.getRoleInfo());
			role.putExtra("roleType", userInfo.getRoleType());
			startActivityForResult(role, ACCOUNT_SETTING_CHANGE_ROLE);
			break;
		case R.id.rl_account_setting_phone:
			hidePortraitSettingLayout();
			Intent phone = new Intent(AccountSettingActivity.this,
					ChangePhoneNumActivity.class);
			phone.putExtra("origin", mTvPhone.getText().toString());
			startActivityForResult(phone, CHANGE_PHONE_NUM);
			break;
		case R.id.rl_account_setting_modify_pwd:
			hidePortraitSettingLayout();
			Intent pwd = new Intent(AccountSettingActivity.this,
					ChangePasswordActivity.class);
			pwd.putExtra("user", userInfo);
			startActivity(pwd);
			break;
		case R.id.rl_account_setting_gesture:
			Intent gesture = new Intent(AccountSettingActivity.this,
					UnlockGesturePasswordActivity.class);
			gesture.putExtra("clear", true);
			startActivity(gesture);
			hidePortraitSettingLayout();
			break;
		case R.id.btn_camera:
			mLlChangePortrait.setVisibility(View.GONE);
			//在启动拍照之前最好先判断一下sdcard是否可用
	        String state = Environment.getExternalStorageState();
	        if (state.equals(Environment.MEDIA_MOUNTED)) {
	        	String portraitPath = Environment.getExternalStorageDirectory() + "/AlarmManager/portrait";
				File dir = new File(portraitPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
	            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), TAKE_PHOTO_WITH_DATA);
	        } else {
	            ToastUtil.showToast(this, "sdcard不可用");
	        }
			break;
		case R.id.btn_pick_from_album:
			mLlChangePortrait.setVisibility(View.GONE);
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
			getImage.addCategory(Intent.CATEGORY_OPENABLE);
			getImage.setType("image/jpeg");
			startActivityForResult(getImage, PICK_PHOTO_FROM_ALBUM);
			break;
		case R.id.btn_cancel_change_pic:
			mLlChangePortrait.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		hidePortraitSettingLayout();
		return super.onTouchEvent(event);
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("account_setting_birthday")) {
			((TextView) findViewById(R.id.tv_account_setting_birthday))
					.setText(message);
			userInfo.setBirthday(message.toString());
		}
	}

	private String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_PHOTO_FROM_ALBUM:
			mLlAccountInfos.setAlpha(1.0f);
			ContentResolver cr = getContentResolver();
			if (resultCode == RESULT_OK) {
				flag = 1;
				Uri uri = data.getData();
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
						&& DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
					String docId = DocumentsContract.getDocumentId(uri);
					String[] split = docId.split(":");
					String type = split[0];
					Uri contentUri = null;
					if ("image".equalsIgnoreCase(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					}
					String selection = MediaStore.Images.Media._ID + "=?";
					String[] selectionArgs = new String[] { split[1] };
					portraitPath = getDataColumn(getApplicationContext(),
							contentUri, selection, selectionArgs);
				} else {
//					String[] proj = { MediaStore.Images.Media.DATA };
//					Cursor cursor = cr.query(uri, proj, null, null, null);
//					int column_index = cursor
//							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//					cursor.moveToFirst();
//					portraitPath = cursor.getString(column_index);
					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
						if (null == cursor) {
							Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
							return;
						}
						cursor.moveToFirst();
						portraitPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
						cursor.close();
					}else {
						portraitPath = uri.getPath();
					}
				}
				Bitmap bm = BitmapCompressUtil.getBitmapFromFile(new File(
						portraitPath), 200, 200);
				mIvPortrait.setImageBitmap(bm);
				portraitPath = Constant.PORTRAIT_PATH + File.separator
						+ "portrait.jpg";
				File fileDir = new File(Constant.PORTRAIT_PATH);
				if(!fileDir.exists()){
					fileDir.mkdirs();
				}
				File f = new File(portraitPath);
				if (f.exists()) {
					f.delete();
				}
				try {
					FileOutputStream out = new FileOutputStream(f);
					bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				mIvPortrait.setScaleType(ScaleType.FIT_XY);
			}

			break;
		case TAKE_PHOTO_WITH_DATA:
			mLlAccountInfos.setAlpha(1.0f);
			if (data == null) {
				return;
			}
			Bitmap photo = data.getParcelableExtra("data");
			if (photo != null) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/AlarmManager/portrait/", "portrait.jpg");
				if (f.exists()) {
					f.delete();
				}
				try {
					FileOutputStream outStream = new FileOutputStream(f);
					photo.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
					outStream.flush();
					outStream.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				portraitPath = f.getAbsolutePath();
				// mIvPortrait.setImageBitmap(BitmapCompressUtil
				// .getBitmapFromFile(new File(portraitPath), 200, 200));
				mIvPortrait.setImageDrawable(new BitmapDrawable(getResources(),
						portraitPath));
				flag = 1;
			}
			break;
		case CHANGE_PHONE_NUM:
			if (data == null) {
				return;
			}
			if (data.getStringExtra("phone") != null) {
				mTvPhone.setText(data.getStringExtra("phone"));
				userInfo.setPhoneNo(data.getStringExtra("phone"));
				UserDao.getInstance().updateUser(userInfo);
			}
			break;
		case CHANGE_NICK_NAME:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String nickName = data.getStringExtra("common");
				mTvNickName.setText(nickName);
				userInfo.setNickName(nickName);
			}
			break;
		case CHANGE_REAL_NAME:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String realName = data.getStringExtra("common");
				mTvRealName.setText(realName);
				userInfo.setName(realName);
			}
			break;
		case ACCOUNT_SETTING_CHANGE_ROLE:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("roleName")) {
				String name = data.getStringExtra("roleName");
				userInfo.setRoleInfo(name);
				mTvRole.setText(name);
			}
			userInfo.setRoleType(data.getIntExtra("roleType", 1));
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String getSurffix(String src) {
		if (src != null && src.length() > 0) {
			return src.substring(src.lastIndexOf(".") + 1, src.length());
		} else
			return null;
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