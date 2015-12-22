package com.despegar.p13n.hestia.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
public class HomeContentThreadPoolConfiguration {

    @Value("${homecontent.threadpool.size:30}")
    private int homeThreadPoolSize;

    @Value("${homecontent.threadpool.queue.capacity.max:200}")
    private int maxQueueCapacity;


    @Bean(name = "homeExecutorService")
    public ExecutorService homeAsyncExecutor() {
        return new ThreadPoolExecutor(this.homeThreadPoolSize,//
            this.homeThreadPoolSize,//
            0L, TimeUnit.MILLISECONDS,//
            new LinkedBlockingQueue<Runnable>(this.maxQueueCapacity),//
            new ThreadFactoryBuilder().setNameFormat("home-%d").build(),//
            new ThreadPoolExecutor.DiscardOldestPolicy());
    }

}
