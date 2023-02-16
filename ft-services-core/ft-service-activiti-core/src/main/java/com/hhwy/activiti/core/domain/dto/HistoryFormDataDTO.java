package com.hhwy.activiti.core.domain.dto;

/**
 * <br>描 述： HistoryFormDataDTO
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/12 16:36
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class HistoryFormDataDTO {
    private String title;
    private String value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HistoryFormDataDTO() {
    }

    public HistoryFormDataDTO(String title, String value) {
        this.title = title;
        this.value = value;
    }
}
