package com.hhwy.demo.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AspectAnnotation {

    @Pointcut("execution(public * com.hhwy.demo.service.*.*(..))")
    public void pc1(){}


    @Before("pc1()")
    public void before() {
        System.out.println("===============拦截before方法=========");
    }

    @Around("pc1()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("==============AspectAnnotation around前置通知=========");
        Object result = joinPoint.proceed();
        System.out.println("==============AspectAnnotation around后置通知=========");
        return result;
    }




}
