package com.cohelp.test.demo03Hystrix技术解析;

import com.netflix.hystrix.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Demo01 {

    @Test
    public void test01() {

        class GetOrderCommand extends HystrixCommand<List> {

            /**
             * 这里是真正的RPC服务。
             */
//            OrderService orderService;

            protected GetOrderCommand() {
                super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestCmdGroup"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("TestCmdKey"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("TestPoolKey"))
                        .andCommandPropertiesDefaults(HystrixCommandProperties
                                .Setter()
                                .withExecutionTimeoutInMilliseconds(5000))
                        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties
                                .Setter()
                                .withMaxQueueSize(10)
                                .withCoreSize(2)));
            }


            @Override
            protected List run() throws Exception {
                /**
                 * 真实场景应该这样。
                 */
//                return orderService.getOrderList();
                return Collections.emptyList();
            }
        }

        GetOrderCommand cmd = new GetOrderCommand();
        Future future = cmd.queue();
        List list = null;
        try {
            list = (List) future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(list);

    }
}