package org.wowbagger.rules;

import java.util.concurrent.CountDownLatch;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.wowbagger.rules.annotation.Concurrent;


@SuppressWarnings("deprecation")
public final class ConcurrentRule implements MethodRule {
    
    public Statement apply(Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Concurrent concurrent = frameworkMethod.getAnnotation(Concurrent.class);
                if (concurrent == null)
                    frameworkMethod.invokeExplosively(o);
                else {
                    final String name = frameworkMethod.getName();
                    final Thread[] threads = new Thread[concurrent.value()];
                    final CountDownLatch go = new CountDownLatch(1);
                    final CountDownLatch finished = new CountDownLatch(threads.length);
                    for (int i = 0; i < threads.length; i++) {
                        threads[i] = new Thread(new Runnable() {
                            
                            public void run() {
                                try {
                                    go.await();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                                try {
                                    frameworkMethod.invokeExplosively(o);
                                } catch (Throwable throwable) {
                                    if (throwable instanceof RuntimeException)
                                        throw (RuntimeException) throwable;
                                    if (throwable instanceof Error)
                                        throw (Error) throwable;
                                    RuntimeException r = new RuntimeException(throwable.getMessage(), throwable);
                                    r.setStackTrace(throwable.getStackTrace());
                                    throw r;
                                } finally {
                                    finished.countDown();
                                }
                            }
                        }, name + "-Thread-" + i);
                        threads[i].start();
                    }
                    go.countDown();
                    finished.await();
                }
            }
        };
    }
}