package com.hhwy.activiti.core.service;

public interface IActImageService {

    /**
     * 根据流程实例Id获取流程图
     *
     * @param procInstId 流程实例id
     * @return inputStream
     * @throws Exception exception
     */
    String getFlowImgByProcInstId(String procInstId) throws Exception;
}
