package com.hhwy.system.core.test;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import java.util.List;

//@AutoConfigureAfter(PageHelperAutoConfiguration.class)
public class SelfBatisConfig implements InitializingBean {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;


    @Override
    public void afterPropertiesSet() throws Exception {
        SelfBatisIntercept selfBatisIntercept = new SelfBatisIntercept();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            org.apache.ibatis.session.Configuration configuration = sqlSessionFactory.getConfiguration();
            if (!containsInterceptor(configuration, selfBatisIntercept)) {
                configuration.addInterceptor(selfBatisIntercept);
            }
        }
    }

    private boolean containsInterceptor(org.apache.ibatis.session.Configuration configuration, Interceptor interceptor) {
        try {
            // getInterceptors since 3.2.2
            return configuration.getInterceptors().contains(interceptor);
        } catch (Exception e) {
            return false;
        }
    }
}
