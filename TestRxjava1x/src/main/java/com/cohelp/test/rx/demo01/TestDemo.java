package com.cohelp.test.rx.demo01;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class TestDemo {

    @Test
    public void test01() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        for (int i = 1; i < 5; i++) {
                            observer.onNext(i);
                        }
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onNext(Integer item) {
                System.out.println("Next: " + item);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("Error: " + error.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Sequence complete.");
            }
        });

    }

    @Test
    public void test02() {

        List<String> list = new ArrayList<>();
        Observable<String> defer = Observable.defer(() -> {
//            return Observable.from(new String[]{});
            return Observable.from(list.toArray(new String[]{}));
        });

        list.add("AB");
        list.add("CD");

        defer.subscribe(s -> System.out.println("First : " + s));

        list.add("EF");

        defer.subscribe(s -> System.out.println("Second : " + s));

    }

//    @Test
//    public void test03() {
//        Future<String> future;
//        Observable.from(future);
//    }

    @Test
    public void test05() {
        Observable.just(new String[]{"AB", "CD"}).subscribe(strings -> System.out.println(strings));

    }
}
