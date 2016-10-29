package me.silong.rxcomponent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

public class ResultActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);
    scheduleResult();
  }

  private void scheduleResult() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Bundle extras = new Bundle();
        extras.putString("result", UUID.randomUUID().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtras(extras);
        setResult(RESULT_OK, resultIntent);
        finish();
      }
    }, 3000);
  }
}
