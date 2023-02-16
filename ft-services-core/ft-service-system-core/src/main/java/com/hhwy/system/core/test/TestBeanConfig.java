package com.hhwy.system.core.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

//@Configuration
public class TestBeanConfig {

     /**
        * @Component
        * public class SelfSysDeptControllerAdapter extends SysDeptControllerAdapter{}
        */
    @Bean
    @ConditionalOnMissingBean
    public TestSysDeptControllerAdapter sysDeptControllerAdapter(){
        return new TestSysDeptControllerAdapter();
    }

}
