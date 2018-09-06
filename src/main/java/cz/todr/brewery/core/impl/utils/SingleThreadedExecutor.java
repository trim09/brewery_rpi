package cz.todr.brewery.core.impl.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class SingleThreadedExecutor {
	private static final String CORE_THREAD_NAME = "brewery-core-thread";

	private static final ScheduledExecutorService executor =
			Executors.newSingleThreadScheduledExecutor(runnable -> new Thread(runnable, CORE_THREAD_NAME));

	public static <T> T executeAndAwait(Callable<T> task) {
		try {
			if (isAlreadyOnCoreThread()) {
				return task.call();
			} else {
				return executor.submit(task).get();
			}
		} catch (Exception e) {
			log.error("Unexpected exception", e);
			throw new IllegalStateException(e);
		}
	}

	public static void executeAndAwait(Runnable task) {
		executeAndAwait(() -> {
			task.run();
			return null;
		});
	}

	private static boolean isAlreadyOnCoreThread() {
		return CORE_THREAD_NAME.equals(Thread.currentThread().getName());
	}
}
