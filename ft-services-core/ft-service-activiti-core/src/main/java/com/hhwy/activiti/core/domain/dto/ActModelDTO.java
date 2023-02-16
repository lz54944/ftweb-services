package com.hhwy.activiti.core.domain.dto;

import org.activiti.engine.impl.persistence.entity.ModelEntityImpl;

/**
 * <br>描 述： ActModelDTO
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/15 14:34
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public class ActModelDTO extends ModelEntityImpl {

    /**
     * 字段描述：描述
     */
    private String description;

    /**
     * act_re_model中的EDITOR_SOURCE_VALUE_ID_ 对应 ACT_GE_BYTEARRAY的ID_，表示该模型对应的模型文件（json格式数据）
     * epositoryService.addModelEditorSource方法实现
     */
    protected String editorSourceValueId;

    /**
     * act_re_model中的EDITOR_SOURCE_EXTRA_VALUE_ID_ 对应 ACT_GE_BYTEARRAY的ID_，表示该模型生成的图片文件。
     * repositoryService.addModelEditorSourceExtra方法实现
     */
    protected String editorSourceExtraValueId;

    /**
     * 字段描述：模型文件（xml、bpmn等 json格式数据）
     */
    protected String editorSourceValue;

    /**
     * 字段描述：模型生成的图片文件(svg等 json格式数据)
     */
    protected String editorSourceExtraValue;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEditorSourceValue() {
        return editorSourceValue;
    }

    public void setEditorSourceValue(String editorSourceValue) {
        this.editorSourceValue = editorSourceValue;
    }

    public String getEditorSourceExtraValue() {
        return editorSourceExtraValue;
    }

    public void setEditorSourceExtraValue(String editorSourceExtraValue) {
        this.editorSourceExtraValue = editorSourceExtraValue;
    }

    @Override
    public String getEditorSourceValueId() {
        return editorSourceValueId;
    }

    @Override
    public void setEditorSourceValueId(String editorSourceValueId) {
        this.editorSourceValueId = editorSourceValueId;
    }

    @Override
    public String getEditorSourceExtraValueId() {
        return editorSourceExtraValueId;
    }

    @Override
    public void setEditorSourceExtraValueId(String editorSourceExtraValueId) {
        this.editorSourceExtraValueId = editorSourceExtraValueId;
    }
}
