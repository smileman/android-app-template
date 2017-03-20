package org.kulabukhov.android.modules;

/**
 * Created by weery on 07.09.16.
 */
public interface BasePresenter {

	void subscribe();

	default void unsubscribe(){}
}
