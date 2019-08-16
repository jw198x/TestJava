package com.cohelp.test.rx.demo01;

import org.junit.jupiter.api.Test;
import rx.Observable;

public class Test02Operator {

    /**
     * map
     */

    @Test
    public void test01() {
        Observable.from(new String[]{"A", "B", "C"})
                .map( s -> {
                    return (int) s.toCharArray()[0];
                })
                .subscribe( s -> {
                    System.out.println("onNext : " + s);
        });
    }


    /**
     * flapMap
     */
    @Test
    public void test02() {
        Observable.from(new String[]{"ABC", "DEF", "GHI"})
                .flatMap(str -> {   // data -> new Obserable
                    String[] arr = str.split("");
                    return Observable.from(arr);
                })
                .subscribe(s -> {
                    System.out.println(s);
                });

        // 等同于
        Observable.from(new String[]{"ABC", "DEF", "GHI"})
                .subscribe(s -> {
                    Observable.from(s.split(""))
                            .subscribe(s1 -> {
                                System.out.println(s1);
                            });
                });

    }
}
