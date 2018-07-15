package cz.todr.brewery.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

@Controller
@Slf4j
public class SingleThreadedExecutor implements ScheduledExecutorService {

	private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
			runnable -> new Thread(runnable, "brewery-control-loop"));
	
	public static ScheduledExecutorService getExecutor() {
		return executor;
	}
	
	public <T> T executeAndAwait(Callable<T> task) {
		try {
			return executor.submit(task).get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unexpected exception", e);
			throw new IllegalStateException(e);
		}
	}

	public void executeAndAwait(Runnable task) {
		try {
			executor.submit(task).get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("Unexpected exception", e);
			throw new IllegalStateException(e);
		}
	}
	
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return executor.schedule(command, delay, unit);
	}

	public void execute(Runnable command) {
		executor.execute(command);
	}

	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		return executor.schedule(callable, delay, unit);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return executor.scheduleAtFixedRate(command, initialDelay, period, unit);
	}

	public void shutdown() {
		executor.shutdown();
	}

	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}

	public boolean isShutdown() {
		return executor.isShutdown();
	}

	public boolean isTerminated() {
		return executor.isTerminated();
	}

	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}

	public <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	public <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return executor.invokeAny(tasks, timeout, unit);
	}

}
