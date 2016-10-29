package me.silong.rxcomponent.android.activityresult;

import android.app.Activity;

import rx.subjects.BehaviorSubject;

/**
 * Created by SILONG on 10/17/16.
 */
public class RxActivityResult {

  static final BehaviorSubject<RxResultMetaData> sSubject = BehaviorSubject.create();

  public static RxActivityResultHandler from(Activity activity) {
    return new RxActivityResultHandler(activity);
  }

}
