package com.celeste.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public class SynchronousController {

    @Getter
    private static final SynchronousController instance = new SynchronousController(new Timer());

    private final Timer timer;

    @Synchronized
    public void execute(final Runnable runnable) {
        runnable.run();
    }

    @Synchronized
    public <T> T submit(final Callable<T> callable) throws Exception {
        return callable.call();
    }

    @Synchronized
    public void waitAndExecute(final TimerTask timerTask, final long delay, final TimeUnit time) {
        timer.schedule(timerTask, time.toMillis(delay));
    }

    @Synchronized
    public void timer(final TimerTask timerTask, final long initialDelay, final long delay, final TimeUnit time) {
        timer.scheduleAtFixedRate(timerTask, time.toMillis(initialDelay), time.toMillis(delay));
    }

}
