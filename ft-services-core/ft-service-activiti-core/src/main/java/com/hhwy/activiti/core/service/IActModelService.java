package com.hhwy.activiti.core.service;

import com.hhwy.activiti.core.domain.dto.ActModelDTO;
import com.hhwy.common.core.web.domain.AjaxResult;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.repository.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * <br>描 述： IActModelService
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/14 15:46
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
public interface IActModelService {
    /**
     * <br>方法描述：模型列表查询
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/14 15:45
     * <br>备注：无
     */
    Map<String, Object> selectModelList(ActModelDTO actModelDTO, Integer pageSize, Integer pageNum);

    /**
     * 查询模型信息
     *
     * @param modelId 模型ID
     * @return 模型信息
     */
    Model selectModelById(String modelId);

    /**
     * 查询模型信息
     *
     * @param processKey 流程key
     * @return 模型信息
     */
    List<Model> selectModelByProcessKey(String processKey);

    /**
     * 创建模型
     *
     * @param actModelDTO 模型信息
     * @return 模型ID
     * @throws UnsupportedEncodingException
     */
    AjaxResult addModel(ActModelDTO actModelDTO) throws Exception;

    /**
     * 修改模型信息
     *
     * @param actModelDTO 模型信息
     * @param actModelDTO editorSourceValue 模型文件（json格式数据）
     * @param actModelDTO editorSourceExtraValue 模型生成的图片文件(svg_json)
     */
    AjaxResult updateModel(ActModelDTO actModelDTO);

    /**
     * 批量删除模型信息
     *
     * @param ids 需要删除的数据IDs
     * @return
     */
    void deleteModelByIds(String ids);

    /**
     * 发布/部署流程
     *
     * @param modelId 模型id
     */
    AjaxResult deployProcess(String modelId) throws Exception;

    /**
     * 获取资源文件信息
     *
     * @param modelId 模型ID
     * @return 资源文件信息
     */
    byte[] getModelEditorSource(String modelId);

    /**
     * 导入流程
     *
     * @param file 流程文件
     * @return errMsg 错误信息
     */
    AjaxResult importModel(MultipartFile file) throws ActivitiException, IOException;
}
