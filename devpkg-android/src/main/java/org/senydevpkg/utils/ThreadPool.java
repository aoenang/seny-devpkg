package org.senydevpkg.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ━━━━ Code is far away from ━━━━━━
 * 　　  () 　　　  ()
 * 　　  ( ) 　　　( )
 * 　　  ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━ bug with the XYY protecting━━━
 * <p/>线程池管理类。
 * Created by Seny on 2016/2/21.
 */
public class ThreadPool {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        //线程安全的递加操作
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread #" + mCount.getAndIncrement());
        }
    };

    /**
     * 超出线程池容量后的的排队队列,超出队列容量后将抛出异常
     */
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<>(128);
    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static ThreadPoolExecutor THREAD_POOL_EXECUTOR;


    /**
     * 执行任务，当线程池处于关闭，将会创建新的线程池
     */
    public synchronized static void execute(Runnable run) {
        if (run == null) {
            return;
        }
        if (THREAD_POOL_EXECUTOR == null || THREAD_POOL_EXECUTOR.isShutdown()) {
            // 参数说明
            // 当线程池中的线程小于mCorePoolSize，直接创建新的线程加入线程池执行任务
            // 当线程池中的线程数目等于mCorePoolSize，将会把任务放入任务队列sPoolWorkQueue中
            // 当sPoolWorkQueue中的任务放满了，将会创建新的线程去执行，
            // 但是当总线程数大于mMaximumPoolSize时，将会抛出异常，交给RejectedExecutionHandler处理
            // mKeepAliveTime是线程执行完任务后，且队列中没有可以执行的任务，存活的时间，后面的参数是时间单位
            // ThreadFactory是每次创建新的线程工厂
            THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                    TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);
            System.out.println("---------");
        }
        THREAD_POOL_EXECUTOR.execute(run);
    }

    /**
     * 取消线程池中某个还未执行的任务
     */
    public synchronized static void cancel(Runnable run) {
        if (THREAD_POOL_EXECUTOR != null && (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
            THREAD_POOL_EXECUTOR.remove(run);
        }
    }

    /**
     * 线程池中是否包含某个任务
     */
    public synchronized static boolean contains(Runnable run) {
        if (THREAD_POOL_EXECUTOR != null && (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
            return THREAD_POOL_EXECUTOR.getQueue().contains(run);
        } else {
            return false;
        }
    }

    /**
     * 立刻关闭线程池，停止所有任务，包括等待的任务。
     */
    public synchronized static void stop() {
        if (THREAD_POOL_EXECUTOR != null && (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
            THREAD_POOL_EXECUTOR.shutdownNow();
        }
    }

    /**
     * 关闭线程池，不再接受新的任务。但已经加入的任务都将会被执行完毕才关闭
     */
    public synchronized static void shutdown() {
        if (THREAD_POOL_EXECUTOR != null && (!THREAD_POOL_EXECUTOR.isShutdown() || THREAD_POOL_EXECUTOR.isTerminating())) {
            THREAD_POOL_EXECUTOR.shutdown();
        }
    }
}
