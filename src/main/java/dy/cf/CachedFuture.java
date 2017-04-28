package dy.cf;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 异步 缓存 相结合
 * @param <P> 入参
 * @param <R> 出参
 *
 * Created by dy on 2017/4/27.
 */
public interface CachedFuture<P, R> {

    /**
     * 设置future
     * @param future
     */
    void setFuture(Future<R> future);

    /**
     * 获取future
     * @return
     */
    Future<R> getFuture();

    /**
     * 从缓存中取值
     * 如果没有 异步从接口中获取值 然后设置
     *
     * 带超时时间
     *
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @throws TimeoutException if the wait timed out
     * @return
     */
    R cachedResult(P parameter, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;

    /**
     * 从缓存中取值
     * 如果没有 异步从接口中获取值 然后设置
     *
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @return
     */
    R cachedResult(P parameter) throws InterruptedException, ExecutionException;

    /**
     * 只从缓存中获取
     * @param parameter
     * @return
     */
    R fromCache(P parameter);

    /**
     * 只设置缓存
     * @param result
     */
    void setCache(R result);

    /**
     * 只从接口中获取值
     * @param parameter
     * @return
     */
    R fromClient(P parameter);

    R fromClient(P parameter, long timeout, TimeUnit unit);

}
