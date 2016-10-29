package me.silong.rxcomponent.android.activityresult;

import android.app.Activity;
import android.content.Intent;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import rx.Observable;

/**
 * Created by SILONG on 10/17/16.
 */
public class RxActivityResultHandler {

  private WeakReference<Activity> mActivity;

  RxActivityResultHandler(Activity from) {
    mActivity = new WeakReference<>(from);
  }

  public Observable<RxResultData> observeResult(Integer... requestCode) {
    return Observable.defer(() -> {
      Activity activity = mActivity.get();
      if (activity != null) {
        Observable<RxResultMetaData> metaDataObservable = RxActivityResult.sSubject
            .filter(rxResultMetaData -> rxResultMetaData != null && rxResultMetaData.mRequestedActivity == activity.getClass());
        if (requestCode.length > 0) {
          Set<Integer> requestedCodes = new HashSet<>(Arrays.asList(requestCode));
          metaDataObservable = metaDataObservable
              .filter(rxResultMetaData -> requestedCodes.contains(rxResultMetaData.mRxResultData.getRequestCode()));
        }
        return metaDataObservable
            .map(rxResultMetaData -> rxResultMetaData.mRxResultData)
            .doOnNext(rxResultData -> {
              RxResultMetaData rxResultMetaData = RxActivityResult.sSubject.getValue();
              if (rxResultMetaData != null && rxResultMetaData.mRxResultData == rxResultData) {
                RxActivityResult.sSubject.onNext(null);
              }
            });
      } else {
        return Observable.empty();
      }
    });
  }

  public void startActivityForResult(Intent intent, int requestCode) {
    Activity activity = mActivity.get();
    if (activity != null) {
      Intent shadowIntent = new Intent(activity, ShadowActivity.class);
      shadowIntent.putExtra(ShadowActivity.EXTRA_REQUEST_CODE, requestCode);
      shadowIntent.putExtra(ShadowActivity.EXTRA_REQUEST_INTENT, intent);
      shadowIntent.putExtra(ShadowActivity.EXTRA_REQUEST_FROM, activity.getClass().getName());
      activity.startActivity(shadowIntent);
    }
  }

}
