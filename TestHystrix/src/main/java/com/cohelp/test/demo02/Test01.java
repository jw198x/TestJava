package com.cohelp.test.demo02;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.TimeUnit;

public class Test01 {

    public static class HelloWorldHystrixCommand extends HystrixCommand<String> {
        private final String name;
        public HelloWorldHystrixCommand(String name) {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
            this.name = name;
        }
        // 如果继承的是HystrixObservableCommand，要重写Observable construct()
        @Override
        protected String run() {
//            try {
////                TimeUnit.MINUTES.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return "Hello " + name;
        }
    }

    public static void main(String[] args) {
//        /* 调用程序对HelloWorldHystrixCommand实例化，执行execute()即触发HelloWorldHystrixCommand.run()的执行 */
//        String result = new HelloWorldHystrixCommand("HLX").execute();
//        System.out.println(result);  // 打印出Hello HLX

        Observable<String> xxx = new HelloWorldHystrixObservableCommand("XXX").observe();
        xxx.subscribe();
    }

    public static class HelloWorldHystrixObservableCommand extends HystrixObservableCommand<String> {

        private final String name;

        public HelloWorldHystrixObservableCommand(String name) {
            super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
//		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("testCommandGroupKey"))
//                .andCommandKey(HystrixCommandKey.Factory.asKey("testCommandKey"))
//                /* 使用HystrixThreadPoolKey工厂定义线程池名称*/
////                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("testThreadPool"))
//                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)));
            this.name = name;
        }

//	@Override
//    protected String getFallback() {
//		System.out.println("触发了降级!");
//        return "exeucute fallback";
//    }

        //	@Override
        protected Observable<String> construct() {
            System.out.println("in construct! thread:" + Thread.currentThread().getName());
            return Observable.create(new Observable.OnSubscribe<String>() {
                //            @Override
                public void call(Subscriber<? super String> observer) {
                    try {
                        System.out.println("in call of construct! thread:" + Thread.currentThread().getName());
                        if (!observer.isUnsubscribed()) {
//                      observer.onError(getExecutionException());	// 直接抛异常退出，不会往下执行
                            observer.onNext("Hello1" + " thread:" + Thread.currentThread().getName());
                            observer.onNext("Hello2" + " thread:" + Thread.currentThread().getName());
                            observer.onNext(name + " thread:" + Thread.currentThread().getName());
                            System.out.println("complete before------" + " thread:" + Thread.currentThread().getName());
                            observer.onCompleted();	// 不会往下执行observer的任何方法
                            System.out.println("complete after------" + " thread:" + Thread.currentThread().getName());
                            observer.onCompleted();	// 不会执行到
                            observer.onNext("abc");	// 不会执行到
                        }
                    } catch (Exception e) {
                        observer.onError(e);
                    }
                }
            } );
        }
    }


}
