package com.hhwy.activiti.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hhwy.activiti.core.domain.dto.ActModelDTO;
import com.hhwy.activiti.core.mapper.ActiModelDeploymentMapper;
import com.hhwy.activiti.core.service.IActModelService;
import com.hhwy.common.core.text.Convert;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.bean.BeanUtils;
import com.hhwy.common.core.web.domain.AjaxResult;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.converter.util.InputStreamProvider;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExtensionAttribute;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>描 述： ActModelServiceImpl
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/14 16:17
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Service
public class ActModelServiceImpl implements IActModelService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActiModelDeploymentMapper actiModelDeploymentMapper;

    /**
     * <br>方法描述：模型列表查询
     * <br>创 建 人：Jinzhaoqiang
     * <br>创建时间：2021/7/14 15:45
     * <br>备注：无
     */
    @Override
    public Map<String, Object> selectModelList(ActModelDTO actModelDTO, Integer pageSize, Integer pageNum) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        if (StringUtils.isNotEmpty(actModelDTO.getKey())) {
            modelQuery = modelQuery.modelKey(actModelDTO.getKey());
        }
        if (StringUtils.isNotEmpty(actModelDTO.getName())) {
            modelQuery = modelQuery.modelNameLike("%" + actModelDTO.getName()+"%");
        }
        long count = modelQuery.count();
        List<Model> models = modelQuery.orderByModelKey().desc().orderByCreateTime().desc().listPage((pageNum - 1) * pageSize, pageSize);//从0开始
        Map<String, Object> result = new HashMap<>();
        result.put("total", count);
        result.put("items", models);
        return result;
    }

    /**
     * 查询模型信息
     *
     * @param modelId 模型ID
     * @return 模型信息
     */
    @Override
    public Model selectModelById(String modelId) {
        Assert.notNull(modelId, "id不能为空");
        return repositoryService.getModel(modelId);
    }

    @Override
    public List<Model> selectModelByProcessKey(String processKey) {
        List<Model> list = repositoryService.createModelQuery().modelKey(processKey).orderByModelVersion().desc().list();
        return list;
    }

    /**
     * 创建模型
     *
     * @param actModelDTO 模型信息
     * @return 模型ID
     */
    @Override
    @Transactional
    public AjaxResult addModel(ActModelDTO actModelDTO){
        BpmnModel bpmnModel;
        try {
            bpmnModel = getBpmnModel(actModelDTO);
        } catch (XMLException e) {
            e.printStackTrace();
            return AjaxResult.error("流程图格式错误");
        }
        //流程key
        String key = bpmnModel.getMainProcess().getId();
        List<Model> models = selectModelByProcessKey(key);
        if (!CollectionUtils.isEmpty(models)) {
            return AjaxResult.error("流程key:" + key + "已存在");
        }
        //流程名字
        String name = bpmnModel.getMainProcess().getName();
        //流程描述
        String description = getDescription(bpmnModel.getMainProcess());
        //流程版本
        Integer version = 1;
        modelInit(actModelDTO, name, key, version, description);
        Model model = repositoryService.newModel();
        BeanUtils.copyBeanProp(model, actModelDTO);
        repositoryService.saveModel(model);
        String modelId = model.getId();
        repositoryService.addModelEditorSource(modelId, actModelDTO.getEditorSourceValue().getBytes());
        repositoryService.addModelEditorSourceExtra(modelId, actModelDTO.getEditorSourceExtraValue().getBytes());
        return AjaxResult.success(actModelDTO);
    }

    /**
     * 修改模型信息
     *
     * @param actModelDTO 模型信息
     * @param actModelDTO editorSourceValue 模型文件（json格式数据）
     * @param actModelDTO editorSourceExtraValue 模型生成的图片文件(svg_json)
     */
    @Override
    @Transactional
    public AjaxResult updateModel(ActModelDTO actModelDTO) {
        try {
            ModelEntityImpl model = (ModelEntityImpl) selectModelById(actModelDTO.getId());
            BpmnModel bpmnModel = getBpmnModel(actModelDTO);
            //流程key
            String key = bpmnModel.getMainProcess().getId();
            if (!model.getKey().equals(key)) {
                return AjaxResult.error("流程key必须为:" + model.getKey() + ",不能修改为：" + key);
            }
            //流程名字
            String name = bpmnModel.getMainProcess().getName();
            //流程描述
            String description = getDescription(bpmnModel.getMainProcess());
            //流程版本
            Model latestModel = repositoryService.createModelQuery().modelKey(key).latestVersion().singleResult();
            int version = 1;
            if (latestModel != null) {
                version = latestModel.getVersion() + 1;
            }
            modelInit(model, name, key, version, description);
            model.setId(null);
            model.setDeploymentId(null);
            modelReset(model);
            repositoryService.saveModel(model);
            repositoryService.addModelEditorSource(model.getId(), actModelDTO.getEditorSourceValue().getBytes());
            repositoryService.addModelEditorSourceExtra(model.getId(), actModelDTO.getEditorSourceExtraValue().getBytes());
        } catch (Exception e) {
            throw new ActivitiException("更新模型失败，模型ID=" + actModelDTO.getId(), e);
        }
        return AjaxResult.success();
    }

    private void modelReset(ModelEntityImpl model) {
        model.setId(null);
        model.setEditorSourceExtraValueId(null);
        model.setEditorSourceValueId(null);
    }

    public byte[] source2Byte(String source) throws Exception {
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        InputStreamProvider inputStreamProvider = () -> {
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(source.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return inputStream;
        };
        BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(inputStreamProvider, true, true, "UTF-8");
        ObjectNode objectNode = new BpmnJsonConverter().convertToJson(bpmnModel);
        byte[] bytes = objectNode.toString().getBytes("UTF-8");
        return bytes;
    }

    /**
     * 根据模型ID批量删除
     *
     * @param ids 需要删除的数据ID
     * @return
     */
    @Override
    @Transactional
    public void deleteModelByIds (String ids){
        String[] modelIds = Convert.toStrArray(ids);
        for (String modelId : modelIds) {
            repositoryService.deleteModel(modelId);
        }
    }

    /**
     * 发布/部署流程
     *
     * @param modelId 模型id
     */
    @Override
    @Transactional
    public AjaxResult deployProcess(String modelId)  throws Exception{
        try {
            // 获取模型
            Model modelData = repositoryService.getModel(modelId);
            if (modelData == null) {
                return AjaxResult.error("模型数据为空，请先设计流程并成功保存，再进行发布。");
            }
            byte[] editorSourceBytes = repositoryService.getModelEditorSource(modelData.getId());
            editorSourceBytes = source2Byte(new String(editorSourceBytes));
            byte[] editorSourceExtraBytes = repositoryService.getModelEditorSourceExtra(modelData.getId());
            JsonNode modelNode = new ObjectMapper().readTree(editorSourceBytes);
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if (model.getProcesses().size() == 0) {
                return AjaxResult.error("数据模型不符要求，请至少设计一条主线流程。");
            }
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            // 发布流程
            String resourceName = modelData.getName();
            String editorSourceName = resourceName;
            String editorSourceExtraName = resourceName;
            if (!resourceName.endsWith("bpmn20.xml") && !resourceName.endsWith(".bpmn")) {
                editorSourceName = editorSourceName + ".bpmn"; //只能 以"bpmn20.xml"或"bpmn" 结尾，不然在部署的时候不会往act_re_procdef插入数据，原因见ParsedDeploymentBuilder中的isBpmnResource函数
            }
            if (!resourceName.endsWith(".svg")) {
                editorSourceExtraName = editorSourceExtraName + ".svg";
            }
            //String resourceName = modelData.getName() + ".bpmn"; //只能 以"bpmn20.xml"或"bpmn" 结尾，不然在部署的时候不会往act_re_procdef插入数据，原因见ParsedDeploymentBuilder中的isBpmnResource函数
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
                    .addString(editorSourceName, new String(bpmnBytes, "UTF-8"))
                    .addString(editorSourceExtraName, new String(editorSourceExtraBytes, "UTF-8"))
                    .deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
            actiModelDeploymentMapper.insertActiModelDeployment(modelId,deployment.getId());
        } catch (IOException e) {
            throw new ActivitiException("发布流程出现获取流程json数据失败，模型ID=" + modelId, e);
        }
        return AjaxResult.success();
    }

    /**
     * 获取资源文件信息
     *
     * @param modelId 模型ID
     * @return 资源文件信息
     */
    @Override
    public byte[] getModelEditorSource (String modelId){
        return repositoryService.getModelEditorSource(modelId);
    }

    @Override
    @Transactional
    public AjaxResult importModel (MultipartFile file) throws ActivitiException, IOException {
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        InputStreamProvider inputStreamProvider = () -> {
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        };
        BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(inputStreamProvider, true, true, "UTF-8");
        List<Model> models = selectModelByProcessKey(bpmnModel.getMainProcess().getId());
        //流程版本
        int version = 1;
        if (!CollectionUtils.isEmpty(models)) {
            //流程版本
            version = version + models.get(0).getVersion();
        }
        Model model = repositoryService.newModel();
        //流程ID
        String key = bpmnModel.getMainProcess().getId();
        //流程名字
        String name = bpmnModel.getMainProcess().getName();
        String description = getDescription(bpmnModel.getMainProcess());
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, version);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        model.setName(name);
        model.setKey(key);
        model.setVersion(version);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        ObjectNode objectNode = new BpmnJsonConverter().convertToJson(bpmnModel);
        byte[] bytes = objectNode.toString().getBytes("UTF-8");
        repositoryService.addModelEditorSource(model.getId(), bytes);
        return AjaxResult.success();
    }

    public String getDescription (Process process){
        return getAttributeValueFromProcess(process, "descriptionTag");
    }

    public String getAttributeValueFromProcess (Process process, String name){
        List<ExtensionAttribute> attributes = process.getAttributes().get(name);
        if (attributes != null && !attributes.isEmpty()) {
            String value = attributes.get(0).getValue();
            return value;
        }
        return null;
    }

    public BpmnModel getBpmnModel (ActModelDTO actModelDTO){
        String editorSourceValue = actModelDTO.getEditorSourceValue();
        Assert.notNull(editorSourceValue, "editorSourceValue不能为空");
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        InputStreamProvider inputStreamProvider = () -> {
            InputStream inputStream = null;
            try {
                inputStream = new ByteArrayInputStream(editorSourceValue.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return inputStream;
        };
        BpmnModel bpmnModel = xmlConverter.convertToBpmnModel(inputStreamProvider, true, true, "UTF-8");
        return bpmnModel;
    }

    public void modelInit (ModelEntityImpl model, String name, String key, Integer version, String description){
        ObjectNode modelJson = objectMapper.createObjectNode();
        modelJson.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelJson.put(ModelDataJsonConstants.MODEL_REVISION, version);

        model.setName(name);
        model.setKey(key);
        model.setVersion(version);
        model.setMetaInfo(modelJson.toString());
    }
}
