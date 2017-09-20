
package com.eastrobot.robotdev.utils.task;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskPool {

    private ThreadPoolExecutor threadPool;
    public static TaskPool taskPool = new TaskPool();
    public static Map<String, ITask> threadManager = new HashMap<String, ITask>();

    
    /***
     * corePoolSize： 线程池维护线程的最少数量
     * maximumPoolSize：线程池维护线程的最大数量
     * keepAliveTime： 线程池维护线程所允许的空闲时间
     * unit： 线程池维护线程所允许的空闲时间的单位
     * workQueue： 线程池所使用的缓冲队列(ArrayBlockingQueue是一个由数组支持的有界阻塞队列。此队列按 FIFO（先进先出）原则对元素进行排序。队列的头部 是在队列中存在时间最长的元素)
     * handler： 线程池对拒绝任务的处理策略
     */
    public TaskPool(){
        // 构造一个线程池
        threadPool = new ThreadPoolExecutor(10, 30, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }
    
    
    
	public void shutDown(String sessionId) {
		try {
			if (getThreadManager().containsKey(sessionId)) {
				ITask qrr = getThreadManager().get(sessionId);
				qrr.threadShutDown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    

    /**
     * 往线程池中添加一个任务，并执行
     * 
     * @param task
     *            任务
     */
    public void addTask(ITask task){
        try{
            // 产生一个任务，并将其加入到线程池
            threadPool.execute(task);
        }catch( Exception e ){
            e.printStackTrace();
        }
    }
    
    /**
     * 往线程池中添加一个任务，并执行
     * 
     * @param task
     *            任务
     */
    public void addTask(ITask task, String taskId){
        try{
        	getThreadManager().put(taskId, task);
            // 产生一个任务，并将其加入到线程池
            threadPool.execute(task);
        }catch( Exception e ){
            e.printStackTrace();
        }
    }

    public static TaskPool getInstance(){
        return taskPool;
    }
    
    public Map<String, ITask> getThreadManager(){
    	return threadManager;
    }

    
    public static void main(String[] args) {
		TaskPool.getInstance().addTask(new ITask() {
			private boolean flag = true;
			@Override
			public void run() {
				while (flag) {
					System.out.println("sdasd");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					flag = false;
				}
			}
			@Override
			public void threadShutDown() {
				
			}
		});
	}
}
