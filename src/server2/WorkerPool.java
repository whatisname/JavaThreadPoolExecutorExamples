package server2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import server.WorkerThread;

public class WorkerPool {
	public static void main(String[] args) throws InterruptedException {
		RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);
		MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);
		Thread monitorThread = new Thread(monitor);
		monitorThread.start();
		
		//submit work to the thread pool
		for (int i = 0; i < 10; i++) {
			executorPool.execute(new WorkerThread("cmd" + i));
		}
		
		Thread.sleep(30000);
		//shut down the pool
		executorPool.shutdown();
		//shut down the monitor thread
		Thread.sleep(5000);
		monitor.shutdown();
	}
}
