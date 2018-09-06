package cz.todr.brewery;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Slf4j
public class Test {
    ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Executor1");
        t.setDaemon(true);
        return t;
    });

    ThreadPoolExecutor executor2 = new ThreadPoolExecutor(1, 1 , 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(10)) {
        ThreadPoolExecutor internal = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                (r -> {
                    Thread t = new Thread(r, "Executor2");
                    t.setDaemon(true);
                    return t;
                }));

        @Override
        public void shutdown() {
            internal.shutdown();
        }

        @Override
        public List<Runnable> shutdownNow() {
            return internal.shutdownNow();
        }

        @Override
        public boolean isShutdown() {
            return internal.isShutdown();
        }

        @Override
        public boolean isTerminating() {
            return internal.isTerminating();
        }

        @Override
        public boolean isTerminated() {
            return internal.isTerminated();
        }

        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return internal.awaitTermination(timeout, unit);
        }

        @Override
        public void setThreadFactory(ThreadFactory threadFactory) {
            internal.setThreadFactory(threadFactory);
        }

        @Override
        public ThreadFactory getThreadFactory() {
            return internal.getThreadFactory();
        }

        @Override
        public void setRejectedExecutionHandler(RejectedExecutionHandler handler) {
            internal.setRejectedExecutionHandler(handler);
        }

        @Override
        public RejectedExecutionHandler getRejectedExecutionHandler() {
            return internal.getRejectedExecutionHandler();
        }

        @Override
        public void setCorePoolSize(int corePoolSize) {
            internal.setCorePoolSize(corePoolSize);
        }

        @Override
        public int getCorePoolSize() {
            return internal.getCorePoolSize();
        }

        @Override
        public boolean prestartCoreThread() {
            return internal.prestartCoreThread();
        }

        @Override
        public int prestartAllCoreThreads() {
            return internal.prestartAllCoreThreads();
        }

        @Override
        public boolean allowsCoreThreadTimeOut() {
            return internal.allowsCoreThreadTimeOut();
        }

        @Override
        public void allowCoreThreadTimeOut(boolean value) {
            internal.allowCoreThreadTimeOut(value);
        }

        @Override
        public void setMaximumPoolSize(int maximumPoolSize) {
            internal.setMaximumPoolSize(maximumPoolSize);
        }

        @Override
        public int getMaximumPoolSize() {
            return internal.getMaximumPoolSize();
        }

        @Override
        public void setKeepAliveTime(long time, TimeUnit unit) {
            internal.setKeepAliveTime(time, unit);
        }

        @Override
        public long getKeepAliveTime(TimeUnit unit) {
            return internal.getKeepAliveTime(unit);
        }

        @Override
        public BlockingQueue<Runnable> getQueue() {
            return internal.getQueue();
        }

        @Override
        public boolean remove(Runnable task) {
            return internal.remove(task);
        }

        @Override
        public void purge() {
            internal.purge();
        }

        @Override
        public int getPoolSize() {
            return internal.getPoolSize();
        }

        @Override
        public int getActiveCount() {
            return internal.getActiveCount();
        }

        @Override
        public int getLargestPoolSize() {
            return internal.getLargestPoolSize();
        }

        @Override
        public long getTaskCount() {
            return internal.getTaskCount();
        }

        @Override
        public long getCompletedTaskCount() {
            return internal.getCompletedTaskCount();
        }

        @Override
        public String toString() {
            return internal.toString();
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            System.out.println("submit size:" + internal.getQueue().size() + "    " + Thread.currentThread().getName());
            return internal.submit(task);
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            System.out.println("submit1 XXXXXXXXXXXXXXXXXXXXX");
            return internal.submit(task, result);
        }

        @Override
        public Future<?> submit(Runnable task) {
            System.out.println("submit3 XXXXXXXXXXXXXXXXXXX");
            return internal.submit(task);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            System.out.println("invokeall XXXXXXXXXXXXXXXXXXX");
            return internal.invokeAll(tasks);
        }

        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
            System.out.println("invokeall XXXXXXXXXXXXXXXXXXX");
            return internal.invokeAll(tasks, timeout, unit);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            System.out.println("invokeall XXXXXXXXXXXXXXXXXXX");
            return internal.invokeAny(tasks);
        }

        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            System.out.println("invokeall XXXXXXXXXXXXXXXXXXX");
            return internal.invokeAny(tasks, timeout, unit);
        }

        @Override
        public void execute(Runnable command) {
            System.out.println("execute XXXXXXXXXXXXXXXXXXXXX");
            internal.execute(command);
        }
    };

    static class EventSource {
        List<Consumer<Long>> cbs = new CopyOnWriteArrayList<>();

        EventSource() {
            Thread thread = new Thread(() -> {
                long state = 0;
                while(!Thread.currentThread().isInterrupted()) {
                    state++;
                    publish(state);
                    sleep(300);
                    //sleep(1);
                }
            }, "MySource");
            thread.setDaemon(true);
            thread.start();
        }

        void publish(long value) {
            print(value, "Generated");
            cbs.forEach(cb -> cb.accept(value));
        }

        void registerCB(Consumer<Long> cb) {
            System.out.println("registered CBF " + Thread.currentThread().getName());
            cbs.add(cb);
        }
    }

    private EventSource source = new EventSource();

    private Flux<Long> registerFlux() {
        return Flux.<Long>push(sink -> {
            source.registerCB(value -> sink.next(value));
            sink.onCancel(() -> System.out.println("XXXXXXXXXXXXXXXXX cancel"+ " " + Thread.currentThread().getName()));
            sink.onDispose(() -> System.out.println("XXXXXXXXXXXXXXXXX dispose"+ " " + Thread.currentThread().getName()));
            sink.onRequest(d -> System.out.println("XXXXXXXXXXXXXXXXX request " + d + " " + Thread.currentThread().getName()));
        }, FluxSink.OverflowStrategy.BUFFER)
                .cache(10)
                .repeat(5)
        //return Flux.<Long>create(sink -> source.registerCB(sink::next), FluxSink.OverflowStrategy.DROP)
//                .onBackpressureBuffer(1)
                //.onBackpressureDrop(value -> {System.out.println("XXXXXXXXXXX dropped" + value);})
//                .onBackpressureDrop()
                //.subscribeOn(Schedulers.fromExecutorService(executor))
                //.publishOn(Schedulers.fromExecutorService(executor2), 1)
                //.subscribeOn(Schedulers.fromExecutorService(executor))
                ;
    }

    private void run() {
        val flux = registerFlux();


        System.out.println("consumer1 ");
        flux.subscribe(value -> {
            print(value, "Consumed1");
            //sleep(1500 );
            //print(value, "Consumed1 end");
        });

        sleep(1500);

        System.out.println("consumer2 ");
        flux.subscribe(value -> {
            print(value, "Consumed2");
            //sleep(1500 );
            //print(value, "Consumed1 end");
        });
//        System.out.println("consumer1 ");
//        flux.subscribe(num -> {
//            print(num, "Consumed1 start");
//            sleep(1500 );
//            print(num, "Consumed1 end");
//        });
//        System.out.println("consumer2 ");
//        flux.subscribe(num -> {
//            print(num, "Consumed2 start");
//            sleep(1500 );
//            print(num, "Consumed2 end");
//        });
    }

    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void print(Long value, String id) {
        System.out.printf("% 3d %10s on %s%n", value, id, Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        new Thread(() -> new Test().run(), "MyConsumer").start();
        sleep(3000 );
    }
}
