package com.hhwy.activiti.core.domain.dto;

import java.util.List;

/**
 * <br>描 述： HistoryDataDTO
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:36
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class HistoryDataDTO {
    private String taskNodeName;
    private String createName;
    private String createdDate;
    private List<HistoryFormDataDTO> formHistoryDataDTO;


    public String getTaskNodeName() {
        return taskNodeName;
    }

    public void setTaskNodeName(String taskNodeName) {
        this.taskNodeName = taskNodeName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<HistoryFormDataDTO> getFormHistoryDataDTO() {
        return formHistoryDataDTO;
    }

    public void setFormHistoryDataDTO(List<HistoryFormDataDTO> formHistoryDataDTO) {
        this.formHistoryDataDTO = formHistoryDataDTO;
    }
}
