package me.silong.rxcomponent.android.subscriptionmanager;

import rx.Subscription;

/**
 * Created by SILONG on 11/13/15.
 */
public interface SubscriptionManageable {

  void bind(UnsubscribeLifeCycle unsubscribeLifeCycle);

  void manageSubscription(Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle);

  void manageSubscription(String id, Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle);

  void unsubscribe();

  void unsubscribe(String id);
}
