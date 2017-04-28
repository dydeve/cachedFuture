package dy.cf;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by duyu3 on 2017/4/28.
 */
public abstract class AbstractCachedFuture<P, R> implements CachedFuture<P, R> {

    /**
     * it has the Semantics of "default value", can be {@code null} or defaultValue.
     * it has benefits of setting a default value: if future.get(...) return null,
     * we can put a default value into cache(like redis, memedcache...) to avoid
     * calling future.get(...)（get data from databases, outer interfaces） frequently,
     * which will improve performance.
     *
     * 具有 “默认值” 的语义， 可以是 {@code null} 或 默认值
     * 设置默认值有很多好处: 如果 future.get(...)没有返回， 可以将默认值放入缓存(redis, memedcache...)，
     * 避免频繁的调用future.get(...)(从数据库、外部接口获取数据),
     * 提高性能
     */
    private final R NULL;

    protected AbstractCachedFuture(R aNull) {
        NULL = aNull;
    }


    @Override
    public R cachedResult(P parameter, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        final long deadLine = System.nanoTime() + unit.toNanos(timeout);
        //step 1: get from cache
        R result = fromCache(parameter);
        if (null != result) {
            return result;
        }
        long nowDiff = deadLine - System.nanoTime();//left time,剩余时间
        if (nowDiff <= 0) {//no left time，没有剩余时间
            return NULL;
        }
        //step 2: get from client（dbs, interfaces...）
        result = fromClient(parameter, nowDiff, TimeUnit.NANOSECONDS);
        if (result == null) {
            result = NULL;
        }
        if (null != result) {
            //step 3: set cache
            setCache(result);
        }
        return result;
    }

    @Override
    public R cachedResult(P parameter) throws InterruptedException, ExecutionException {
        return null;
    }


    @Override
    public R fromClient(P parameter) {
        return null;
    }

    @Override
    public R fromClient(P parameter, long timeout, TimeUnit unit) {
        return null;
    }
}
