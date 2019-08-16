package com.cohelp.test.rx.demo01;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;

import javax.sound.midi.Soundbank;

public class Test01 {

    @Test
    public void test01() {

        Observable<String> o = Observable.from(new String[]{"A", "B", "C"});




    }

    @Test
    public void test02() {
        Observable<String> observable = Observable.from(new String[]{"A", "B", "C"});
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onEnd");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext : " + s);
                throw new RuntimeException();
            }
        });

    }

    @Test
    public void test03() {
        Observable.just("Hello").subscribe(s -> {
            System.out.println("onNext : " + s);
        });
    }




    public static void main(String[] args) {
        System.out.println("A");

//        Flowable.fromArray(args).subscribe(s -> System.out.println("Hello " + s + "!"));
    }
}
