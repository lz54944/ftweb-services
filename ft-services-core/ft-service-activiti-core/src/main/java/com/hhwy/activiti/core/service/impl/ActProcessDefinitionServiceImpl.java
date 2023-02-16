package com.hhwy.activiti.core.service.impl;

import com.github.pagehelper.Page;
import com.hhwy.activiti.core.domain.dto.ActRunTaskDTO;
import com.hhwy.activiti.core.domain.dto.DefinitionIdDTO;
import com.hhwy.activiti.core.domain.dto.ProcessDefinitionDTO;
import com.hhwy.activiti.core.domain.vo.ActReDeploymentVO;
import com.hhwy.activiti.core.mapper.ActReDeploymentMapper;
import com.hhwy.activiti.core.mapper.ActiModelDeploymentMapper;
import com.hhwy.activiti.core.mapper.ActiTaskConfigMapper;
import com.hhwy.activiti.core.mapper.ActivitiMapper;
import com.hhwy.activiti.core.service.IActProcessDefinitionService;
import com.hhwy.common.core.utils.StringUtils;
import com.hhwy.common.core.utils.file.FileUploadUtils;
import com.hhwy.common.core.web.page.PageDomain;
import com.hhwy.common.security.service.TokenService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * <br>描 述： ActProcessDefinitionServiceImpl
 * <br>创 建 人：Jinzhaoqiang
 * <br>创建时间：2021/7/28 15:24
 * <br>修改备注：无
 * <br>版本：1.0.0
 */
@Service
public class ActProcessDefinitionServiceImpl implements IActProcessDefinitionService {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActReDeploymentMapper actReDeploymentMapper;

    @Autowired
    private ActiModelDeploymentMapper actiModelDeploymentMapper;

    @Autowired
    private ActiTaskConfigMapper actiTaskConfigMapper;

    @Autowired
    private ActivitiMapper activitiMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TokenService tokenService;

    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.path}")
    private String localFilePath;

    @Override
    public Page<ProcessDefinitionDTO> selectProcessDefinitionList(ProcessDefinitionDTO processDefinition, PageDomain pageDomain) {
        Page<ProcessDefinitionDTO> list = new Page<>();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        if (StringUtils.isNotBlank(processDefinition.getName())) {
            processDefinitionQuery.processDefinitionNameLike("%" + processDefinition.getName() + "%");
        }
        if (StringUtils.isNotBlank(processDefinition.getKey())) {
            processDefinitionQuery.processDefinitionKeyLike("%" + processDefinition.getKey() + "%");
        }
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.orderByProcessDefinitionKey().desc().listPage((pageDomain.getPageNum() - 1) * pageDomain.getPageSize(), pageDomain.getPageSize());
        long count = processDefinitionQuery.count();
        list.setTotal(count);
        if (count != 0) {
            List<String> ids = processDefinitions.parallelStream().map(pdl -> pdl.getDeploymentId()).collect(Collectors.toList());
            List<ActReDeploymentVO> actReDeploymentVOS = actReDeploymentMapper.selectActReDeploymentByIds(ids);
            List<ProcessDefinitionDTO> processDefinitionDTOS = processDefinitions.stream()
                    .map(pd -> new ProcessDefinitionDTO((ProcessDefinitionEntityImpl) pd, actReDeploymentVOS.parallelStream().filter(ard -> pd.getDeploymentId().equals(ard.getId())).findAny().orElse(new ActReDeploymentVO())))
                    .collect(Collectors.toList());
            list.addAll(processDefinitionDTOS);
        }
        Collections.sort(list);
        return list;
    }

    @Override
    public List<ActRunTaskDTO> selectInstanceList(String definitionId) {
        List<ActRunTaskDTO> TaskEntityImplList = activitiMapper.selectRunningTasksByDefinitionId(definitionId);
        return TaskEntityImplList;
    }

    @Override
    public DefinitionIdDTO getDefinitionsByInstanceId(String instanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        String deploymentId = processInstance.getDeploymentId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        return new DefinitionIdDTO(processDefinition);
    }

    @Override
    public int deleteProcessDefinitionById(String defId,String deploymentId) {
        actiTaskConfigMapper.deleteActiTaskConfigByProcDefId(defId,tokenService.getTenantKey());
        actiModelDeploymentMapper.deleteActiModelDeploymentByDeploymentId(deploymentId);
        repositoryService.deleteDeployment(deploymentId, false);
        return 1;
    }

    @Override
    public void uploadStreamAndDeployment(MultipartFile file) throws IOException {
        // 获取上传的文件名
        String fileName = file.getOriginalFilename();
        // 得到输入流（字节流）对象
        InputStream fileInputStream = file.getInputStream();
        // 文件的扩展名
        String extension = FilenameUtils.getExtension(fileName);

        if (extension.equals("zip")) {
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            repositoryService.createDeployment()//初始化流程
                    .addZipInputStream(zip)
                    .deploy();
        } else {
            repositoryService.createDeployment()//初始化流程
                    .addInputStream(fileName, fileInputStream)
                    .deploy();
        }
    }

    @Override
    public void suspendOrActiveApply(String id, Integer suspendState) {
        if (1 == suspendState) {
            // 当流程定义被挂起时，已经发起的该流程定义的流程实例不受影响（如果选择级联挂起则流程实例也会被挂起）。
            // 当流程定义被挂起时，无法发起新的该流程定义的流程实例。
            // 直观变化：act_re_procdef 的 SUSPENSION_STATE_ 为 2
            repositoryService.suspendProcessDefinitionById(id);
        } else if (2 == suspendState) {
            repositoryService.activateProcessDefinitionById(id);
        }
    }

    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
        return FileUploadUtils.upload(localFilePath + "/processDefinition", multipartFile);
    }

    @Override
    public void addDeploymentByString(String stringBPMN) {
        repositoryService.createDeployment()
                .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                .deploy();
    }

    @Override
    public String getProcessDefineXML(String modelId) throws Exception {
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
        return new String(modelEditorSource);
    }

    @Override
    public String getDefinitionSvg(String modelId, String deploymentId) throws Exception {
        if(StringUtils.isBlank(modelId)){
            modelId = actiModelDeploymentMapper.selectModelIdByDeploymentId(deploymentId);
        }
        byte[] modelEditorSourceExtra = repositoryService.getModelEditorSourceExtra(modelId);

        String xmlStr = new String(modelEditorSourceExtra);
        String modelEditorSourceExtraStr = DocumentHelper.parseText(xmlStr).getRootElement().asXML(); //只返回 <svg> ... </svg>
        return modelEditorSourceExtraStr;
    }

    @Override
    public void downLoad(HttpServletResponse response, String deploymentId, String resourceName) throws IOException {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        int count = inputStream.available();
        byte[] bytes = new byte[count];
        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();
        while (inputStream.read(bytes) != -1) {
            outputStream.write(bytes);
        }
        inputStream.close();
    }

 /*   public String sourceByteConvertToXmlStr(byte[] modelSource) throws Exception {
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        JsonNode editorNode = new ObjectMapper().readTree(modelSource);
        //将节点信息转换为xml
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
        return new String(bpmnBytes);
    }*/

    /**
     * Desc: 通过流程实例ID获取流程中正在执行的节点(只需要在历史表中寻找是否有过该流程实例即可)
     * (因为画图时可能已经走完所有的流程，此时已无还需执行的流程实例，只有历史表中才会有)
     *
     * @param processInstanceId 流程实例ID
     * @return 正在执行的节点
     */
    public List<Execution> getRunningActivityInstance(String processInstanceId) {
        return runtimeService.createExecutionQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

}
