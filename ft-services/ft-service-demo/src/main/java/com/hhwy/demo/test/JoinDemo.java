package com.hhwy.demo.test;

import org.apache.catalina.startup.Tomcat;

public class JoinDemo {

    public static void main(String[] args) throws Exception {
        //创建子线程，并启动子线程
        Thread subThread = new Thread(new SubThread());
        subThread.start();
        System.out.println("Now all thread done!");

        Tomcat tomcat = new Tomcat();
        tomcat.init();
        tomcat.start();
        tomcat.getServer().await();
    }

    /**
     * 子线程类
     * @author fuhg
     */

    private static class SubThread implements Runnable{

        public void run() {

            // TODO Auto-generated method stub

            System.out.println("Sub thread is starting!");

            try {

                Thread.sleep(5000L);

            } catch (InterruptedException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();

            }

            System.out.println("Sub thread is stopping!");

        }

    }

}