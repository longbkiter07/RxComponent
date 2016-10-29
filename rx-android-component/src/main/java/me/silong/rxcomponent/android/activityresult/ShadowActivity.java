package me.silong.rxcomponent.android.activityresult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by SILONG on 10/17/16.
 */
public class ShadowActivity extends Activity {

  static final String EXTRA_REQUEST_CODE = "request_code";

  static final String EXTRA_REQUEST_INTENT = "request_intent";

  static final String EXTRA_INITIALIZED = "initialized";

  static final String EXTRA_REQUEST_FROM = "request_from";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent().getParcelableExtra(EXTRA_REQUEST_INTENT);
    int requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, 0);
    boolean isInitialized = savedInstanceState != null && savedInstanceState.getBoolean(EXTRA_INITIALIZED, false);
    if (intent != null && !isInitialized) {
      startActivityForResult(intent, requestCode);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (outState != null) {
      outState.putBoolean(EXTRA_INITIALIZED, true);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Class<? extends Activity> activityClass;
    try {
      activityClass = (Class<? extends Activity>) Class.forName(getIntent().getStringExtra(EXTRA_REQUEST_FROM));
    } catch (Exception e) {
      activityClass = Activity.class;
    }
    RxActivityResult.sSubject.onNext(
        new RxResultMetaData(new RxResultData(requestCode, resultCode, data), activityClass)
    );
    super.onActivityResult(requestCode, resultCode, data);
    finish();
  }
}
