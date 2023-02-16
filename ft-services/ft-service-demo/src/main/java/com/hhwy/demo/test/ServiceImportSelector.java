package com.hhwy.demo.test;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ServiceImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //可以是@Configuration注解修饰的类，也可以是具体的Bean类的全限定名称
        return new String[]{"com.test.ConfigB"};
    }
}
