package me.silong.rxcomponent.android.activityresult;

import android.app.Activity;

/**
 * Created by SILONG on 10/25/16.
 */
class RxResultMetaData {

  final RxResultData mRxResultData;

  final Class<? extends Activity> mRequestedActivity;

  public RxResultMetaData(RxResultData rxResultData, Class<? extends Activity> requestedActivity) {
    mRxResultData = rxResultData;
    mRequestedActivity = requestedActivity;
  }
}
