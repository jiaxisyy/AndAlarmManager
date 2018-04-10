package com.babacit.alarm.task;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.babacit.alarm.logger.Logger;

public class TaskManager {

	private static TaskManager instance = new TaskManager();

	private TaskManager() {

	}

	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}

	private HashMap<String, BaseTask> tasks = new HashMap<String, BaseTask>();
	private ExecutorService pool = Executors.newCachedThreadPool();

	public void exec(BaseTask task) {
		if (task != null) {
			synchronized (tasks) {
				if (!tasks.containsKey(task.getTaskId())) {
					tasks.put(task.getTaskId(), task);
					pool.execute(task);
//					Logger.d("请求数据中" + task.getTaskId());
				}
			}
		}
	}

	public void remove(BaseTask task) {
		if (task != null) {
			synchronized (tasks) {
				if (tasks.containsKey(task.getTaskId())) {
					tasks.remove(task.getTaskId());
//					Logger.d("移除task， taskid = " + task.getTaskId()
//							+ ", 移除完后size = " + tasks.size());
				}
			}
		}
	}

}
