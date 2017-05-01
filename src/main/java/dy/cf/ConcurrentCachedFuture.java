package dy.cf;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * a thread-safe cachedFuture.
 * can e shared among threads.
 * Created by dy on 2017/4/29.
 */
public abstract class ConcurrentCachedFuture<P, R> extends AbstractCachedFuture<P, R> {

    private final ThreadLocal<Future<R>> threadLocal = new ThreadLocal<Future<R>>() {
        @Override
        protected Future<R> initialValue() {
            throw new NullPointerException("no Future<R>");
        }
    };


    protected ConcurrentCachedFuture(R aNull) {
        super(aNull);
    }

    @Override
    public void setFuture(Future<R> future) {
        Objects.requireNonNull(future, "future can't be null!");
        threadLocal.set(future);
    }

    @Override
    public Future<R> getFuture() {
        return threadLocal.get();
    }

    /**
     * {@inheritDoc}
     * <p/>
     * remember to remove from threadLocal.
     * {@see ThreadLocal#remove()}
     * @param parameter
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public R fromClient(P parameter) throws InterruptedException, ExecutionException {
        try {
            return doFromClient(parameter);
        } finally {
            threadLocal.remove();
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * remember to remove from threadLocal.
     * {@see ThreadLocal#remove()}
     * @param parameter
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Override
    public R fromClient(P parameter, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException {
        try {
            return doFromClient(parameter, timeout, unit);
        } finally {
            threadLocal.remove();
        }
    }

    protected abstract R doFromClient(P parameter)  throws InterruptedException, ExecutionException;

    protected abstract R doFromClient(P parameter, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException;
}
