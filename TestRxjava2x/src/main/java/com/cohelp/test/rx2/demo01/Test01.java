package com.cohelp.test.rx2.demo01;

import io.reactivex.Flowable;

public class Test01 {

    public static void main(String[] args) {
        Flowable.fromArray(args).subscribe(s -> System.out.println("Hello " + s + "!"));
    }
}
