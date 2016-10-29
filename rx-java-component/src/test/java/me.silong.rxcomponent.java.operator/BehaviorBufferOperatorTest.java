package me.silong.rxcomponent.java.operator;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by SILONG on 10/26/16.
 */
public class BehaviorBufferOperatorTest {

  @Test
  public void testBuffer() throws Exception {
    BehaviorSubject<Integer> subject = BehaviorSubject.create();
    TestSubscriber<List<Integer>> testSubscriber = new TestSubscriber<>();
    subject.compose(BehaviorBufferOperator
        .applyBehaviorBuffer(200, TimeUnit.MILLISECONDS, Schedulers.io()))
        .subscribe(testSubscriber);
    Thread.sleep(500);
    subject.onNext(1);
    subject.onNext(2);
    subject.onNext(3);
    Thread.sleep(300);
    subject.onNext(4);
    subject.onNext(5);
    subject.onNext(6);
    subject.onNext(7);
    subject.onCompleted();
    List<List<Integer>> onNextEvents = testSubscriber.getOnNextEvents();
    assertThat(onNextEvents.size(), equalTo(2));
    List<Integer> events1 = onNextEvents.get(0);
    List<Integer> events2 = onNextEvents.get(1);
    assertThat(events1.size(), equalTo(3));
    assertThat(events2.size(), equalTo(4));
    assertThat(events1.get(0), equalTo(1));
    assertThat(events1.get(1), equalTo(2));
    assertThat(events1.get(2), equalTo(3));

    assertThat(events2.get(0), equalTo(4));
    assertThat(events2.get(1), equalTo(5));
    assertThat(events2.get(2), equalTo(6));
    assertThat(events2.get(3), equalTo(7));
  }

  @Test
  public void testMultipleThread() throws Exception {
    BehaviorSubject<Integer> subject = BehaviorSubject.create();
    TestSubscriber<List<Integer>> testSubscriber = new TestSubscriber<>();
    subject.compose(BehaviorBufferOperator
        .applyBehaviorBuffer(200, TimeUnit.MILLISECONDS, Schedulers.io()))
        .subscribe(testSubscriber);
    for (int i = 0; i < 5; i++) {
      int j = i;
      new Thread() {
        @Override
        public void run() {
          try {
            subject.onNext(j * 10);
            Thread.sleep(50);
            subject.onNext(j * 10 + 1);
            Thread.sleep(100);
            subject.onNext(j * 10 + 2);
            Thread.sleep(150);
            subject.onNext(j * 10 + 3);
            Thread.sleep(650);
            subject.onNext(j * 10 + 4);
            Thread.sleep(200);
            subject.onNext(j * 10 + 5);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }.start();
      Thread.sleep(5);
    }
    Observable.interval(1000, TimeUnit.MILLISECONDS)
        .doOnNext(aLong -> subject.onCompleted())
        .subscribe();
    testSubscriber.awaitTerminalEvent();
    testSubscriber.assertNoErrors();
    testSubscriber.assertCompleted();
    List<List<Integer>> lists = testSubscriber.getOnNextEvents();
    assertThat(lists.size(), equalTo(3));
    List<Integer> list1 = lists.get(0);
    List<Integer> list2 = lists.get(1);
    List<Integer> list3 = lists.get(2);
    assertThat(list1.size(), equalTo(15));
    assertThat(list2.size(), equalTo(5));
    assertThat(list3.size(), equalTo(5));
    System.out.println(list1);
    System.out.println(list2);
    System.out.println(list3);
  }
}
