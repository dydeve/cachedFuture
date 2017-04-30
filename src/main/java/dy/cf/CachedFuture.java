package dy.cf;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * async & cache
 * 异步 缓存 相结合
 * @param <P> 入参 parameter
 * @param <R> 出参 result
 *
 * Created by dy on 2017/4/27.
 */
public interface CachedFuture<P, R> {

    /**
     * set future
     * 设置future
     * @param future
     */
    void setFuture(Future<R> future);

    /**
     * get future
     * 获取future
     * @return
     */
    Future<R> getFuture();

    /**
     * get result from cache with timeout, if not exists,
     * get from client(db,interface...), and then set to cache
     * <p>
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
     * get result from cache without timeout, if not exists,
     * get from client(db,interface...), and then set to cache
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
     * get from cache only
     * 只从缓存中获取
     * @param parameter
     * @return
     */
    R fromCache(P parameter);

    /**
     * set cache only
     * 只设置缓存
     * @param result
     */
    void setCache(R result);

    /**
     * get from client, without timeout
     * 只从接口中获取值, 不带超时时间
     *
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @param parameter
     * @return
     */
    R fromClient(P parameter) throws InterruptedException, ExecutionException;

    /**
     * get from cache, with timeout
     *
     * @throws CancellationException if the computation was cancelled
     * @throws ExecutionException if the computation threw an
     * exception
     * @throws InterruptedException if the current thread was interrupted
     * while waiting
     * @param parameter
     * @param timeout
     * @param unit
     * @return
     */
    R fromClient(P parameter, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException;


}
