package com.celeste.controller;

import lombok.Getter;

import java.util.concurrent.*;

@Getter
public class AsynchronousController {

    @Getter
    public static final AsynchronousController instance = new AsynchronousController();

    private final ExecutorService executor;
    private final ScheduledExecutorService scheduled;

    private AsynchronousController() {
        executor = Executors.newCachedThreadPool();
        scheduled = Executors.newScheduledThreadPool(2);
    }

    public void execute(final Runnable runnable) {
        executor.execute(runnable);
    }

    public Future<?> submit(final Runnable runnable) {
        return executor.submit(runnable);
    }

    public <T> Future<T> submit(final Runnable runnable, final T result) {
        return executor.submit(runnable, result);
    }

    public <T> Future<T> submit(final Callable<T> callable) {
        return executor.submit(callable);
    }

    public ScheduledFuture<?> waitAndExecute(final Runnable runnable, final long delay, final TimeUnit time) {
        return scheduled.schedule(runnable, delay, time);
    }

    public <T> ScheduledFuture<T> waitAndExecute(final Callable<T> callable, final long delay, final TimeUnit time) {
        return scheduled.schedule(callable, delay, time);
    }

    public ScheduledFuture<?> timer(final Runnable runnable, final long initialDelay, final long delay, final TimeUnit time) {
        return scheduled.scheduleWithFixedDelay(runnable, initialDelay, delay, time);
    }

    public void shutdown() {
        executor.shutdown();
        scheduled.shutdown();
    }

}
