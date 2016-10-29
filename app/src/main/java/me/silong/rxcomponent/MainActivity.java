package me.silong.rxcomponent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import me.silong.rxcomponent.android.activityresult.RxActivityResult;
import me.silong.rxcomponent.android.activityresult.RxActivityResultHandler;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {

  private RxActivityResultHandler mRxActivityResultHandler;

  private Subscription mSubscription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mRxActivityResultHandler = RxActivityResult.from(this);
    mSubscription = mRxActivityResultHandler.observeResult()
        .subscribe(rxResultData -> {
          TextView vContent = (TextView) findViewById(R.id.main_content);
          vContent.setText(rxResultData.getIntent().getStringExtra("result"));
        });
  }

  @Override
  protected void onDestroy() {
    if (mSubscription != null && !mSubscription.isUnsubscribed()) {
      mSubscription.unsubscribe();
    }
    super.onDestroy();
  }

  public void getResult(View v) {
    Intent intent = new Intent(this, ResultActivity.class);
    mRxActivityResultHandler.startActivityForResult(intent, 1);
  }

}
