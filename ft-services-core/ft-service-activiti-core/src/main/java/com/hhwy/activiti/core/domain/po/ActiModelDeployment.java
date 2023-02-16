package com.hhwy.activiti.core.domain.po;

import com.hhwy.common.core.web.domain.BaseEntity;

/**
 * acti_model_deployment
 * 
 * @author jzq
 * @date 2021-07-28
 */
public class ActiModelDeployment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** act_re_model ID_ */
    private String modelId;

    /** act_re_deployment ID_ */
    private String deploymentId;

    public void setModelId(String modelId){
        this.modelId = modelId;
    }

    public String getModelId(){
        return modelId;
    }

    public void setDeploymentId(String deploymentId){
        this.deploymentId = deploymentId;
    }

    public String getDeploymentId(){
        return deploymentId;
    }
}
