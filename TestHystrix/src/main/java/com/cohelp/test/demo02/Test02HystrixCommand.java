package com.cohelp.test.demo02;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Test02HystrixCommand {

    static class TestCommand extends HystrixCommand<String> {

        String str;

        protected TestCommand(String str) {
            super(HystrixCommandGroupKey.Factory.asKey("TestGroup"));
            this.str = str;
        }

        @Override
        protected String run() throws Exception {
            return "Hello " + str + " " + Thread.currentThread().getName();
        }
    }

    @Test
    public void test01() {
        // demo 01 同步
        TestCommand cmd01 = new TestCommand("cmd01");
        String str = cmd01.execute();
        System.out.println(str);
    }

    @Test
    public void test03() {
        // demo 03 订阅
        TestCommand cmd03 = new TestCommand("cmd03");
        Observable<String> o = cmd03.observe();
        o.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("call : " + s);
            }
        });

        o.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("end03");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error03");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext03 : " + s);
            }
        });
    }

    /**
     * 降级
     */
    @Test void test04() {

        class TestCommand extends HystrixCommand<String> {

            String str;

            protected TestCommand(String str) {

                super(Setter
                        .withGroupKey(HystrixCommandGroupKey
                                .Factory
                                .asKey("TestGroup"))
                        .andCommandPropertiesDefaults(HystrixCommandProperties
                                .Setter()
                                .withExecutionIsolationThreadTimeoutInMilliseconds(500)));

                this.str = str;
            }

            @Override
            protected String run() throws Exception {

                TimeUnit.SECONDS.sleep(1);

                return "Hello : " + str + " " + Thread.currentThread().getName();
            }

            @Override
            protected String getFallback() {
                return "Fallback";
            }
        }

        TestCommand cmd04 = new TestCommand("cmd04");
        String s = cmd04.execute();
        System.out.println(s);
    }

    @Test void test05() {

        class TestCommand extends HystrixCommand<String> {

            String str;
            int key;

            protected TestCommand(String str, int key) {
                super(HystrixCommandGroupKey.Factory.asKey("TestGroup"));
                this.str = str;
                this.key = key;
            }

            @Override
            protected String run() throws Exception {
                return "Run : " + str + "。 Random : " + new Random().nextInt();
            }

            @Override
            protected String getCacheKey() {
                return String.valueOf(this.key);
            }
        }

        HystrixRequestContext context = HystrixRequestContext.initializeContext();

        TestCommand cmd01 = new TestCommand("A", 1);
        TestCommand cmd02 = new TestCommand("B", 2);
        TestCommand cmd03 = new TestCommand("B", 2);

        System.out.println(cmd01.execute());
        System.out.println(cmd02.execute());
        System.out.println(cmd03.execute());

    }

    public static void main(String[] args) {

        // demo 01 同步
        TestCommand cmd01 = new TestCommand("cmd01");
        String str = cmd01.execute();
        System.out.println(str);

        // demo 02 异步
        TestCommand cmd02 = new TestCommand("cmd02");
        Future<String> f = cmd02.queue();
        try {
            str = f.get(10, TimeUnit.SECONDS);
            System.out.println(str);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }



        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
