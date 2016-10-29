package me.silong.rxcomponent.android.activityresult;

import android.content.Intent;

/**
 * Created by SILONG on 10/17/16.
 */
public class RxResultData {

  int mRequestCode;

  int mResultCode;

  Intent mIntent;

  public RxResultData(int requestCode, int resultCode, Intent data) {
    mRequestCode = requestCode;
    mResultCode = resultCode;
    mIntent = data;
  }

  public int getRequestCode() {
    return mRequestCode;
  }

  public int getResultCode() {
    return mResultCode;
  }

  public Intent getIntent() {
    return mIntent;
  }

}
