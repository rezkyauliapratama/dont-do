package rezkyaulia.android.dont_do;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Mutya Nayavashti on 02/01/2017.
 */

public class eventBus {

    private static eventBus mInstance;
    private PublishSubject<String> mSubject = PublishSubject.create();

    public static eventBus instanceOf() {
        if (mInstance == null) {
            mInstance = new eventBus();
        }
        return mInstance;
    }

    /**
     * Pass a String down to event listeners.
     */
    public void setToken(String string) {
        mSubject.onNext(string);
    }

    /**
     * Subscribe to this Observable. On event, do something e.g. replace a fragment
     */
    public Observable<String> getTokenObservable() {
        return mSubject;
    }
}
