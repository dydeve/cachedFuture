package dy.cf;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 异步 缓存 简单实现
 * Created by duyu3 on 2017/4/27.
 */
@Deprecated
public abstract class SimpleCachedFuture<P, R> implements CachedFuture<P, R> {

    /**
     * with default value or not
     * when dependency returns null, we can put default value into cache(redis, memcache...) to avoid calling dependency frequently
     * 全局设置 是否需要默认值
     * 当依赖方返回null时，可以将默认值存入缓存，避免频繁调用外部接口，影响性能
     */
    private final boolean withDefault;

    /**
     * if {@link #withDefault} == {#code true}, defaultValue can't be null
     * 如果需要默认值 不可为null
     */
    private final R defaultValue;

    private AtomicBoolean concurrent = new AtomicBoolean();


    private Future<R> future;
    AtomicReference<Future<R>> reference = new AtomicReference<>();

    private SimpleCachedFuture(boolean withDefault, R defaultValue) {

        if (withDefault && defaultValue == null) {
            throw new NullPointerException();
        }
        this.withDefault = withDefault;
        this.defaultValue = defaultValue;
    }

    /**
     * 只可设置一次
     * 代理  屏蔽if else
     * 函数式方法 屏蔽 if else
     * @param future
     */
    @Override
    public void setFuture(Future<R> future) {

        if (future == null) {
            throw new NullPointerException();
        }

        if (concurrent.get()) {

            reference.compareAndSet(null, future);

        } else {
            if (future == null) {
                this.future = future;
            } else {
                throw new RuntimeException("future has exists!");
            }
        }

    }

    /*public <I, O> O ifElse(Function<I, O> fun1, Function<I, O> fun2) {

        if (concurrent.get()) {
            fun1.apply();
        }

    }*/

    /**
     * future一旦初始化 就不可改变
     * @return
     */
    @Override
    public Future<R> getFuture() {
        reference.get();
        return future;
    }

    /**
     * 可用用 默认值 替代null
     * @param parameter
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Override
    public R cachedResult(P parameter, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        R result = fromCache(parameter);
        if (result == null) {
            result = fromClient(parameter, timeout, unit);
        }
        if (result == null && withDefault) {
            result = defaultValue;
        }
        return null;
    }

    @Override
    public R cachedResult(P parameter) throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public R fromCache(P parameter) {
        return null;
    }

    @Override
    public void setCache(R result) {

    }

    @Override
    public R fromClient(P parameter) {
        return null;
    }

    @Override
    public R fromClient(P parameter, long timeout, TimeUnit unit) {
        return null;
    }

    public R defaultValue() {
        return null;
    }
}
