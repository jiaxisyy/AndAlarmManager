package com.babacit.alarm.notify;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.SparseArray;

/**
 * The class <code>StatusNotifier</code>
 * 
 * @author likai
 * @version 1.0
 */
public class NotifyManager {

	private static NotifyManager instance = new NotifyManager();

	private SparseArray<List<INotifyListener>> mListenerArray = new SparseArray<List<INotifyListener>>();

	private NotifyManager() {

	}

	public synchronized static NotifyManager getInstance() {
		if (instance == null) {
			instance = new NotifyManager();
		}
		return instance;
	}

	/**
	 * 注册通知器
	 * 
	 * @param type
	 *            注册的种类
	 * @param listener
	 *            回调通知器
	 */
	public synchronized void register(int type, INotifyListener listener) {
		synchronized (mListenerArray) {
			if (type != 0 && listener != null) {
				List<INotifyListener> listeners = mListenerArray.get(type);
				if (listeners == null) {
					listeners = new ArrayList<INotifyListener>();
					mListenerArray.put(type, listeners);
				}
				if (listeners != null) {
					synchronized (listeners) {
						if (!listeners.contains(listener)) {
							listeners.add(listener);
						}
					}
				}
			}
		}
	}

	/**
	 * 移除监听器
	 * 
	 * @param type
	 *            注册的种类
	 * @param listener
	 *            回调通知器
	 */
	public synchronized void unregister(int type, INotifyListener listener) {
		synchronized (mListenerArray) {
			if (type != 0 && listener != null) {
				List<INotifyListener> listeners = mListenerArray.get(type);
				if (listeners != null) {
					synchronized (listeners) {
						Iterator<INotifyListener> iterator = listeners
								.iterator();
						while (iterator.hasNext()) {
							if (listener.equals(iterator.next())) {
								iterator.remove();
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 通知
	 * 
	 * @param type
	 * @param obj
	 */
	public synchronized void notify(int type, Object obj) {
		synchronized (mListenerArray) {
			List<INotifyListener> listeners = mListenerArray.get(type);
			if (listeners != null && listeners.size() > 0) {
				synchronized (listeners) {
					List<INotifyListener> templist = new ArrayList<INotifyListener>();
					templist.addAll(listeners);// 复制一个
												// 因为在notify里面可能会调用unregister
					int size = templist.size();
					for (int i = 0; i < size; i++) {
						INotifyListener listener = templist.get(i);
						if (listener != null) {
							listener.notify(type, obj);
						}
					}
				}
			}
		}
	}

}
