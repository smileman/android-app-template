package org.kulabukhov.android.apptemplate.api;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.JsonNode;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gkulabukhov on 16/12/14.
 */
public final class BackgroundResponseProcessor {

	// Sets the amount of time an idle thread will wait for a task before terminating
	private static final int KEEP_ALIVE_TIME = 1;

	// Sets the Time Unit to seconds
	private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

	// The initial threadpool size
	private static final int CORE_POOL_SIZE = 2;

	// The maximum threadpool size
	private static final int MAXIMUM_POOL_SIZE = 4;

	/**
	 * NOTE: This is the number of total available cores. On current versions of
	 * Android, with devices that use plug-and-play cores, this will return less
	 * than the total number of cores. The total number of cores is not
	 * available in current Android implementations.
	 */
	private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

	@NonNull
	private final BlockingQueue<Runnable> processorWorkQueue;

	@NonNull
	private final ThreadPoolExecutor processorThreadPool;

	//private Handler handler;

	@NonNull
	private static BackgroundResponseProcessor instance = new BackgroundResponseProcessor();

	/**
	 * Returns the OperationsProcessor object
	 *
	 * @return The global OperationsProcessor object
	 */
	@NonNull
	public static BackgroundResponseProcessor getInstance() {

		return instance;
	}

	private BackgroundResponseProcessor() {

		processorWorkQueue = new LinkedBlockingQueue<>();
		processorThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
				KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, processorWorkQueue);
	}

	protected void processRequestResult(AsyncResultHandler resultHandler, JsonNode unprocessedResponse,
										JsonNode rawResult) {
		ResponseProcessorRunnable processorRunnable =
				new ResponseProcessorRunnable(resultHandler, unprocessedResponse, rawResult);

		processorThreadPool.execute(processorRunnable);

	}
}
