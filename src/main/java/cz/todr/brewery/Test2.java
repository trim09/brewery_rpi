package cz.todr.brewery;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Slf4j
public class Test2 {
    ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Executor1");
        t.setDaemon(true);
        return t;
    });

    private void run() {
        val flux = Flux.range(1, 20)
                .delayElements(Duration.of(100, ChronoUnit.MILLIS))
                .cache(5);

        sleep(1500);
        System.out.println("consumer1 ");
        flux.subscribe(value -> {
            print(value, "Consumed1");
            //sleep(1500 );
            //print(value, "Consumed1 end");
        });



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

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void print(long value, String id) {
        System.out.printf("% 3d %10s on %s%n", value, id, Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        new Thread(() -> new Test2().run(), "MyConsumer").start();
        sleep(3000 );
    }
}
