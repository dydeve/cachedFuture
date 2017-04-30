package dy.cf;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * a common cachedFuture
 * not thread-safe. can't be shared among threads.
 * it is recommended to {@code new} it every time you use it.
 *
 * Created by dy on 2017/4/30.
 */
public abstract class CommonCachedFuture<P, R> extends AbstractCachedFuture<P, R> {

    private Future<R> future;

    protected CommonCachedFuture(R aNull) {
        super(aNull);
    }

    @Override
    public void setFuture(Future<R> future) {

        if (this.future == null && future != null) {
            this.future = future;
        } else {
            throw new NullPointerException("this.future != null || parameter future == null");
        }

    }

    @Override
    public Future<R> getFuture() {
        return future;
    }

}
