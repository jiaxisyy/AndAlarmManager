package com.babacit.alarm.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import com.babacit.alarm.R;
import com.babacit.alarm.config.SharedConfig;
import com.babacit.alarm.constant.Constant;
import com.babacit.alarm.db.dao.HoneyDao;
import com.babacit.alarm.entity.HoneyInfo;
import com.babacit.alarm.err.ErrUtils;
import com.babacit.alarm.interfaces.RequestCallBack;
import com.babacit.alarm.server.UpdateHoneyInfosServer;
import com.babacit.alarm.server.UploadFileServer;
import com.babacit.alarm.ui.dialog.CircleWheelDialogFragment2;
import com.babacit.alarm.ui.dialog.DateWheelDialogFragment;
import com.babacit.alarm.ui.dialog.OnMyDialogClickListener;
import com.babacit.alarm.update2.PermissionUtils;
import com.babacit.alarm.utils.BitmapCompressUtil;
import com.babacit.alarm.utils.NetworkUtils;
import com.babacit.alarm.utils.ToastUtil;
import com.babacit.alarm.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class BabyInfoActivity extends BaseActivity implements OnClickListener,
		OnMyDialogClickListener {
	private static final int RESULT_PICK = 9009;
	private TextView mTvNickName, mTvRealName, mTvGender, mTvBirthday,
			mTvSchool, mTvGrade, mTvRole, mTvInterest;
	private ImageView mIvPortrait;
	private HoneyInfo honeyInfo;
	/** 修改头像布局 */
	private LinearLayout mLlChangePortrait;
	private LinearLayout mLlBabyInfos;
	private String portraitPath;
	private SharedConfig config;
	private static final int TAKE_PHOTO_WITH_DATA = 1001;
	private static final int PICK_PHOTO_FROM_ALBUM = 1002;
	private static final int CHANGE_NICK_NAME = 1003;
	private static final int CHANGE_REAL_NAME = 1004;
	private static final int CHANGE_GENDER = 1005;
	private static final int CHANGE_SCHOOL = 1006;
	private static final int CHANGE_GRADE = 1007;
	private static final int CHANGE_ROLE = 1008;
	private static final int CHANGE_HOBBY = 1009;

	private static final int UPLOAD_IMG_SUCCESS = 1010;
	private static final int UPLOAD_IMG_FAIL = 1011;
	private int flag = 0;

	private RequestCallBack updateHoneyInfoCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(0);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(1);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private RequestCallBack uploadFileCallBack = new RequestCallBack() {
		@Override
		public void onSuccess(Object obj) {
			Message msg = mHandler.obtainMessage(UPLOAD_IMG_SUCCESS);
			msg.obj = obj;
			msg.sendToTarget();
		}

		@Override
		public void onFail(Object object, int errCode) {
			Message msg = mHandler.obtainMessage(UPLOAD_IMG_FAIL);
			msg.arg1 = errCode;
			msg.sendToTarget();
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				HoneyDao.getInstance().update(honeyInfo);
				setResult(MainActivity.RESULT_CODE_FROM_UPDATE_HONEY_INFO);
				config.setHoneyPortraitPath(portraitPath);
				dismissProgress();
				Intent update = new Intent("update_honey");
				sendBroadcast(update);
				ToastUtil.showToast(BabyInfoActivity.this, "修改信息成功！");
				finish();

				break;
			case 1:
				System.out.println("===============1======");
				dismissProgress();
				ToastUtil.showToast(BabyInfoActivity.this,
						ErrUtils.getErrorReasonStr(msg.arg1));
				break;
			case UPLOAD_IMG_SUCCESS:
				System.out.println("===============UPLOAD_IMG_SUCCESS======");
				honeyInfo.setImgUrl((String) msg.obj);
				new UpdateHoneyInfosServer().start(config.getUserId(),
						honeyInfo, updateHoneyInfoCallBack);
				break;
			case UPLOAD_IMG_FAIL:
				System.out.println("===============失败======");
				dismissProgress();
				ToastUtil.showToast(BabyInfoActivity.this,
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
		setContentView(R.layout.activity_baby_info);
		config = new SharedConfig(this);
		honeyInfo = HoneyDao.getInstance().queryHoneyByCode(
				config.getAlarmCode());
		if (honeyInfo == null) {
			honeyInfo = new HoneyInfo();
		}
		bindField();
		if (honeyInfo != null) {
			if (honeyInfo.getImgUrl() != null) {
				ImageLoader.getInstance().displayImage(honeyInfo.getImgUrl(),
						mIvPortrait);
			}
			refreshUI();
		}
	}

	private void bindField() {
		findViewById(R.id.tv_save_baby_info).setOnClickListener(this);
		mTvNickName = (TextView) findViewById(R.id.tv_baby_info_nickname);
		mTvRealName = (TextView) findViewById(R.id.tv_baby_info_name);
		mTvGender = (TextView) findViewById(R.id.tv_baby_info_gender);
		mTvBirthday = (TextView) findViewById(R.id.tv_baby_info_birthday);
		mTvSchool = (TextView) findViewById(R.id.tv_baby_info_school);
		mTvGrade = (TextView) findViewById(R.id.tv_baby_info_grade);
		mTvRole = (TextView) findViewById(R.id.tv_baby_info_role);
		mTvInterest = (TextView) findViewById(R.id.tv_baby_info_hobby);

		findViewById(R.id.btn_baby_info_back).setOnClickListener(this);
		mIvPortrait = (ImageView) findViewById(R.id.iv_baby_info_portrait);
		mIvPortrait.setScaleType(ScaleType.FIT_XY);
		mLlChangePortrait = (LinearLayout) findViewById(R.id.ll_change_portrait);
		mLlBabyInfos = (LinearLayout) findViewById(R.id.ll_baby_info_list);
		findViewById(R.id.btn_camera).setOnClickListener(this);
		findViewById(R.id.btn_pick_from_album).setOnClickListener(this);
		findViewById(R.id.btn_cancel_change_pic).setOnClickListener(this);

		findViewById(R.id.rl_baby_info_portrait).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_nickname).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_name).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_gender).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_birthday).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_school).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_grade).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_role).setOnClickListener(this);
		findViewById(R.id.rl_baby_info_hobby).setOnClickListener(this);
	}

	private void showOrHidePortraitSettingLayout() {
		if (mLlChangePortrait.getVisibility() == View.VISIBLE) {
			mLlChangePortrait.setVisibility(View.GONE);
			mLlBabyInfos.setAlpha(1.0f);
		} else {
			mLlChangePortrait.setVisibility(View.VISIBLE);
			mLlBabyInfos.setAlpha(0.3f);
		}
	}

	private void refreshUI() {
		mTvNickName.setText(honeyInfo.getNickName());
		mTvRealName.setText(honeyInfo.getName());
		if (honeyInfo.getGender() == 0) {
			mTvGender.setText("男");
		} else {
			mTvGender.setText("女");
		}
		mTvBirthday.setText(honeyInfo.getBirthday());
		mTvSchool.setText(honeyInfo.getSchoolName());
		mTvGrade.setText(honeyInfo.getGradeName());
		mTvRole.setText(honeyInfo.getRoleName());
		mTvInterest.setText(honeyInfo.getInterest());
	}

	private void hidePortraitSettingLayout() {
		mLlChangePortrait.setVisibility(View.GONE);
		mLlBabyInfos.setAlpha(1.0f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_save_baby_info:
			if (!NetworkUtils.isNetWorkOk(getApplicationContext())) {
				ToastUtil.showToast(getApplicationContext(), "请检查您的网络！");
				return;
			}
			showProgressDialog("", false);
			if (flag == 1) {
				new UploadFileServer()
						.start(getSurffix(portraitPath),
								Utils.fileToByteArray(portraitPath),
								uploadFileCallBack);
				System.out.println("测试执行1===portraitPath="+portraitPath);
				System.out.println("测试执行1");
				System.out.println("测试执行1===length=="+Utils.fileToByteArray(portraitPath)==null);
			} else {
				new UpdateHoneyInfosServer().start(config.getUserId(),
						honeyInfo, updateHoneyInfoCallBack);
				System.out.println("测试执行2");
			}
			break;
		case R.id.btn_baby_info_back:
			finish();
			break;
		case R.id.rl_baby_info_portrait:
			showOrHidePortraitSettingLayout();
			break;
		case R.id.rl_baby_info_nickname:
			hidePortraitSettingLayout();
			Intent nick = new Intent(BabyInfoActivity.this,
					CommonSettingActivity.class);
			nick.putExtra("title", "设置昵称");
			nick.putExtra("content", mTvNickName.getText().toString());
			startActivityForResult(nick, CHANGE_NICK_NAME);
			break;
		case R.id.rl_baby_info_name:
			hidePortraitSettingLayout();
			Intent realName = new Intent(BabyInfoActivity.this,
					CommonSettingActivity.class);
			realName.putExtra("title", "设置姓名");
			realName.putExtra("content", mTvRealName.getText().toString());
			startActivityForResult(realName, CHANGE_REAL_NAME);
			break;
		case R.id.rl_baby_info_gender:
			hidePortraitSettingLayout();
			CircleWheelDialogFragment2 gender = new CircleWheelDialogFragment2();
			Bundle args = new Bundle();
			args.putStringArray("wheel",
					getResources().getStringArray(R.array.gender));
			gender.setArguments(args);
			gender.show(getFragmentManager(), "gender");
			break;
		case R.id.rl_baby_info_birthday:
			hidePortraitSettingLayout();
			DateWheelDialogFragment date = new DateWheelDialogFragment();
			date.show(getFragmentManager(), "baby_info_birthday");
			break;
		case R.id.rl_baby_info_school:
			hidePortraitSettingLayout();
			Intent school = new Intent(BabyInfoActivity.this,
					CommonSettingActivity.class);
			school.putExtra("title", "设置学校名称");
			school.putExtra("content", mTvSchool.getText().toString());
			startActivityForResult(school, CHANGE_SCHOOL);
			break;
		case R.id.rl_baby_info_grade:
			hidePortraitSettingLayout();
			Intent grade = new Intent(BabyInfoActivity.this,
					CommonSettingActivity.class);
			grade.putExtra("title", "设置年级信息");
			grade.putExtra("content", mTvGrade.getText().toString());
			startActivityForResult(grade, CHANGE_GRADE);
			break;
		case R.id.rl_baby_info_role:
			hidePortraitSettingLayout();
			// Intent role = new Intent(BabyInfoActivity.this,
			// ChangeRoleActivity.class);
			// role.putExtra("roleName", honeyInfo.getRoleName());
			// startActivityForResult(role, CHANGE_ROLE);
			Intent role = new Intent(BabyInfoActivity.this,
					CommonSettingActivity.class);
			role.putExtra("title", "设置角色名称");
			role.putExtra("content", mTvRole.getText().toString());
			startActivityForResult(role, CHANGE_ROLE);
			break;
		case R.id.rl_baby_info_hobby:
			hidePortraitSettingLayout();
			Intent hobby = new Intent(BabyInfoActivity.this,
					CommonSettingActivity.class);
			hobby.putExtra("title", "设置兴趣爱好");
			hobby.putExtra("content", mTvInterest.getText().toString());
			startActivityForResult(hobby, CHANGE_HOBBY);
			break;
		case R.id.btn_camera:
			mLlChangePortrait.setVisibility(View.GONE);
			Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			portraitPath = Environment.getExternalStorageDirectory()
					+ "/AlarmManager/portrait";
			File dir = new File(portraitPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			startActivityForResult(camera, TAKE_PHOTO_WITH_DATA);
			break;
		case R.id.btn_pick_from_album:
			mLlChangePortrait.setVisibility(View.GONE);
			requestPermissions();
			break;
		case R.id.btn_cancel_change_pic:
			mLlChangePortrait.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void startPickPhoto() {
		Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
//		getImage.setType("image/jpeg");
		getImage.setType("image/*");
		startActivityForResult(getImage, PICK_PHOTO_FROM_ALBUM);
	}

	private String getSurffix(String src) {
		if (src != null && src.length() > 0) {
			return src.substring(src.lastIndexOf(".") + 1, src.length());
		} else
			return null;
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
	/**内置存储*/
	private static String ROOT_PATH_TEST = Environment.getExternalStorageDirectory().getPath() + "/AlarmManager";
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_PHOTO_FROM_ALBUM:
			mLlBabyInfos.setAlpha(1.0f);
			ContentResolver cr = getContentResolver();
			if (resultCode == RESULT_OK) {
				flag = 1;
				Uri uri = data.getData();
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
								getApplicationContext(), uri)) {
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
					System.out.println("================="+portraitPath);
				} else {
					System.out.println("针对一些特殊机型比如红米note系列的手机获取到的图片路径为null");
					// 针对一些特殊机型比如红米note系列的手机获取到的图片路径为null
					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
						if (null == cursor) {
							Toast.makeText(this, "图片没找到", Toast.LENGTH_SHORT).show();
							return;
						}
						cursor.moveToFirst();
						portraitPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
						System.out.println("针对一些特殊机型比如红米note系列的手机获取到的图片路径为null的portraitPath==="+portraitPath);
						cursor.close();
					}else {
						portraitPath = uri.getPath();
					}
//					String[] proj = { MediaStore.Images.Media.DATA };
//					Cursor cursor = cr.query(uri, proj, null, null, null);
//					int column_index = cursor
//							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//					cursor.moveToFirst();
//					portraitPath = cursor.getString(column_index);
				}
				Bitmap bm = BitmapCompressUtil.getBitmapFromFile(new File(
						portraitPath), 200, 200);
				mIvPortrait.setImageBitmap(bm);
				portraitPath = Constant.PORTRAIT_PATH + File.separator
						+ "honey_portrait.jpg";
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
			mLlBabyInfos.setAlpha(1.0f);
			if (data == null) {
				return;
			}
			Bitmap photo = data.getParcelableExtra("data");
			if (photo != null) {
				File f = new File(Environment.getExternalStorageDirectory()
						+ "/AlarmManager/portrait/", "honey_portrait.jpg");
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
				mIvPortrait.setScaleType(ScaleType.FIT_XY);
				flag = 1;
			}
			break;
		case CHANGE_NICK_NAME:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String nickName = data.getStringExtra("common");
				mTvNickName.setText(nickName);
				honeyInfo.setNickName(nickName);
			}
			break;
		case CHANGE_REAL_NAME:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String name = data.getStringExtra("common");
				mTvRealName.setText(name);
				honeyInfo.setName(name);
			}
			break;
		case CHANGE_GRADE:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String name = data.getStringExtra("common");
				mTvGrade.setText(name);
				honeyInfo.setGradeName(name);
			}
			break;
		case CHANGE_SCHOOL:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String name = data.getStringExtra("common");
				mTvSchool.setText(name);
				honeyInfo.setSchoolName(name);
			}
			break;
		case CHANGE_HOBBY:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String name = data.getStringExtra("common");
				mTvInterest.setText(name);
				honeyInfo.setInterest(name);
			}
			break;
		case CHANGE_ROLE:
			if (data == null) {
				return;
			}
			if (null != data.getStringExtra("common")) {
				String name = data.getStringExtra("common");
				mTvRole.setText(name);
				honeyInfo.setRoleName(name);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private void displayImage(String Path){
		Bitmap bm= BitmapFactory.decodeFile(Path);
		mIvPortrait.setImageBitmap(bm);
	}
	private String getImagePath(Uri uri,String selection){
		String Path=null;
		Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
		if(cursor!=null){
			if(cursor.moveToFirst()){
				Path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			}
			cursor.close();
		}
		return Path;
	}
	private void handleImageBeforeKitKat(Intent data){
		String imagePath=null;
		Uri uri=data.getData();
		imagePath=getImagePath(uri,null);
		displayImage(imagePath);
	}
	private void handleImageOnKitKat(Intent data){
		String imagePath=null;
		Uri uri=data.getData();
		if(DocumentsContract.isDocumentUri(this,uri)){
			String docId=DocumentsContract.getDocumentId(uri);
			if("com.android.providers.media.documents".equals(uri.getAuthority())){
				String id=docId.split(":")[1];
				String selection=MediaStore.Images.Media._ID+"="+id;
				imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
			}else if("com.android.provider.downloads.documents".equals(uri.getAuthority())){
				Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
				imagePath=getImagePath(contentUri,null);
			}
		}else if("content".equalsIgnoreCase(uri.getScheme())){
			imagePath=getImagePath(uri,null);
		}else if("file".equalsIgnoreCase(uri.getScheme())){
			imagePath=uri.getPath();
		}
		displayImage(imagePath);
	}
	/**
	 * 图片地址权限申请
	 */
	private void requestPermissions() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//			Toast.makeText(this,"test1",Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS}, RESULT_PICK);
//				Toast.makeText(this,"requestPermissions",Toast.LENGTH_SHORT).show();
            }
        }else {
//			Toast.makeText(this,"test2",Toast.LENGTH_SHORT).show();
			startPickPhoto();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

		if(requestCode==RESULT_PICK){
			// 如果权限被拒绝，grantResults 为空
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//选择图片
//				Toast.makeText(this,"onRequestPermissionsResult",Toast.LENGTH_SHORT).show();
				startPickPhoto();
			} else {
				Toast.makeText(this, "没有权限,请手动打开权限", Toast.LENGTH_SHORT).show();
			}
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		hidePortraitSettingLayout();
		return super.onTouchEvent(event);
	}

	@Override
	public void onDialogConfirm(String tag, boolean cancelled,
			CharSequence message) {
		if (tag.equals("baby_info_birthday")) {
			((TextView) findViewById(R.id.tv_baby_info_birthday))
					.setText(message);
			honeyInfo.setBirthday(message.toString());
		} else if (tag.equals("gender")) {
			((TextView) findViewById(R.id.tv_baby_info_gender))
					.setText(message);
			if (message.equals("男")) {
				honeyInfo.setGender(0);
			} else
				honeyInfo.setGender(1);
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
