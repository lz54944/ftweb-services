package com.hhwy.demo.test;

import com.hhwy.demo.domain.Demo;
import com.hhwy.demo.service.IDemoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 项目初始化加载
 */
//@Component
@Slf4j
public class TradeApplicationRunner implements ApplicationRunner {

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private IDemoService demoService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    /**
     * 模拟多线程下事务处理
     * @param args
     * @throws Exception
     */
   /* @Override
    public void run(ApplicationArguments args) throws Exception {
        //线程池
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(2);
        for(int i=0;i<2;i++) {
            cachedThreadPool.execute(new Runnable() {
                @SneakyThrows
                public void run() {
                    System.err.println("222222222222222222222222222222222222222222222222222222");
                    //开启编程式事务；
                    transactionTemplate.execute(new TransactionCallback<Boolean>() {
                        @Override
                        public Boolean doInTransaction(TransactionStatus transactionStatus) {
                            try {
                                Demo demo1 = new Demo();
                                demo1.setName("1");
                                demoService.add(demo1);
                                //模拟报错；

                                Demo demo2 = new Demo();
                                demo2.setName("2");
                                demoService.add(demo2);
                                System.err.println("555555555555555555555555555555555555555555555555555555555555555555555555555555555555");
                                return true;
                            }catch (Exception e){
                                //事务回滚；
                                transactionStatus.setRollbackOnly();
                                e.printStackTrace();
                                return false;
                            }
                        }
                    });
                }
            });
        }
    }*/

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<TransactionStatus> transactionStatuses = new ArrayList<>();
        TransactionStatus status1 = transactionManager.getTransaction(new DefaultTransactionDefinition());
        transactionStatuses.add(status1);
        Demo demo1 = new Demo();
        demo1.setName("1");
        demoService.add(demo1);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<String> c = (() -> {
            TransactionStatus status2 = transactionManager.getTransaction(new DefaultTransactionDefinition());
            Demo demo2 = new Demo();
            demo2.setName("2");
            demoService.add(demo2);
            int i=9/1;
            transactionStatuses.add(status2);
            return "1";
        });
        FutureTask<String> ft = new FutureTask<>(c);
        executorService.submit(ft);
        try {
            String s = ft.get();
            for (TransactionStatus transactionStatus : transactionStatuses) {
                transactionManager.commit(transactionStatus);
            }
        }catch (Exception e){
            System.err.println(1);
            for (TransactionStatus transactionStatus : transactionStatuses) {
                transactionManager.rollback(transactionStatus);
            }
        }
    }
}
