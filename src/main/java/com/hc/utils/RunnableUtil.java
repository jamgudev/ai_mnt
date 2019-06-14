package com.hc.utils;

/**
 * Created by GOPENEDD on 2019/6/6
 */
public abstract class RunnableUtil implements Runnable {
    public volatile boolean cancelled = false;

    public void cancel() {
        cancelled = true;
    }

}
