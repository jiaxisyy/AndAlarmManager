package com.babacit.alarm.swvideo;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import io.agora.rtc.video.ViEAndroidGLES20;

/**
 * 用于视频监控截图
 * @author fxl
 */
public class SWVideoSurfaceView extends ViEAndroidGLES20 {
	
	private SWVideoSVListener mSWVideoSVListener;

	public void setSWVideoSVListener(SWVideoSVListener listener) {
		this.mSWVideoSVListener = listener;
	}
	
	public SWVideoSurfaceView(Context context) {
		super(context);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		super.onDrawFrame(gl);
		
		if (mSWVideoSVListener != null) {
			SWVideoSVListener listener = mSWVideoSVListener;
			mSWVideoSVListener = null;
			listener.onDrawFrame(gl);
		}
	}
	
	public interface SWVideoSVListener {
		
		public void onDrawFrame(GL10 gl);
		
	}
	
}