package net.bradbowie.alain;

import net.bradbowie.alain.util.LOG;

import rx.Subscriber;

/**
 * Created by bradbowie on 4/11/16.
 */
public class SimpleSubscriber<T> extends Subscriber<T> {
    private static final String TAG = LOG.tag(SimpleSubscriber.class);

    @Override
    public void onCompleted() {
        LOG.d(TAG, "Completed");
    }

    @Override
    public void onError(Throwable e) {
        LOG.e(TAG, "Err", e);
    }

    @Override
    public void onNext(T t) {
        LOG.d(TAG, "On next");
    }
}
