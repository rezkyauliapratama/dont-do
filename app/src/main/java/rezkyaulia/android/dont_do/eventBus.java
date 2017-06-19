package rezkyaulia.android.dont_do;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Mutya Nayavashti on 02/01/2017.
 */

public class eventBus<T> {
    private static eventBus mInstance;

    private PublishSubject<T> mSubject = PublishSubject.create();

    public static eventBus instanceOf() {
        if (mInstance == null) {
            mInstance = new eventBus();
        }
        return mInstance;
    }

    /**
     * Pass a String down to event listeners.
     */
    public void setObservable(T object) {
        mSubject.onNext(object);
    }

    /**
     * Subscribe to this Observable. On event, do something e.g. replace a fragment
     */
    public Observable<T> getObservable() {
        return mSubject;
    }
}
