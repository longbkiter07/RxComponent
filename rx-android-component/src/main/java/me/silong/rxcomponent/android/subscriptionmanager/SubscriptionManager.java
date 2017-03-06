package me.silong.rxcomponent.android.subscriptionmanager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import rx.Subscription;

/**
 * Created by SILONG on 11/13/15.
 */
public class SubscriptionManager implements SubscriptionManageable {

  private boolean mIsDead = false;

  private Map<UnsubscribeLifeCycle, Map<String, Subscription>> mSubscriptions;

  public SubscriptionManager() {
    mSubscriptions = new HashMap<>();
  }

  @Override
  public void bind(UnsubscribeLifeCycle unsubscribeLifeCycle) {
    Map<String, Subscription> subscriptions = mSubscriptions.get(unsubscribeLifeCycle);
    if (subscriptions != null) {
      mSubscriptions.remove(unsubscribeLifeCycle);
      unsubscribe(subscriptions);
    }
  }

  @Override
  public void manageSubscription(Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    manageSubscription(UUID.randomUUID().toString(), subscription, unsubscribeLifeCycle);
  }

  @Override
  public void manageSubscription(String id, Subscription subscription, UnsubscribeLifeCycle unsubscribeLifeCycle) {
    if (mIsDead) {
      return;
    }
    Map<String, Subscription> subscriptions = mSubscriptions.get(unsubscribeLifeCycle);
    if (subscriptions == null) {
      subscriptions = new HashMap<>();
      mSubscriptions.put(unsubscribeLifeCycle, subscriptions);
    } else {
      Subscription oldSubscription = subscriptions.get(id);
      if (oldSubscription != null && !oldSubscription.isUnsubscribed()) {
        oldSubscription.unsubscribe();
      }
    }
    subscriptions.put(id, subscription);
  }

  @Override
  public void unsubscribe() {
    mIsDead = true;
    for (Map.Entry<UnsubscribeLifeCycle, Map<String, Subscription>> entry : mSubscriptions.entrySet()) {
      unsubscribe(entry.getValue());
    }
  }

  @Override
  public void unsubscribe(String id) {
    for (Map.Entry<UnsubscribeLifeCycle, Map<String, Subscription>> entry : mSubscriptions.entrySet()) {
      unsubscribe(entry.getValue().get(id));
    }
  }

  private void unsubscribe(Map<String, Subscription> subscriptions) {
    for (Map.Entry<String, Subscription> entry : subscriptions.entrySet()) {
      unsubscribe(entry.getValue());
    }
    subscriptions.clear();
  }

  private void unsubscribe(Subscription subscription) {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}
